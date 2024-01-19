package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.parser.Parser;
import businessdirt.svgHandler.svg.parser.XMLHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class PathTest {

    private static Path[] paths;


    @BeforeAll
    static void setup() {
        Document doc = XMLHandler.getDocumentFromFile("src/test/resources/twitter.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);
        PathTest.paths = Parser.parsePaths(paths);
    }
}