package org.libex.w3c.dom;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class Documents {

    private static final String INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";

    public static Document createNamespaceAwareXml(
            final String xml)
    {
        return createFromXml(xml, true, true);
    }

    public static Document createFromXml(
            final String xml,
            final boolean namespaceAware,
            final boolean coalescing)
    {
        ByteArrayInputStream inputStream = null;
        inputStream = new ByteArrayInputStream(xml.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);
        factory.setCoalescing(coalescing);
        try {
            Document document = factory.newDocumentBuilder().parse(inputStream);
            return document;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toString(
            final Document document)
    {
        Source xmlSource = new DOMSource(document);
        return transform(xmlSource, false, 0);
    }

    public static String toIndentedString(
            final Document document)
    {
        Source xmlSource = new DOMSource(document);
        return transform(xmlSource, true, 2);
    }

    public static String toIndentedString(
            final String xml)
    {
        Source xmlSource = new StreamSource(new StringReader(xml));
        return transform(xmlSource, true, 2);
    }

    static String transform(
            final Source xmlSource,
            final boolean indent,
            final int indentAmount)
    {
        try {
            StringWriter writer = new StringWriter();
            Result outputTarget = new StreamResult(writer);
            Transformer transformer = getTransformer(indent, indentAmount);
            transformer.transform(xmlSource, outputTarget);
            return writer.toString();
        } catch (TransformerException te) {
            throw new RuntimeException(te.getMessage(), te);
        }
    }

    private static Transformer getTransformer(
            final boolean indent,
            final int indentAmount)
            throws TransformerConfigurationException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        if (indent) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(INDENT_AMOUNT, Integer.toString(indentAmount));
        }
        return transformer;
    }

    private Documents() {
    }

}
