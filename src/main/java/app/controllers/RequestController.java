package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.RequestMapper;
import app.services.CarportRequestSVG;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Locale;


public class RequestController {

    public static void AddRequestRoutes(Javalin app, ConnectionPool connectionPool){

        app.get("/carportBuilder", ctx -> {
            System.out.println("markør route 0: ");
            ctx.render("carportBuilder.html");
            });

        app.post("/showDrawing", ctx -> {
            System.out.println("markør route 1: ");
            selectAndDisplayCarport(ctx, connectionPool);
        });


        app.post("/customerContactInformation", ctx ->
                typeCustomerContactInformation(ctx, connectionPool) );

        app.post("/sendRequest", ctx ->
                requestController(ctx, connectionPool));
    }

    public static void selectAndDisplayCarport(Context ctx, ConnectionPool connectionPool){
        Locale.setDefault(new Locale("US")); // Makes sure that decimals are displayed with.

        int width = Integer.parseInt(ctx.formParam("carport-width"));
       int length = Integer.parseInt(ctx.formParam("carport-length"));

        CarportRequestSVG carportRequestSVG = new CarportRequestSVG(length, width );

        String carportSideView = carportRequestSVG.toString();

        ctx.attribute("drawing", carportSideView);

        ctx.render("carportBuilder.html");
    }

    public static void typeCustomerContactInformation(Context ctx, ConnectionPool connectionPool){

        ctx.render("customerContactInformation.html");

        int width = Integer.parseInt(ctx.formParam("carport-width"));
        int length = Integer.parseInt(ctx.formParam("carport-length"));

        ctx.sessionAttribute("carport-width", width);
        ctx.sessionAttribute("carport-length", length);
    }

    public static void requestController(Context ctx, ConnectionPool connectionPool) {
        int customerId;

        try {
            int carportWidth = ctx.sessionAttribute("carport-width");
            int carportLength = ctx.sessionAttribute("carport-length");

            String firstname = ctx.formParam("first-name");
            String lastname = ctx.formParam("last-name");
            String address = ctx.formParam("address");
            String city = ctx.formParam("city");
            // makes sure that typed city can match the coresponding city in the database
            String citySearch = city.toLowerCase();
            String phonenumber = ctx.formParam("phone-number");
            String zipcode = ctx.formParam("zip-code");
            String email = ctx.formParam("e-mail");

            // Insets the customer information in the database if selected city and zip cde matches
            CustomerMapper.customerMapper(firstname, lastname, address, zipcode, citySearch, phonenumber, email, connectionPool);
            //gets the customer id from the newly registered customer information so the request cam be registered with a customer ID
            customerId =  CustomerMapper.getCustomerId(firstname, lastname, email, phonenumber, connectionPool);
            // registers the carport request
            RequestMapper.requestMapper(carportWidth, carportLength, customerId);

            ctx.render("confirmationPageUser.html");
        }
        catch (DatabaseException e ){

            ctx.attribute("message", "Dit valg af by og postkode matcher ikke");
            selectAndDisplayCarport(ctx, connectionPool);
        }
        catch(IllegalArgumentException e){

            ctx.attribute("message", "Du har udfyldt nogle oplysninger forkert. \n Postnummer skal udfyldes, og må kun bestå af tal");
            ctx.render("carportRequest.html");
        }
        catch (NullPointerException n){

            ctx.attribute("message", "Det skete en fejl, med dit valg af carport dimensionerm, prøv venligst igen.");
            ctx.render("carportBuilder.html");
        }
    }
}
