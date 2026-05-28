/**
 * Creates a TrainedPokemon to be added to the user's party; can gain experience through battles
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package pokemon.src;
import java.util.*;

public class TrainedPokemon extends Pokemon {
    private int level; //instance variables
    private int xp;
    private Map<String, Integer> victories; //tracks wins in battles

    //constructor
    public TrainedPokemon(String name, Type type, int hp, int attack, int defense,
                          int speed, int specialAttack, boolean isLegendary,
                          int level, int xp) {
        super(name, type, hp, attack, defense, speed, specialAttack, isLegendary);
        this.level = level;
        this.xp = xp;
        this.victories = new HashMap<>();
    }

    //copy constuctor without level and xp
    public TrainedPokemon(String name, Type type, int hp, int attack, int defense,
                          int speed, int specialAttack, boolean isLegendary) {
        super(name, type, hp, attack, defense, speed, specialAttack, isLegendary);
        this.level = 5;
        this.xp = 0;
        this.victories = new HashMap<>();
    }

    //copy constructor without legendary check
    public TrainedPokemon(String name, Type type, int hp, int attack, int defense,
                          int specialAttack, int speed) {
        this(name, type, hp, attack, defense, speed, specialAttack,
                false,
                5,
                0);
    }

    /**
     * gain xp from defeating pokemon
     */
    public void gainXP(int amount) {
        xp += amount;
        System.out.println(name + " gained " + amount + " XP!");

        //check if leveled up (100 xp per level)
        while (xp >= 100) {
            xp -= 100;
            levelUp();
        }
    }

    /**
     * allows pokemon to level up
     */
    public void levelUp() {
        level++;
        System.out.println("\n" + name + " leveled up to Level " + level + "!");

        // increasing stats +5%
        hp += hp / 20;
        attack += attack / 20;
        defense += defense / 20;
        speed += speed / 20;
        specialAttack += specialAttack / 20;
        currentHP = hp;
    }

    /**
     * records victories
     *
     * @param opponentName the opponent you win against
     */
    public void recordVictory(String opponentName) {
        victories.put(opponentName, victories.getOrDefault(opponentName, 0) + 1);
        gainXP(50); //IF YOU WANT TO CHANGE HOW MUCH XP POKEMON GET PER BATTLE, OR WANT TO MAKE IT VARIABLE, CHANGE THIS
    }

    public int getLevel() {
        return level;
    } //getters

    public int getXp() {
        return xp;
    }

    public Map<String, Integer> getVictories() {
        return victories;
    }



    @Override //toString
    public String toString() {
        return "Name: " + super.getName() + " (Level. " + getLevel() + ")\n" + "\tHP: " + + currentHP + "/" + super.getHp() + "\n\tType: " + super.getType();
    }
}
