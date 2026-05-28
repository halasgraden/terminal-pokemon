package pokemon.src;

/**
 * Assigns Pokemon to a type based on their class
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/6/26
 */
public class PokemonFactory {

    //thresholds based on top 25% of stats within each column
    private static final int ATTACK_THRESHOLD = 100; //constants
    private static final int DEFENSE_THRESHOLD = 90;
    private static final int SPEED_THRESHOLD = 100;

    /**
     * creates a trained pokemon
     *
     * @param data pokemon object
     */
    public static Pokemon createTrainedPokemon(PokemonData data) {
        //extract all stats from csv data
        String name = data.getName();
        Type type = Type.valueOf(data.getType().toUpperCase());
        int hp = data.getHp();
        int attack = data.getAttack();
        int defense = data.getDefense();
        int speed = data.getSpeed();
        int specialAttack = data.getSpAtk();
        boolean isLegendary = data.isLegendary();

        //since pokemon cannot be more than one type, I am creating a priority system for which type they are assigned
        //priority order: speed, attack, tank
        if (attack >= ATTACK_THRESHOLD) {
            System.out.println("Creating ATTACKER Pokemon: " + name);
            return new AttackPokemon(name, type, hp, attack, defense, speed, specialAttack, isLegendary);
        }

        if (speed >= SPEED_THRESHOLD) {
            System.out.println("Creating SPEEDSTER Pokemon: " + name);
            return new SpeedPokemon(name, type, hp, attack, defense, speed, specialAttack, isLegendary);
        }

        if (defense >= DEFENSE_THRESHOLD) {
            System.out.println("Creating Tank Pokemon: " + name);
            return new TankPokemon(name, type, hp, attack, defense, speed, specialAttack, isLegendary);
        }

        data.setCurrentHP(data.getHp());

        return new TrainedPokemon(name, type, hp, attack, defense, speed, specialAttack, isLegendary);
    }

    /**
     * creates a wild pokemon
     *
     * @param data pokemon object
     */
    public static WildPokemon createWildPokemon(PokemonData data) {
        String name = data.getName();
        Type type = Type.valueOf(data.getType().toUpperCase());
        int hp = data.getHp();
        int attack = data.getAttack();
        int defense = data.getDefense();
        int speed = data.getSpeed();
        int specialAttack = data.getSpAtk();
        boolean isLegendary = data.isLegendary();

        //calculate catch difficulty based on total stats
        int totalStats = hp + attack + defense + speed + specialAttack;
        int catchDifficulty = totalStats / 10;  //higher stats = harder to catch

        //legendary Pokemon are extra hard to catch
        if (isLegendary) {
            catchDifficulty = (int)(catchDifficulty * 1.5);
        }

        System.out.println("A wild " + name + " appeared!");

        return new WildPokemon(name, type, hp, attack, defense, speed, specialAttack, isLegendary, catchDifficulty);
    }
}
