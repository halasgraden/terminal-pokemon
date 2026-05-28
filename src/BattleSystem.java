import java.util.*;
import pokemon.src.*;
import trainer.src.*;
import move.src.*;

/**
 * Turn-based logic for battles between trainer and computer
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/7/26
 */
public class BattleSystem {

    private PlayerTrainer player; //instance variables
    private RivalTrainer rival;
    private boolean battleActive; //using this to check when the battle isnt active
    private boolean playerWon;

    public BattleSystem(PlayerTrainer player, RivalTrainer rival) { //constructor
        this.player = player;
        this.rival = rival;
        this.battleActive = false;
        this.playerWon = false;
    }

    public boolean didPlayerWin() { //getter
        return playerWon;
    }

    private enum PlayerTurnResult { //using an internal enum to manage state
        ATTACK,
        SWITCHED,
        USED_ITEM,
        RAN_AWAY
    }

    /**
     * starts a trainer battle
     */
    public void startTrainerBattle() { //trainer vs trainer battle
        IOHelper.println("\n" + "=".repeat(60)); //found this .repeat() method
        IOHelper.println("TRAINER BATTLE");
        IOHelper.println(player.getName() + " vs " + rival.getName());
        IOHelper.println("=".repeat(60));

        //quick check to ensure both parties have pokemon
        if (player.getParty().isEmpty()) {
            IOHelper.println(player.getName() + " has no Pokemon!");
            return;
        }

        if (rival.getParty().isEmpty()) {
            IOHelper.println(rival.getName() + " has no Pokemon!");
            return;
        }

        //set active pokemon for both parties
        if (player.getParty().get(0).hasFainted()) {
            //find first non-fainted Pokemon
            for (Pokemon p : player.getParty()) {
                if (!p.hasFainted()) {
                    player.setActivePokemon(p);
                    break;
                }
            }
        } else {
            player.setActivePokemon(player.getParty().get(0));
        }
        rival.setActivePokemon(rival.getParty().get(0));

        IOHelper.println(player.getName() + " sent out " + player.getActivePokemon().getName() + "!"); //display
        IOHelper.println(rival.getName() + " sent out " + rival.getActivePokemon().getName() + "!");

        battleActive = true;
        int turnCount = 1; //keeps track of how many turns have been played in the battle

        //main battle loop
        while (battleActive) {
            IOHelper.println("\n" + "-".repeat(60));
            IOHelper.println("TURN " + turnCount);
            IOHelper.println("-".repeat(60));

            //show current status of battle
            showBattleStatus();

            //check who goes first based on speed
            Pokemon playerPokemon = player.getActivePokemon();
            Pokemon rivalPokemon = rival.getActivePokemon();

            boolean playerGoesFirst = playerPokemon.getEffectiveSpeed() >= rivalPokemon.getEffectiveSpeed(); //determine first turn

            PlayerTurnResult playerResult = executePlayerAction();

            if (playerResult == PlayerTurnResult.RAN_AWAY) {
                IOHelper.println("\n" + player.getName() + " ran away!");
                battleActive = false;
                return; //I'm returning here so the rival doesn't execute a move after I run away
            }

            Move rivalMove = rival.selectMove(playerPokemon);

            boolean playerSwitched = (playerResult == PlayerTurnResult.SWITCHED);
            boolean playerUsedItem = (playerResult == PlayerTurnResult.USED_ITEM);

            if (playerGoesFirst) {
                //player attacks first
                if (playerResult == PlayerTurnResult.ATTACK) {
                    boolean canAct = playerPokemon.applyStatusEffects(); //apply status effects before turn is executed
                    if (canAct && !playerPokemon.hasFainted()) {
                        Move playerMove = player.selectMove(rivalPokemon);
                        if (playerMove != null) {
                            playerMove.execute(playerPokemon, rivalPokemon);
                        }
                    }
                }

                //check if rival pokemon fainted
                if (rivalPokemon.hasFainted()) {
                    IOHelper.println("\n" + rivalPokemon.getName() + " fainted!");
                    if (!rival.hasAvailablePokemon()) {
                        endBattle(player);
                        break;
                    }
                    rival.autoSwitch();
                    turnCount++;
                    continue;
                }

                //rival attacks second
                boolean rivalCanAct = rivalPokemon.applyStatusEffects();
                if (rivalCanAct && !rivalPokemon.hasFainted() && rivalMove != null) {
                    rivalMove.execute(rivalPokemon, playerPokemon);
                }

            } else {
                //rival attacks first
                boolean rivalCanAct = rivalPokemon.applyStatusEffects();
                if (rivalCanAct && !rivalPokemon.hasFainted() && rivalMove != null) {
                    rivalMove.execute(rivalPokemon, playerPokemon);
                }

                //check if player pokemon fainted
                if (playerPokemon.hasFainted()) {
                    IOHelper.println("\n" + playerPokemon.getName() + " fainted!");
                    if (!player.hasAvailablePokemon()) {
                        if (!player.hasAvailablePokemon()) {
                            IOHelper.println("\nYou blacked out!");
                            IOHelper.println("GAME OVER");
                            endBattle(rival);
                            break;
                        }
                        player.autoSwitch();
                        turnCount++;
                        continue;
                    }
                }

                //player attacks second
                if (playerResult == PlayerTurnResult.ATTACK) {
                    boolean canAct = playerPokemon.applyStatusEffects();
                    if (canAct && !playerPokemon.hasFainted()) {
                        Move playerMove = player.selectMove(rivalPokemon);
                        if (playerMove != null) {
                            playerMove.execute(playerPokemon, rivalPokemon);
                        }
                    }
                }
            }

            if (playerPokemon.hasFainted()) { //check if player pokemon fainted
                IOHelper.println("\n" + playerPokemon.getName() + " fainted!");
                if (!player.hasAvailablePokemon()) {
                    IOHelper.println("\nYou blacked out!");
                    IOHelper.println("GAME OVER");
                    endBattle(rival);
                    break;
                }
                player.autoSwitch();
            }

            if (rivalPokemon.hasFainted()) { //check if rival fainted
                IOHelper.println("\n" + rivalPokemon.getName() + " fainted!");
                if (!rival.hasAvailablePokemon()) {
                    endBattle(player);
                    break;
                }
                rival.autoSwitch();
            }

            turnCount++;

            if (turnCount > 35) { //I realized that battles had a chance of dragging on; set max turn count at 35
                IOHelper.println("\nBattle ended in a draw!");
                battleActive = false;
            }
        }
        for (Pokemon p : player.getParty()) { //reset status
            if (!p.hasFainted()) {
                p.setStatus(Status.NORMAL);
            }
        }
    }

