/**
 * Speed pokemon with high speed and unique abilities
 * Has double speed (basically will go first) and can dodge attacks
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

public class SpeedPokemon extends Pokemon {
    private int dodgeCount; //instance variable

    private static final double SPEED_MULTIPLIER = 2; //constants
    private static final double DODGE_CHANCE = 0.20;

    //constructor
    public SpeedPokemon(String name, Type type, int attack, int hp, int defense, int speed, int specialAttack, boolean isLegendary) {
        super(name, type, attack, hp, defense, speed, specialAttack, isLegendary);

        this.speed = (int) (speed * SPEED_MULTIPLIER);
        this.dodgeCount = 0;
    }

    /**
     * allows pokemon to take damage from a move
     *
     * @param damage amount of hp taken off the pokemon
     */
    @Override
    public void takeDamage(int damage) {
        if (Math.random() < DODGE_CHANCE) {
            dodgeCount++;
            System.out.println(name + " dodged the attack with blazing speed!");
            return;  //doesn't take damage
        }

        super.takeDamage(damage);
    }

    public int getDodgeCount() {
        return dodgeCount;
    } //getter

    @Override //toString
    public String toString() {
        return "[SPEEDSTER] " + super.toString() + " | Dodges: " + dodgeCount;
    }
}
