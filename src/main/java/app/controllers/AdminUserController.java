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

        app.post("carport-top-svg", ctx -> showDrawingAtOrders(ctx, connectionPool));

        app.get("/adminViewOrders", ctx -> displayAllOrders(ctx, connectionPool));

    }

    public static void displayAllOrders( Context ctx, ConnectionPool connectionPool){

        try {
            System.out.println("displayAllOrders m1");
            ArrayList<OrderCustomerDTO> allOrders = OrderAndDetailsDTOMapper.getAllOrderCustomerDTOsMapper();
            System.out.println("displayAllOrders m2");
            ctx.sessionAttribute("allOrders", allOrders);
            System.out.println("displayAllOrders m3");
            ctx.render("adminViewOrders");
            System.out.println("displayAllOrders m4");
        }
        catch (DatabaseException e ){

            ctx.attribute("message", "Der er et problem med databasen.");
        }
    }

    public void adminMainMenu(Context ctx, ConnectionPool connectionPool){
        AdminUser admin = ctx.sessionAttribute("adminUser");
        ctx.render("adminMainMenu");
        ctx.render("adminViewOrders");
    }

    public static void showDrawingAtOrders(Context ctx, ConnectionPool connectionPool){
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

        int orderId = Integer.parseInt(ctx.sessionAttribute("orderId"));
        try {
            ArrayList<OrderDetailsMaterialLengthDTO> detailsDTO = OrderAndDetailsDTOMapper.getOrderDetailsMapper(orderId);
            double length = 0;
           // CarportSVG topViewCarportSVG = new CarportSVG(length, width);
          //  ctx.attribute("svg", topViewCarportSVG.toString());
            ctx.sessionAttribute("detailsDTO",detailsDTO);
            ctx.render("adminViewOrders.html");
        }
        catch (DatabaseException e){
            ctx.attribute("message", "Der er et problem med databasen.");
        }
    }
}

