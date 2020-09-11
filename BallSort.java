import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;

public class BallSort {

    public enum Color {

        NIL,
        ORG,
        RED,
        BLU,
        PNK,
        GRN,
        GRY,
        SKY,
        OLV,
        PRP,
        YLW,
        NVY

    }

    public static class Pair<T, U> {

        private final T fst;
        private final U snd;

        public Pair(T t, U u) {
            this.fst = t;
            this.snd = u;
        }

        @Override
        public String toString() {
            return String.format("[%s, %s]", this.fst, this.snd);
        }

    }

    public static class Ball {

        private static final Map<String, Color> map = new HashMap<>();

        static {
            map.put("org", Color.ORG);
            map.put("red", Color.RED);
            map.put("blu", Color.BLU);
            map.put("pnk", Color.PNK);
            map.put("grn", Color.GRN);
            map.put("gry", Color.GRY);
            map.put("sky", Color.SKY);
            map.put("olv", Color.OLV);
            map.put("prp", Color.PRP);
            map.put("ylw", Color.YLW);
            map.put("nvy", Color.NVY);
        }

        private final Color color;

        public Ball(Color color) {
            this.color = color;
        }

        public boolean equals(Ball ball) {
            return this.color == ball.color;
        }

        public Ball clone() {
            return new Ball(this.color);
        }

        public static Ball parse(String str) {
            return new Ball(map.getOrDefault(str, Color.NIL));
        }

        public boolean validate() {
            return this.color != Color.NIL;
        }

        @Override
        public String toString() {
            return this.color.toString();
        }

    }

    public static class Tube {

        private int ballCount = 0;
        private final Ball[] balls = new Ball[4];

        public Tube(Ball ... balls) {
            for (Ball b : balls) {
                this.balls[ballCount++] = b;
            }
        }

        public Tube clone() {
            Tube tube = new Tube();
            for (Ball b : this.balls) {
                if (b != null) {
                    tube.push(b.clone());
                }
            }
            return tube;
        }

        public boolean isEmpty() {
            return this.ballCount == 0;
        }

        public boolean isFull() {
            return this.ballCount == 4;
        }

        public int size() {
            return this.ballCount;
        }

        public void push(Ball ball) {
            this.balls[this.ballCount++] = ball;
        }

        public Ball pop() {
            Ball ball = this.balls[--this.ballCount];
            this.balls[this.ballCount] = null;
            return ball;
        }

        public Ball peek() {
            return this.balls[this.ballCount - 1];
        }

        public boolean canMoveTo(Tube tube) {
//            if (this.isComplete() || this.isEmpty() || tube.isFull()) {
//                return false;
//            } else {
//                if (!tube.isEmpty()) {
//                    return this.peek().equals(tube.peek()) &&
//                            (this.size() == 1 ||
//                                !this.balls[this.ballCount - 1].equals(this.balls[this.ballCount - 2]));
//                } else {
//                    return true;
//                }
//            }
            return !this.isComplete() && !tube.isFull() &&
                    (tube.isEmpty() || tube.peek().equals(this.peek()));
        }

