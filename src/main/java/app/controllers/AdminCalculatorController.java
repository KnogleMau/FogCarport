package app.controllers;

import app.entities.OrderDetail;
import app.exceptions.DatabaseException;
import app.services.CarportCalculator;

import java.util.List;

import static app.Main.connectionPool;

public class AdminCalculatorController {

    public void AdminCalcController() throws DatabaseException {
CarportCalculator calculator = new CarportCalculator(780, 600, connectionPool);
        calculator.calcCarport();
        List<OrderDetail> details = calculator.getOrderDetails();
        System.out.println(details);

    }


}
