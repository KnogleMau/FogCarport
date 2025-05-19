package app.services;

public class CarportRequestSVG {
    private double dwidth;
    private double length;
    private int width;
    private int height;
    int postHeight = 290;

    private SVG carportSVGElements;

    double scopeDisTop = 300; // Distance from top of scope to where carport drawing begin
    double scopeDisSide = 30; // Distance from top of scope to where carport drawing begin

    public CarportRequestSVG(int width, int height) {
        this.length = length;
        this.width = width;

        int intLength = (int) length;
        int intWidth = (int) width;

        carportSVGElements = new SVG(0, 0, "0 0 900 500" , "40%");
        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        sideViewBeam(width);
        sideViewPost(width, height);
        ground(width);
    }

    public void ground (int width){
        double groundBegin = scopeDisSide - 15;
        double groundThickness = 10;
        double extraGround = 50;
        carportSVGElements.addRectangle(groundBegin, scopeDisTop+postHeight, width+extraGround, groundThickness,"stroke-width:1px; stroke:#000000; fill: #ffffff"  );
    }

    public void sideViewBeam(int length) {
        double beamHeight = 19.5;
        int scopeDistTop = 100;

        carportSVGElements.addRectangle(scopeDisSide, scopeDistTop, length, beamHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");

    }
    public void sideViewPost(int length, int width) {

        int postWidth = 10;

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

        int totalOverhang = (width - maxPostDistanceBeamPostsOuterMeassure + postDimension);

        double addedFrontOverHang = (2.0 / 3.0) * totalOverhang;
        System.out.println((width - (maxPostDistanceBeamPostsOuterMeassure+postDimension)));

        double addedBackOverHang = (1.0 / 3.0) * totalOverhang;

        if (width <= maxPostDistanceBeamPostsOuterMeassure) {
            // Post one
            carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, postDimension, postHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - postDimension, scopeDisTop, postDimension, postHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");


        } else if (width > maxPostDistanceBeamPostsOuterMeassure && width <= maxCarportLenghtFourPosts) {

            // Makes sure that back roof over hang is never over 30 works because its the last increment
            if (addedBackOverHang > maxBackRoofOverhang) {
                addedFrontOverHang += addedBackOverHang - maxBackRoofOverhang;
                addedBackOverHang = maxBackRoofOverhang;
            }

            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDisTop, postDimension, postHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + width - addedBackOverHang - postDimension, scopeDisTop, postDimension, postHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");

        }
        else if (width > maxCarportLenghtFourPosts) {
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
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDisTop, postDimension, postHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // Post two in middle on same beam row
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDisTop, postDimension, postHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");
            // Post three end of top beam row
            carportSVGElements.addRectangle(scopeDisSide + width - sixPostsAddedBackOverHang -postDimension, scopeDisTop, postDimension, postHeight, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        }
    }

    @Override
    public String toString()
    {
        return carportSVGElements.toString();
    }
}