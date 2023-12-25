package dp1key.cfs.svg.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XMLHandler {

    public static Document getDocumentFromFile(String path) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File("src/main/resources/" + path));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getSvgPathsFromDocument(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("path");
        String[] d = new String[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            d[i] = nodeList.item(i).getAttributes().getNamedItem("d").getNodeValue();
        }
        return d;
    }
}