    /**
     * execute trainer's turn
     */
    private boolean executeTurn(Trainer attacker, Trainer defender, boolean isPlayer) {
        Pokemon attackerPokemon; //initializing attack and defender
        Pokemon defenderPokemon;

        if (isPlayer) {
            attackerPokemon = player.getActivePokemon();
            defenderPokemon = rival.getActivePokemon();
        } else {
            attackerPokemon = rival.getActivePokemon();
            defenderPokemon = player.getActivePokemon();
        }

        //apply status effect (some effects dont allow user to move)
        boolean canAct = attackerPokemon.applyStatusEffects();

        if (attackerPokemon.hasFainted()) { //check if fainted
            IOHelper.println(attackerPokemon.getName() + " fainted!");
            handleFaintedPokemon(attacker, isPlayer);

            boolean battleIsOver = battleOver(); //check if battle over
            if (battleIsOver) {
                return false;
            } else {
                return true;
            }
        }

        if (!canAct) {
            IOHelper.println(attackerPokemon.getName() + " cannot move!");
            return true; //skip turn
        }

        //get trainer action
        if (isPlayer) {
            PlayerTurnResult result = executePlayerAction();
            if (result == PlayerTurnResult.RAN_AWAY) {
                IOHelper.println("\n" + player.getName() + " ran away!");
                battleActive = false; //shut battle off
                return false;
            } else if (result == PlayerTurnResult.SWITCHED) {
                return true; //turn was used up
            } else if (result == PlayerTurnResult.USED_ITEM) {
                return true; //turn used
            }
        } else {
            //this is for the computer, uses its .run() function to determine what to do
            rival.run();
        }

        //attack with selected move
        Move selectedMove = attacker.selectMove(defenderPokemon);

        if (selectedMove != null) {
            selectedMove.execute(attackerPokemon, defenderPokemon);
        }

        if (defenderPokemon.hasFainted()) { //check if defender fainted
            IOHelper.println(defenderPokemon.getName() + " fainted!");

            boolean notIsPlayer = !isPlayer;
            handleFaintedPokemon(defender, notIsPlayer);

            boolean battleIsOver = battleOver();
            if (battleIsOver) {
                return false;
            } else {
                return true;
            }
        }

        return true; //continue battle
    }

