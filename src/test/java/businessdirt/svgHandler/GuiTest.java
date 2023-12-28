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

        // normal
        ComplexNumber offset = new ComplexNumber(000.0, 0.0);
        drawFigure(parser[0], 50, offset);

        System.out.println("---------------------");

        // reversed
        ComplexNumber offset2 = new ComplexNumber(200.0, 0.0);
        Parser reversed = parser[0].clone();
        reversed.path().reverse();
        drawFigure(reversed, 50, offset2);
    }

    private static void drawFigure(Parser parser, int n, ComplexNumber offset) {
        Draw.Figure figure = new Draw.Figure();
        for (int i = 0; i < n; i++) {
            ComplexNumber point = parser.path().point(i / (double) n);
            System.out.println(point);
            figure.add(ComplexNumber.add(point, offset));
        }
        window.drawFigure(figure);
    }
}