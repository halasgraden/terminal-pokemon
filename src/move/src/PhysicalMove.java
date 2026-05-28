/**
 * Physical moves pulled from Move CSV
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package move.src;

import pokemon.src.*;
import java.util.*;

public class PhysicalMove extends Move {

    //constructor
    public PhysicalMove(String name, Type type, int power, int accuracy, int pp) {
        super(name, type, power, accuracy, pp);
    }

    /**
     * executes the physical move
     *
     * @param attacker pokemon executing the move
     * @param defender pokemon being hit by the move
     */
    @Override
    public int execute(Pokemon attacker, Pokemon defender) {
        //check pp
        if (!usePP()) {
            return 0;  //pokemon cant use move if they dont have pp
        }

        //track which pokemon used move
        trackUsage(attacker);

        //check if hits
        if (!doesHit()) {
            System.out.println(attacker.getName() + "'s " + name + " missed!");
            return 0;
        }

        System.out.println(attacker.getName() + " used " + name + "!");

        //calculate damage by multiplying attack and move power, then dividing by defense
        int attackStat = attacker.getEffectiveAttack(); //accounts for burn
        int defenseStat = defender.getDefense();
        int baseDamage = (attackStat * power) / defenseStat;

        //ensure damage is at least 1 so I avoid errors
        int damage = Math.max(1, baseDamage);

        //check for critical hit
        damage = applyCritical(damage, attacker);

        //apply type effectiveness
        damage = TypeEffectiveness.applyEffectiveness(damage, this.type, defender.getType());

        //apply damage to defender
        defender.takeDamage(damage);
        System.out.println(defender.getName() + " took " + damage + " damage!");

        return damage;
    }

    @Override
    public String toString() {
        return "[PHYSICAL MOVE] " + super.toString();
    }
}
