/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

/**
 *
 * @author Usuario
 */
public class Conector {
    private final String RUTA = "sucursal.dat";
    private ObjectContainer bd;

    public Conector() {
    }
    
    public boolean conectar() {
        boolean flag = false;
        
        try {
            bd = Db4o.openFile(RUTA);
            flag = true;
        } catch (Exception e) {
            System.err.println("Error al abrir la base de datos: " + e.getMessage());
        }
        
        return flag;
    }
    
    public boolean insertarCoche (Object o) {
        boolean flag = false;
        
        try {
            bd.store(o);
        } catch (Exception e) {
            System.err.println("Error al guardar un dato en la base de datos: " + e.getMessage());
        }
        
        return flag;
    }
    
    public boolean cerrar () {
        try {
            return bd.close();
        } catch (Exception e) {
            System.err.println("Error al cerrar la base de datos: " + e.getMessage());
            return false;
        }
        
    }
    
}
