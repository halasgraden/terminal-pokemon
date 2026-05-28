/**
 * Special move type extracted from pokemonMoves.csv
 * Uses special attack vs defense
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package move.src;

import pokemon.src.*;
import java.util.*;

public class SpecialMove extends Move {

    private Map<String, Integer> totalDamageDealt; //instance variable

    public SpecialMove(String name, Type type, int power, int accuracy, int pp) { //constructor
        super(name, type, power, accuracy, pp);
        this.totalDamageDealt = new HashMap<>();
    }

    /**
     * executes the special move
     *
     * @param attacker pokemon executing the move
     * @param defender pokemon being hit by the move
     */
    @Override
    public int execute(Pokemon attacker, Pokemon defender) {
        //check if move has pp
        if (!usePP()) {
            return 0;
        }

        //track attacker
        trackUsage(attacker);

        //check accuracy
        if (!doesHit()) {
            System.out.println(attacker.getName() + "'s " + name + " missed!");
            return 0;
        }

        System.out.println(attacker.getName() + " used " + name + "!");

        //calculate baseDamage using ((spAtk * power) / defense)
        int specialAttackStat = attacker.getSpecialAttack();
        int defenseStat = defender.getDefense();
        int baseDamage = (specialAttackStat * power) / defenseStat;

        //apply type effectiveness
        int damage = TypeEffectiveness.applyEffectiveness(baseDamage, this.type, defender.getType());

        //ensure damage is at least 1 to avoid errors
        int finalDamage = Math.max(1, damage);

        //apply damage
        defender.takeDamage(finalDamage);
        System.out.println(defender.getName() + " took " + finalDamage + " damage!");

        return finalDamage;
    }

    //toString
    @Override
    public String toString() {
        return "[SPECIAL] " + super.toString();
    }
}
