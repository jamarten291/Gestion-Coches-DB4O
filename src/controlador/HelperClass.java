
package controlador;

import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * @author jamarten291
 * @version 22 nov 2025
 */
public class HelperClass {

    public static void lanzarAlerta(String titulo, String mensaje, int tipo, Component c) {
        JOptionPane.showMessageDialog(c, mensaje, titulo, tipo);
    }
    
    public static boolean tryParseToInt(String num) {
        try {
            Integer.valueOf(num);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public static boolean tryParseToDouble(String num) {
        try {
            Double.valueOf(num);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public static String dateToString(LocalDate date) {
        String fecha;
        // El campo en el que se introduce la cadena debe ser tipo varchar(10)
        DateTimeFormatter formatPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        fecha = date.format(formatPattern);
        
        return fecha;
    }
    
    public static boolean matchesRegex(String input, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        
        return m.matches();
    }
    
}
