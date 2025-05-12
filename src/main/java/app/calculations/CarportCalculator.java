package app.calculations;

public class CarportCalculator {

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


