package app.services;

import app.persistence.ConnectionPool;
import app.services.SVG;

import java.awt.*;

import static app.Main.connectionPool;

public class CarportSVG {

    private double width;
    private double length;
    private SVG carportSVGElements;

    double scopeDisTop = 100; // Distance from top of scope to where carport drawing begin
    double scopeDisSide = 150; // Distance from top of scope to where carport drawing begin
    int postDimension = 10;  // On both side
    double beamWidth = 4.5; // As in // With of rafters in the order details of the construct manual we got from the customer
    int beamWidthInd = (int) beamWidth;
    int sideRoofOverhangToBeam = (600 - 530) / 2; // Values from delivered topVieW drawing in carport build guide (half dist roof width - post placements regards carport width).
    double raftersWidth = 4.5; // With of rafters in the order details of the construct manual we got from the customer
    int sideRoofOverhangToPost = sideRoofOverhangToBeam - postDimension + beamWidthInd;


    public CarportSVG(double length, double width) {
        this.length = length;
        this.width = width;

        int intLength = (int) length;
        int intWidth = (int) width;

        carportSVGElements = new SVG(0, 0, "0 0 1430 700" , "90%");
        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");

        // Added first to show correct placement in relation to the beams.
        addPosts(intLength, intWidth);
        addBeams(intLength, intWidth);
        addRafters(intLength, intWidth);
        // carportSVGElements = new SVG(0, 0, "0 0 400 400" , "100%");

        addArrowsModuleArrows(intLength, intWidth);
    }

    public void addArrowsModuleArrows(int lengthAr, int widthAr) {

        CarportCalculator carportCalculator = new CarportCalculator(240, 240, connectionPool); // Needed to use che raftersCalculator from the object
        int amountOFRafters = carportCalculator.raftersCalculator(lengthAr);
        double actModDist = (lengthAr - raftersWidth) / (amountOFRafters - 1); // Actual module distance. Ammount of rafters is reduced by one
        double moduleStartPoint = raftersWidth / 2; // Because a module begin at center of the rafters width
        double spaceDrawingToModuleArrows = 35; // place Arrows outside the carport sketch
        double arrowSpaceLength = 30; // Length of lines between the module arrows
        double arrowSpacingStart = spaceDrawingToModuleArrows + 5;
        double moduleTextPlacM = spaceDrawingToModuleArrows + 57;

        double ActModDistEven = Math.round(actModDist);
        String actModDistText = Double.toString(ActModDistEven);

        // Top module arrows and space lines. lengthAr - actModDist because its the start point of the double arrow, and it ends inside the length dimension
        for (double i = 0; i < lengthAr - actModDist; i += actModDist) {
            carportSVGElements.addArrow(scopeDisSide + moduleStartPoint + i, scopeDisTop - spaceDrawingToModuleArrows,
                    scopeDisSide + moduleStartPoint + i + actModDist, scopeDisTop - spaceDrawingToModuleArrows, "stroke:#000000; fill: #000000");
        }
        // arrowSpaceLines. amountOfRafters is used because there is a space line in each end of the moduls and there is always one more rafter than modules
        for (double j = 0; j < lengthAr; j += actModDist) {
            carportSVGElements.addLine(scopeDisSide + moduleStartPoint + j, scopeDisTop - arrowSpacingStart,
                    scopeDisSide + moduleStartPoint + j, scopeDisTop - arrowSpacingStart + arrowSpaceLength, "stroke:#000000; fill: #000000");
        }
// Top module values. lengthAr - actModDist because its the start point of the double arrow, and it ends inside the length dimension
        for (double k = 0; k < lengthAr -actModDist; k += actModDist) {
            carportSVGElements.addText(scopeDisSide + moduleStartPoint + k +(actModDist/2), scopeDisSide - moduleTextPlacM, 0, actModDistText);
        }
    }

