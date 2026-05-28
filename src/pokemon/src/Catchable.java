/**
 * Interface for pokemon that are catchable
 *
 * @author Halas Graden
 * @email graden@usc.edu
 * TAC 265, Spring 2026
 * Date created: 4/30/26
 */

package pokemon.src;


public interface Catchable {
    boolean attemptCatch(int catchDifficulty);
}
