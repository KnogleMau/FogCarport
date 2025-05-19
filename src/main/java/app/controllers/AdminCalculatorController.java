package app.controllers;

import app.entities.OrderDetail;
import app.exceptions.DatabaseException;
import app.persistence.OrderDetailsMapper;
import app.services.CarportCalculator;

import java.util.List;

import static app.Main.connectionPool;

public class AdminCalculatorController {
    private CarportCalculator calculator;


    public void calcController(int length, int width, int orderId) throws DatabaseException {
        new CarportCalculator(length, width, connectionPool);
        calculator.calcCarport();
        List<OrderDetail> details = calculator.getOrderDetails();

        OrderDetailsMapper o = new OrderDetailsMapper();
        for(OrderDetail detail : details){
        detail.setOrderId(orderId);
        }

        o.insertOrderDetails(details, connectionPool);

    }

    public int calcPrice(int length, int width){
        new CarportCalculator(length, width, connectionPool);
        List<OrderDetail> details = calculator.getOrderDetails();
        int price = 0;
        for(OrderDetail detail : details){
            price += detail.getPrice();
        }
        return price;
    }


}
