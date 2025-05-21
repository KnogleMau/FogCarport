package app.controllers;

import app.entities.CarportRequest;
import app.exceptions.DatabaseException;

import app.persistence.*;

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

            ctx.render("carportBuilder.html");
            });

        app.post("/showDrawing", ctx -> {

            selectAndDisplayCarport(ctx, connectionPool);
        });

        app.post("/customerContactInformation", ctx ->
                typeCustomerContactInformation(ctx, connectionPool) );

        app.post("/sendRequest", ctx ->
                requestController(ctx, connectionPool));
    }

    public static void selectAndDisplayCarport(Context ctx, ConnectionPool connectionPool){
        Locale.setDefault(new Locale("US")); // Makes sure that decimals are displayed with.
        System.out.println("selectAndDisplay m1: ");
        int width = Integer.parseInt(ctx.formParam("carport-width"));
       int length = Integer.parseInt(ctx.formParam("carport-length"));

        CarportRequestSVG carportRequestSVG = new CarportRequestSVG(length, width );
        System.out.println("selectAndDisplay m2: ");
        String carportSideView = carportRequestSVG.toString();
        System.out.println("selectAndDisplay m3: ");
        ctx.attribute("drawing", carportSideView);

        ctx.render("carportBuilder.html");
        System.out.println("selectAndDisplay m4: ");
    }

    public static void typeCustomerContactInformation(Context ctx, ConnectionPool connectionPool){
        System.out.println("typeCustomerContactInformation m1: ");
        ctx.render("customerContactInformation.html");
        System.out.println("typeCustomerContactInformation m2: ");
        int width = Integer.parseInt(ctx.formParam("carport-width"));
        int length = Integer.parseInt(ctx.formParam("carport-length"));
        System.out.println("typeCustomerContactInformation m3: ");
        ctx.sessionAttribute("carport-width", width);
        ctx.sessionAttribute("carport-length", length);
        System.out.println("typeCustomerContactInformation m4: ");
    }

    public static void requestController(Context ctx, ConnectionPool connectionPool) {
        int customerId;
        System.out.println("requestController m1: ");
        AdminCalculatorController acc = new AdminCalculatorController();

        try {
            System.out.println("requestController m2: ");
            int carportWidth = ctx.sessionAttribute("carport-width");
            int carportLength = ctx.sessionAttribute("carport-length");
            System.out.println("requestController m3: ");

            String firstname = ctx.formParam("first-name");
            System.out.println("requestController m4: ");
            String lastname = ctx.formParam("last-name");
            System.out.println("requestController m5: ");
            String address = ctx.formParam("address");
            String city = ctx.formParam("city");
            System.out.println("requestController m6: ");
            // makes sure that typed city can match the coresponding city in the database
            String citySearch = city.toLowerCase();
            System.out.println("requestController m7: ");
            String phonenumber = ctx.formParam("phone-number");
            String zipcode = ctx.formParam("zip-code");
            System.out.println("requestController m8: ");
            String email = ctx.formParam("e-mail");

            // Insets the customer information in the database if selected city and zip cde matches
            CustomerMapper.customerMapper(firstname, lastname, address, zipcode, citySearch, phonenumber, email, connectionPool);
            System.out.println("requestController m9: ");
            //gets the customer id from the newly registered customer information so the request cam be registered with a customer ID
            customerId =  CustomerMapper.getCustomerId(firstname, lastname, email, phonenumber, connectionPool);
            System.out.println("requestController m10: ");
            // registers the carport request
            RequestMapper.requestMapper(carportWidth, carportLength, customerId);

            System.out.println("requestController m11: ");
            OrdersMapper ordersMapper = new OrdersMapper();
            CarportRequest carportRequest = RequestMapper.getCarportRequest(customerId);
            System.out.println("requestController m12: ");
            ordersMapper.insertIntoOrders(customerId,carportRequest.getRequestID(), acc.calcPrice(carportLength,carportWidth), "pending");


            System.out.println("requestController m13: ");
            ctx.render("confirmationPageUser.html");
            System.out.println("requestController m14: ");
        }
        catch (DatabaseException e ){
            System.out.println("requestController m7 catch m1: ");
            ctx.attribute("message", "Dit valg af by og postkode matcher ikke");
            selectAndDisplayCarport(ctx, connectionPool);
        }
        catch(IllegalArgumentException e){
            System.out.println("requestController catch m2: ");
            ctx.attribute("message", "Du har udfyldt nogle oplysninger forkert. \n Postnummer skal udfyldes, og må kun bestå af tal");
            ctx.render("carportRequest.html");
        }
        catch (NullPointerException n){
            System.out.println("requestController catch m3: ");
            ctx.attribute("message", "Det skete en fejl, med dit valg af carport dimensionerm, prøv venligst igen.");
            ctx.render("carportBuilder.html");
        }
    }
}
