package net.stuxcrystal.configuration.generators.xml;

import net.stuxcrystal.configuration.node.Node;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Dumps an Node-Tree into a stream.
 */
class CommentAwareDumper {

    /**
     * Internal Reference to the dumper.
     */
    private static final CommentAwareDumper DUMPER = new CommentAwareDumper();

    /**
     * Internal reference to the default XML declaration.
     */
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

    private CommentAwareDumper() {
    }

    /**
     * Dumps a node tree into a XML-File.
     *
     * @param stream
     * @param node
     * @throws IOException
     */
    public static void dump(OutputStream stream, Node<?> node) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
        writer.write(XML_DECLARATION);
        writer.newLine();

        // Set the node name if necessary.
        if (node.getName() == null || node.getName().isEmpty())
            node.setName("xml");

        DUMPER.dumpNode(writer, node, 0);
        writer.close();
    }

    /**
     * Dumps the content of a node.
     *
     * @param writer
     * @param node
     * @param indent
     * @throws IOException
     */
    private void dump(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        if (node.hasChildren()) {
            writer.newLine();
            for (Node<?> child : ((Node<Node<?>[]>) node).getData()) {
                dumpComment(writer, child, indent);
                dumpNode(writer, child, indent);
            }
            writeIndent(writer, indent - 1);

        } else {
            dumpScalar(writer, node);
        }
    }

    /**
     * Dumps the content of a string node.
     *
     * @param writer
     * @param node
     * @throws IOException
     */
    private void dumpScalar(BufferedWriter writer, Node<?> node) throws IOException {
        writer.write(StringEscapeUtils.escapeXml(((Node<String>) node).getData()));
    }

    /**
     * Dumps the content of a single node.
     *
     * @param writer
     * @param node
     * @param indent
     * @throws IOException
     */
    private void dumpNode(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        String name = (node.getName() != null && !node.getName().isEmpty()) ? node.getName() : "item";

        writeIndent(writer, indent);

        writer.write("<");
        writer.write(name);
        writer.write(">");

        dump(writer, node, indent + 1);

        writer.write("</");
        writer.write(name);
        writer.write(">");

        writer.newLine();
    }

    /**
     * Dumps a comment.
     *
     * @param writer
     * @param node
     * @param indent
     * @throws IOException
     */
    private void dumpComment(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        String[] comment = node.getComments();

        for (String line : comment) {
            writeIndent(writer, indent);
            if (!(line == null || line.isEmpty())) {
                writer.write("<!-- ");
                writer.write(line);
                writer.write(" -->");
            }

            writer.newLine();
        }
    }

    /**
     * Writes an indent.
     *
     * @param writer
     * @param indent
     * @throws IOException
     */
    private void writeIndent(BufferedWriter writer, int indent) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) sb.append(" ");
        writer.write(sb.toString());
    }

}
