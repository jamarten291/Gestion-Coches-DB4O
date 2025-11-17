
package controlador;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jamarten291
 * @version 17 nov 2025
 */
public class Checker {

    public static boolean formatoMatricula(String texto) {
        Pattern pattern = Pattern.compile("[0-9]{4} [BCDFGHJKLMNPQRSTVWXYZ]{3}");
        Matcher matcher = pattern.matcher(texto);
        return matcher.matches();
    }

    public static boolean tryParseToInt(String num) {
        try {
            Integer.valueOf(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
