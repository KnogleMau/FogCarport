package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.AdminCalculatorController;
import app.controllers.render;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.services.CarportCalculator;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = System.getenv("USER");
    private static final String PASSWORD = System.getenv("CODE");
    private static final String URL = System.getenv("URL");
    private static final String DB = System.getenv("DATABASE");


    public static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) throws DatabaseException {

        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        CarportCalculator c = new CarportCalculator(780,600,connectionPool);
        c.calcBeam();

        //render.routes(app, connectionPool);


    }
}