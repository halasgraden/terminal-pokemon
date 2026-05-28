/**
 * Move parent class that creates move fields and blueprints for subclasses
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package move.src;

import pokemon.src.*;
import trainer.src.*;
import java.util.*;

public abstract class Move {
    protected String name; //instance variables
    protected Type type;
    protected int power;
    protected int accuracy;
    protected int pp;
    protected int maxPP;
    protected Map<String, Integer> usedBy;

    public Move(String name, Type type, int power, int accuracy, int pp) { //constructor
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.maxPP = pp;
        this.usedBy = new HashMap<>();
    }

    public abstract int execute(Pokemon attacker, Pokemon defender); //how the move is executed; abstract

    protected boolean doesHit() {
        return Math.random() * 100 < accuracy; //checks accuracy
    }

    //everytime a move is used, 1 pp is used as well (the amount of times a pokemon can use a move)
    protected boolean usePP() {
        if (pp > 0) {
            pp--;
            return true;
        }
        System.out.println(name + " has no PP left!");
        return false;
    }

    /**
     * Tracks which pokemon uses the move
     *
     * @param user the pokemon that uses the move
     */
    protected void trackUsage(Pokemon user) {
        String pokemonName = user.getName();
        usedBy.put(pokemonName, usedBy.getOrDefault(pokemonName, 0) + 1);
    }

    /**
     * Applies a critical hit and configures damage
     *
     * @param baseDamage initial damage of the move
     * @param attacker the pokemon executing the move
     */
    protected int applyCritical(int baseDamage, Pokemon attacker) {
        if (attacker instanceof AttackPokemon) {
            AttackPokemon attackMon = (AttackPokemon) attacker;
            if (attackMon.isCriticalHit()) {
                System.out.println("Critical hit!");
                return baseDamage * 2;
            }
        }
        return baseDamage;
    }

    //getters
    public int getPP() {
        return pp;
    }

    public Type getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }

    public int getMaxPP() {
        return maxPP;
    }

    public int getAccuracy() {
        return accuracy;
    }

    //toString
    @Override
    public String toString() {
        return "\n" +
                "\tname: " + name + "\n" +
                "\ttype: " + type + "\n" +
                "\tpower: " + power + "\n" +
                "\taccuracy: " + accuracy + "\n" +
                "\tpp: " + pp;
    }
}
