package move.src;

/**
 * Creates move data
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/6/26
 */
public class MoveData {
    //instance variables
    private int id;
    private String name;
    private String effect;
    private String type;
    private String kind;  //physical, status, or special
    private int power;
    private int accuracy;
    private int pp;

    //constructor
    public MoveData(int id, String name, String effect, String type, String kind, int power, int accuracy, int pp) {
        this.id = id;
        this.name = name;
        this.effect = effect;
        this.type = type;
        this.kind = kind;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public String getType() {
        return type;
    }

    public String getKind() {
        return kind;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getPP() {
        return pp;
    }

    //toString
    @Override
    public String toString() {
        return "MoveData: " + name + " (" + type + ") - " + kind +
                " - Power: " + power + " - Acc: " + accuracy + "% - PP: " + pp;
    }
}
