package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;


public class AdminUserController {

    public static void addAdminRoutes(Javalin app, ConnectionPool connectionPool){
        app.post("carport-top-svg", ctx -> showDrawingAtOrders(ctx, connectionPool));

    }

    public static void showDrawingAtOrders(Context ctx, ConnectionPool connectionPool){
// skal evt laves så det hele ligger her og bliver kaldt med eller uden  topViewCarportSVG
        // afhængigt af kaldet

           String topViewCarportSVG ="hej";
            ctx.attribute("svg", topViewCarportSVG);
            ctx.render("adminViewOrders.html");
    }



}
