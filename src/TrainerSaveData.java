/**
 * Stores trainer data for saving/loading
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/6/26
 */

import pokemon.src.*;
import trainer.src.*;
import move.src.*;
import java.util.*;
import java.io.*;

public class TrainerSaveData implements Serializable {
    private static final long serialVersionUID = 1L; //unique identifier for check

    private String trainerName; //instance variables
    private List<String> partyPokemonNames;
    private int pokeballs;
    private List<SavedPokemon> party;
    private int trainerLevel;

    public TrainerSaveData(String trainerName, int trainerLevel, int pokeballs, List<SavedPokemon> party) { //constructor
        this.trainerName = trainerName;
        this.trainerLevel = trainerLevel;
        this.pokeballs = pokeballs;
        this.party = party;

        this.partyPokemonNames = new ArrayList<>();
        for (SavedPokemon p : party) {
            this.partyPokemonNames.add(p.getName());
        }
    }

    public String getTrainerName() {
        return trainerName;
    } //getters

    public int getTrainerLevel() {
        return trainerLevel;
    }

    public List<String> getPartyPokemonNames() {
        return partyPokemonNames;
    }

    public int getPokeballs() {
        return pokeballs;
    }

    public List<SavedPokemon> getParty() {
        return party;
    }

    /**
     * inner class to store pokemon data
     */
    public static class SavedPokemon implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private String type;
        private int level;
        private int currentHP;
        private int maxHP;
        private int currentXP;
        private String status;
        private List<String> moveNames;

        public SavedPokemon(String name, String type, int level, int currentHP, int maxHP,
                            int currentXP, String status, List<String> moveNames) {
            this.name = name;
            this.type = type;
            this.level = level;
            this.currentHP = currentHP;
            this.maxHP = maxHP;
            this.currentXP = currentXP;
            this.status = status;
            this.moveNames = moveNames;
        }

        //getters
        public String getName() { return name; }
        public String getType() { return type; }
        public int getLevel() { return level; }
        public int getCurrentHP() { return currentHP; }
        public int getMaxHP() { return maxHP; }
        public int getCurrentXP() { return currentXP; }
        public String getStatus() { return status; }
        public List<String> getMoveNames() { return moveNames; }
    }

    @Override //toString
    public String toString() {
        return "Save Data: " + "\n\t" +
                "Trainer: " + trainerName + "\n\t" +
                "Party: " + partyPokemonNames.size() + "\n\t" + "Pokemon";
    }
}
