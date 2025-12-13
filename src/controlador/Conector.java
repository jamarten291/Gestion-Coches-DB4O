/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Query;
import java.util.ArrayList;
import modelo.Coche;
import modelo.Propietario;

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
        } catch (DatabaseFileLockedException | DatabaseReadOnlyException | Db4oIOException | IncompatibleFileFormatException | OldFormatException e) {
            System.err.println("Error al abrir la base de datos: " + e.getMessage());
        }
        
        return flag;
    }
    
    public boolean insertarObjeto (Object o) {
        boolean flag = false;
        
        try {
        if (conectar()) {
            if (o instanceof Coche c && c.deAlta()) {
                // Si está de alta y es un coche, se comprueba que tenga un DNI válido
                flag = existeDni(c.getDni());
            } else {
                flag = true;
            }

            if (flag) {
                bd.store(o);
            }
        }
        } catch (DatabaseClosedException | DatabaseReadOnlyException e) {
            bd.rollback();
            System.err.println("Error al guardar un dato en la base de datos: " + e.getMessage());
        } finally {
            cerrar();
        }
        
        return flag;
    }
    
    public <T> ArrayList<T> listarObjetos (Class<T> clazz) {
        ArrayList<T> objetos = new ArrayList<>();
        
        if (conectar()) {
            ObjectSet resultado = bd.query(clazz);
            for (Object o : resultado) {
                objetos.add((T) o);
            }

            cerrar();
        }
        
        return objetos;
    }
    
    public <T> boolean actualizarObjeto (T obj) {
        boolean flag = false;
        try {
        if (conectar()) {
            Query query = bd.query();
            query.constrain(obj.getClass());

            if (obj instanceof Coche c) {
                // Busca el coche en la lista y lo actualiza si existe
                query.descend("matricula").constrain(c.getMatricula());
                ObjectSet<Coche> resultado = query.execute();
                Coche modificado = null;

                if (!resultado.isEmpty()) {
                    modificado = resultado.get(0);
                    modificado.setMarca(c.getMarca());
                    modificado.setModelo(c.getModelo());
                    modificado.setColor(c.getColor());
                    modificado.setPrecio(c.getPrecio());
                    modificado.setAlta(c.deAlta());
                    modificado.setDni(c.getDni());
                }

                if (c.deAlta()) {
                    flag = existeDni(c.getDni());
                }

                // No introduce el campo hasta que compruebe que el DNI existe si está de alta
                // Si no está de alta no lo comprueba
                if (flag) {
                    bd.store(modificado);
                    bd.commit();
                    flag = true;
                }
            } else if (obj instanceof Propietario p) {
                // Busca el propietario en la lista y lo actualiza si existe
                query.descend("dni").constrain(p.getDni());

                ObjectSet<Propietario> resultado = query.execute();

                if (!resultado.isEmpty()) {
                    Propietario modificado = resultado.get(0);
                    modificado.setDni(p.getDni());
                    modificado.setNombre(p.getNombre());
                    modificado.setEdad(p.getEdad());
                    modificado.setEmail(p.getEmail());
                    bd.store(modificado);
                    bd.commit();
                    flag = true;
                }
            }
        }
        } catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
            bd.rollback();
            System.err.println("Error al guardar un dato en la base de datos: " + e.getMessage());
        } finally {
            cerrar();
        }
        
        return flag;
    }
    
    private boolean existeDni(String dni) {
        Query query = bd.query();
        query.constrain(Propietario.class);
        query.descend("dni").constrain(dni);

        return query.execute() != null;
    }
    
    public <T> boolean borrarObjeto (T obj){
        boolean flag = false;
        
        try {
        if (conectar()) {
            Query query = bd.query();
            query.constrain(obj.getClass());

            // Dependiendo de si se trate de un coche o un propietario, lo busca
            // Si existe, lo elimina
            if (obj instanceof Coche c) {
                query.descend("matricula").constrain(c.getMatricula());
            } else if (obj instanceof Propietario p) {
                query.descend("dni").constrain(p.getDni());
            }
            ObjectSet<T> resultado = query.execute();

            if (!resultado.isEmpty()) {
                T modificado = resultado.get(0);
                bd.delete(modificado);
                bd.commit();
                flag = true;
            }
        }
        } catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
            bd.rollback();
            System.err.println("Error al guardar un dato en la base de datos: " + e.getMessage());
        } finally {
            cerrar();
        }
        
        return flag;
    }
    
    /* ---------- CONSULTAS ---------- */
    
    public ArrayList getPropietariosMayoresDeCuarenta () {
        ArrayList<Propietario> propietarios = new ArrayList<>();
        
        if (conectar()) {
            Query query = bd.query();
            query.constrain(Propietario.class);
            query.descend("edad").constrain(40).greater();
            
            ObjectSet<Propietario> resultado = query.execute();
            for (Propietario p : resultado) {
                propietarios.add(p);
            }
            
            cerrar();
        }
        
        return propietarios;
    }
    
    public ArrayList getCochesEntreDosPrecios (int precioMin, int precioMax) {
        ArrayList<Coche> coches = new ArrayList<>();
        
        if (conectar()) {
            Query query = bd.query();
            query.constrain(Coche.class);
            query.descend("precio").constrain(precioMin).greater();
            query.descend("precio").constrain(precioMax).smaller();
            
            ObjectSet<Coche> resultado = query.execute();
            for (Coche c : resultado) {
                coches.add(c);
            }
            
            cerrar();
        }
        
        return coches;
    }
    
    public ArrayList getCochesByPropietario (String propietario) {
        ArrayList<Coche> coches = new ArrayList<>();
        
        if (conectar()) {
            Query query = bd.query();
            query.constrain(Coche.class);
            query.descend("dni").constrain(propietario);
            
            ObjectSet<Coche> resultado = query.execute();
            for (Coche c : resultado) {
                coches.add(c);
            }
            
            cerrar();
        }
        
        return coches;
    }
    
    public boolean modificarCochesPorPorcentaje (int valor) {
        boolean flag = false;
        
        if (conectar()) {
            double porcentaje = (double) valor / 100;
            
            Query query = bd.query();
            
            query.constrain(Coche.class);
            ObjectSet<Coche> rs = query.execute();
            while (rs.hasNext()) {
                Coche c = rs.next();
                double nuevoPrecio = c.getPrecio() * porcentaje;
                
                // Se asegura de que tenga un precio mínimo de 0
                c.setPrecio(Math.max(0, c.getPrecio() + ((int) nuevoPrecio)));
                bd.store(c);
                bd.commit();
            }
            
            bd.commit();
            flag = true;
            
            cerrar();
        }
        
        return flag;
    }
    
    public boolean borrarCochePorMatricula (String matricula){
        boolean flag = false;
        
        try {
        if (conectar()) {
            Query query = bd.query();
            query.constrain(Coche.class);

            query.descend("matricula").constrain(matricula);
            
            ObjectSet<Coche> resultado = query.execute();

            if (!resultado.isEmpty()) {
                Coche modificado = resultado.get(0);
                bd.delete(modificado);
                bd.commit();
                flag = true;
            }
        }
        } catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
            bd.rollback();
            System.err.println("Error al guardar un dato en la base de datos: " + e.getMessage());
        } finally {
            cerrar();
        }
        
        return flag;
    }
    
    public boolean cerrar () {
        try {
            return bd.close();
        } catch (Db4oIOException e) {
            System.err.println("Error al cerrar la base de datos: " + e.getMessage());
            return false;
        }
    }
    
}
