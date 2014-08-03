import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Component {

    public List<Edge> pq = new ArrayList<>();
    public double totalWeight = 0;
    public long totalEdges = 0;
    ReentrantLock locker = new ReentrantLock();

    public Component() {

    }

    public boolean isDead = false;

    void addEdge(final Edge e) {
        // insert edge in weight order

        int i = 0;
        while (i < pq.size()) {
            if (e.weight < pq.get(i).weight) {
                break;
            }
            i++;
        }
        pq.add(i, e);

    }

    public Edge getMinEdge() {
        if (pq.size() == 0) {
            return null;
        }
        return pq.get(0);
    }

    void merge(final Component other, final double edgeWeight) {

        totalWeight += other.totalWeight + edgeWeight;
        totalEdges += other.totalEdges + 1;
        final List<Edge> npq = new ArrayList<Edge>();
        int i = 0, j = 0;
        while (i + j < pq.size() + other.pq.size()) {
            // get rid of intercomponent edges
            while (i < pq.size()) {
                final Edge e = pq.get(i);
                if ((e.fromComponent != this && e.fromComponent != other) || (e.toComponent != this && e.toComponent != other)) {
                    break;
                }
                i++;
            }
            while (j < other.pq.size()) {
                final Edge e = other.pq.get(j);
                if ((e.fromComponent != this && e.fromComponent != other) || (e.toComponent != this && e.toComponent != other)) {
                    break;
                }
                j++;
            }
            if (j < other.pq.size() && (i >= pq.size() || pq.get(i).weight > other.pq.get(j).weight)) {
                npq.add(other.pq.get(j++).replaceComponent(other, this));
            } else if (i < pq.size()) {
                npq.add(pq.get(i++).replaceComponent(other, this));
            }
        }
        other.pq.clear();
        pq.clear();
        pq = npq;

    }
}
