package businessdirt.svgHandler;

import businessdirt.svgHandler.svg.parser.Parser;
import businessdirt.svgHandler.svg.parser.XMLHandler;
import businessdirt.svgHandler.svg.path.Path;
import com.vm.jcomplex.Complex;
import org.w3c.dom.Document;

import java.awt.*;

public class GuiTest {

    private static Gui window;
    private static final double MULTIPLIER = 3.0;

    public static void main(String[] args) {
        GuiTest.window = new Gui();
        window.setDrawLines(true);

        Document doc = XMLHandler.getDocumentFromFile("src/test/resources/test.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);
        Path[] parser = Parser.parsePaths(paths);

        //drawFigure(parser[0], 5, Color.BLUE);
        drawFigure(parser[0], 10, Color.GREEN);
        //drawFigure(parser[0], 500, Color.ORANGE);

        //drawFigure2(parser[0], 5, Color.BLUE, new Complex(500, 0));
        drawFigure2(parser[0], 10, Color.GREEN, new Complex(500, 0));
        //drawFigure2(parser[0], 500, Color.ORANGE, new Complex(500, 0));
    }

    private static void drawFigure(Path path, int n, Color color) {
        Draw.Figure figure = new Draw.Figure(path.points(n, MULTIPLIER), color);
        window.drawFigure(figure);
    }

    private static void drawFigure2(Path path, int n, Color color, Complex offset) {
        Draw.Figure figure = new Draw.Figure(color);
        for (int i = 0; i < n; i++) {
            figure.add(path.point(i / (double) n).multiply(MULTIPLIER).add(offset));
        }
        window.drawFigure(figure);
    }
}