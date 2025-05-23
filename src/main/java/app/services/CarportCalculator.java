package app.services;

import app.entities.Material;
import app.entities.MaterialVariant;
import app.entities.OrderDetail;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.util.*;

public class CarportCalculator {
    private final List<OrderDetail> orderDetails = new ArrayList<>();
    private List<Integer> polePositions;
    private static final int BEAM = 1;
    private static final int RAFTER = 2;
    private static final int POLE = 3;
    private ConnectionPool connectionPool;
    private int lenght;
    private int width;
    MaterialMapper materialMapper = new MaterialMapper();
    // Stolper


    public CarportCalculator(int lenght, int width, ConnectionPool connectionPool) {
        this.lenght = lenght;
        this.width = width;
        this.connectionPool = connectionPool;
    }

    public void calcCarport() throws DatabaseException {
        calculatePole();
        calculateRafter();
        calculateBeam();
    }

    public void calculatePole() throws DatabaseException {

        int quantity = poleCalc();
        Material material = materialMapper.selectProduct(POLE, connectionPool);
        List<MaterialVariant> materialVariants = materialMapper.selectMaterialVariant(POLE, 300, connectionPool);

        OrderDetail detail = new OrderDetail(5, material.getId(), quantity, materialVariants.get(0).getLengthId(), material.getPrice() * (materialVariants.get(0).getLength() / 100 * quantity));

        orderDetails.add(detail);

    }

    private int poleCalc() {
        return 2 * (2 + (lenght - 130) / 340);
    }

    private void calculateRafter() throws DatabaseException {
        int quantity = raftersCalculator(width);

        Material material = materialMapper.selectProduct(RAFTER, connectionPool);
        List<MaterialVariant> materialVariants = materialMapper.selectMaterialVariant(RAFTER, width, connectionPool);

        OrderDetail detail = new OrderDetail(5, material.getId(), quantity, materialVariants.get(0).getLengthId(), material.getPrice() * (materialVariants.get(0).getLength() / 100) * quantity);

        orderDetails.add(detail);
    }

    public int raftersCalculator(int beamLengthCentimeters) throws DatabaseException {
        double decimalNumberOfRafters;
        //Stand width for used rafters
        final double rafterWidth = 4.5;
        //Standard module distance between center of two rafters as descriped i repport
        final double rafterModuleLength = 59.5;

// one is added because there is one more rafter than number of mudules between rafters.
        decimalNumberOfRafters = (beamLengthCentimeters - rafterWidth) / rafterModuleLength + 1;

        /*Rounds the value up to first whole value because che construction cant implement half rafters
        and any rounded down value would beamLengthCentimeters the CertPathBuilderResult for any shorter
                module length (even if the calculation resultet in 11.07 rafters the criteria for the given module
    length would notify() have been meet) */

        return (int) Math.ceil(decimalNumberOfRafters);
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }


    public void calculateBeam() throws DatabaseException {
        int quantity = calcBeam();
        Material material = materialMapper.selectProduct(BEAM, connectionPool);

        if (quantity == 2) {
            int mLength = 420;
            List<MaterialVariant> materialVariants = materialMapper.selectMaterialVariant(BEAM, mLength , connectionPool);
            OrderDetail detail = new OrderDetail(5, material.getId(), quantity * 2, materialVariants.get(0).getLengthId(), material.getPrice() * (materialVariants.get(0).getLength() / 100) * (quantity * 2));
            orderDetails.add(detail);
        } else if(quantity == 1) {
            List<MaterialVariant> materialVariants = materialMapper.selectMaterialVariant(BEAM, lenght, connectionPool);
            OrderDetail detail = new OrderDetail(5, material.getId(), quantity * 2, materialVariants.get(0).getLengthId(), material.getPrice() * (materialVariants.get(0).getLength() / 100) * (quantity * 2));
            orderDetails.add(detail);
        } else {
            System.out.println("FEJL");
        }

    }


        public int calcBeam () {
            int beam;

            if (lenght <= 600) {
                beam = 1;
            } else {
                beam = 2;
            }
            return beam;
        }


    }
