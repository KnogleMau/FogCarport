package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
//import app.services.CarportSVG;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Locale;


public class AdminUserController {
/*
    public static void addAdminRoutes(Javalin app, ConnectionPool connectionPool){
        app.post("carport-top-svg", ctx -> showDrawingAtOrders(ctx, connectionPool));

    }

    public static void showDrawingAtOrders(Context ctx, ConnectionPool connectionPool){
        Locale.setDefault(new Locale("US")); // Makes sure that decimals are displayed with . instead of so it can be read by the SVG templates
// skal evt laves så det hele ligger her og bliver kaldt med eller uden  topViewCarportSVG
        // afhængigt af kaldet
    //    int width = 240;
     //   int height = 300;
        double length = 450;
        double width = 300;
        CarportSVG topViewCarportSVG = new CarportSVG(length, width);

            ctx.attribute("svg", topViewCarportSVG.toString());
            ctx.render("adminViewOrders.html");
    }
*/
}
