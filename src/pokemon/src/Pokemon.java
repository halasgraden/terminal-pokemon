/**
 * Parent pokemon class acting as a blueprint for each pokemon
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 4/30/26
 */
package pokemon.src;

import move.src.*;
import trainer.src.*;
import java.util.*;
import java.util.Collections;

public abstract class Pokemon {
    protected String name; //instance variable
    protected Type type;
    protected int attack;
    protected int hp;
    protected int currentHP;
    protected int defense;
    protected int speed;
    protected int specialAttack;
    protected boolean isLegendary;
    protected Status status;
    protected List<Move> moveSet;
    protected Trainer currentTrainer;

    //constructor
    public Pokemon(String name, Type type, int attack, int hp, int defense, int speed, int specialAttack, boolean isLegendary) {
        this.name = name;
        this.type = type;
        this.attack = attack;
        this.hp = hp;
        this.currentHP = hp;
        this.defense = defense;
        this.speed = speed;
        this.specialAttack = specialAttack;
        this.isLegendary = isLegendary;
        this.status = Status.NORMAL;
        this.moveSet = new ArrayList<>();
        this.currentTrainer = null;
    }

    //helper methods
    /**
     * generates 4 moves for pokemon; prioritizes moves of type
     */
    public void generateMoves() {
        //get all moves macthing type
        List<MoveData> typedMoves = MoveDataLoader.getMovesByType(this.type.toString());

        //load other moves
        List<MoveData> allMoves = new ArrayList<>(MoveDataLoader.getAllMoves().values());

        //start with typed moves
        List<MoveData> availableMoves = new ArrayList<>();
        availableMoves.addAll(typedMoves);

        //add all moves
        for (MoveData move : allMoves) {
            if (!availableMoves.contains(move)) {
                availableMoves.add(move);
            }
        }

        //if no moves available, return
        if (availableMoves.isEmpty()) {
            System.out.println("Warning: No moves available for " + name);
            return;
        }

        //shuffle to get random moves
        //had to look this up, but we have gone over other Collections methods in class
        Collections.shuffle(typedMoves);
        Collections.shuffle(availableMoves);

        //add up to 4 moves
        int movesAdded = 0;

        for (MoveData moveData : typedMoves) { //priority for own type moves
            if (movesAdded >= 2) {
                break; //stop after 2 type moves
            }

            //create the actual move object from MoveData
            Move move = MoveDataLoader.createMove(moveData.getName());
            if (move != null) {
                addMove(move);
                movesAdded++;
            }
        }

        for (MoveData moveData : availableMoves) {
            if (movesAdded >= 4) {
                break; //pokemon can only have 4 moves
            }

            Move move = MoveDataLoader.createMove(moveData.getName());

            if (move != null) {
                addMove(move);
                movesAdded++;
            }
        }

    }

    /**
     * allows pokemon to take damage from a move
     *
     * @param damage amount of hp taken off the pokemon
     */
    public void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage); //doesnt allow negative values
        if (currentHP == 0) {
            System.out.println(name + " fainted!");
        }
    }

    /**
     * applies status effects to pokemon and logic for how it will be implemented
     */
    public boolean applyStatusEffects() {
        switch (status) {
            case NORMAL:
                //no status
                return true;

            case POISONED:
                //take 1/8 of max hp
                int poisonDamage = Math.max(1, hp / 8);
                System.out.println("💀 " + name + " is hurt by poison!");
                takeDamage(poisonDamage);
                return true;  //can attack

            case BURNED:
                //take 1/16 of max hp, attack is halved
                int burnDamage = Math.max(1, hp / 16);
                System.out.println("🔥 " + name + " is hurt by its burn!");
                takeDamage(burnDamage);
                return true;  // Can still attack (but attack stat is reduced)

            case PARALYZED:
                //25% chance to be fully paralyzed and unable to move
                if (Math.random() < 0.25) {
                    System.out.println("⚡ " + name + " is paralyzed! It can't move!");
                    return false;  //cannot attack
                }
                return true;  //can attack

            case FROZEN:
                //20% chance to thaw out each turn
                if (Math.random() < 0.20) {
                    System.out.println("❄️ " + name + " thawed out!");
                    status = Status.NORMAL;
                    return true;  //can attack
                }
                System.out.println("❄️ " + name + " is frozen solid!");
                return false;  //cannot attack

            case ASLEEP:
                //wake up with a 33% chance
                if (Math.random() < 0.33) {
                    System.out.println("😴 " + name + " woke up!");
                    status = Status.NORMAL;
                    return true;  //can attack
                }
                System.out.println("😴 " + name + " is fast asleep!");
                return false;  //cannot attack

            default:
                return true;
        }
    }

    /**
     * gets attack after status effects have been applied
     */
    public int getEffectiveAttack() {
        //burned pokemon have half attack
        if (status == Status.BURNED) {
            return attack / 2;
        }
        return attack;
    }

    /**
     * gets speed after status effects have been applied
     */
    public int getEffectiveSpeed() {
        //paralyzed pokemon have half speed
        if (status == Status.PARALYZED) {
            return speed / 2;
        }
        return speed;
    }

    /**
     * allows pokemon to restore hp
     *
     * @param amount number of hp being added
     */
    public void restoreHP(int amount) {
        currentHP = Math.min(hp, currentHP + amount); //doesnt allow more than max hp
    }

    /**
     * checks if fainted
     */
    public boolean hasFainted() {
        return currentHP <= 0;
    }

    /**
     * adds a move
     *
     * @param move move being added
     */
    public void addMove(Move move) {
        if (moveSet.size() < 4) {
            moveSet.add(move);
        } else {
            System.out.println(name + " already knows 4 moves.");
        }
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getAttack() {
        return attack;
    }

    public int getHp() {
        return hp;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public boolean getLegendaryStatus() {
        return isLegendary;
    }

    public Status getStatus() {
        return status;
    }

    public List<Move> getMoveSet() {
        return moveSet;
    }

    public Trainer getCurrentTrainer() {
        return currentTrainer;
    }

    public int getCurrentHP() {
        return this.currentHP;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCurrentTrainer(Trainer currentTrainer) {
        this.currentTrainer = currentTrainer;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    @Override //toString
    public String toString() {
        return "Stats: \n" +
                "\tname: " + name + "\n" +
                "\ttype: " + type + "\n" +
                "\thp: " + hp + "\n" +
                "\tattack: " + attack + "\n" +
                "\tdefense: " + defense + "\n" +
                "\tspAtk: " + specialAttack + "\n" +
                "\tspeed: " + speed + "\n" +
                "\tisLegendary: " + isLegendary;
    }
}
