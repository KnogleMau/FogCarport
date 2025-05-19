package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class render {

    public static void routes(Javalin app, ConnectionPool connectionPool) {

        RequestController.AddRequestRoutes(app, connectionPool);

     /*   app.get("/", ctx ->
                ctx.render("adminlogin"));
*/

        app.get("/", ctx -> {
            ctx.render("frontpage.html");
        });
     /*   app.get("/carportBuilder", ctx -> {
            ctx.render("carportBuilder.html");
        });

        app.get("/customerContactInformation", ctx -> {
            ctx.render("customerContactInformation.html");
        });

        app.get("/confirmationPageUser", ctx -> {
            ctx.render("confirmationPageUser.html");
        });

*/

       /* app.get("/", ctx -> {
            ctx.render("adminViewOrders");
        });  */

        AdminUserController.addAdminRoutes(app, connectionPool);

//Hvor henne Spørger just in case
        //
        //
        //
        //
      /*  app.get("/frontpage",ctx ->

        {
            ctx.render("frontpage.html");
        });  */
    }
}
