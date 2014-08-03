import java.util.Comparator;

class Edge implements Comparable<Edge> {

    Component fromComponent, toComponent;
    public double weight;

    public static class EdgeComparator implements Comparator<Edge> {
        public int compare(final Edge e1, final Edge e2) {
            if (e1.weight == e2.weight) {
                return 0;
            } else if (e1.weight < e2.weight) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public Edge(final Component from, final Component to, final double weight) {
        this.fromComponent = from;
        this.toComponent = to;

        this.weight = weight;
    }

    public Component getOther(final Component from) {
        if (fromComponent == from) {
            assert (toComponent != from);
            return toComponent;
        }

        if (toComponent == from) {
            assert (fromComponent != from);
            return fromComponent;
        }
        assert (false);
        return null;

    }

    public int compareTo(final Edge e) {
        if (e.weight == weight) {
            return 0;
        } else if (weight < e.weight) {
            return -1;
        } else {
            return 1;
        }
    }

    public Edge replaceComponent(final Component from, final Component to) {
        if (fromComponent == from) {
            fromComponent = to;
        }
        if (toComponent == from) {
            toComponent = to;
        }
        return this;
    }

}
