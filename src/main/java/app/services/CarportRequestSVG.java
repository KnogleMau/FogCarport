package app.services;

public class CarportRequestSVG {
    private double length;
    private double width;
    int postHeight = 290;
    double beamHeight = 19.5;
    int postDimension = 10;  // On both side

    private SVG carportSVGElements;

    double scopeDisTop = 300; // Distance from top of scope to where carport drawing begin
    double scopeDisSide = 100; // Distance from top of scope to where carport drawing begin

    public CarportRequestSVG(double length, double width) {
        this.length = length;
        this.width = width;

        int intLength = (int) length;
        int intWidth = (int) width;

        carportSVGElements = new SVG(0, 0, "0 0 1200 650" , "100%");

        sideViewPost(intLength, intWidth);
        ground(intLength);
        sideViewBeam(intLength);
        carportFrontView(intWidth,intLength);
    }

    public void carportFrontView(int width, int length) {
        int distDrawings = 80;
        int scopeDistSide = (int) scopeDisSide;
        int basepoint = scopeDistSide + distDrawings + length;
        int sideRoofOverhang = (600 - 530) / 2;  // Values from delivered topVieW drawing in carport build guide (half dist roof width - post placements regards carport width).


        // First post from left.
        carportSVGElements.addRectangle(basepoint + sideRoofOverhang, scopeDisTop, postHeight, postDimension, "stroke:#000000; fill: #F5DEB3");
        // Last post from left. X = - sideRoofOverhang - postDimension to get a roof overhang
        carportSVGElements.addRectangle(basepoint - sideRoofOverhang -postDimension + width, scopeDisTop, postHeight, postDimension, "stroke:#000000; fill: #F5DEB3");
        // RoofBase.
        carportSVGElements.addRectangle(basepoint, scopeDisTop, beamHeight, width, "stroke:#000000; fill: #F5DEB3");
    }

    public void ground (int length){
        double groundBegin = scopeDisSide - 80; // adjust ammount of ground on left side of carport
        double groundThickness = 40;
        double extraGround = 200;  // makes the ground longer than the carport
        carportSVGElements.addRectangle(groundBegin, scopeDisTop +postHeight, groundThickness,width+length+extraGround ,"stroke:#000000; fill: #404040"  );
    }

    public void sideViewBeam(int length) {

        carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, beamHeight , length , "stroke:#000000; fill: #F5DEB3");

    }

    public void sideViewPost(int length, int width) {

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

        int totalOverhang = (length - maxPostDistanceBeamPostsOuterMeassure + postDimension);

        double addedFrontOverHang = (2.0 / 3.0) * totalOverhang;
        System.out.println((length - (maxPostDistanceBeamPostsOuterMeassure+postDimension)));

        double addedBackOverHang = (1.0 / 3.0) * totalOverhang;

        if (length <= maxPostDistanceBeamPostsOuterMeassure) {
            // Post one
            carportSVGElements.addRectangle(scopeDisSide, scopeDisTop, postHeight ,postDimension , " stroke:#000000; fill: #F5DEB3");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + length - postDimension, scopeDisTop, postHeight ,postDimension , " stroke:#000000; fill: #F5DEB3");


        } else if (length > maxPostDistanceBeamPostsOuterMeassure && length <= maxCarportLenghtFourPosts) {

            // Makes sure that back roof over hang is never over 30 works because its the last increment
            if (addedBackOverHang > maxBackRoofOverhang) {
                addedFrontOverHang += addedBackOverHang - maxBackRoofOverhang;
                addedBackOverHang = maxBackRoofOverhang;
            }

            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + addedFrontOverHang, scopeDisTop, postHeight ,postDimension , " stroke:#000000; fill: #F5DEB3");
            // post two same beam row
            carportSVGElements.addRectangle(scopeDisSide + length - addedBackOverHang - postDimension, scopeDisTop, postHeight, postDimension, " stroke:#000000; fill: #F5DEB3");

        }
        else if (length > maxCarportLenghtFourPosts) {
            // 20 is added to the distance calculated on, to make sure that roof over hang are even number at 2/# and 1/3 distributions
            int totalOverhangSixPosts = (length - maxCarportLenghtFourPosts + 20);
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
            double midtPostPlacementY = (( length - sixPostsAddedFrontOverHang - sixPostsAddedBackOverHang) / 2 ) - (postDimension / 2);
            System.out.println("Afstand stolpe 1 og 2: " + midtPostPlacementY);
            System.out.println("sixpostfront: " + sixPostsAddedFrontOverHang + " sixpostback: " + sixPostsAddedBackOverHang);

            // Post one top left corner horizontally drawing in carport manual
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang, scopeDisTop, postHeight, postDimension, "stroke:#000000; fill: #F5DEB3");
            // Post two in middle on same beam row
            carportSVGElements.addRectangle(scopeDisSide + sixPostsAddedFrontOverHang + midtPostPlacementY, scopeDisTop, postHeight, postDimension, "stroke:#000000; fill: #F5DEB3");
            // Post three end of top beam row
            carportSVGElements.addRectangle(scopeDisSide + length - sixPostsAddedBackOverHang -postDimension, scopeDisTop, postHeight, postDimension, "stroke:#000000; fill: #F5DEB3");
        }
    }

    @Override
    public String toString()
    {
        return carportSVGElements.toString();
    }
}