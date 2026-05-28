/**
 * Handles saving and loading trainer data
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/7/26
 */

import pokemon.src.*;
import trainer.src.*;
import move.src.*;
import java.io.*;
import java.util.*;

public class LoadSystem {
    private static final String SAVE_FILE = "trainer_save.txt";
    private static final String SAVE_DIR = "saves/";
    private static final int MAX_SLOTS = 3;

    /**
     * ensure save directory exists
     */
    private static void ensureSaveDirectory() { //ensure the saves exist
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdir(); //had to look up how to create a folder; I have used terminal a lot so mkdir wasn't completely foreign
        }
    }

    /**
     * get save file name for a slot
     */
    private static String getSaveFileName(int slot) {
        return SAVE_DIR + "save_slot_" + slot + ".txt";
    }


    /**
     * save game/player data to a specific slot
     */
    public static boolean saveGame(PlayerTrainer player, int trainerLevel, int slot) {
        ensureSaveDirectory();

        try {
            //convert player's party to savedpokemon format
            List<TrainerSaveData.SavedPokemon> savedParty = new ArrayList<>();

            for (Pokemon p : player.getParty()) {
                //get move names
                List<String> moveNames = new ArrayList<>();
                for (Move m : p.getMoveSet()) {
                    moveNames.add(m.getName());
                }

                //get level and xp
                int level = 5;
                int xp = 0;
                if (p instanceof TrainedPokemon) {
                    TrainedPokemon trained = (TrainedPokemon) p;
                    level = trained.getLevel();
                    xp = trained.getXp();
                }

                //create saved pokemon
                TrainerSaveData.SavedPokemon savedPokemon = new TrainerSaveData.SavedPokemon(
                        p.getName(),
                        p.getType().toString(),
                        level,
                        p.getCurrentHP(),
                        p.getHp(),
                        xp,
                        p.getStatus().toString(),
                        moveNames
                );

                savedParty.add(savedPokemon);
            }

            //create save data
            TrainerSaveData saveData = new TrainerSaveData(
                    player.getName(),
                    trainerLevel,
                    player.getPokeBalls(),
                    savedParty
            );

            //write to file
            String fileName = getSaveFileName(slot);
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(saveData);
            out.close();
            fileOut.close();

            System.out.println("\nGame saved to Slot " + slot + "!");
            System.out.println("Trainer: " + player.getName());
            System.out.println("Level: " + trainerLevel);
            System.out.println("Pokemon: " + savedParty.size());

            return true;

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * load game from a slot
     */
    public static TrainerSaveData loadGame(int slot) {
        String fileName = getSaveFileName(slot);
        File saveFile = new File(fileName);

        if (!saveFile.exists()) {
            System.out.println("Slot " + slot + " is empty.");
            return null;
        }

        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TrainerSaveData saveData = (TrainerSaveData) in.readObject();
            in.close();
            fileIn.close();

            System.out.println("\nLoaded from Slot " + slot + "!");
            System.out.println("Trainer: " + saveData.getTrainerName());
            System.out.println("Level: " + saveData.getTrainerLevel());
            System.out.println("Pokemon: " + saveData.getParty().size());

            return saveData;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading slot " + slot + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * check if slot has a save
     */
    public static boolean slotExists(int slot) {
        return new File(getSaveFileName(slot)).exists();
    }

    /**
     * get save info for a slot
     */
    public static String getSlotInfo(int slot) {
        if (!slotExists(slot)) {
            return "[EMPTY]";
        }

        try {
            TrainerSaveData data = loadGameQuietly(slot); //showing without loading
            if (data != null) {
                return data.getTrainerName() + " - Lv." + data.getTrainerLevel() +
                        " - " + data.getParty().size() + " Pokemon";
            }
        } catch (Exception e) {
            return "[CORRUPTED]";
        }

        return "[EMPTY]";
    }

    /**
     * load game quietly with no output
     */
    private static TrainerSaveData loadGameQuietly(int slot) {
        String fileName = getSaveFileName(slot);
        File saveFile = new File(fileName);

        if (!saveFile.exists()) {
            return null;
        }

        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TrainerSaveData saveData = (TrainerSaveData) in.readObject();
            in.close();
            fileIn.close();
            return saveData;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * delete a specific save slot
     */
    public static boolean deleteSlot(int slot) {
        File saveFile = new File(getSaveFileName(slot));
        if (saveFile.exists()) {
            return saveFile.delete();
        }
        return false;
    }

    /**
     * check if any save exists
     */
    public static boolean anySaveExists() {
        for (int i = 1; i <= MAX_SLOTS; i++) {
            if (slotExists(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get max number of save slots
     */
    public static int getMaxSlots() {
        return MAX_SLOTS;
    }
}