import java.util.HashMap;
import java.util.Map;

public class Ball {

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
