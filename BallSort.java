/*
v1.0 - 0.10-0.11s
Todo:
Remove use of Integer pairs - optimize with custom structures
Explore caching?
Use StringBuilders?
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class BallSort {

    public static void sort(int num, State state) {

        StopWatch sw = new StopWatch();
        sw.start();

        var steps = new PriorityQueue<Pair<State, List<Pair<Integer, Integer>>>>(
                (x, y) -> (y.fst().completeTubes() - x.fst().completeTubes()));
        var allStates = new HashSet<State>();
        List<Pair<Integer, Integer>> sol = null;
        steps.add(new Pair<>(state, new ArrayList<>()));
        while (!steps.isEmpty()) {
            var move = steps.poll();
            var currState = move.fst();
            var prevSteps = move.snd();
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
                System.out.printf("%02d: Move from tube %d to %d\n", ++index, p.fst() + 1, p.snd() + 1);
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

        System.out.println("BallSort v1.0");
        System.out.println("Use \"help\" to view commands.");

        Scanner sc = new Scanner(System.in);
        label:
        while (true) {
            System.out.print("Enter a command:");
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
                        "\"nvy\" - navy\n" +
                        "\"brn\" - brown\n" +
                        "\"lim\" - lime\n" +
                        "\"wht\" - white\n");
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
                        "\"nvy\" - navy\n" +
                        "\"brn\" - brown\n" +
                        "\"lim\" - lime\n" +
                        "\"wht\" - white\n");
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