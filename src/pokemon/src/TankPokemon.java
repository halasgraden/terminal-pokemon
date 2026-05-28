/**
 * Pokemon with high defense and hp with unique abilities
 * This pokemon has 30% more defense, 25% more hp, 20% less attack, 20% less speed, takes 20 less damage
 * Gets stronger the longer it fights. Once fortified, pokemon gains health passively and doubles defense
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

public class TankPokemon extends Pokemon {
    private int turnsInBattle; //instance variables
    private boolean fortified;
    private int totalDamageBlocked;

    private static final double DEFENSE_MULTIPLIER = 1.3; //constants
    private static final double HP_MULTIPLIER = 1.25;
    private static final double ATTACK_REDUCTION = 0.8;
    private static final double SPEED_REDUCTION = 0.2;
    private static final int DAMAGE_REDUCTION = 20;
    private static final int FORTIFY_THRESHOLD = 5;

    //constructor
    public TankPokemon(String name, Type type, int attack, int hp, int defense, int speed, int specialAttack, boolean isLegendary) {
        super(name, type, attack, hp, defense, speed, specialAttack, isLegendary);

        this.attack = (int) (attack * ATTACK_REDUCTION);
        this.defense = (int) (defense * DEFENSE_MULTIPLIER);
        this.hp = (int) (hp * HP_MULTIPLIER);
        this.speed = (int) (speed * SPEED_REDUCTION);
        this.currentHP = hp;

        this.fortified = false;
        this.totalDamageBlocked = 0;
        this.turnsInBattle = 0;
    }

    /**
     * tracks how many turns and initiates fortify ability
     */
    public void onTurnStart() {
        turnsInBattle++;

        //pokemon becomes fortified after 5 turns
        if (turnsInBattle >= FORTIFY_THRESHOLD && !fortified) {
            fortify();
        }
    }

    /**
     * applies fortify ability
     */
    private void fortify() {
        fortified = true;
        this.defense *= 2;
        System.out.println("🛡️ " + name + " became fortified! Defense doubled!");
    }

    /**
     * allows regeneration
     */
    public void regenerate() {
        if (fortified && !hasFainted()) {
            int healAmount = hp / 20;  //5% of max hp
            restoreHP(healAmount);
            System.out.println(name + " regenerated " + healAmount + " HP!");
        }
    }

    /**
     * allows pokemon to take damage from a move
     *
     * @param damage amount of hp taken off the pokemon
     */
    @Override
    public void takeDamage(int damage) {
        int reducedDamage = Math.max(1, damage - DAMAGE_REDUCTION);
        int damageBlocked = damage - reducedDamage;

        totalDamageBlocked += damageBlocked;

        super.takeDamage(reducedDamage);

        if (damageBlocked > 0) {
            System.out.println(name + " blocked " + damageBlocked + " damage with its thick armor!");
        }
    }

    public boolean isFortified() {
        return fortified;
    } //check fortify ability

    public int getTurnsInBattle() {
        return turnsInBattle;
    } //getters
    public int getTotalDamageBlocked() {
        return totalDamageBlocked;
    }

    @Override //toString
    public String toString() {
        return "[TANK] " + super.toString() + " | Damage Blocked: " + totalDamageBlocked;
    }
}