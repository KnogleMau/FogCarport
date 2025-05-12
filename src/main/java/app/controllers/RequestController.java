
        package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.CarportOrderMapper;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.RequestMapper;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.persistence.CustomerMapper.getCustomerId;

public class RequestController {

    public static void requestController(Context ctx, ConnectionPool connectionPool) {
        int customerId;

        try {
            int carportWidth = Integer.parseInt(ctx.formParam("carportWidth"));
            int carportLength = Integer.parseInt(ctx.formParam("carportLength"));
// address 2 dd,
            String firstname = ctx.formParam("firstname");
            String lastname = ctx.formParam("lastname");
            String adress = ctx.formParam("adress");
            String zipcode = ctx.formParam(ctx.formParam("zipcode"));
            String city = ctx.formParam("city");
            String phonenumber = ctx.formParam("phonenumber");
            String email = ctx.formParam("email");

            CustomerMapper.customerMapper(firstname, lastname, adress, zipcode, city, phonenumber, email, connectionPool);

            // Inkluder firstname
            customerId =  CustomerMapper.getCustomerId(lastname, email, phonenumber, connectionPool);

            RequestMapper.requestMapper(carportWidth, carportLength, customerId, connectionPool);

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