        public boolean equals(Tube tube) {
            if (this.size() == tube.size()) {
                for (int i = 0; i < this.size(); i++) {
                    if (!this.balls[i].equals(tube.balls[i])) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        public boolean isComplete() {
            return this.isEmpty()
                    || (this.isFull()
                        && this.balls[0].equals(this.balls[1])
                        && this.balls[0].equals(this.balls[2])
                        && this.balls[0].equals(this.balls[3]));
        }

        public boolean validate() {
            // for some odd reason, the stream method is faster
//            boolean allMatchBoolean = true;
//            int i = 0;
//            while (allMatchBoolean && i < this.balls.length) {
//                if (balls[i] == null) {
//                    break;
//                } else {
//                    allMatchBoolean = balls[i].validate();
//                    i++;
//                }
//            }
//            return allMatchBoolean;
            return Arrays.stream(this.balls)
                    .filter(Objects::nonNull)
                    .allMatch(Ball::validate);
        }

        @Override
        public String toString() {
            return Arrays.toString(this.balls);
        }

    }

    public static class State {

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

        private int completeTubes() {
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

    public static class StopWatch {

        long startTime = 0;
        long endTime = 0;
        long total = 0;
        boolean running = false;

        StopWatch() {
            reset();
        }

        void reset() {
            startTime = 0;
            endTime = 0;
            total = 0;
            running = false;
        }

        void start() {
            startTime = System.nanoTime();
            running = true;
        }

        void stop() {
            endTime = System.nanoTime();
            total += (endTime - startTime);
            running = false;
        }

        float getTime() {
            float r = total;
            r /= 1000000000;
            return r;
        }
    }

    public static void sort(int num, State state) {

        StopWatch sw = new StopWatch();
        sw.start();

        var steps = new PriorityQueue<Pair<State, List<Pair<Integer, Integer>>>>(
                (x, y) -> (y.fst.completeTubes() - x.fst.completeTubes()));
        var allStates = new HashSet<State>();
        List<Pair<Integer, Integer>> sol = null;
        steps.add(new Pair<>(state, new ArrayList<>()));
        while (!steps.isEmpty()) {
            var move = steps.poll();
            var currState = move.fst;
            var prevSteps = move.snd;
            if (currState.isComplete()) {
                sol = prevSteps;
                break;
            }
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                    if (i != j && currState.canMove(i, j)) {
                        var newState = currState.move(i, j);
                        if (!allStates.contains(newState)) {
                            var nextSteps = new ArrayList<>(prevSteps);
                            nextSteps.add(new Pair<>(i, j));
                            allStates.add(newState);
                            steps.add(new Pair<>(newState, nextSteps));
                        }
                    }
                }
            }
        }
        if (sol == null) {
            System.out.println("No solution detected; likely due to input error.");
        } else {
            System.out.println();
            int index = 0;
            for (Pair<Integer, Integer> p : sol) {
                System.out.printf("%02d: Move from tube %d to %d\n", ++index, p.fst + 1, p.snd + 1);
                if (index % 5 == 0) {
                    System.out.println();
                }
            }
            System.out.printf("\nSolution requires %d moves.", sol.size());
        }

        sw.stop();
        System.out.printf("\nIterated through %d states.\n", allStates.size());
        System.out.printf("Algorithm took %f seconds to run.\n", sw.getTime());
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        label:
        while (true) {
            System.out.print("Enter a command (use help to view commands):");
            String command = sc.nextLine();
            switch (command) {
            case "exit":
                break label;
            case "help":
                System.out.println("\nUse \"exit\" to exit.");
                System.out.println("Use \"sort\" to sort.");
                System.out.println("Use load to load from a file.");
                System.out.println("\nWhen entering into the solver, use the following format, from bottom to top: ");
                System.out.println("\"[CLR1]<space>[CLR2]<space>[CLR3]<space>[CLR4]\"");
                System.out.println("For instance, a tube with four blue balls should be:");
                System.out.println("\"BLU BLU BLU BLU\"");
                System.out.println("\nHere are the codes for colors: ");
                System.out.println("\"org\" - orange,\n" +
                        "\"red\" - red,\n" +
                        "\"blu\" - blue,\n" +
                        "\"pnk\" - pink,\n" +
                        "\"grn\" - green,\n" +
                        "\"gry\" - grey,\n" +
                        "\"sky\" - sky blue,\n" +
                        "\"olv\" - olive,\n" +
                        "\"prp\" - purple\n" +
                        "\"ylw\" - yellow\n" +
                        "\"nvy\" - navy\n");
                System.out.println("Use \"code\" if you want to see the codes only.\n");
                break;
            case "code":
                System.out.println("\nHere are the codes for colors: ");
                System.out.println("\"org\" - orange,\n" +
                        "\"red\" - red,\n" +
                        "\"blu\" - blue,\n" +
                        "\"pnk\" - pink,\n" +
                        "\"grn\" - green,\n" +
                        "\"gry\" - grey,\n" +
                        "\"sky\" - sky blue,\n" +
                        "\"olv\" - olive,\n" +
                        "\"prp\" - purple\n" +
                        "\"ylw\" - yellow\n" +
                        "\"nvy\" - navy\n");
                break;
            case "load":
                System.out.print("\nEnter file path here: ");
                String filePath = sc.nextLine();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(filePath));
                    int num = Integer.parseInt(br.readLine());
                    State state = new State(num);
                    for (int i = 0; i < num; i++) {
                        String string = br.readLine();
                        if (string != null) {
                            String[] input = string.split(" ");
                            Ball[] balls = new Ball[input.length];
                            for (int j = 0; j < input.length; j++) {
                                balls[j] = Ball.parse(input[j]);
                            }
                            if (Arrays.stream(balls).allMatch(Ball::validate)) {
                                state.addTube(new Tube(balls));
                            } else {
                                System.out.println("Invalid input detected; try again.");
                            }
                        } else {
                            state.addTube(new Tube());
                        }
                    }
                    br.close();
                    if (state.validate()) {
                        sort(num, state);
                    } else {
                        System.out.println("Invalid input detected; try again.");
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("No file detected.");
                } catch (IOException e) {
                    System.out.println("Something went wrong with the I/O; please try again.");
                }
                break;
            case "sort":
                System.out.print("\nEnter the number of tubes: ");
                int num = sc.nextInt();
                sc.nextLine();
                State state = new State(num);
                int count = 0;
                while (count < num) {
                    System.out.printf("Enter the color of the balls for tube %d (bottom to top): ", count + 1);
                    String string = sc.nextLine();
                    if (!string.equals("")) {
                        String[] input = string.split(" ");
                        Ball[] balls = new Ball[input.length];
                        for (int j = 0; j < input.length; j++) {
                            balls[j] = Ball.parse(input[j]);
                        }
                        if (Arrays.stream(balls).allMatch(Ball::validate)) {
                            state.addTube(new Tube(balls));
                            count++;
                        } else {
                            System.out.println("Invalid input detected; try again.");
                        }
                    } else {
                        state.addTube(new Tube());
                        count++;
                    }
                }
                sort(num, state);
                break;
            default:
                System.out.println("\nInvalid command detected; try again.\n");
            }
        }
    }
}