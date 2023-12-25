package dp1key.cfs;

import dp1key.cfs.svg.parser.Parser;
import dp1key.cfs.svg.parser.XMLHandler;
import dp1key.cfs.svg.path.Path;
import org.w3c.dom.Document;

public class Main {
    public static void main(String[] args) {
        Document doc = XMLHandler.getDocumentFromFile("twitter.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);
        Path[] parsed = Parser.parsePaths(paths);

        for (Path path : parsed) {
            System.out.println(path);
        }
    }
}