    /**
     * player chooses what action to do and assigns it to PlayerTurnResult enum
     */
    private PlayerTurnResult executePlayerAction() {
        IOHelper.println("\nWhat will you do?");
        IOHelper.println("1. Attack");
        IOHelper.println("2. Switch Pokemon");
        IOHelper.println("3. Use Item");
        IOHelper.println("4. Run");

        int choice = IOHelper.readInt("Choice: ", 1, 4);

        if (choice == 1) {
            return PlayerTurnResult.ATTACK;
        } else if (choice == 2) {
            player.switchPokemon();
            return PlayerTurnResult.SWITCHED;
        } else if (choice == 3) {
            player.useItem();
            return PlayerTurnResult.USED_ITEM;
        } else if (choice == 4) {
            return PlayerTurnResult.RAN_AWAY;
        } else {
            return PlayerTurnResult.ATTACK;
        }
    }

    /**
     * displays current pokemon in battle
     */
    private void showBattleStatus() {
        IOHelper.println("\n[PLAYER] " + player.getActivePokemon().getName() +
                " (" + player.getActivePokemon().getType() + ")" +
                " - HP: " + player.getActivePokemon().getCurrentHP() + "/" +
                player.getActivePokemon().getHp() +
                " - Status: " + player.getActivePokemon().getStatus());

        IOHelper.println("[RIVAL] " + rival.getActivePokemon().getName() +
                " (" + rival.getActivePokemon().getType() + ")" +
                " - HP: " + rival.getActivePokemon().getCurrentHP() + "/" +
                rival.getActivePokemon().getHp() +
                " - Status: " + rival.getActivePokemon().getStatus());
    }

    /**
     * if pokemon faints; checks win
     */
    private void handleFaintedPokemon(Trainer trainer, boolean isPlayer) {
        if (!trainer.hasAvailablePokemon()) {
            //if trainer has no more pokemon they lose
            Trainer winner;
            if (isPlayer) {
                winner = rival;
            } else {
                winner = player;
            }
            endBattle(winner);
            return;
        }

        //auto switch to next pokemon
        if (isPlayer) {
            player.autoSwitch();
        } else {
            rival.autoSwitch();
        }
    }

    /**
     * checks if battle is over
     */
    private boolean battleOver() {
        if (!player.hasAvailablePokemon()) {
            endBattle(rival);
            return true;
        }
        if (!rival.hasAvailablePokemon()) {
            endBattle(player);
            return true;
        }
        return false;
    }

    /**
     * end battle and announce winner
     */
    private void endBattle(Trainer winner) {
        IOHelper.println("\n" + "=".repeat(60));
        IOHelper.println("BATTLE OVER!");
        IOHelper.println(winner.getName() + " wins!");
        IOHelper.println("=".repeat(60));
        battleActive = false;

        if (winner == player) {
            playerWon = true;
        } else {
            playerWon = false;
        }
    }

