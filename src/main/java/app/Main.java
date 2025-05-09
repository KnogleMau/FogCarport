package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.javaCode.Sendgrid;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("KODE");
    private static final String URL = "jdbc:postgresql://46.101.114.35:5432/%s?currentSchema=public";
    private static final String DB = "fogcarport";

    public static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        Sendgrid sendgrid = new Sendgrid();

        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        Sendgrid s = new Sendgrid();
        try {
            s.sendGridAction("johannesfogcarport@gmail.com", "Mr FOG", "d-1dc8cdf4e45d44139c6ac4711ea45adc");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }


       // CupcakeController.routes(app, connectionPool);
    }
}