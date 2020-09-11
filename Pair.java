public class Pair<T, U> {

    private final T fst;
    private final U snd;

    public Pair(T t, U u) {
        this.fst = t;
        this.snd = u;
    }

    public T fst() {
        return this.fst;
    }

    public U snd() {
        return this.snd;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.fst, this.snd);
    }

}