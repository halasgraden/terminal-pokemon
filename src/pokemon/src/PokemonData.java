/**
 * Constructs pokemon data
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package pokemon.src;
import java.util.*;

public class PokemonData {
    private String name; //instance variables
    private String type;
    private int hp;
    private int attack;
    private int defense;
    private int spAtk;
    private int speed;
    private boolean isLegendary;
    private int currentHP;

    //constructors
    public PokemonData(String name, String type, int hp, int attack, int defense, int spAtk, int speed, boolean isLegendary) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.currentHP = hp;
        this.attack = attack;
        this.defense = defense;
        this.spAtk = spAtk;
        this.speed = speed;
        this.isLegendary = isLegendary;
    }

    //getters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpAtk() {
        return spAtk;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public int getTotal() {
        return hp + attack + defense + spAtk + speed;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpAtk(int spAtk) {
        this.spAtk = spAtk;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLegendary(boolean legendary) {
        isLegendary = legendary;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    //equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PokemonData that = (PokemonData) o;
        return hp == that.hp && attack == that.attack && defense == that.defense && spAtk == that.spAtk && speed == that.speed && isLegendary == that.isLegendary && Objects.equals(name.toLowerCase(), that.name.toLowerCase()) && Objects.equals(type.toLowerCase(), that.type.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), type.toLowerCase(), hp, attack, defense, spAtk, speed, isLegendary);
    }

    @Override
    public String toString() {
        return "Stats: \n" +
                "\tname: " + name + "\n" +
                "\ttype: " + type + "\n" +
                "\thp: " + hp + "\n" +
                "\tattack: " + attack + "\n" +
                "\tdefense: " + defense + "\n" +
                "\tspAtk: " + spAtk + "\n" +
                "\tspeed: " + speed + "\n" +
                "\tisLegendary: " + isLegendary;
    }
}
