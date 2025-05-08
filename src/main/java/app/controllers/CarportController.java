package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.CarportOrderMapper;
import app.persistence.ConnectionPool;

import io.javalin.http.Context;

public class CarportController {

    public static void carportRequestController(Context ctx, ConnectionPool connectionPool) {

       try {
           int carportWidth = Integer.parseInt(ctx.formParam("carportWidth"));
           int carportLength = Integer.parseInt(ctx.formParam("carportLength"));

           String firstname = ctx.formParam("firstname");
           String lastname = ctx.formParam("lastname");
           String adress = ctx.formParam("adress");
           int zipcode = Integer.parseInt(ctx.formParam("zipcode"));
           String city = ctx.formParam("city");
           String phonenumber = ctx.formParam("phonenumber");
           String email = ctx.formParam("email");

           CarportOrderMapper.carportRequestMapper(carportWidth, carportLength, firstname, lastname, adress, zipcode, city, phonenumber, email, connectionPool);
       }
       catch (DatabaseException e ){
           ctx.attribute("message ", "Dit valg af by og postkode matcher ikke");
           ctx.render("carportRequest.html");
       }
       catch(IllegalArgumentException e){
           ctx.attribute("message ", "Du har udfyldt nogle oplysninger forkert. \n Postnummer skal udfyldes, og må kun bestå af tal");
           ctx.render("carportRequest.html");
       }
    }
}
