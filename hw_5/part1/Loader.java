import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.GZIPInputStream;

/**
 * @author Shams Imam (shams@rice.edu)
 */
class Loader {
    static Map<Integer, Component> nodes = new ConcurrentHashMap<>();
    static Map<Pair<Integer, Integer>, Edge> edges = new ConcurrentHashMap<>();
    static Queue<Component> nodesLoaded = new ConcurrentLinkedQueue<>();//LinkedList<>();

    static Component getComponent(final int node) {
        if (!nodes.containsKey(node)) {
            nodes.put(node, new Component());
        }
        return nodes.get(node);
    }

    static void addEdge(final int from, final int to, final Component fromC, final Component toC, final double w) {
        final Pair<Integer, Integer> p;
        if (from < to) {
            p = new Pair<>(from, to);
        } else {
            p = new Pair<>(to, from);
        }
        if (!edges.containsKey(p)) {
            final Edge e = new Edge(fromC, toC, w);
            edges.put(p, e);
            fromC.addEdge(e);
            toC.addEdge(e);
        } else {
            assert (edges.get(p).weight == w);
        }
    }

    static void read(final String filename) {
        System.out.println("Sequential version (seq): Reading " + filename);
        double totweight = 0;
        int edges = 0;
        try {
            // Open the compressed file
            final GZIPInputStream in = new GZIPInputStream(new FileInputStream(filename));
            final Reader r = new BufferedReader(new InputStreamReader(in));
            final StreamTokenizer st = new StreamTokenizer(r);
            final String cstring = "c";
            final String pstring = "p";
            st.commentChar(cstring.charAt(0));
            st.commentChar(pstring.charAt(0));
            // read graph
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                assert (st.sval.equals("a"));
                st.nextToken();
                final int from = (int) st.nval;
                st.nextToken();
                final int to = (int) st.nval;
                final Component nodeFrom = getComponent(from);
                final Component nodeTo = getComponent(to);
                assert (nodeTo != nodeFrom); // Assume no self-loops in the input graph
                st.nextToken();
                final int weight = (int) st.nval;
                addEdge(from, to, nodeFrom, nodeTo, weight);
                totweight += weight;
                edges++;
            }
            // Close the file and stream
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final List<Component> nd = new ArrayList<>();
        nd.addAll(nodes.values());
        Collections.shuffle(nd);
        nodesLoaded.addAll(nd);
        nodes.clear();
        Loader.edges.clear();

        System.out.println("loaded " + nodesLoaded.size() + " - edges " + edges + "- total weight " + totweight);
    }
} // class Loader
