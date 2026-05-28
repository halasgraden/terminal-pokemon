/**
 * Controller that runs game
 *
 * @author Halas Graden
 * TAC 265, Spring 2026
 * Date created: 5/6/26
 */

import pokemon.src.*;
import trainer.src.*;
import move.src.*;
import java.util.*;
import java.io.*;

public class Game {

    private Scanner scanner; //instance variables
    private PlayerTrainer player;
    private int trainerLevel;
    private boolean gameOver;

    public Game() { //constructor
        scanner = new Scanner(System.in);
        trainerLevel = 1;
        gameOver = false;
    }

    /**
     * executes the game
     */
    public void run() {
        //load pokemon data
        System.out.println("Loading pokemon data...");
        PokemonDataLoader.loadPokemonCSV("pokemonData.csv");

        System.out.println("Loading moves...");
        MoveDataLoader.loadMovesCSV("pokemonMoves.csv");

        System.out.println("Total moves loaded: " + MoveDataLoader.getMoveCount());

        //welcome message
        displayWelcome();

        //choose whether to start or load game
        startOrLoadGame();

        //main menu loop
        mainMenu();

        //save game
        saveGame();
        System.out.println("Thank you for playing!\nFeel free to come back and load your save anytime.");
    }

    /**
     * welcome message
     */
    private void displayWelcome() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("  WELCOME to Terminal Pokemon!");
        System.out.println("  Your goal is to get the highest trainer level, which you earn from each trainer battle victory!");
        System.out.println("=".repeat(100));
    }

    /**
     * asks if user wants to start or load game
     */
    private void startOrLoadGame() {
        System.out.println("1. Start New Game");
        System.out.println("2. Load Game");

        int choice = IOHelper.readInt("Choice: ", 1, 2);

        if (choice == 2) {
            loadGameMenu();
        } else {
            newGame();
        }
    }

    /**
     * creates a new game
     */
    private void newGame() {
        String name = IOHelper.readLine("Enter your trainer name: ");

        player = new PlayerTrainer(name);

        System.out.println("\nWelcome, " + name + "!");
        System.out.println("Professor Oak: It's time to choose your starter Pokemon!");

        chooseStarter(); //allows user to choose their starter
    }

    /**
     * show load menu with all save slots
     */
    private void loadGameMenu() {
        if (!LoadSystem.anySaveExists()) { //if no files exist
            System.out.println("\nNo save files found! Starting new game...\n");
            newGame();
            return;
        }

        System.out.println("\n=== LOAD GAME ===");
        System.out.println("Select a save slot:");

        // show all slots with info
        for (int i = 1; i <= LoadSystem.getMaxSlots(); i++) {
            String slotInfo = LoadSystem.getSlotInfo(i);
            System.out.println("Slot " + i + ": " + slotInfo);
        }

        int slot = IOHelper.readInt("\nLoad which slot? (0 to start new game): ",
                0, LoadSystem.getMaxSlots());

        if (slot == 0) {
            newGame();
            return;
        }

        if (!LoadSystem.slotExists(slot)) {
            System.out.println("\nSlot " + slot + " is empty! Starting new game.\n");
            newGame();
            return;
        }

        // Load the chosen slot
        loadGame(slot);
    }

    /**
     * load a saved game from specific slot
     */
    private void loadGame(int slot) {
        TrainerSaveData saveData = LoadSystem.loadGame(slot);

        if (saveData == null) {
            System.out.println("\nFailed to load save file! Starting new game.\n");
            newGame();
            return;
        }

        //create player from save data
        player = new PlayerTrainer(saveData.getTrainerName());
        player.setPokeballs(saveData.getPokeballs());
        trainerLevel = saveData.getTrainerLevel();
        player.setTrainerLevel(trainerLevel);

        //recreate party
        for (TrainerSaveData.SavedPokemon savedPokemon : saveData.getParty()) {
            //get pokemon from database
            PokemonData data = PokemonDataLoader.getPokemon(savedPokemon.getName());

            if (data == null) {
                System.out.println("Warning: Could not find " + savedPokemon.getName());
                continue;
            }

            //create the Pokemon
            TrainedPokemon pokemon = new TrainedPokemon(
                    savedPokemon.getName(),
                    Type.valueOf(savedPokemon.getType()),
                    data.getHp(),
                    data.getAttack(),
                    data.getDefense(),
                    data.getSpeed(),
                    data.getSpAtk(),
                    data.isLegendary(),
                    savedPokemon.getLevel(),
                    savedPokemon.getCurrentXP()
            );

            //restore HP and status
            pokemon.setCurrentHP(savedPokemon.getCurrentHP());
            pokemon.setStatus(Status.valueOf(savedPokemon.getStatus()));

            //restore moves
            for (String moveName : savedPokemon.getMoveNames()) {
                Move move = MoveDataLoader.createMove(moveName);
                if (move != null) {
                    pokemon.addMove(move);
                }
            }

            player.addToParty(pokemon);
        }

        System.out.println("\nWelcome back, " + player.getName() + "!");
        System.out.println("Trainer Level: " + trainerLevel);
        System.out.println("Pokemon in party: " + player.getParty().size());
    }

    /**
     * choose starter
     */
    public void chooseStarter() {

        System.out.println("\n1. Charmander (Fire)");
        System.out.println("2. Squirtle (Water)");
        System.out.println("3. Bulbasaur (Grass)");

        String starter = IOHelper.readLine("Your choice", "charmander", "squirtle", "bulbasaur");

        PokemonData data = PokemonDataLoader.getPokemon(starter);

        if (data == null) {
            System.out.println("Cannot find this pokemon in the database!");
            return;
        }

        Pokemon starterName = PokemonFactory.createTrainedPokemon(data);

        starterName.generateMoves(); //generate moves for starer

        player.addToParty(starterName); //adding to user's party
        System.out.println("\nYou chose " + starterName.getName() + "!");
    }

    /**
     * main menu loop
     */
    private void mainMenu() {
        while(true) {

            if (gameOver) { //before anything, check is the game is over
                System.out.println("\n" + "=".repeat(60));
                System.out.println("All your Pokemon have fainted.");
                System.out.println("You can no longer continue this adventure.");
                System.out.println("=".repeat(60));

                boolean restart = IOHelper.readYesNo("Start a new game?");
                if (restart) { //start new game
                    gameOver = false;
                    newGame();
                    continue;
                } else {
                    return;  //exit game
                }
            }

            System.out.println("\n=== MAIN MENU ===");
            System.out.println("TRAINER: " + player.getName() + " (Lv. " + trainerLevel + ")");
            System.out.println("1. Start Wild Encounter");
            System.out.println("2. View Party");
            System.out.println("3. Battle Trainer");
            System.out.println("4. Manage Party");
            System.out.println("5. Save Game");
            System.out.println("6. Exit");

            int choice = IOHelper.readInt("Choice: ", 1, 6);

            switch (choice) {
                case 1: wildEncounter(); break;
                case 2: viewParty(); break;
                case 3: trainerBattle(); break;
                case 4: manageParty(); break;
                case 5: saveGame(); break;
                case 6:
                    System.out.println("Saving and exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    //helper menu methods
    /**
     * view pokemon party
     */
    private void viewParty() {
        System.out.println("\n=== YOUR PARTY ===");

        if (player.getParty().isEmpty()) {
            System.out.println("You have no Pokemon yet!");
            return;
        }

        for (int i = 0; i < player.getParty().size(); i++) {
            Pokemon p = player.getParty().get(i);
            System.out.println((i + 1) + ". " + p);
        }
    }

    /**
     * saves user's game
     */
    private void saveGame() {
        System.out.println("\n=== SAVING GAME ===");
        System.out.println("Select a save slot:");

        // show all slots with info
        for (int i = 1; i <= LoadSystem.getMaxSlots(); i++) {
            String slotInfo = LoadSystem.getSlotInfo(i);
            System.out.println("Slot " + i + ": " + slotInfo);
        }

        int slot = IOHelper.readInt("\nSave to which slot? (0 to cancel): ",
                0, LoadSystem.getMaxSlots());

        if (slot == 0) {
            System.out.println("Save cancelled.");
            return;
        }

        // warn if overwriting a slot
        if (LoadSystem.slotExists(slot)) {
            boolean confirm = IOHelper.readYesNo("Slot " + slot + " already has a save. Overwrite");
            if (!confirm) {
                System.out.println("Save cancelled.");
                return;
            }
        }

        // save to slot
        LoadSystem.saveGame(player, trainerLevel, slot);
    }

    /**
     * allows user to manage party
     */
    private void manageParty() {
        if (player.getParty().isEmpty()) { //empty party check
            System.out.println("You have no Pokemon!");
            return;
        }

        while (true) { //manage party look
            System.out.println("\n=== MANAGE PARTY ===");
            System.out.println("1. View Pokemon Details");
            System.out.println("2. Heal All Pokemon");
            System.out.println("3. Release Pokemon");
            System.out.println("4. Back to Main Menu");

            int choice = IOHelper.readInt("Choice: ", 1, 4);

            switch (choice) {
                case 1:
                    viewPokemonDetails();
                    break;
                case 2:
                    player.healAllPokemon();
                    break;
                case 3:
                    releasePokemon();
                    break;
                case 4:
                    return; //exit to main menu
            }
        }
    }

    /**
     * view info about a specific pokemon
     */
    private void viewPokemonDetails() {
        viewParty();

        if (player.getParty().isEmpty()) {
            return;
        }

        int choice = IOHelper.readInt("Select Pokemon (0 to cancel): ", 0, player.getParty().size());

        if (choice == 0) {
            return;
        }

        Pokemon selected = player.getParty().get(choice - 1);

        System.out.println("\n" + "=".repeat(50));
        System.out.println(selected);
        System.out.println("\nMOVES:");
        for (int i = 0; i < selected.getMoveSet().size(); i++) {
            System.out.println("  " + (i + 1) + ". " + selected.getMoveSet().get(i));
        }
        System.out.println("=".repeat(50));
    }

    /**
     * release a pokemon from party
     */
    private void releasePokemon() {
        viewParty();

        if (player.getParty().size() <= 1) { //ensures trainer HAS pokemon
            System.out.println("You must have at least 1 Pokemon!");
            return;
        }

        int choice = IOHelper.readInt("Release which Pokemon? (0 to cancel): ", 0, player.getParty().size());

        if (choice == 0) {
            return;
        }

        Pokemon toRelease = player.getParty().get(choice - 1);

        boolean confirm = IOHelper.readYesNo("Are you sure you want to release " + toRelease.getName()); //ensure no misinput

        if (confirm) {
            player.getParty().remove(choice - 1);
            System.out.println(toRelease.getName() + " was released into the wild!");
        } else {
            System.out.println("Cancelled.");
        }
    }


    /**
     * initiates a wild encounter
     */
    private void wildEncounter() {
        System.out.println("You're walking through tall grass...");

        // Create a random wild Pokemon
        WildPokemon wild = createRandomWildPokemon();

        if (wild == null) {
            System.out.println("No Pokemon appeared...");
            return;
        }

        // Generate moves for the wild Pokemon
        wild.generateMoves();

        // Start the battle
        BattleSystem battle = new BattleSystem(player, null);
        battle.startWildBattle(wild);

        if (battle.didPlayerWin()) {
            //give XP to active Pokemon
            Pokemon activePokemon = player.getActivePokemon();

            if (activePokemon instanceof TrainedPokemon) {
                TrainedPokemon trained = (TrainedPokemon) activePokemon;
                trained.gainXP(50);  //50 XP for defeating wild pokemon
            }
        }

        if (!player.hasAvailablePokemon()) {
            gameOver = true;
        }
    }

    /**
     * creates a random wild pokemon from csv
     */
    private WildPokemon createRandomWildPokemon() {
        //get all pokemon
        Map<String, PokemonData> allPokemon = PokemonDataLoader.getAllPokemon();

        if (allPokemon == null || allPokemon.isEmpty()) {
            System.out.println("Error: No Pokemon data loaded!");
            return null;
        }

        //convert to a list to get random index
        List<PokemonData> pokemonList = new ArrayList<>(allPokemon.values());

        //pick a random pokemon
        int randomIndex = (int)(Math.random() * pokemonList.size());
        PokemonData data = pokemonList.get(randomIndex);

        Type pokemonType = Type.valueOf(data.getType().toUpperCase()); //converts type String to type enum

        //create wild pokemon
        WildPokemon wild = new WildPokemon(
                data.getName(),
                pokemonType,
                data.getAttack(),
                data.getHp(),
                data.getDefense(),
                data.getSpeed(),
                data.getSpAtk(),
                data.isLegendary(),
                data.getTotal() / 10
        );

        return wild;
    }

    /**
     * initiates a trainer battle
     */
    private void trainerBattle() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("A trainer approaches you!");
        System.out.println("=".repeat(60));

        if (player.getParty().size() < 3) {
            boolean check = IOHelper.readYesNo("It is recommended that you only engage trainer battles if you have at least 3 pokemon. Are you sure you want to continue? ");
            if (!check) {
                System.out.println("Try again when you have at least 3 pokemon!");
                return;  // Exit early if they decline
            }
        }

        RivalTrainer rival = createRivalTrainer();

        if (rival == null || rival.getParty().isEmpty()) { //null check
            System.out.println("The trainer has no Pokemon... you win..?");
            return;
        }

        //start battle
        BattleSystem battle = new BattleSystem(player, rival);
        battle.startTrainerBattle();

        if (battle.didPlayerWin()) { //check if player won
            System.out.println("\nYou won the battle!");

            //level up trainer
            player.levelUp();
            trainerLevel = player.getTrainerLevel();

            System.out.println("\nXP GAINED");
            for (Pokemon p : player.getParty()) {
                if (p instanceof TrainedPokemon && !p.hasFainted()) {
                    TrainedPokemon trained = (TrainedPokemon) p;
                    trained.gainXP(50);  //50 xp for ALL pokemon in party
                }
            }

            //heal after battle
            System.out.println("\nYour Pokemon have been healed at the Pokemon Center!");
            player.healAllPokemon();
        }

        if (!player.hasAvailablePokemon()) {
            gameOver = true;
            return;
        }
    }

    /**
     * creates a rival trainer with pokemon
     */
    private RivalTrainer createRivalTrainer() {
        String[] trainerNames = {"Gary", "Blue", "Green", "Trace", "Silver"};
        String randomName = trainerNames[(int)(Math.random() * trainerNames.length)]; //randomly pick trainer name

        RivalTrainer rival = new RivalTrainer(randomName);

        //give rival 1-6 Pokemon
        int numPokemon = 1 + (int)(Math.random() * 6);

        //get all Pokemon from database
        Map<String, PokemonData> allPokemon = PokemonDataLoader.getAllPokemon();
        List<PokemonData> pokemonList = new ArrayList<>(allPokemon.values());

        for (int i = 0; i < numPokemon; i++) {
            //pick random pokemon
            int randomIndex = (int)(Math.random() * pokemonList.size());
            PokemonData data = pokemonList.get(randomIndex);

            if (data != null) {
                Pokemon pokemon = PokemonFactory.createTrainedPokemon(data);
                pokemon.generateMoves();

                rival.getParty().add(pokemon);
                pokemon.setCurrentTrainer(rival);
            }
        }

        System.out.println(rival.getName() + " has " + rival.getParty().size() + " Pokemon!"); //show how many pokemon rival has

        return rival;
    }
}
