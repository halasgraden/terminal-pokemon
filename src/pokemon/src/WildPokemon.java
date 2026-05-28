/**
 * Wild pokemon not owned by any trainer; can be caught and turned into a TrainedPokemon
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package pokemon.src;
import move.src.*;

public class WildPokemon extends Pokemon implements Catchable {
    private boolean canFlee; //instance variables
    private int catchDifficulty;
    private int catchAttempts;

    //constructor
    public WildPokemon(String name, Type type, int attack, int hp, int defense, int speed, int specialAttack, boolean isLegendary, int catchDifficulty) {
        super(name, type, attack, hp, defense, speed, specialAttack, isLegendary);
        this.catchDifficulty = catchDifficulty;
        this.canFlee = true;
        this.catchAttempts = 0;
    }

    //constructor if catchDifficulty is unknown
    public WildPokemon(String name, Type type, int attack, int hp, int defense, int speed, int specialAttack, boolean isLegendary) {
        super(name, type, attack, hp, defense, speed, specialAttack, isLegendary);

        int total = hp + attack + defense + speed + specialAttack;
        this.catchDifficulty = total / 10;

        this.canFlee = true;
        this.catchAttempts = 0;
    }

    /**
     * attempt to catch a wild pokemon
     *
     * @param catchDifficulty how hard it is to catch a pokemon
     */
    public boolean attemptCatch(int catchDifficulty) {
        catchAttempts++;

        //easier to catch with lower hp
        double hpFactor = (double) currentHP / hp;

        //calculate with hp factor
        double baseCatchRate = this.catchDifficulty;
        double hpBonus = (100 - this.catchDifficulty) * (1 - hpFactor); //as hp decreases, catch chance increases
        double catchChance = baseCatchRate + hpBonus;

        //
        if (isLegendary) {
            catchChance *= 0.5;  //legendaries are 50% harder to catch
        }

        //random chance to catch
        boolean caught = Math.random() * 100 < catchChance;

        if (caught) {
            canFlee = false;  //cant flee once caught
        }

        return caught;
    }

    public boolean attemptCatch() { //copy constructor
        return attemptCatch(0);
    }

    /**
     * allows pokemon to flee
     */
    public boolean flee() {

        System.out.println(name + " attempts to flee...");

        boolean fled = Math.random() < 0.34; //34% chance to flee

        if (fled) {
            System.out.println(name + " has fled!");
        } else {
            System.out.println(name + " couldn't escape");
        }

        return fled;
    }

    /**
     * converts to a trained pokemon
     */
    public TrainedPokemon convertToTrained() { //once a wild pokemon is caught, convert to trainedpokemon
        TrainedPokemon trained = new TrainedPokemon(
                this.name,
                this.type,
                this.hp,
                this.attack,
                this.defense,
                this.speed,
                this.specialAttack,
                this.isLegendary,
                5,      // Start at level 5
                0       // Start with 0 XP
        );

        //copies current hp and status
        trained.setCurrentHP(this.currentHP);
        trained.setStatus(this.status);

        //copies moves
        for (Move move : this.moveSet) {
            trained.addMove(move);
        }

        return trained;
    }

    public boolean getCanFlee() { //getters
        return canFlee;
    }

    public int getCatchDifficulty() {
        return catchDifficulty;
    }

    public int getCatchAttempts() {
        return catchAttempts;
    }

    @Override //toString
    public String toString() {
        return "[WILD] " + super.toString();
    }
}
