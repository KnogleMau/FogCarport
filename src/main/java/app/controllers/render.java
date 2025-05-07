package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class render {

    public static void routes(Javalin app, ConnectionPool connectionPool) {
    app.get("/", ctx -> {
        ctx.render("frontpage.html");
    });
    }
}
