/**
 * Attack pokemon with increased attack and unique abilities
 * Has a chance for a critical hit (+50% attack), has 50% attack
 * Becomes enraged with low HP
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 4/30/26
 */

package pokemon.src;
import trainer.src.*;
import move.src.*;
import java.util.*;


public class AttackPokemon extends Pokemon {
    private int criticalHits; //instance variables
    private int damageTaken;
    private boolean enraged;


    private static final double ATTACK_MULTIPLIER = 1.5; //constants
    private static final double CRIT_CHANCE = 0.25;
    private static final int RAGE_THRESHOLD = 50;

    //constructor
    public AttackPokemon(String name, Type type, int attack, int hp, int defense, int speed, int specialAttack, boolean isLegendary) {
        super(name, type, attack, hp, defense, speed, specialAttack, isLegendary);

        this.attack = (int) (attack * ATTACK_MULTIPLIER);

        this.damageTaken = 0;
        this.criticalHits = 0;
        this.enraged = false;
    }

    public boolean isCriticalHit() {
        return Math.random() < CRIT_CHANCE;
    } //checks critical hit

    public int getCriticalHits() {
        return criticalHits;
    }

    /**
     * allows pokemon to take damage from a move
     *
     * @param damage amount of hp taken off the pokemon
     */
    @Override
    public void takeDamage(int damage) {
        damageTaken += damage;
        super.takeDamage(damage);

        // Enter RAGE mode at 50 HP
        if (!enraged && currentHP <= RAGE_THRESHOLD) {
            enterRage();
        }
    }

    private void enterRage() {
        enraged = true;
        this.attack = (int)(attack * 1.5);  // Additional 50% attack!
        System.out.println(name + " is enraged! Attack massively increased!");
    }

    @Override
    public String toString() {
        return "[ATTACKER] " + super.toString() + " | Crits: " + criticalHits;
    }
}
