/**
 * Computer trainer that user fights against
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 4/30/26
 */

package trainer.src;

import pokemon.src.*;
import move.src.*;
import java.util.*;

public class RivalTrainer extends Trainer {
    private Pokemon activePokemon; //instance variable

    public RivalTrainer(String name, List<Pokemon> party) { //constructor
        super(name, party);

        //set first pokemon as active
        if (!party.isEmpty()) {
            this.activePokemon = party.get(0);
        }
    }

    public RivalTrainer(String name) { //without party
        super(name);
    }

    /**
     * runs the player's turn in battle
     */
    @Override
    public void run() {
        if (activePokemon == null && !getParty().isEmpty()) { //check for null and empty
            activePokemon = getParty().get(0);
        }

        System.out.println("\n=== " + getName() + "'s Turn ===");
        System.out.println("Active: " + activePokemon);

        int action = (int)(Math.random() * 10); //calculate computer action with 0-9

        if (action < 7) {
            System.out.println(getName() + " will attack!"); //70% chance to attack
        } else if (action < 9) {
            if (activePokemon.getCurrentHP() < activePokemon.getHp() / 3) {
                randomSwitchPokemon(); //20% chance to switch
            } else {
                System.out.println(getName() + " will attack!");
            }
        } else {
            if (activePokemon.getCurrentHP() < activePokemon.getHp() / 2) {
                heal(); //10% chance to heal
            } else {
                System.out.println(getName() + " will attack!");
            }
        }
    }

    /**
     * allows user to select a move
     *
     * @param opponent opponent receiving the effects of the move
     */
    @Override
    public Move selectMove(Pokemon opponent) {
        if (activePokemon == null || activePokemon.getMoveSet().isEmpty()) { //ensure pokemon has moves
            System.out.println(getName() + " has no moves!");
            return null;
        }

        List<Move> moves = activePokemon.getMoveSet(); //get all available moves

        int randomIndex = (int)(Math.random() * moves.size()); //pick a random move
        Move selectedMove = moves.get(randomIndex);

        System.out.println(getName() + " selected " + selectedMove.getName() + "!");
        return selectedMove;
    }

    /**
     * randomly switches pokemon
     */
    public void randomSwitchPokemon() {
        List<Pokemon> available = new ArrayList<>();
        for (Pokemon p : getParty()) {
            if (!p.hasFainted() && p != activePokemon) { //find all pokemon that havent fainted
                available.add(p);
            }
        }

        if (!available.isEmpty()) { //if options, switch
            Pokemon newActive = available.get((int)(Math.random() * available.size()));
            activePokemon = newActive;
            System.out.println(getName() + " switched to " + activePokemon.getName() + "!");
        } else {
            System.out.println(getName() + " has no other Pokemon to switch to!");
        }
    }

    /**
     * heals active pokemon
     */
    public void heal() {
        if (activePokemon == null) {
            return;
        }

        int healAmount = activePokemon.getHp() / 3;  //heal 1/3 of max hp
        activePokemon.restoreHP(healAmount);
        System.out.println(getName() + " used a potion on " + activePokemon.getName() + "!");
        System.out.println(activePokemon.getName() + " restored " + healAmount + " HP!");
    }

    /**
     * checks if pokemon are left
     */
    @Override
    public boolean hasAvailablePokemon() { //check if the rival has pokemon
        for (Pokemon p : getParty()) {
            if (!p.hasFainted()) {
                return true;
            }
        }
        return false;
    }

    /**
     * auto switch after pokemon faints
     */
    public void autoSwitch() {
        if (activePokemon != null && !activePokemon.hasFainted()) {
            return;  //active pokemon is alive
        }

        for (Pokemon p : getParty()) { //find first non fainted pokemon
            if (!p.hasFainted()) {
                activePokemon = p;
                System.out.println(getName() + " sent out " + activePokemon.getName() + "!");
                return;
            }
        }

        System.out.println(getName() + " has no more Pokemon!"); //no pokemon to switch
        activePokemon = null;
    }

    //getters
    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    //setters
    public void setActivePokemon(Pokemon pokemon) {
        if (getParty().contains(pokemon) && !pokemon.hasFainted()) {
            this.activePokemon = pokemon;
        }
    }

    @Override //toString
    public String toString() {
        return "[RIVAL] " + super.toString();
    }
}
