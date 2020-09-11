import java.util.Arrays;

public class State {

    private int tubeCount = 0;
    private final Tube[] tubes;

    public State(int num) {
        this.tubes = new Tube[num];
    }

    public State(State state) {
        this.tubes = new Tube[state.tubes.length];
        for (Tube tube : state.tubes) {
            this.addTube(tube.clone());
        }
    }

    public State addTube(Tube tube) {
        this.tubes[tubeCount++] = tube;
        return this;
    }

    public boolean canMove(int tubeFrom, int tubeTo) {
        return this.tubes[tubeFrom].canMoveTo(this.tubes[tubeTo]);
    }

    public State move(int tubeFrom, int tubeTo) {
        State state = new State(this);
        Ball ball = state.tubes[tubeFrom].pop();
        state.tubes[tubeTo].push(ball);
        return state;
    }

    @Override
    public int hashCode() {
        int[] hash = new int[this.tubes.length];
        for (int i = 0; i < this.tubes.length; i++) {
            hash[i] = this.tubes[i].size();
        }
        Arrays.sort(hash);
        return Arrays.hashCode(hash);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof State) {
            boolean allMatchBoolean = true;
            int i = 0;
            while (allMatchBoolean && i < this.tubes.length) {
                int j = 0;
                boolean anyMatchBoolean = false;
                while (!anyMatchBoolean && j < ((State) object).tubes.length) {
                    anyMatchBoolean = this.tubes[i].equals(((State) object).tubes[j]);
                    j++;
                }
                allMatchBoolean = anyMatchBoolean;
                i++;
            }
            return allMatchBoolean;
        } else {
            return false;
        }
    }

    public boolean validate() {
        boolean allMatchBoolean = true;
        int i = 0;
        while (allMatchBoolean && i < this.tubes.length) {
            allMatchBoolean = this.tubes[i].validate();
            i++;
        }
        return allMatchBoolean;
    }

    public boolean isComplete() {
        boolean allMatchBoolean = true;
        int i = 0;
        while (allMatchBoolean && i < this.tubes.length) {
            allMatchBoolean = this.tubes[i].isComplete();
            i++;
        }
        return allMatchBoolean;
    }

    public int completeTubes() {
        int count = 0;
        for (Tube t : this.tubes) {
            if (t.isComplete()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.tubes);
    }

}