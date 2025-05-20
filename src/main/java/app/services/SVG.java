package app.services;


public class SVG {


    private static final String SVG_Template = "<SVG version=\"1.1\"\n" +
            " x=\"%d\" y=\"%d\"\n" + " viewBox=\"%s\" width=\"%s\" height=\"auto\" \n" +
            "     preserveAspectRatio=\"xMinYMin\">\n";

    private static final String SVG_Rectangle_Template ="<rect x=\"%f\" y=\"%f\" height=\"%f\" width=\"%f\"\n" +
            " style=\"%s\"/>";

    private static final String SVG_Line_Template ="<line x1=\"%.2f\" y1=\"%.2f\" x2=\"%.2f\" y2=\"%.2f\" " +
            "style=\"%s\" />";

    private static final String SVG_LineSpaces_Template ="<line x1=\"%.2f\" y1=\"%.2f\" x2=\"%.2f\" y2=\"%.2f\" " +
            "style=\"%s\" " +  "stroke=\"black\" stroke-width=\"%d\" stroke-dasharray=\"5,5\" />";

    private static final String SVG_Arrow_Def = "  <defs>\n" +
            "        <marker\n" + " id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"10\" refX=\"0\" refY=\"6\"\n" +
            "                orient=\"auto\"> <path d=\"M0,6 L12,0 L8,6 L12,10 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "        <marker\n" + " id=\"endArrow\" markerWidth=\"12\" markerHeight=\"10\" refX=\"12\" refY=\"6\"\n" +
            "       orient=\"auto\"><path d=\"M0,0 L12,6 L0,10 L4,6 L0,0 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" + "  </defs>";

    private static final String SVG_Line_With_Arrows_Template ="<line x1=\"%.2f\" y1=\"%.2f\" x2=\"%.2f\" y2=\"%.2f\" " +
            "style=\"%s\" " +  "marker-start=\"url(#beginArrow)\" " +
            "marker-end=\"url(#endArrow)\" />";

    private static final String SVG_Text = "<text style=\"text-anchor: middle\" transform=\"translate(%d,%d) rotate(%d)\">%s</text>";

    /* Java class used to make it easier to manipulate the string templates for the drawing objects
    and append the templates in to the drawing functions */
    private StringBuilder svg = new StringBuilder();

    public SVG(int x, int y, String viewBox, String width){

        svg.append(String.format(SVG_Template, x, y, viewBox, width));
        svg.append(SVG_Arrow_Def);
    }

    public void addRectangle(double x, double y, double height, double width, String style){

        svg.append(String.format(SVG_Rectangle_Template, x, y, height, width, style));

    }

    public void addLine(double x1, double y1, double x2, double y2, String style) {

        svg.append(String.format(SVG_Line_Template, x1, y1, x2, y2, style));
    }

    public void addLineSpaces(int x1, int y1, int x2, int y2, String style){

        svg.append(String.format(SVG_LineSpaces_Template, x1, y1, x2, y2, style));
    }

    public void addArrow(double x1, double y1, double x2, double y2, String style){

        //   svg.append(String.format(SVG_Arrow_Def, x1, y1, x2, y2, style));
        //Call addLine with a style containing Arrow heads
        svg.append(String.format(SVG_Line_With_Arrows_Template, x1, y1, x2, y2, style));
    }

    public void addText(int x, int y, int rotation, String text){

        svg.append(String.format(SVG_Text, x, y, rotation, text));
    }
    public void addSvg(SVG innerSvg){

        svg.append(innerSvg.toString());
    }

    @Override
    public String toString() {

        return svg.append("</svg>").toString();
    }
  /*  SVG carportSVG = new SVG(0, 0, "0, 0, 855, 690", "100%", "auto" );
   ctx.attribute("svg thymrleaf navn", carportSVG.toString());
    ctx.render(HTML);

    i routes for at sætte det til . istedet for , eller lignenge (dyn vid 2 32.30): Locale.SetDefault(new Locale(US));
    css.Styling for div omkring tegningen  max-width 600px;  max-height 600px;*/

}