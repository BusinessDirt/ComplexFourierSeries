package businessdirt.svgHandler.svg.path;

import businessdirt.svgHandler.svg.parsing.XMLHandler;
import businessdirt.svgHandler.svg.parsing.generator.Generator;
import businessdirt.svgHandler.svg.parsing.parser.Parser;
import businessdirt.svgHandler.svg.parsing.tokenizer.Tokenizer;
import org.junit.jupiter.api.*;
import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class PathTest {

    private static Path path;


    @BeforeAll
    static void setup() {
        Document doc = XMLHandler.getDocumentFromFile("src/test/resources/twitter.svg");
        String[] paths = XMLHandler.getSvgPathsFromDocument(doc);

        Tokenizer tokenizer = new Tokenizer(paths[0]);
        Parser parser = new Parser(tokenizer);
        Generator generator = new Generator(parser);
        PathTest.path = generator.path();
    }

    @TestFactory
    @DisplayName("Number of points should be the same as the given input n")
    Stream<DynamicTest> testNumberOfPoints_points() {
        int[] data = {5, 20, 100};
        return Arrays.stream(data).mapToObj(entry -> {
            return dynamicTest(String.format("n=%s should result in %s points", entry, entry), () -> {
                assertEquals(entry, path.points(entry).size());
            });
        });
    }
}