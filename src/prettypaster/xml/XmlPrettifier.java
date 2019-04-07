package prettypaster.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import prettypaster.Prettifier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Prettifies xml by placing newlines after tags and indenting 2 spaces.
 */
public class XmlPrettifier implements Prettifier {

    private final DocumentBuilder docBuilder;
    {
        var factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Somehow the document builder was misconfigured.", e);
        }
    }

    private final Transformer transformer;
    {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException("Somehow the transformer was misconfigured.", e);
        }
    }

    @Override
    public Optional<String> prettify(String in) {
        return Optional.of(in)
                .map(this::asXmlDoc)
                .map(this::asPrettyString);
    }

    private Document asXmlDoc(String content) {
        try (var is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            return docBuilder.parse(is);
        } catch (SAXException | IOException e) {
            return null; // Unable to parse as xml doc so assume it's not valid xml
        }
    }

    private String asPrettyString(Document doc) {
        recursivelyRemoveEmptyWhitespace(doc);
        doc.normalizeDocument();

        try (var sw = new StringWriter()) {
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (IOException | TransformerException e) {
            return null; // Unable to pretty print the xml, not much we can do
        }
    }

    private static void recursivelyRemoveEmptyWhitespace(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) removeEmptyWhitespaceFrom(node);

        var children = node.getChildNodes();
        IntStream.range(0, children.getLength())
                .mapToObj(children::item)
                .forEach(XmlPrettifier::recursivelyRemoveEmptyWhitespace);
    }

    private static void removeEmptyWhitespaceFrom(Node node) {
        var trimmedContent = node.getTextContent().trim();
        if (trimmedContent.length() == 0) node.setTextContent(trimmedContent);
    }

}
