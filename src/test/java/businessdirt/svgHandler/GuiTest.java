package businessdirt.svgHandler;

import businessdirt.svgHandler.svg.parser.Parser;
import businessdirt.svgHandler.svg.parser.XMLHandler;
import businessdirt.svgHandler.svg.path.Path;
import com.vm.jcomplex.Complex;
import org.w3c.dom.Document;

public class GuiTest {

    private static Gui window;

    public static void main(String[] args) {
        GuiTest.window = new Gui();
        window.setDrawLines(true);

        Document doc = XMLHandler.getDocumentFromFile("src/test/resources/twitter.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);
        Path[] parser = Parser.parsePaths(paths);

        drawFigure(parser[0], 50);
        System.out.println(parser[0]);

    }

    private static void drawFigure(Path path, int n) {
        Draw.Figure figure = new Draw.Figure();
        for (int i = 0; i < n; i++) {
            Complex point = path.point(i / (double) n);
            figure.add(point.multiply(3));
        }
        window.drawFigure(figure);
    }
}