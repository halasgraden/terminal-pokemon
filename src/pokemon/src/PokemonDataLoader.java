/**
 * Loads and parse pokemon data
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/2/26
 */

package pokemon.src;
import java.io.*;
import java.util.*;

public class PokemonDataLoader {
    private static final String filename = "pokemonData.csv"; //constant
    private static Map<String, PokemonData> pokemonDatabase = new HashMap<>(); //MAP where pokemon are stored

    /**
     * reads csv and loads pokemon
     *
     * @param filename pokemon csv
     */
    public static void loadPokemonCSV(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            int lineCount = 0; //keeping track

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                try {
                    PokemonData pokemon = parsePokemon(line);

                    pokemonDatabase.put(pokemon.getName().toLowerCase(), pokemon);
                    lineCount++;

                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }

            scanner.close();
            System.out.println("Successfully loaded " + lineCount + " Pokemon!");

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            System.err.println("Make sure the CSV file is in the correct location.");
        }
    }

    /**
     * Parses each line of csv and converts it into usable parts
     *
     * @param line a line from pokemon csv
     */
    public static PokemonData parsePokemon(String line) {
        String[] parts = line.split(",");

        String name = parts[1].trim();
        String type = parts[2].trim();
        int hp = Integer.parseInt(parts[5].trim());
        int attack = Integer.parseInt(parts[6].trim());
        int defense = Integer.parseInt(parts[7].trim());
        int specialAttack = Integer.parseInt(parts[8].trim());
        int speed = Integer.parseInt(parts[10].trim());
        boolean isLegendary = parts[12].trim().equalsIgnoreCase("True");

        return new PokemonData(name, type, hp, attack, defense, specialAttack, speed, isLegendary);
    }

    public static PokemonData getPokemon(String name) {
        return pokemonDatabase.get(name.toLowerCase());
    } //getters

    public static Map<String, PokemonData> getAllPokemon() {
        return pokemonDatabase;
    }

    public static int getPokemonCount() {
        return pokemonDatabase.size();
    }

    public static boolean hasPokemon(String name) {
        return pokemonDatabase.containsKey(name);
    }
}
