package app.controllers;

import app.entities.AdminUser;
import app.exceptions.DatabaseException;
import app.persistence.AdminUserMapper;
import app.persistence.ConnectionPool;
import app.services.CarportSVG;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Locale;


public class AdminUserController {

    public static void addAdminRoutes(Javalin app, ConnectionPool connectionPool){
        app.post("carport-top-svg", ctx -> showDrawingAtOrders(ctx, connectionPool));

    }

    public static void showLoginPage(Context ctx) {
        ctx.render("adminlogin.html");
    }

    public static void handleLogin(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            AdminUser user = AdminUserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.redirect("/adminMainMenu");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("adminlogin.html");
        }
    }

    public static void showAdminMenu(Context ctx) {
        AdminUser user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/adminlogin");
        } else {
            ctx.render("adminMainMenu.html");
        }
    }

    public static void showDrawingAtOrders(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
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

}
