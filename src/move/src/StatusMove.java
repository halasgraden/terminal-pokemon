/**
 * Status move type extracted from pokemonMoves.csv
 * Does not inflict damage, but rather inflicts a certain effect on the other pokemon
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package move.src;

import pokemon.src.*;
import java.util.*;

public class StatusMove extends Move {
    private Status statusToInflict; //instance variable

    public StatusMove(String name, Type type, int accuracy, int pp, Status status) { //constructor
        super(name, type, 0, accuracy, pp);  //no power for status moves
        this.statusToInflict = status;
    }

    /**
     * executes the status move
     *
     * @param attacker pokemon executing the move
     * @param defender pokemon being hit by the move
     */
    @Override
    public int execute(Pokemon attacker, Pokemon defender) {
        //check pp
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

        //try to inflict
        System.out.println(attacker.getName() + " used " + name + "!");

        //check if defender already has a status
        if (defender.getStatus() != Status.NORMAL) {
            System.out.println("But it failed! " + defender.getName() + " already has a status condition.");
            return 0;
        }

        //inflict status
        defender.setStatus(statusToInflict);
        System.out.println(defender.getName() + " is now " + statusToInflict + "!");

        return 0;  //no damage
    }

    public Status getStatusToInflict() {
        return statusToInflict;
    }

    @Override //toString
    public String toString() {
        return "[STATUS] " + name + " (" + type + ") inflicts: " + statusToInflict + " - PP: " + pp + "/" + maxPP;
    }
}
