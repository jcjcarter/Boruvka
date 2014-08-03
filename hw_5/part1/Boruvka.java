import java.util.concurrent.locks.ReentrantLock;

public class Boruvka {
    static int workers;
    static public long runtime;
    static ReentrantLock locker = new ReentrantLock();

    public static void main(final String[] argv) {

        for(int i = 0; i < 10; i++){
            System.out.println("Run Number: " + i);
	    workers = Integer.parseInt(argv[1]);
        main(argv[0]);
        }
    }

    public static void main(final String filename) {
        Loader.read(filename); // Exclude file input from timing measurement
        Thread[] holding = new Thread[workers];


        Component[] n = new Component[1];
        // START OF EDGE CONTRACTION ALGORITHM
        for (int i = 0; i < holding.length; i++) {
            holding[i] = new Thread(() -> {
                Component jj;
                while ((jj = Loader.nodesLoaded.poll()) != null) {
                    // poll() removes first element (node n) from the nodesLoaded work-list
                    if (jj.locker.tryLock() == false){
                        continue;
                    }
                    if (jj.isDead) {
                        continue; // node n has already been merged
                    }
                    final Edge e = jj.getMinEdge(); // retrieve n's edge with minimum cost
                    if (e == null) {
                        n[0] = jj;
                        break; // done - we've contracted the graph to a single node
                    }
                    final Component other = e.getOther(jj);
                    if(other.locker.tryLock() == false){
                        jj.locker.unlock();
                        Loader.nodesLoaded.add(jj);
                        continue;
                    }
                    other.isDead = true;


                    jj.merge(other, e.weight); // merge node other into node n
                    other.locker.unlock();
                    jj.locker.unlock();
                    Loader.nodesLoaded.add(jj); // add newly merged n back in the work-list
                }
            });
            //holding[i].start();
        }
        final long start = System.nanoTime();
        for (int i = 0; i < holding.length; i++){
            holding[i].start();
        }

        for (int i = 0; i < holding.length; i++) {
            try {
                holding[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // END OF EDGE CONTRACTION ALGORITHM
        runtime = System.nanoTime() - start;
        System.out.println("Finished with edges = " + n[0].totalEdges + ",  total weight = " + n[0].totalWeight + ", time = " + (runtime / 1000000000.0) + " seconds");
    }
} // class Boruvka


