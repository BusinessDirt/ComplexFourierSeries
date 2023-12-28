package businessdirt.svgHandler;

import businessdirt.svgHandler.svg.ComplexNumber;
import businessdirt.svgHandler.svg.parser.Parser;
import businessdirt.svgHandler.svg.parser.XMLHandler;
import org.w3c.dom.Document;

public class GuiTest {

    private static Gui window;


    public static void main(String[] args) {
        GuiTest.window = new Gui();
        window.setDrawLines(true);

        Document doc = XMLHandler.getDocumentFromFile("src/test/resources/twitter.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);
        Parser[] parser = Parser.parsePaths(paths);

        // generated n points on the path and draw them
        ComplexNumber offset = new ComplexNumber(0.0, 0.0);
        drawFigure(parser[0], 50, offset);
    }

    private static void drawFigure(Parser parser, int n, ComplexNumber offset) {
        for (int i = 0; i < n; i++) {
            ComplexNumber point = parser.path().point(i / (double) n);
            window.drawComplexNumber(ComplexNumber.add(point, offset));
        }
    }
}