/**
 * PlayerTrainer controls the user trainer's party and battle logic
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

public class PlayerTrainer extends Trainer {
    private int pokeballs; //instance variables
    private Scanner scanner;
    private Pokemon activePokemon;

    private int trainerLevel;

    //constructors
    public PlayerTrainer(String name, List<Pokemon> party) {
        super(name, party);
        this.pokeballs = 10;
        this.scanner = new Scanner(System.in);
        this.trainerLevel = 1;

        if (!party.isEmpty()) {
            this.activePokemon = party.get(0);
        }
    }

    public PlayerTrainer(String name) {
        super(name);
        this.pokeballs = 5;
        this.scanner = new Scanner(System.in); //I cannot use IOHelper outside of root :( would love to know workaround to this
        this.trainerLevel = 1;
    }

    public int getPokeBalls() { //getters
        return pokeballs;
    }

    public int getTrainerLevel() {
        return trainerLevel;
    }

    public void setTrainerLevel(int trainerLevel) { //setter
        this.trainerLevel = trainerLevel;
    }

    /**
     * allows trainer to level up
     */
    public void levelUp() {
        trainerLevel++;
        System.out.println("\nLEVEL UP!");
        System.out.println("You are now level " + trainerLevel + "!");

        //going to give them a pokeball every time they level up
        pokeballs++;
        System.out.println("You received 1 pokeball!");
    }

    /**
     * runs the player's turn in battle
     */
    @Override
    public void run() {
        if (activePokemon == null && !getParty().isEmpty()) { //ensure there is an active pokemon
            activePokemon = getParty().get(0);
        }

        System.out.println("\n=== " + getName() + "'s Turn ===");
        System.out.println("Active: " + activePokemon);

        //show action menu
        System.out.println("\nWhat will you do?");
        System.out.println("1. Attack");
        System.out.println("2. Switch Pokemon");
        System.out.println("3. Use Item");
        System.out.println("4. Run");

        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); //get rid of the newline

        switch (choice) {
            case 1:
                System.out.println("You chose to attack!");
                break;
            case 2:
                switchPokemon();
                break;
            case 3:
                useItem();
                break;
            case 4:
                System.out.println("You ran away!");
                break;
            default:
                System.out.println("Invalid choice! Turn wasted.");
        }
    }

    /**
     * allows user to select a move
     *
     * @param opponent opponent receiving the effects of the move
     */
    @Override
    public Move selectMove(Pokemon opponent) {
        //ensure the pokemon has moves
        if (activePokemon == null || activePokemon.getMoveSet().isEmpty()) {
            System.out.println("No moves available!");
            return null;
        }

        //show moves
        System.out.println("\n=== SELECT MOVE ===");
        List<Move> moves = activePokemon.getMoveSet();

        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            System.out.println((i + 1) + ". " + move);
        }

        //get the player choice
        System.out.print("Choose move (1-" + moves.size() + "): ");
        int choice = scanner.nextInt() - 1; //converting to index
        scanner.nextLine(); //get rid of newline

        //retrieve choice from moves
        if (choice >= 0 && choice < moves.size()) {
            return moves.get(choice);
        } else {
            System.out.println("Invalid choice! Using first move.");
            return moves.get(0);
        }
    }

    /**
     * allows user to switch pokemon
     */
    public void switchPokemon() {
        System.out.println("\n=== YOUR PARTY ===");

        //show all pokemon in party
        for (int i = 0; i < getParty().size(); i++) {
            Pokemon p = getParty().get(i);
            String status;
            if (p.hasFainted()) { //check if fainted
                status = " [FAINTED]";
            } else {
                status = "";
            }

            String current;
            if (p == activePokemon) { // determine if active
                current = " [ACTIVE]";
            } else {
                current = "";
            }

            System.out.println((i + 1) + ". " + p + status + current);
        }

        System.out.print("\nSwitch to which Pokemon? (0 to cancel): "); //get user choice
        int choice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (choice == -1) {
            System.out.println("Cancelled."); //if user enters 0
            return;
        }

        if (choice >= 0 && choice < getParty().size()) {
            Pokemon newActive = getParty().get(choice);

            if (newActive.hasFainted()) {
                System.out.println(newActive.getName() + " has fainted! Choose another."); //cant allow fainted pokemon to switch in
                return;
            }

            if (newActive == activePokemon) {
                System.out.println(newActive.getName() + " is already active!"); //cant allow active pokemon to switch in
                return;
            }

            activePokemon = newActive; //switch active to new pokemon
            System.out.println("Go, " + activePokemon.getName() + "!");
        } else {
            System.out.println("Invalid choice!");
        }
    }

    //allow user to use items

    /**
     * allows user to use items
     */
    public void useItem() {
        System.out.println("\n=== ITEMS ===");
        System.out.println("1. Potion (Heal 20 HP)");
        System.out.println("2. Pokeball (Catch wild Pokemon) - " + pokeballs + " remaining");
        System.out.println("0. Cancel");

        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                usePotion();
                break;
            case 2:
                System.out.println("Can only use Pokeballs on wild Pokemon!");
                break;
            default:
                System.out.println("Cancelled.");
        }
    }

    /**
     * user uses a potion
     */
    private void usePotion() { //used above to heal pokemon
        if (activePokemon == null) {
            System.out.println("No active Pokemon!"); //check for active pokemon
            return;
        }

        int healAmount = 20;
        activePokemon.restoreHP(healAmount); //heal hp by 20
        System.out.println(activePokemon.getName() + " was healed for " + healAmount + " HP!");
    }

    //attempt to catch the pokemon

    /**
     * user attempts catch
     *
     * @param wild the wild pokemon being caught
     */
    public boolean attemptCatch(WildPokemon wild) {
        if (pokeballs <= 0) {
            System.out.println("No pokeballs left!"); //if no pokeballs left
            return false;
        }

        pokeballs--; //use up a pokeball
        System.out.println(getName() + " threw a pokeball! (" + pokeballs + " left)");

        //attempts catch
        boolean caught = wild.attemptCatch();

        if (caught) {
            System.out.println("Gotcha! " + wild.getName() + " was caught!");

            //convert wild to trained Pokemon
            TrainedPokemon trained = wild.convertToTrained();

            //add trained to party
            addToParty(trained);

            System.out.println(wild.getName() + " was added to your party!");
            return true;
        } else {
            System.out.println(wild.getName() + " broke free!");
            return false;
        }
    }

    /**
     * heal ALL pokemon
     */
    public void healAllPokemon() {
        for (Pokemon p : getParty()) {
            p.setCurrentHP(p.getHp()); //resets to max hp
            p.setStatus(Status.NORMAL); //resets to no status
        }
        System.out.println("All Pokemon have been fully healed!");
    }

    /**
     * checks if pokemon are left
     */
    @Override
    public boolean hasAvailablePokemon() { //I created this as a check to see if the user HAS pokemon left to fight
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
    public void autoSwitch() { //this auto switches to next pokemon if a pokemon has fainted
        if (activePokemon != null && !activePokemon.hasFainted()) {
            return;
        }

        //find first non fainted pokemon
        for (Pokemon p : getParty()) {
            if (!p.hasFainted()) {
                activePokemon = p;
                System.out.println("Go, " + activePokemon.getName() + "!");
                return;
            }
        }

        //no pokemon available
        System.out.println("All your Pokemon have fainted!");
        activePokemon = null;
    }

    public Pokemon getActivePokemon() { //getter and setter for active pokemon
        return activePokemon;
    }

    public void setActivePokemon(Pokemon activePokemon) {
        this.activePokemon = activePokemon;
    }

    public void setPokeballs(int pokeballs) {
        this.pokeballs = pokeballs;
    }
}
