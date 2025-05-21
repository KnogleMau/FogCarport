package app.services;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.SVG;
import static app.Main.connectionPool;

public class CarportSVG {

    private final double width;
    private final double length;
    private final SVG carportSVGElements;

    double scopeDistTop = 100; // Distance from top of scope to where carport drawing begin
    double scopeDisSide = 150; // Distance from top of scope to where carport drawing begin
    int postDimension = 10;  // On both side
    double beamWidth = 4.5; // As in // With of rafters in the order details of the construct manual we got from the customer
    int beamWidthInt = (int) beamWidth;
    int roofOverhangToBeam = (600 - 530) / 2; // Values from delivered topVieW drawing in carport build guide (half dist roof width - post placements regards carport width).
    double rafterWidth = 4.5; // With of rafters in the order details of the construct manual we got from the customer
    int sideRoofOverhangPost = roofOverhangToBeam - postDimension + beamWidthInt;  // Assuming same roof owerhang for all possible width dimensions

    public CarportSVG(double length, double width) throws DatabaseException {
        this.length = length;
        this.width = width;

        int intLength = (int) length;
        int intWidth = (int) width;

        carportSVGElements = new SVG(0, 0, "0 0 1430 700" , "90%");
        carportSVGElements.addRectangle(scopeDisSide, scopeDistTop, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");

        // Added first to show correct placement in relation to the beams.
        addPosts(intLength, intWidth);
        addBeams(intLength, intWidth);
        addRafters(intLength, intWidth);

        // Add dimension and module distance arrows to the drawing
        addModuleArrowsAndValues(intLength);
        addTopViewDimensionArrows(intWidth);
    }

    /*
    public void addPerforatedBands(int intLength, int intWidth) {


    }  */

    private void addModuleArrowsAndValues(int lengthAr) {

        CarportCalculator carportCalculator = new CarportCalculator(240, 240, connectionPool); // Needed to use che raftersCalculator from the object
        int amountOFRafters = carportCalculator.raftersCalculator(lengthAr);
        double actModDist = (lengthAr - rafterWidth) / (amountOFRafters - 1); // Actual module distance. Ammount of rafters is reduced by one
        double moduleStartPoint = rafterWidth / 2; // Because a module begin at center of the rafters width
        double spaceDrawingToModuleArrows = 35; // place Arrows outside the carport sketch
        double arrowSpaceLength = 30; // Length of lines between the module arrows
        double arrowSpacingStart = spaceDrawingToModuleArrows + 5;
        double moduleTextPlacM = spaceDrawingToModuleArrows + 57;

        double ActModDistEven = Math.round(actModDist);
        String actModDistText = Double.toString(ActModDistEven);

        // Top module arrows and space lines. lengthAr - actModDist because its the start point of the double arrow, and it ends inside the length dimension
        for (double i = 0; i < lengthAr - actModDist; i += actModDist) {
            carportSVGElements.addArrow(scopeDisSide + moduleStartPoint + i, scopeDistTop - spaceDrawingToModuleArrows,
                    scopeDisSide + moduleStartPoint + i + actModDist, scopeDistTop - spaceDrawingToModuleArrows, "stroke:#000000; fill: #000000");
        }
        // arrowSpaceLines. amountOfRafters is used because there is a space line in each end of the moduls and there is always one more rafter than modules
        for (double j = 0; j < lengthAr; j += actModDist) {
            carportSVGElements.addLine(scopeDisSide + moduleStartPoint + j, scopeDistTop - arrowSpacingStart,
                    scopeDisSide + moduleStartPoint + j, scopeDistTop - arrowSpacingStart + arrowSpaceLength, "stroke:#000000; fill: #000000");
        }
// Top module values. lengthAr - actModDist because its the start point of the double arrow, and it ends inside the length dimension
        for (double k = 0; k < lengthAr -actModDist; k += actModDist) {
            carportSVGElements.addText(scopeDisSide + moduleStartPoint + k +(actModDist/2), scopeDisSide - moduleTextPlacM, 0, actModDistText);
        }

    }

    private void addTopViewDimensionArrows(int widthAr) {
        int arroxDistanceSketchForWidth = 70;
        int arrowXWidth = (int) scopeDisSide -arroxDistanceSketchForWidth;
        int arrowXBeams = arrowXWidth + 35;
        String widthText = Double.toString(widthAr);
        int arrowLengthBeam = widthAr - (roofOverhangToBeam * 2);
        String beamWidthText = Double.toString(arrowLengthBeam);
        int distanceTextAndArrow = 3;

        // Arrow showing width
        carportSVGElements.addArrow(arrowXWidth, scopeDistTop, arrowXWidth, scopeDistTop + widthAr, "stroke:#000000; fill: #000000");
       // Text for Arrow showing width
        carportSVGElements.addText(arrowXWidth -distanceTextAndArrow, scopeDistTop + (widthAr / 2 ) - distanceTextAndArrow, -90, widthText + " cm." );
        // Arrow showing beam width
        carportSVGElements.addArrow(arrowXBeams, scopeDistTop + roofOverhangToBeam, arrowXBeams, scopeDistTop + widthAr - roofOverhangToBeam + beamWidth, "stroke:#000000; fill: #000000");
        // Text for beam width Arrow
        carportSVGElements.addText(arrowXBeams - distanceTextAndArrow, scopeDistTop + (widthAr / 2 ), -90, beamWidthText + " cm." );
    }

    private void addRafters(int width, int height) throws DatabaseException {

        double raftersWidth = 4.5; // With of rafters in the order details of the construct manual we got from the customer
   
        CarportCalculator carportCalculator = new CarportCalculator(240,240, connectionPool); // Needed to use che raftersCalculator from the object

        int ammountOFRafters = carportCalculator.raftersCalculator(width);
        double actModDist = (width - raftersWidth) / (ammountOFRafters -1);    // Actual module distance. Ammount of rafters is reduced by one
        // from 0one because the first is placed at 0 width
        for (double i = 0; i < width; i += actModDist) {
            carportSVGElements.addRectangle(scopeDisSide + i, scopeDistTop, height, raftersWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        }
    }

    private void addBeams (int width, int height){

        carportSVGElements.addRectangle(scopeDisSide, scopeDistTop + roofOverhangToBeam, beamWidth, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSVGElements.addRectangle(scopeDisSide, scopeDistTop + height -roofOverhangToBeam, beamWidth, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }


    private void addPosts(int width, int height) {

        int startPost = 100; //  displayed distance from carport front to first post for all carports based on delivered sketch of carport in side view
        int maxFrontRoofOverhang = 100; // max distance from front post start to end of front roof part
        int maxBackRoofOverhang = 30;  // Max distance from back corner post to end of roof horizontally
        int maxRoofOverhang = maxBackRoofOverhang + maxFrontRoofOverhang;

        int maxBeamPostSpacing  = 130 + 210; // From fist post begin to second post end.
            /* Assuming post number 2 from left (on side view drawing of carport in the delivered carport build manual)
            would have the same placement if there wasn´t any shed added 130 is the distance between end of arrows on same
            drawing between arrow heads on double arrow set number 3 and 210 is the next distance on arrow set 4 under the displayed shed*/
        int maxCarportLengthFourPosts = maxBeamPostSpacing  + maxRoofOverhang; // assuming max total roof over hang is 130
        int maxInnerPostDistBeamOrientedPosts = maxBeamPostSpacing  - 2 * postDimension; // Longest accepted distance between based on side view sketch

        int maxPostDistanceBeamPostsOuterMeassure = 130 + 210; // From fist post begin to second post end.
            /* Assuming post number 2 from left (on side view drawing of carport in the delivered carport build manual)
            would have the same placement if there wasn´t any shed added 130 is the distance between end of arrows on same
            drawing between arrow heads on double arrow set number 3 and 210 is the next distance on arrow set 4 under the displayed shed*/

       int maxCarportLenghtFourPosts = maxPostDistanceBeamPostsOuterMeassure + maxRoofOverhang; // assuming max total roof over hang is 130

        int maxInnerPostDistBeamOrientedPosts = maxPostDistanceBeamPostsOuterMeassure - 2 * postDimension; // Longest accepted distance between based on side view sketch

        // if we assume post placement would be the same without shed
        int secondPost = startPost + postDimension + maxInnerPostDistBeamOrientedPosts;
        /* Assuming two thirds of the overhang is added to the front and ont third to the back.
       Postdimension added to bring the value into the 30-times table */
        int totalOverhang = (width - maxBeamPostSpacing  + postDimension);

        double addedFrontOverHang = (2.0 / 3.0) * totalOverhang;

        double addedBackOverHang = (1.0 / 3.0) * totalOverhang;

        if (width <= maxBeamPostSpacing ) {
            // Post one
            carportSVGElements.addRectangle(scopeDisSide, scopeDistTop + sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - postDimension, scopeDistTop + sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post three front bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide, scopeDistTop + height - 0.5*postDimension - sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post four
            carportSVGElements.addRectangle(scopeDisSide + width - postDimension, scopeDistTop + height -0.5*postDimension - sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");

        } else if (width > maxBeamPostSpacing  && width <= maxCarportLengthFourPosts) {

            // Makes sure that back roof over hang is never over 30 works because its the last increment
            if (addedBackOverHang > maxBackRoofOverhang) {
                addedFrontOverHang += addedBackOverHang - maxBackRoofOverhang;
                addedBackOverHang = maxBackRoofOverhang;
            }
            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDistTop + sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - addedBackOverHang - postDimension, scopeDistTop + sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post three front bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDistTop + height - 0.5*postDimension - sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post four
            carportSVGElements.addRectangle(scopeDisSide + width - addedBackOverHang - postDimension, scopeDistTop + height - 0.5*postDimension - sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");

        } else if (width > maxCarportLengthFourPosts) {
            // 20 is added to the distance calculated on, to make sure that roof over hang are even number at 2/# and 1/3 distributions
            int totalOverhangSixPosts = (width - maxCarportLengthFourPosts + 20);

           double sixPostsAddedFrontOverHang = (2.0 / 3.0) * totalOverhangSixPosts;
           double sixPostsAddedBackOverHang = (1.0 / 3.0) * totalOverhangSixPosts;

            if (sixPostsAddedBackOverHang > maxBackRoofOverhang) {
                sixPostsAddedBackOverHang = maxBackRoofOverhang;
                sixPostsAddedFrontOverHang += addedBackOverHang - maxFrontRoofOverhang;
            }

            if (sixPostsAddedFrontOverHang > maxFrontRoofOverhang) {
                sixPostsAddedFrontOverHang = maxFrontRoofOverhang;
            }

            // Makes sure that the middle post is always placed with same distance to both corner posts in same beam row
            double midtPostPlacementY = (( width - sixPostsAddedFrontOverHang - sixPostsAddedBackOverHang) / 2 ) - (postDimension / 2);

            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDistTop + sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post two in middle on same beam row
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDistTop + sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post three end of top beam row
            carportSVGElements.addRectangle(scopeDisSide + width - sixPostsAddedBackOverHang -postDimension, scopeDistTop + sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");

            // Post four. First in bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDistTop + height -0.5*postDimension - sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post five. Middle one in second beam row topview
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDistTop + height -0.5*postDimension - sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post six. Last post in bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + width - sixPostsAddedBackOverHang - postDimension, scopeDistTop + height -0.5*postDimension - sideRoofOverhangPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
        }
    }

private void addArrows(int width, int height) {

        carportSVGElements.addArrow(1,1,1,1,"ok");

 //   public void addArrow(int x1, int y1, int x2, int y2, String style)
}


    @Override
    public String toString()
    {
        return carportSVGElements.toString();
    }
}






