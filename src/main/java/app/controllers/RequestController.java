
        package app.controllers;

import app.entities.CarportRequest;
import app.exceptions.DatabaseException;
import app.persistence.*;

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

        int width = Integer.parseInt(ctx.formParam("carport-width"));
        int length = Integer.parseInt(ctx.formParam("carport-length"));

        CarportRequestSVG carportRequestSVG = new CarportRequestSVG(length, width );

        String carportSideView = carportRequestSVG.toString();

        ctx.attribute("drawing", carportSideView);

        ctx.render("carportBuilder.html");
    }

    public static void typeCustomerContactInformation(Context ctx, ConnectionPool connectionPool){

        String carportWidth = ctx.formParam("carport-width");
        String carportLength = ctx.formParam("carport-length");

        if(carportWidth == null || carportLength == null){
            ctx.attribute("error", "Der skete desværre en fejl med valg af by og postnummer, der gør at vihat behov for valget af dinensioner igen.");
            ctx.render("/carportBuilder.html");
        }
        int width = Integer.parseInt(ctx.formParam("carport-width"));
        int length = Integer.parseInt(ctx.formParam("carport-length"));
        ctx.sessionAttribute("carport-width", width);
        ctx.sessionAttribute("carport-length", length);

        ctx.render("customerContactInformation.html");
    }

    public static void requestController(Context ctx, ConnectionPool connectionPool) {
        int customerId;
        AdminCalculatorController acc = new AdminCalculatorController();

        try {
            int carportWidth = ctx.sessionAttribute("carport-width");
            System.out.println("carportWidth: controller m1:  " + carportWidth);
            int carportLength = ctx.sessionAttribute("carport-length");

            String firstname = ctx.formParam("first-name");
            String lastname = ctx.formParam("last-name");
            String address = ctx.formParam("address");
            String city = ctx.formParam("city");
            System.out.println("city requestController: m1 " + firstname + " " + lastname + " " + address + " " + city);
            String phonenumber = ctx.formParam("phone-number");
            String zipcode = ctx.formParam("zip-code");
            String email = ctx.formParam("e-mail");

            // Insets the customer information in the database if selected city and zip cde matches
            CustomerMapper.customerMapper(firstname, lastname, address, zipcode, city, phonenumber, email, connectionPool);

            //gets the customer id from the newly registered customer information so the request cam be registered with a customer ID
            customerId =  CustomerMapper.getCustomerId(firstname, lastname, email, phonenumber, connectionPool);

            // registers the carport request
            RequestMapper.requestMapper(carportWidth, carportLength, customerId);

            OrdersMapper ordersMapper = new OrdersMapper();
            CarportRequest carportRequest = RequestMapper.getCarportRequest(customerId);
            //    ordersMapper.insertIntoOrders(customerId,carportRequest.getRequestID(), 4000, "pending");

            ctx.render("confirmationPageUser.html");
        }
        catch (DatabaseException e ){
            System.out.println("requestController m7 catch m1: ");
            //   ctx.attribute("message", "Dit valg af by og postkode matcher ikke");
            ctx.attribute("error", "Der skete desværre en fejl med valg af by og postnummer, der gør at vi har behov for valget af dinensioner igen.");
            ctx.render("/carportBuilder.html");
        }
        catch(IllegalArgumentException e){
            System.out.println("requestController catch m2: ");
            ctx.attribute("message", "Du har udfyldt nogle oplysninger forkert. \n Postnummer skal udfyldes, og må kun bestå af tal");
            typeCustomerContactInformation(ctx, connectionPool);
        }
        catch (NullPointerException n){
            System.out.println("requestController catch m3: ");
            ctx.attribute("message", "Det skete en fejl, med dit valg af carport dimensionerm, prøv venligst igen.");
            ctx.render("carportBuilder.html");
        }
    }
}