package app.controllers;

import app.dto.OrderCustomerDTO;
import app.dto.OrderDetailsMaterialLengthDTO;
import app.entities.AdminUser;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderAndDetailsDTOMapper;
import app.services.CarportSVG;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Locale;

public class AdminUserController {

    public static void addAdminRoutes(Javalin app, ConnectionPool connectionPool){

        app.post("/adminMainMenu", ctx -> {
       ctx.render("AdminMainMenu");
        });

        app.post("/displayOrderDetails", ctx -> showOrderDetailsAndDrawing(ctx, connectionPool));

        app.get("/adminViewOrders", ctx -> displayAllOrders(ctx, connectionPool));
    }

    public static void displayAllOrders( Context ctx, ConnectionPool connectionPool){

        try {
            ArrayList<OrderCustomerDTO> allOrders = OrderAndDetailsDTOMapper.getAllOrderCustomerDTOsMapper();
           ctx.attribute("allOrders", allOrders);
            ctx.render("adminViewOrders");
        }
        catch (DatabaseException e ){

            ctx.attribute("message", "Der er et problem med databasen.");
            ctx.render("/adminViewOrders");
        }
    }

    public void adminMainMenu(Context ctx, ConnectionPool connectionPool){
        AdminUser admin = ctx.sessionAttribute("adminUser");
        ctx.render("adminMainMenu");
        ctx.render("adminViewOrders");
    }

        public static void showOrderDetailsAndDrawing(Context ctx, ConnectionPool connectionPool){
        Locale.setDefault(new Locale("US")); // Makes sure that decimals are displayed with . instead of so it can be read by the SVG templates

        double length = 0;
        double width = 0;

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        try {
            ArrayList<OrderDetailsMaterialLengthDTO> detailsDTO = OrderAndDetailsDTOMapper.getOrderDetailsMapper(orderId);
        for(OrderDetailsMaterialLengthDTO dto : detailsDTO){
            if(dto.getMaterialDescription().substring(0,2).toLowerCase().equals("sp"));{
                   width = dto.getMaterialLength(); }
            if(dto.getMaterialDescription().substring(0,2).toLowerCase().equals("re")) {
                   length = dto.getMaterialLength();
            }
        }
            CarportSVG topViewCarportSVG = new CarportSVG(length, width);

            ctx.attribute("svg", topViewCarportSVG.toString());
            ctx.attribute("detailsDTO", detailsDTO);
            ctx.render("adminViewOrders.html");
        }
        catch (DatabaseException e){
            ctx.attribute("message", "Der er et problem med databasen.");
        }
    }
}

