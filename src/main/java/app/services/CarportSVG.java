package app.services;

import app.services.SVG;

public class CarportSVG {

 //   private int width;
 //   private int height;
    private double width;
    private double length;
    private SVG carportSVGElements;

    double scopeDisTop = 100; // Distance from top of scope to where carport drawing begin
    double scopeDisSide = 150; // Distance from top of scope to where carport drawing begin

    public CarportSVG(double length, double width) {
        this.length = length;
        this.width = width;

        int intLength = (int) length;
        int intWidth = (int) width;

        carportSVGElements = new SVG(0, 0, "0 0 1430 700" , "90%");
        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");


        addBeams(intLength, intWidth);
        addRafters(intLength, intWidth);
        // Added last to make them more visible
        addPosts(intLength, intWidth);

    }

    private void addRafters(int width, int height) {

        double raftersWidth = 4.5; // With of rafters in the order details of the construct manual we got from the customer
        CarportCalculator carportCalculator = new CarportCalculator();
        int ammountOFRafters = carportCalculator.raftersCalculator(width);
        double actModDist = (width - raftersWidth) / (ammountOFRafters -1);    // Actual module distance. Ammount of rafters is reduced by one
        //because the first is placed at 0 width
        for (double i = 0; i < width; i += actModDist) {
            carportSVGElements.addRectangle(scopeDisSide + i, scopeDisTop, height, raftersWidth, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        }
    }
    private void addBeams (int width, int height){

        double beamWidth = 4.5; // As in // With of rafters in the order details of the construct manual we got from the customer

        //mangler objekt order.get(); for korrekt height
        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, beamWidth, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop + height, beamWidth, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    private void addPosts(int width, int height) {
        int postDimension = 10;  // On both side
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
        System.out.println("carport længde: " + width + " maksimal stolpe afstand: " + maxPostDistanceBeamPostsOuterMeassure);
        int totalOverhang = (width - maxPostDistanceBeamPostsOuterMeassure + postDimension);

        double addedFrontOverHang = (2.0 / 3.0) * totalOverhang;
        System.out.println((width - (maxPostDistanceBeamPostsOuterMeassure+postDimension)));
        System.out.println("Front udhæng: " + addedFrontOverHang);
        double addedBackOverHang = (1.0 / 3.0) * totalOverhang;
        System.out.println("Back udhæng: " + addedBackOverHang);

        if (width <= maxPostDistanceBeamPostsOuterMeassure) {
            // Post one
            carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - postDimension, scopeDisTop, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post three front bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide, scopeDisTop + height - 0.5*postDimension, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post four
            carportSVGElements.addRectangle(scopeDisSide + width - postDimension, scopeDisTop + height -0.5*postDimension, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");

        } else if (width > maxPostDistanceBeamPostsOuterMeassure && width <= maxCarportLenghtFourPosts) {



            // Makes sure that back roof over hang is never over 30 works because its the last increment
            if (addedBackOverHang > maxBackRoofOverhang) {
                addedFrontOverHang += addedBackOverHang - maxBackRoofOverhang;
                addedBackOverHang = maxBackRoofOverhang;
            }

            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDisTop, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - addedBackOverHang - postDimension, scopeDisTop, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post three front bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDisTop + height - 0.5*postDimension, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post four
            System.out.println("ok");
            carportSVGElements.addRectangle(scopeDisSide + width - addedBackOverHang - postDimension, scopeDisTop + height - 0.5*postDimension, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
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
            System.out.println("Afstand stolpe 1 og 2: " + midtPostPlacementY);
            System.out.println("sixpostfront: " + sixPostsAddedFrontOverHang + " sixpostback: " + sixPostsAddedBackOverHang);


            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDisTop, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // Post two in middle on same beam row
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDisTop, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // Post three end of top beam row
            carportSVGElements.addRectangle(scopeDisSide + width - sixPostsAddedBackOverHang -postDimension, scopeDisTop, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");

            // Post four. First in front bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDisTop + height -0.5*postDimension, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // Post five. Middle one in second beam row topview
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDisTop + height -0.5*postDimension, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // Post six. Last post in bottom beaam row from topview
            carportSVGElements.addRectangle(scopeDisSide + width - sixPostsAddedBackOverHang - postDimension, scopeDisTop + height -0.5*postDimension, postDimension, postDimension, "stroke-width:1px; stroke:#000000; fill: #ffffff");

        }
    }



/*
private void addArrows(int width, int height) {

        carportSVGElements.addArrow(1,1,1,1,"ok");

 //   public void addArrow(int x1, int y1, int x2, int y2, String style)
}  */

    @Override
    public String toString()
    {

        return carportSVGElements.toString();
    }
}




