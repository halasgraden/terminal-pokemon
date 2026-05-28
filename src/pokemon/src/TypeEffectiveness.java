package pokemon.src;
import java.util.*;
import trainer.src.*;
import move.src.*;

/**
 * Logic for pokemon to have type effectiveness and weakness
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/7/26
 */
public class TypeEffectiveness {

    private static Map<String, Double> effectivenessChart; //initializing a chart for type effectiveness

    static {
        initializeEffectiveness(); //ran into an error where the other methods were running before the chart was initialized; had to look this up but found that wrapping it in static ensured it always ran first
    }

    /**
     * initialize the type effectiveness chart
     */
    private static void initializeEffectiveness() {
        effectivenessChart = new HashMap<>();

        //normal matchups
        addEffectiveness(Type.NORMAL, Type.ROCK, 0.5);
        addEffectiveness(Type.NORMAL, Type.GHOST, 0.0);
        addEffectiveness(Type.NORMAL, Type.STEEL, 0.5);

        //fire matchups
        addEffectiveness(Type.FIRE, Type.FIRE, 0.5);
        addEffectiveness(Type.FIRE, Type.WATER, 0.5);
        addEffectiveness(Type.FIRE, Type.GRASS, 2.0);
        addEffectiveness(Type.FIRE, Type.ICE, 2.0);
        addEffectiveness(Type.FIRE, Type.BUG, 2.0);
        addEffectiveness(Type.FIRE, Type.ROCK, 0.5);
        addEffectiveness(Type.FIRE, Type.DRAGON, 0.5);
        addEffectiveness(Type.FIRE, Type.STEEL, 2.0);

        //water matchups
        addEffectiveness(Type.WATER, Type.FIRE, 2.0);
        addEffectiveness(Type.WATER, Type.WATER, 0.5);
        addEffectiveness(Type.WATER, Type.GRASS, 0.5);
        addEffectiveness(Type.WATER, Type.GROUND, 2.0);
        addEffectiveness(Type.WATER, Type.ROCK, 2.0);
        addEffectiveness(Type.WATER, Type.DRAGON, 0.5);

        //grass matchups
        addEffectiveness(Type.GRASS, Type.FIRE, 0.5);
        addEffectiveness(Type.GRASS, Type.WATER, 2.0);
        addEffectiveness(Type.GRASS, Type.GRASS, 0.5);
        addEffectiveness(Type.GRASS, Type.POISON, 0.5);
        addEffectiveness(Type.GRASS, Type.GROUND, 2.0);
        addEffectiveness(Type.GRASS, Type.FLYING, 0.5);
        addEffectiveness(Type.GRASS, Type.BUG, 0.5);
        addEffectiveness(Type.GRASS, Type.ROCK, 2.0);
        addEffectiveness(Type.GRASS, Type.DRAGON, 0.5);
        addEffectiveness(Type.GRASS, Type.STEEL, 0.5);

        //electric matchups
        addEffectiveness(Type.ELECTRIC, Type.WATER, 2.0);
        addEffectiveness(Type.ELECTRIC, Type.ELECTRIC, 0.5);
        addEffectiveness(Type.ELECTRIC, Type.GRASS, 0.5);
        addEffectiveness(Type.ELECTRIC, Type.GROUND, 0.0);
        addEffectiveness(Type.ELECTRIC, Type.FLYING, 2.0);
        addEffectiveness(Type.ELECTRIC, Type.DRAGON, 0.5);

        //ice matchups
        addEffectiveness(Type.ICE, Type.FIRE, 0.5);
        addEffectiveness(Type.ICE, Type.WATER, 0.5);
        addEffectiveness(Type.ICE, Type.GRASS, 2.0);
        addEffectiveness(Type.ICE, Type.ICE, 0.5);
        addEffectiveness(Type.ICE, Type.GROUND, 2.0);
        addEffectiveness(Type.ICE, Type.FLYING, 2.0);
        addEffectiveness(Type.ICE, Type.DRAGON, 2.0);
        addEffectiveness(Type.ICE, Type.STEEL, 0.5);

        //fighting matchups
        addEffectiveness(Type.FIGHTING, Type.NORMAL, 2.0);
        addEffectiveness(Type.FIGHTING, Type.ICE, 2.0);
        addEffectiveness(Type.FIGHTING, Type.POISON, 0.5);
        addEffectiveness(Type.FIGHTING, Type.FLYING, 0.5);
        addEffectiveness(Type.FIGHTING, Type.PSYCHIC, 0.5);
        addEffectiveness(Type.FIGHTING, Type.BUG, 0.5);
        addEffectiveness(Type.FIGHTING, Type.ROCK, 2.0);
        addEffectiveness(Type.FIGHTING, Type.GHOST, 0.0);
        addEffectiveness(Type.FIGHTING, Type.DARK, 2.0);
        addEffectiveness(Type.FIGHTING, Type.STEEL, 2.0);
        addEffectiveness(Type.FIGHTING, Type.FAIRY, 0.5);

        //poision matchups
        addEffectiveness(Type.POISON, Type.GRASS, 2.0);
        addEffectiveness(Type.POISON, Type.POISON, 0.5);
        addEffectiveness(Type.POISON, Type.GROUND, 0.5);
        addEffectiveness(Type.POISON, Type.ROCK, 0.5);
        addEffectiveness(Type.POISON, Type.GHOST, 0.5);
        addEffectiveness(Type.POISON, Type.STEEL, 0.0);
        addEffectiveness(Type.POISON, Type.FAIRY, 2.0);

        //ground matchups
        addEffectiveness(Type.GROUND, Type.FIRE, 2.0);
        addEffectiveness(Type.GROUND, Type.ELECTRIC, 2.0);
        addEffectiveness(Type.GROUND, Type.GRASS, 0.5);
        addEffectiveness(Type.GROUND, Type.POISON, 2.0);
        addEffectiveness(Type.GROUND, Type.FLYING, 0.0);
        addEffectiveness(Type.GROUND, Type.BUG, 0.5);
        addEffectiveness(Type.GROUND, Type.ROCK, 2.0);
        addEffectiveness(Type.GROUND, Type.STEEL, 2.0);

        //flying matchups
        addEffectiveness(Type.FLYING, Type.ELECTRIC, 0.5);
        addEffectiveness(Type.FLYING, Type.GRASS, 2.0);
        addEffectiveness(Type.FLYING, Type.FIGHTING, 2.0);
        addEffectiveness(Type.FLYING, Type.BUG, 2.0);
        addEffectiveness(Type.FLYING, Type.ROCK, 0.5);
        addEffectiveness(Type.FLYING, Type.STEEL, 0.5);

        //psychic matchups
        addEffectiveness(Type.PSYCHIC, Type.FIGHTING, 2.0);
        addEffectiveness(Type.PSYCHIC, Type.POISON, 2.0);
        addEffectiveness(Type.PSYCHIC, Type.PSYCHIC, 0.5);
        addEffectiveness(Type.PSYCHIC, Type.DARK, 0.0);
        addEffectiveness(Type.PSYCHIC, Type.STEEL, 0.5);

        //bug matchups
        addEffectiveness(Type.BUG, Type.FIRE, 0.5);
        addEffectiveness(Type.BUG, Type.GRASS, 2.0);
        addEffectiveness(Type.BUG, Type.FIGHTING, 0.5);
        addEffectiveness(Type.BUG, Type.POISON, 0.5);
        addEffectiveness(Type.BUG, Type.FLYING, 0.5);
        addEffectiveness(Type.BUG, Type.PSYCHIC, 2.0);
        addEffectiveness(Type.BUG, Type.GHOST, 0.5);
        addEffectiveness(Type.BUG, Type.DARK, 2.0);
        addEffectiveness(Type.BUG, Type.STEEL, 0.5);
        addEffectiveness(Type.BUG, Type.FAIRY, 0.5);

        //rock matchups
        addEffectiveness(Type.ROCK, Type.FIRE, 2.0);
        addEffectiveness(Type.ROCK, Type.ICE, 2.0);
        addEffectiveness(Type.ROCK, Type.FIGHTING, 0.5);
        addEffectiveness(Type.ROCK, Type.GROUND, 0.5);
        addEffectiveness(Type.ROCK, Type.FLYING, 2.0);
        addEffectiveness(Type.ROCK, Type.BUG, 2.0);
        addEffectiveness(Type.ROCK, Type.STEEL, 0.5);

        //ghost matchups
        addEffectiveness(Type.GHOST, Type.NORMAL, 0.0);
        addEffectiveness(Type.GHOST, Type.PSYCHIC, 2.0);
        addEffectiveness(Type.GHOST, Type.GHOST, 2.0);
        addEffectiveness(Type.GHOST, Type.DARK, 0.5);

        //dragon matchups
        addEffectiveness(Type.DRAGON, Type.DRAGON, 2.0);
        addEffectiveness(Type.DRAGON, Type.STEEL, 0.5);
        addEffectiveness(Type.DRAGON, Type.FAIRY, 0.0);

        //dark matchups
        addEffectiveness(Type.DARK, Type.FIGHTING, 0.5);
        addEffectiveness(Type.DARK, Type.PSYCHIC, 2.0);
        addEffectiveness(Type.DARK, Type.GHOST, 2.0);
        addEffectiveness(Type.DARK, Type.DARK, 0.5);
        addEffectiveness(Type.DARK, Type.FAIRY, 0.5);

        //steel matchups
        addEffectiveness(Type.STEEL, Type.FIRE, 0.5);
        addEffectiveness(Type.STEEL, Type.WATER, 0.5);
        addEffectiveness(Type.STEEL, Type.ELECTRIC, 0.5);
        addEffectiveness(Type.STEEL, Type.ICE, 2.0);
        addEffectiveness(Type.STEEL, Type.ROCK, 2.0);
        addEffectiveness(Type.STEEL, Type.STEEL, 0.5);
        addEffectiveness(Type.STEEL, Type.FAIRY, 2.0);

        //fairy matchups
        addEffectiveness(Type.FAIRY, Type.FIRE, 0.5);
        addEffectiveness(Type.FAIRY, Type.FIGHTING, 2.0);
        addEffectiveness(Type.FAIRY, Type.POISON, 0.5);
        addEffectiveness(Type.FAIRY, Type.DRAGON, 2.0);
        addEffectiveness(Type.FAIRY, Type.DARK, 2.0);
        addEffectiveness(Type.FAIRY, Type.STEEL, 0.5);
    }

