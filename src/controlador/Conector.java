/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Query;
import java.util.ArrayList;
import modelo.Coche;

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
        } finally {
            cerrar();
        }
        
        return flag;
    }
    
    public boolean actualizarCoche (Coche c) {
        boolean flag = false;
        
        try {
            Query query = bd.query();
            query.constrain(Coche.class);
            query.descend("matricula").constrain(c.getMatricula());
            
            ObjectSet<Coche> resultado = query.execute();
            
            if (!resultado.isEmpty()) {
                Coche modificado = resultado.get(0);
                modificado.setMarca(c.getMarca());
                modificado.setModelo(c.getModelo());
                modificado.setAnio(c.getAnio());
                modificado.setPrecio(c.getPrecio());
                bd.store(modificado);
                bd.commit();
                flag = true;
            }
        } catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
            System.err.println("Error al guardar un dato en la base de datos: " + e.getMessage());
        } finally {
            cerrar();
        }
        
        return flag;
    }
    
    public boolean borrarCoche (Coche c){
        boolean flag = false;
        
        try {
            Query query = bd.query();
            query.constrain(Coche.class);
            query.descend("matricula").constrain(c.getMatricula());
            
            ObjectSet<Coche> resultado = query.execute();
            
            if (!resultado.isEmpty()) {
                Coche modificado = resultado.get(0);
                bd.delete(modificado);
                bd.commit();
                flag = true;
            }
        } catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
            System.err.println("Error al guardar un dato en la base de datos: " + e.getMessage());
        } finally {
            cerrar();
        }
        
        return flag;
    }
    
    public ArrayList<Coche> listarCoches () {
        ArrayList<Coche> coches = new ArrayList<>();
        
        ObjectSet resultado = bd.query(Coche.class);
        for (Object o : resultado) {
            coches.add((Coche) o);
        }
        
        cerrar();
        return coches;
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
