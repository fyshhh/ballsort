import java.util.Arrays;
import java.util.Objects;

public class Tube {

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