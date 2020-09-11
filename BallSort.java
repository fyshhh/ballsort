import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BallSort {

    public enum Color {

        ORG,
        RED,
        BLU,
        PNK,
        LGN,
        GRY,
        LBL

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

        private static final Map<String, Color> map = Map.of(
                "ORG", Color.ORG,
                "RED", Color.RED,
                "BLU", Color.BLU,
                "PNK", Color.PNK,
                "LGN", Color.LGN,
                "GRY", Color.GRY,
                "LBL", Color.LBL
        );

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
            return new Ball(map.getOrDefault(str, Color.BLU));
        }

        @Override
        public String toString() {
            return this.color.toString();
        }

    }

    public static class Tube {

        private final List<Ball> balls;

        public Tube() {
            this.balls = new ArrayList<>();
        }

        public Tube(Ball ... balls) {
            this.balls = new ArrayList<>(Arrays.asList(balls));
        }

        public Tube clone() {
            Tube tube = new Tube();
            for (Ball b : this.balls) {
                tube.push(b.clone());
            }
            return tube;
        }

        public boolean isEmpty() {
            return this.balls.size() == 0;
        }

        public boolean isFull() {
            return this.balls.size() == 4;
        }

        public void push(Ball ball) {
            this.balls.add(ball);
        }

        public Ball pop() {
            return this.balls.remove(this.balls.size() - 1);
        }

        public Ball peek() {
            return this.balls.get(this.balls.size() - 1);
        }

        public boolean canMoveTo(Tube tube) {
            return !this.isEmpty() && !tube.isFull() &&
                    (tube.isEmpty() || tube.peek().equals(this.peek()));
        }

        public boolean equals(Tube tube) {
            if (this.balls.size() == tube.balls.size()) {
                for (int i = 0; i < this.balls.size(); i++) {
                    if (!this.balls.get(i).equals(tube.balls.get(i))) {
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
                        && balls.get(0).equals(balls.get(1))
                        && balls.get(0).equals(balls.get(2))
                        && balls.get(0).equals(balls.get(3)));
        }

        public int size() {
            return this.balls.size();
        }

        @Override
        public String toString() {
            return this.balls.toString();
        }

    }

    public static class State {

        private final List<Tube> tubes;

        public State() {
            this.tubes = new ArrayList<>();
        }

        public State(State state) {
            this.tubes = new ArrayList<>();
            for (Tube tube : state.tubes) {
                this.tubes.add(tube.clone());
            }
        }

        public State addTube(Tube tube) {
            this.tubes.add(tube);
            return this;
        }

        public boolean canMove(int tubeFrom, int tubeTo) {
            return this.tubes.get(tubeFrom).canMoveTo(this.tubes.get(tubeTo));
        }

        public State move(int tubeFrom, int tubeTo) {
            State state = new State(this);
            Ball ball = state.tubes.get(tubeFrom).pop();
            state.tubes.get(tubeTo).push(ball);
            return state;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(
                    tubes.stream()
                        .map(Tube::size)
                        .sorted()
                        .toArray(Integer[]::new));
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof State
                    && tubes.stream()
                    .allMatch(t -> ((State) object).tubes.stream().anyMatch(t::equals));
        }

        public boolean isComplete() {
            return tubes.stream().allMatch(Tube::isComplete);
        }

        @Override
        public String toString() {
            return this.tubes.toString();
        }

    }

    public static class StopWatch {

        long startTime = 0;
        long endTime = 0;
        long total = 0;
        boolean running = false;

        StopWatch(){
            reset();
        }

        void reset(){
            startTime = 0;
            endTime = 0;
            total = 0;
            running = false;
        }

        void start(){
            startTime = System.nanoTime();
            running = true;
        }

        void stop(){
            endTime = System.nanoTime();
            total += (endTime - startTime);
            running = false;
        }

        float getTime(){
            float r = total;
            r /= 1000000000;
            return r;
        }
    }

    public static void main(String[] args) {

//        Tube tube1 = new Tube(new Ball(Color.BLU), new Ball(Color.BLU));
//        Tube tube2 = new Tube(new Ball(Color.RED), new Ball(Color.RED));
//        Tube tube3 = new Tube();
//
//        State state1 = new State().addTube(tube1).addTube(tube3);
//        System.out.println(state1.hashCode());
//        State state2 = new State().addTube(tube3).addTube(tube2);
//        System.out.println(state2.hashCode());
//
//        var set = new HashSet<State>();
//        set.add(state1);
//        System.out.println(set.contains(state2));

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a command (use help to view commands):");
            String command = sc.nextLine();
            if (command.equals("exit")) {
                break;
            } else if (command.equals("help")) {
                System.out.println("Use exit to exit.");
                System.out.println("Use solve to solve.");
                System.out.println("When entering into the solver, use the following format, from bottom to top: ");
                System.out.println("[CLR1]<space>[CLR2]<space>[CLR3]<space>[CLR4]");
                System.out.println("For instance, a tube with four blue balls should be:");
                System.out.println("BLU BLU BLU BLU");
                System.out.println("Here are the codes for colors: ");
                System.out.println("\"ORG\" - orange,\n" +
                        "\"RED\" - red,\n" +
                        "\"BLU\" - blue,\n" +
                        "\"PNK\" - pink,\n" +
                        "\"LGN\" - light green,\n" +
                        "\"GRY\" - grey,\n" +
                        "\"LBL\" - light blue");
                System.out.println("Use codes if you want to see the codes only.");
            } else if (command.equals("codes")) {
                System.out.println("Here are the codes for colors: ");
                System.out.println("\"ORG\" - orange,\n" +
                        "\"RED\" - red,\n" +
                        "\"BLU\" - blue,\n" +
                        "\"PNK\" - pink,\n" +
                        "\"LGN\" - light green,\n" +
                        "\"GRY\" - grey,\n" +
                        "\"LBL\" - light blue");
            } else if (command.equals("solve")) {
                System.out.print("Enter the number of tubes: ");
                int num = sc.nextInt();
                sc.nextLine();
                State state = new State();
                for (int i = 0; i < num; i++) {
                    System.out.printf("Enter the color of the balls for tube %d (bottom to top): ", i + 1);
                    String string = sc.nextLine();
                    if (string.equals("")) {
                        state.addTube(new Tube());
                    } else {
                        String[] input = string.split(" ");
                        Ball[] balls = new Ball[input.length];
                        for (int j = 0; j < input.length; j++) {
                            balls[j] = Ball.parse(input[j]);
                        }
                        state.addTube(new Tube(balls));
                    }
                }

                StopWatch sw = new StopWatch();
                sw.start();

                var steps = new LinkedList<Pair<State, List<Pair<Integer, Integer>>>>();
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
                            if (i == j) {
                            } else if (currState.canMove(i, j)) {
                                var newState = currState.move(i, j);
                                if (allStates.contains(newState)) {
                                } else {
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
                    int index = 0;
                    for (Pair<Integer, Integer> p : sol) {
                        System.out.printf("%d: Move from tube %d to %d\n", ++index, p.fst + 1, p.snd + 1);
                    }
                }

                sw.stop();
                System.out.printf("Algorithm took %f seconds to run.\n", sw.getTime());

            } else {
                System.out.println("Command invalid.");
            }
        }
    }

}