    /**
     * start a wild encounter; a lot of the same logic as above
     */
    public void startWildBattle(WildPokemon wild) {
        IOHelper.println("\n" + "=".repeat(60));
        IOHelper.println("WILD ENCOUNTER");
        IOHelper.println("A wild " + wild.getName() + " appeared!");

        if (player.getParty().isEmpty()) { //party check
            IOHelper.println(player.getName() + " has no Pokemon!");
            return;
        }

        if (player.getParty().get(0).hasFainted()) {
            //find first non-fainted Pokemon
            for (Pokemon p : player.getParty()) {
                if (!p.hasFainted()) {
                    player.setActivePokemon(p);
                    break;
                }
            }
        } else {
            player.setActivePokemon(player.getParty().get(0));
        }

        IOHelper.println("Go, " + player.getActivePokemon().getName() + "!");
        IOHelper.println("=".repeat(60));

        battleActive = true;
        int turnCount = 1;

        while (battleActive) {
            IOHelper.println("\n" + "-".repeat(60));
            IOHelper.println("TURN " + turnCount);
            IOHelper.println("-".repeat(60));

            //show status of both pokemon
            IOHelper.println("\n[PLAYER] " + player.getActivePokemon().getName() +
                    " (" + player.getActivePokemon().getType() + ")" +
                    " - HP: " + player.getActivePokemon().getCurrentHP() + "/" +
                    player.getActivePokemon().getHp() +
                    " - Status: " + player.getActivePokemon().getStatus());
            IOHelper.println("[WILD] " + wild.getName() +
                    " (" + wild.getType() + ")" +
                    " - HP: " + wild.getCurrentHP() + "/" + wild.getHp() +
                    " - Status: " + wild.getStatus());

            //apply status effect (some effects dont allow user to move)
            boolean canAct = player.getActivePokemon().applyStatusEffects();

            if (player.getActivePokemon().hasFainted()) { //check if all pokemon have fainted
                IOHelper.println(player.getActivePokemon().getName() + " fainted!");
                if (!player.hasAvailablePokemon()) {
                    IOHelper.println("All your Pokemon fainted!");
                    IOHelper.println("You blacked out!");
                    IOHelper.println("GAME OVER");

                    playerWon = false;
                    battleActive = false;

                    return;
                }
                player.autoSwitch();
            }

            //players turn
            IOHelper.println("\n=== YOUR TURN ===");
            IOHelper.println("\nWhat will you do?");
            IOHelper.println("1. Attack");
            IOHelper.println("2. Switch Pokemon");
            IOHelper.println("3. Use Item (Potion/Pokeball)");
            IOHelper.println("4. Run");

            int choice = IOHelper.readInt("Choice: ", 1, 4);

            boolean playerAttacked = false;
            boolean playerUsedPotion = false;

            if (choice == 1) {
                //attack
                if (canAct) {
                    Move playerMove = player.selectMove(wild);
                    if (playerMove != null) {
                        playerMove.execute(player.getActivePokemon(), wild);
                        playerAttacked = true;
                    }
                }
            } else if (choice == 2) {
                //switch
                player.switchPokemon();
            } else if (choice == 3) {
                //use item
                IOHelper.println("\n1. Potion");
                IOHelper.println("2. Pokeball");
                int itemChoice = IOHelper.readInt("Choice: ", 1, 2);

                if (itemChoice == 2) {
                    boolean caught = player.attemptCatch(wild);
                    if (caught) {
                        battleActive = false;
                        return; //battle ends
                    }
                    playerAttacked = true; //using an item counts as a move
                } else {
                    player.useItem();
                    playerUsedPotion = true; //same here
                }
            } else if (choice == 4) {
                //run
                IOHelper.println("Got away safely!");
                battleActive = false;
                return;
            }

            //check if fainted
            if (wild.hasFainted()) {
                IOHelper.println("\nThe wild " + wild.getName() + " fainted!");
                IOHelper.println(player.getName() + " won!");
                playerWon = true;
                battleActive = false;
                break;
            }

            //wild pokemons turn
            if (playerAttacked || playerUsedPotion) {
                //apply status effects
                boolean wildCanAct = wild.applyStatusEffects();

                if (wild.hasFainted()) {
                    IOHelper.println("\nThe wild " + wild.getName() + " fainted!");
                    IOHelper.println(player.getName() + " won!");
                    battleActive = false;
                    break;
                }

                //wild pokemon attack
                if (wildCanAct && !wild.getMoveSet().isEmpty()) {
                    IOHelper.println("\n=== WILD " + wild.getName().toUpperCase() + "'S TURN ===");

                    int randomIndex = (int)(Math.random() * wild.getMoveSet().size());
                    Move wildMove = wild.getMoveSet().get(randomIndex);

                    wildMove.execute(wild, player.getActivePokemon());
                }

                //the pokemon has the ability to flee
                if (wild.getCanFlee() && Math.random() < 0.1) { //10% chance to flee
                    boolean fled = wild.flee();
                    if (fled) {
                        battleActive = false;
                        break;
                    }
                }
            }

            //check if players pokemon fainted after wild attack
            if (player.getActivePokemon().hasFainted()) {
                IOHelper.println("\n" + player.getActivePokemon().getName() + " fainted!");
                if (!player.hasAvailablePokemon()) {
                    IOHelper.println("All your Pokemon fainted!");
                    IOHelper.println("You lost!");
                    battleActive = false;
                    break;
                }
                player.autoSwitch();
            }

            turnCount++;

            //safety of 35 turns per battle
            if (turnCount > 35) {
                IOHelper.println("\nThe wild Pokemon got away!");
                battleActive = false;
            }
        }

        for (Pokemon p : player.getParty()) { //reset status
            if (!p.hasFainted()) {
                p.setStatus(Status.NORMAL);
            }
        }
    }
}