    /**
     * add effectiveness to the chart
     */
    private static void addEffectiveness(Type attackType, Type defenseType, double multiplier) {
        String key = attackType + "_" + defenseType;
        effectivenessChart.put(key, multiplier);
    }

    /**
     * get effectiveness multiplier for attack
     *
     * @param attackType type of the attacking move
     * @param defenseType type of the defending pokemon
     * @return the multiplier (2.0 = super effective, 1.0 = normal, 0.5 = not very effective, 0.0 = no effect)
     */
    public static double getEffectiveness(Type attackType, Type defenseType) {
        String key = attackType + "_" + defenseType;
        return effectivenessChart.getOrDefault(key, 1.0);
    }

    /**
     * apply type effectiveness to damage; print message
     *
     * @param baseDamage base damage before type effectiveness
     * @param attackType type of the attacking move
     * @param defenseType type of the defending pokemon
     * @return new damage after type effectiveness
     */
    public static int applyEffectiveness(int baseDamage, Type attackType, Type defenseType) {
        double multiplier = getEffectiveness(attackType, defenseType);
        int finalDamage = (int)(baseDamage * multiplier);

        //print message
        if (multiplier > 1.0) {
            System.out.println("It's super effective!");
        } else if (multiplier < 1.0 && multiplier > 0.0) {
            System.out.println("It's not very effective...");
        } else if (multiplier == 0.0) {
            System.out.println("It doesn't affect the opponent...");
        }

        return finalDamage;
    }

    /**
     * Check if a move has no effect (0.0)
     *
     * @param attackType type of the attacking move
     * @param defenseType type of the defending pokemon
     * @return true if move has no effect
     */
    public static boolean hasNoEffect(Type attackType, Type defenseType) {
        return getEffectiveness(attackType, defenseType) == 0.0;
    }
}
