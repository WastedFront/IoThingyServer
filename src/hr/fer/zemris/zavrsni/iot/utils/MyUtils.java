package hr.fer.zemris.zavrsni.iot.utils;

/**
 * Class contains methods which are used by multiple other classes.
 * 
 * @author Nikola Presečki
 * @version 1.0
 *
 */
public class MyUtils {

	/** Representation of idle return. */
	public static final String RETURN_IDLE = "IDLE\r\n";

	public static String getReturnClientMessage(String srcID) {
		// provjeri da li postoji neka spremljena poruka za navedenu stvar
		// pa to onda pošalji ili idle
		return RETURN_IDLE;
	}
}
