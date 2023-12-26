package dp1key.cfs;

import dp1key.cfs.svg.ComplexNumber;
import dp1key.cfs.svg.parser.Parser;
import dp1key.cfs.svg.parser.XMLHandler;
import dp1key.cfs.svg.path.Path;
import org.w3c.dom.Document;

public class GuiTest {


    public static void main(String[] args) {
        Gui window = new Gui();
        window.setDrawLines(true);

        Document doc = XMLHandler.getDocumentFromFile("src/test/resources/twitter.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);
        Path[] parsed = Parser.parsePaths(paths);

        // generated n points on the path and draw them
        int n = 1000;
        for (int i = 0; i < n; i++) {
            ComplexNumber point = parsed[0].point(i / (double) n);
            window.drawComplexNumber(point);
        }
    }
}