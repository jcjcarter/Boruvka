public final class Pair<A, B> {
    final private A fst;
    final private B snd;

    public Pair(final A f, final B s) {
        fst = f;
        snd = s;
    }

    public A getFst() {
        return fst;
    }

    public B getSnd() {
        return snd;
    }

    @SuppressWarnings("unchecked")
    final public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }
        final Pair<A, B> p = (Pair<A, B>) o;
        return p.fst.equals(fst) && p.snd.equals(snd);
    }

    final public int hashCode() {
        int hash = 13;
        hash = 31 * hash + (fst == null ? 0 : fst.hashCode());
        hash = 31 * hash + (snd == null ? 0 : snd.hashCode());
        return hash;
    }

    final public String toString() {
        return String.format("<%s,%s>", fst == null ? "null" : fst, snd == null ? "null" : snd);
    }
}
