/**
 * Trainer parent with blueprints for user and computer trainers
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

public abstract class Trainer {
    private String name; //instance variables
    private List<Pokemon> party;

    //constructors
    public Trainer(String name, List<Pokemon> party) {
        this.name = name;
        this.party = party;
    }

    public Trainer(String name) {
        this(name, new ArrayList<>());
    }

    public abstract void run(); //executes turn; abstract

    public abstract Move selectMove(Pokemon opponent); //selects move; abstract

    public abstract boolean hasAvailablePokemon(); //checks if user has pokemon; abstract

    public boolean addToParty(Pokemon pokemon) { //adds pokemon to party
        boolean check = false;
        if (party.size() < 6) {
            party.add(pokemon);
            pokemon.setCurrentTrainer(this);
            System.out.println(pokemon.getName() + " added! Your party size is now " + party.size() + ".");
            check = true;
        } else {
            System.out.println("You have 6 pokemon already! You cannot have more than 6.");
        }

        return check;
    }

    public String getName() {
        return name;
    } //getters

    public List<Pokemon> getParty() {
        return party;
    }

    @Override //toString
    public String toString() {
        return name + "'s Party: " + party.size() + "/6 Pokemon";
    }
}
