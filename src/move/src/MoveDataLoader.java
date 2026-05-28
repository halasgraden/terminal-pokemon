package move.src;

/**
 * Loads and stores move data
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 5/6/26
 */

import pokemon.src.*;
import java.io.*;
import java.util.*;

public class MoveDataLoader {
    private static Map<String, MoveData> moveDatabase = new HashMap<>(); //MAP where moves will be stored

    /**
     * reads csv and loads moves
     *
     * @param filename pokemon moves csv
     */
    public static void loadMovesCSV(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            if (scanner.hasNextLine()) { //skipping header
                scanner.nextLine();
            }

            int lineCount = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                try {
                    MoveData move = parseMove(line);

                    //only storing physical, special, and status moves
                    if (isValidMoveKind(move.getKind())) {
                        moveDatabase.put(move.getName(), move);
                        lineCount++;  //tracking how many moves
                    }

                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }

            scanner.close();
            System.out.println("Successfully loaded " + lineCount + " moves!");

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename); //if file not found
        }
    }

    /**
     * Parses each line of csv and converts it into usable parts
     *
     * @param line a line from pokemon moves csv
     */
    private static MoveData parseMove(String line) {
        String[] parts = line.split(",");

        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        String effect = parts[2].trim();
        String type = parts[3].trim();
        String kind = parts[4].trim();

        int power = 0; //defaults to 0 if empty
        if (parts[5] != null && !parts[5].trim().isEmpty()) {
            power = Integer.parseInt(parts[5].trim());
        }

        int accuracy = 90; //defaults to 90 if empty
        if (parts[6] != null && !parts[6].trim().isEmpty()) {
            String accStr = parts[6].trim().replace("%", ""); //remove the % and return as integer
            if (!accStr.isEmpty()) {
                accuracy = Integer.parseInt(accStr);
            }
        }

        int pp = Integer.parseInt(parts[7].trim());

        return new MoveData(id, name, effect, type, kind, power, accuracy, pp);
    }

    /**
     * Checks if physical, special, or status move
     *
     * @param kind "type" of move
     */
    private static boolean isValidMoveKind(String kind) { //checking if physical, special, or status move
        return kind.equals("Physical") || kind.equals("Special") || kind.equals("Status");
    }

    //get a move by name
    public static MoveData getMove(String name) {
        return moveDatabase.get(name);
    }

    /**
     * Creates a new move
     *
     * @param moveName name of the move
     */
    public static Move createMove(String moveName) {
        MoveData data = moveDatabase.get(moveName);

        if (data == null) {
            System.out.println("Move not found: " + moveName);
            return null;
        }

        //converts move type to Type
        Type moveType;
        try {
            moveType = Type.valueOf(data.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown type for move " + moveName + ": " + data.getType());
            moveType = Type.NORMAL;  //defaults to normal
        }

        //creates move type
        switch (data.getKind()) { //in each case, creates an object of that type
            case "Physical":
                return new PhysicalMove(
                        data.getName(),
                        moveType,
                        data.getPower(),
                        data.getAccuracy(),
                        data.getPP()
                );

            case "Special":
                return new SpecialMove(
                        data.getName(),
                        moveType,
                        data.getPower(),
                        data.getAccuracy(),
                        data.getPP()
                );

            case "Status":
                Status statusEffect = determineStatusEffect(data.getName(), data.getEffect());
                return new StatusMove(
                        data.getName(),
                        moveType,
                        data.getAccuracy(),
                        data.getPP(),
                        statusEffect
                );

            default:
                System.out.println("Unknown move kind: " + data.getKind());
                return null;
        }
    }

    /**
     * checks by name and effect to assign Status type
     *
     * @param name name of the move
     * @param effect effect of the move
     */
    private static Status determineStatusEffect(String name, String effect) {
        String nameUpper = name.toUpperCase().trim(); //ignore case and unusable characters
        String effectLower = effect.toLowerCase();

        //check by name
        if (nameUpper.contains("PARALYZE") || nameUpper.contains("THUNDER WAVE") ||
                nameUpper.contains("GLARE") || nameUpper.contains("STUN SPORE")) {
            return Status.PARALYZED;
        }

        if (nameUpper.contains("POISON") || nameUpper.contains("TOXIC")) {
            return Status.POISONED;
        }

        if (nameUpper.contains("BURN") || nameUpper.contains("WILL-O-WISP")) {
            return Status.BURNED;
        }

        if (nameUpper.contains("SLEEP") || nameUpper.contains("HYPNOSIS") ||
                nameUpper.contains("SING") || nameUpper.contains("SPORE")) {
            return Status.ASLEEP;
        }

        if (nameUpper.contains("FREEZE") || nameUpper.contains("ICE")) {
            return Status.FROZEN;
        }

        //check by effect
        if (effectLower.contains("paralyze")) {
            return Status.PARALYZED;
        }
        if (effectLower.contains("poison")) {
            return Status.POISONED;
        }
        if (effectLower.contains("burn")) {
            return Status.BURNED;
        }
        if (effectLower.contains("sleep")) {
            return Status.ASLEEP;
        }
        if (effectLower.contains("freeze")) {
            return Status.FROZEN;
        }

        //default to normal if none of the above apply
        return Status.NORMAL;
    }

    public static Map<String, MoveData> getAllMoves() {
        return moveDatabase;
    }

    /**
     * categorize moves by type
     *
     * @param type type of the move
     */
    public static List<MoveData> getMovesByType(String type) {
        List<MoveData> result = new ArrayList<>();
        for (MoveData move : moveDatabase.values()) {
            if (move.getType().equalsIgnoreCase(type)) {
                result.add(move);
            }
        }
        return result;
    }

    /**
     * categorize moves by kind
     *
     * @param kind kind of the move
     */
    public static List<MoveData> getMovesByKind(String kind) {
        List<MoveData> result = new ArrayList<>();
        for (MoveData move : moveDatabase.values()) {
            if (move.getKind().equals(kind)) {
                result.add(move);
            }
        }
        return result;
    }

    public static int getMoveCount() {
        return moveDatabase.size();
    }
}
