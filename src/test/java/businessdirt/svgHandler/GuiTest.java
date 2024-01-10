package businessdirt.svgHandler;

import businessdirt.svgHandler.svg.ComplexNumber;
import businessdirt.svgHandler.svg.parser.Parser;
import businessdirt.svgHandler.svg.parser.XMLHandler;
import businessdirt.svgHandler.svg.path.Path;
import org.w3c.dom.Document;

public class GuiTest {

    private static Gui window;

    public static void main(String[] args) {
        GuiTest.window = new Gui();
        window.setDrawLines(true);

        Document doc = XMLHandler.getDocumentFromFile("src/test/resources/twitter.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);
        Path[] parser = Parser.parsePaths(paths);

        // normal
        ComplexNumber offset = new ComplexNumber(0.0, 0.0);
        drawFigure(parser[0], 5, offset);

    }

    private static void drawFigure(Path path, int n, ComplexNumber offset) {
        Draw.Figure figure = new Draw.Figure();
        for (int i = 0; i < n; i++) {
            ComplexNumber point = path.point(i / (double) n);
            System.out.println(point);
            figure.add(ComplexNumber.add(point, offset));
        }
        window.drawFigure(figure);
    }
}