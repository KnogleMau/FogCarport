package app.services;

import app.entities.Material;
import app.entities.MaterialVariant;
import app.entities.OrderDetail;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ProductMapper;

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
    ProductMapper productMapper = new ProductMapper();
    // Stolper


    public CarportCalculator(int lenght, int width, ConnectionPool connectionPool) {
        this.lenght = lenght;
        this.width = width;
        this.connectionPool = connectionPool;
    }

    public void calcCarport() throws DatabaseException {
        calculatePole();
        calculateRafter();
        polePositions();

    }

    public void calculatePole() throws DatabaseException {

        int quantity = poleCalc();
        Material material = productMapper.selectProduct(POLE, connectionPool);
        List<MaterialVariant> materialVariants = productMapper.selectMaterialVariant(POLE, 300, connectionPool);

        OrderDetail detail = new OrderDetail(1, material.getId(), quantity, materialVariants.get(0).getLengthId(), material.getPrice() * (materialVariants.get(0).getLength() / 100 * quantity));

        orderDetails.add(detail);

    }

    private int poleCalc() {
            return 2 * (2 + (lenght - 130) / 340);
    }

    private void calculateRafter() throws DatabaseException {
        int quantity = raftersCalculator(width);

        Material material = productMapper.selectProduct(BEAM, connectionPool);
        List<MaterialVariant> materialVariants = productMapper.selectMaterialVariant(BEAM, width, connectionPool);

        OrderDetail detail = new OrderDetail(1, material.getId(), quantity, materialVariants.get(0).getLengthId(), material.getPrice() * (materialVariants.get(0).getLength() / 100) * quantity);

        orderDetails.add(detail);
    }

    public int raftersCalculator(int beamLengthCentimeters) throws DatabaseException {
        double decimalNumberOfRafters;
        //Stand width for used rafters
        final double rafterWidth = 4.5;
        //Standard module distance between center of two rafters as descriped i repport
        final double rafterModuleLength = 59.5;

// one is added because there is one more rafter than number of mudules between rafters.
        decimalNumberOfRafters =  (beamLengthCentimeters -rafterWidth)/rafterModuleLength + 1;

        /*Rounds the value up to first whole value because che construction cant implement half rafters
        and any rounded down value would beamLengthCentimeters the CertPathBuilderResult for any shorter
                module length (even if the calculation resultet in 11.07 rafters the criteria for the given module
    length would notify() have been meet) */

        return (int) Math.ceil(decimalNumberOfRafters);
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public Map<MaterialVariant, Integer> beamCalculator(int materialId) throws DatabaseException {
        Map<MaterialVariant, Integer> beamsNeeded = new HashMap<>();
        int remaining = this.lenght;

        List<MaterialVariant> variants = productMapper.selectMaterialVariant(materialId, 0, connectionPool);

        //Sorts the boards by length, in reversed order so the biggest comes first. This allows the if-statement to work as intended.
        variants.sort(Comparator.comparingInt(MaterialVariant::getLength).reversed());

        while(remaining > 0) {
            for(MaterialVariant variant : variants) {
                int boardLength = variant.getLength();

                if(boardLength <= remaining) {
                    beamsNeeded.put(variant, beamsNeeded.getOrDefault(variant,0)+2);
                    remaining -= boardLength;
                    break;
                }
            }

        }
            return beamsNeeded;

    }

    public List<Integer> polePositionCalculator(int carportLength, int maxSpaceBetweenPoles, int startOffset, int maxEndOverhang) {

        List<Integer> polePositions = new ArrayList<>();

        int position = startOffset;

        while (position < carportLength) {
            polePositions.add(position);
            position += maxSpaceBetweenPoles;
        }

        int lastPost = polePositions.get(polePositions.size() - 1);
        if (carportLength - lastPost > maxEndOverhang) {
            polePositions.add(carportLength);
        }

        return polePositions;
    }

    public void polePositions(){
        int carportLength = this.lenght;
        int startOffset = 30;
        int maxSpaceBetweenPoles = 340;
        int maxEndOverhang = 100;

        this.polePositions = polePositionCalculator(carportLength, maxSpaceBetweenPoles, startOffset, maxEndOverhang);
    }

}