    private void addRafters(int width, int height) {

        double raftersWidth = 4.5; // With of rafters in the order details of the construct manual we got from the customer
        CarportCalculator carportCalculator = new CarportCalculator(240,240, connectionPool); // Needed to use che raftersCalculator from the object
        int ammountOFRafters = carportCalculator.raftersCalculator(width);
        double actModDist = (width - raftersWidth) / (ammountOFRafters -1);    // Actual module distance. Ammount of rafters is reduced by one
        // from 0one because the first is placed at 0 width
        for (double i = 0; i < width; i += actModDist) {
            carportSVGElements.addRectangle(scopeDisSide + i, scopeDisTop, height, raftersWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        }
    }
    private void addBeams (int width, int height){

        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop + sideRoofOverhangToBeam, beamWidth, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop + height -sideRoofOverhangToBeam, beamWidth, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    private void addPosts(int width, int height) {

        int startPost = 100; //  displayed distance from carport front to first post for all carports based on delivered sketch of carport in side view
        int maxFrontRoofOverhang = 100; // max distance from front post start to end of front roof part
        int maxBackRoofOverhang = 30;  // Max distance from back corner post to end of roof horizontally
        int maxRoofOverhang = maxBackRoofOverhang + maxFrontRoofOverhang;
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
        int totalOverhang = (width - maxPostDistanceBeamPostsOuterMeassure + postDimension);

        double addedFrontOverHang = (2.0 / 3.0) * totalOverhang;

        double addedBackOverHang = (1.0 / 3.0) * totalOverhang;

        if (width <= maxPostDistanceBeamPostsOuterMeassure) {
            // Post one
            carportSVGElements.addRectangle(scopeDisSide, scopeDisTop + sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - postDimension, scopeDisTop + sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post three front bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide, scopeDisTop + height - 0.5*postDimension - sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post four
            carportSVGElements.addRectangle(scopeDisSide + width - postDimension, scopeDisTop + height -0.5*postDimension - sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");

        } else if (width > maxPostDistanceBeamPostsOuterMeassure && width <= maxCarportLenghtFourPosts) {

            // Makes sure that back roof over hang is never over 30 works because its the last increment
            if (addedBackOverHang > maxBackRoofOverhang) {
                addedFrontOverHang += addedBackOverHang - maxBackRoofOverhang;
                addedBackOverHang = maxBackRoofOverhang;
            }
            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDisTop + sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - addedBackOverHang - postDimension, scopeDisTop + sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post three front bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDisTop + height - 0.5*postDimension - sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // post four
            carportSVGElements.addRectangle(scopeDisSide + width - addedBackOverHang - postDimension, scopeDisTop + height - 0.5*postDimension - sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");

        } else if (width > maxCarportLenghtFourPosts) {
            // 20 is added to the distance calculated on, to make sure that roof over hang are even number at 2/# and 1/3 distributions
            int totalOverhangSixPosts = (width - maxCarportLenghtFourPosts + 20);
            double sixPostsAddedFrontOverHang = 0;
            sixPostsAddedFrontOverHang = (2.0 / 3.0) * totalOverhangSixPosts;
            double sixPostsAddedBackOverHang = 0;
            sixPostsAddedBackOverHang = (1.0 / 3.0) * totalOverhangSixPosts;

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
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDisTop + sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post two in middle on same beam row
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDisTop + sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post three end of top beam row
            carportSVGElements.addRectangle(scopeDisSide + width - sixPostsAddedBackOverHang -postDimension, scopeDisTop + sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");

            // Post four. First in bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDisTop + height -0.5*postDimension - sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post five. Middle one in second beam row topview
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDisTop + height -0.5*postDimension - sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
            // Post six. Last post in bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + width - sixPostsAddedBackOverHang - postDimension, scopeDisTop + height -0.5*postDimension - sideRoofOverhangToPost, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #000000");
        }
    }

    @Override
    public String toString()
    {
        return carportSVGElements.toString();
    }
}




