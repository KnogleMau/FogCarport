package app.services;

import app.entities.Material;
import app.entities.MaterialVariant;
import app.entities.OrderDetail;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ProductMapper;

import java.util.HashMap;
import java.util.List;

public class CarportCalculator {
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

   // public List<MaterialVariant> calcCarport() throws DatabaseException {
      //  calculatePole();
    //}

    public void calculatePole() throws DatabaseException {

        int quantity = poleCalc();
        List<MaterialVariant> materialVariants = productMapper.selectMaterialVariant(POLE, 300, connectionPool);
        HashMap<MaterialVariant, Integer> hs = new HashMap<>();
        hs.put(materialVariants.get(0),quantity);


    }

    private int poleCalc() {
            return 2 * (2 + (lenght - 130) / 340);
    }

    public int raftersCalculator(int beamLengthCentimeters){
        int numberOfRafters;
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

        numberOfRafters = (int) Math.ceil(decimalNumberOfRafters);

        return numberOfRafters;
    }

}
