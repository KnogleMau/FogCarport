package app.controllers;

import app.dto.OrderCustomerDTO;
import app.dto.OrderDetailsMaterialLengthDTO;
import app.entities.AdminUser;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderAndDetailsDTOMapper;
import app.persistence.OrderDetailsMapper;
import app.services.CarportSVG;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.swing.border.AbstractBorder;
import java.util.ArrayList;
import java.util.Locale;

public class AdminUserController {

    public static void addAdminRoutes(Javalin app, ConnectionPool connectionPool){

        app.post("/adminMainMenu", ctx -> {

      ctx.render("AdminMainMenu");
        });

        app.post("/displayOrderDetails", ctx -> showDrawingAtOrders(ctx, connectionPool));

       // app.post("carport-top-svg", ctx -> showDrawingAtOrders(ctx, connectionPool));

        app.get("/adminViewOrders", ctx -> displayAllOrders(ctx, connectionPool));

    }

    public static void displayAllOrders( Context ctx, ConnectionPool connectionPool){

        try {
            System.out.println("displayAllOrders m1");
            ArrayList<OrderCustomerDTO> allOrders = OrderAndDetailsDTOMapper.getAllOrderCustomerDTOsMapper();
        //    System.out.println("displayAllOrders m2");
            for(OrderCustomerDTO order : allOrders){
                System.out.println(order.getEmail() +" " +order.getFirstName() +" "+ order.getLastName() +" "+  order.getOrderID() +" "+ order.getOrderStatus() +" "+order.getPhoneNumber()+" "+ order.getOrderID()+"\n");


            }
           ctx.attribute("allOrders", allOrders);

            System.out.println("displayAllOrders m3");
            ctx.render("adminViewOrders");
            System.out.println("displayAllOrders m4");

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

    public static void showDrawingAtOrders(Context ctx, ConnectionPool connectionPool){
        Locale.setDefault(new Locale("US")); // Makes sure that decimals are displayed with . instead of so it can be read by the SVG templates

        double length = 0;
        double width = 0;

        String materialName;
        int materialLength;
        int quantity;
        String materialUnit;
        String materialDescription;

        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        System.out.println("showDrawingAtOrders m1 orderID: " + orderId);
        try {
            ArrayList<OrderDetailsMaterialLengthDTO> detailsDTO = OrderAndDetailsDTOMapper.getOrderDetailsMapper(orderId);
        for(OrderDetailsMaterialLengthDTO dto : detailsDTO){

            if(dto.getMaterialDescription().substring(0,1).toLowerCase().equals("s"));{
                   width = dto.getMaterialLength(); }
            if(dto.getMaterialDescription().substring(0,1).toLowerCase().equals("r")) {
                   length = dto.getMaterialLength();
            }

        }

            CarportSVG topViewCarportSVG = new CarportSVG(length, width);
            ctx.attribute("svg", topViewCarportSVG.toString());
            System.out.println("detailsDTO: " + detailsDTO.size() +" "+ detailsDTO.get(0).getMaterialDescription() +" " + detailsDTO.get(1).getMaterialLength());
            ctx.attribute("detailsDTO", detailsDTO);
            ctx.render("adminViewOrders.html");
        }
        catch (DatabaseException e){
            ctx.attribute("message", "Der er et problem med databasen.");
        }
    }

    public void showDrawings(Context ctx, ConnectionPool connectionPool){
        Locale.setDefault(new Locale("US")); // Makes sure that decimals are displayed with . instead of so it can be read by the SVG templates
// skal evt laves så det hele ligger her og bliver kaldt med eller uden  topViewCarportSVG
        // afhængigt af kaldet
        //    int width = 240;
        //   int height = 300;
        //  double length = 450;
        //  double width = 300;
        String materialName;
        int materialLength;
        int quantity;
        String materialUnit;
        String materialDescription;

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        double width = Double.parseDouble(ctx.formParam("width"));
        double height = Double.parseDouble(ctx.formParam("height"));


    }
}

