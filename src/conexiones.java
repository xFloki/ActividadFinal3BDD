
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alejandro
 */
public class conexiones {

    Connection conn1 = null;
    int check = 0;

    public void GestorConexion() {

        try {
            String url1 = "jdbc:oracle:thin:@localhost:1521:XE";
            String user = "SYSTEM";
            String password = "root";
            conn1 = (Connection) DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Conectado a Carcel�");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: dirección o usuario/clave no válida");
            ex.printStackTrace();
        }
    }

    public void cerrar_conexion() {
        try {
            conn1.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:al cerrar la conexión");
            ex.printStackTrace();
        }

    }
    
     public void insertarPreso(String _codigo_preso, String _dni, String _nombre, String _apellidos, String _fecha_nacimiento, 
             String _lugar_nacimiento, String _nacionalidad, String _estatura, String _color_piel, String _banda, String _fotoPreso,String telefonos) {
        try {
            // Crea un statement
            Statement sta = conn1.createStatement();
            // Ejecuta la inserción
//            sta.executeUpdate("INSERT INTO `preso` VALUES "
//                    + "('" + _codigo_preso + "' , '" + _dni + "', '"+ _nombre + "', '"+ _apellidos + "', '"+ _fecha_nacimiento 
//                    + "', '"+ _lugar_nacimiento + "', '"+ _nacionalidad + "', '"+ _estatura + "', '"+ _color_piel + "', '"+ _banda + "', '"+ _fotoPreso + "' )");
//            Cierra el statement
            sta.executeUpdate("Insert INTO preso VALUES  ('"+_codigo_preso+"', '"+_dni+"', '"+_nombre+"', '"+_apellidos+"', to_date('"+_fecha_nacimiento+"','DD-MM-YYYY'),"
         + " '"+_lugar_nacimiento+"', '"+_nacionalidad+"', "+_estatura+", '"+_color_piel+"', '"+_banda+"' , tabla_telefonos("+telefonos+") , '/imagenes/paccino.jpeg')");
            System.out.println("Insert INTO preso VALUES  ('"+_codigo_preso+"', '"+_dni+"', '"+_nombre+"', '"+_apellidos+"', to_date('"+_fecha_nacimiento+"','DD-MM-YYYY'),"
         + " '"+_lugar_nacimiento+"', '"+_nacionalidad+"', "+_estatura+", '"+_color_piel+"', '"+_banda+"' , tabla_telefonos("+telefonos+") , '/imagenes/paccino.jpeg')");
          
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:al hacer un Insert");
            ex.printStackTrace();
        }
    }

    public void insertarDelitoPreso(String _id, String codigoDelito, String circunstancias, String ubicacion) {
        try {
            // Crea un statement
            Statement sta = conn1.createStatement();
            // Ejecuta la inserción
            sta.executeUpdate("INSERT INTO preso_delito (`codigo_preso`, `codigo_delito`, `circustancias`, `ubicacion`)"
                    + "VALUES ('" + _id + "' , " + codigoDelito + ", '"+ circunstancias + "', '"+ ubicacion + "' );");
            // Cierra el statement
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:al hacer un Insert");
            ex.printStackTrace();
        }
    }
    
    public int insertarDelitoPresoNuevoDelito(String _id, String codigoDelito, String circunstancias, String ubicacion, String descripcion, String condena) {
         try {
            conn1.setAutoCommit(false);
            Statement sta = conn1.createStatement();
            sta.executeUpdate("INSERT INTO `delito` (`codigo_delito`, `descripcion`, `condena`)"
                    + "VALUES ( " + codigoDelito + ", '" + descripcion + "', " + condena + ")");
            sta.executeUpdate("INSERT INTO preso_delito (`codigo_preso`, `codigo_delito`, `circustancias`, `ubicacion`)"
                    + "VALUES ('" + _id + "' , " + codigoDelito + ", '"+ circunstancias + "', '"+ ubicacion + "' )");
            conn1.commit();
             conn1.setAutoCommit(true);
            return 0;
        } catch (SQLException ex) {           
            System.out.println("ERROR:al hacer un Insert");
            try {
                if (conn1 != null) {
                    conn1.rollback();
                }               
            } catch (SQLException se2) {
                se2.printStackTrace();
                
            }           
            ex.printStackTrace();           
            return 1;
        }
    }
    
    //Para modificar los datos de un preso quitando en el autocommit para que en el caso de que se produzca un error a medias 
    //se realice un rollback, o se modifican todos o ninguno
       public int modificarDatosPreso(String _codigo_preso, String _dni, String _nombre, String _apellidos, String _fecha_nacimiento, String _nacionalidad,
               String _estatura, String _color_piel, String _banda) {
         try {
             System.out.println(_fecha_nacimiento + "ATENTO");
            conn1.setAutoCommit(false);
            Statement sta = conn1.createStatement();
            sta.executeUpdate("UPDATE preso SET dni = '"+ _dni +"' WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            sta.executeUpdate("UPDATE preso SET nombre = '"+ _nombre +"' WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            sta.executeUpdate("UPDATE preso SET apellidos = '"+ _apellidos +"' WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            sta.executeUpdate("UPDATE preso SET fecha_nacimiento = '"+ _fecha_nacimiento +"' WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            sta.executeUpdate("UPDATE preso SET nacionalidad = '"+ _nacionalidad +"' WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            sta.executeUpdate("UPDATE preso SET estatura = "+ _estatura +" WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            sta.executeUpdate("UPDATE preso SET color_piel = '"+ _color_piel +"' WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            sta.executeUpdate("UPDATE preso SET banda = '"+ _banda +"' WHERE preso.codigo_preso = '"+ _codigo_preso +"'");
            
            conn1.commit();
             conn1.setAutoCommit(true);
            return 0;
        } catch (SQLException ex) {           
            System.out.println("ERROR:al hacer un Insert");
            try {
                if (conn1 != null) {
                    conn1.rollback();
                }               
            } catch (SQLException se2) {
                se2.printStackTrace();
                
            }           
            ex.printStackTrace();           
            return 1;
        }
    }
    
    public void modificarTelefono(String _preso, String _telMovil, String _telAbogado, String _telFamiliar,
            boolean _nuevoMovil , boolean _nuevoAbogado , boolean _nuevoFamiliar, boolean _actuaMovil , boolean _actuaAbogado , boolean _actuaFamiliar) {
        try {
            // Crea un statement
            Statement sta = conn1.createStatement();
            //Con las variables de actua comprobamos si el campo estaba vacio cuando se cargaron los telefonos y cuando se dio a modificar tambi�n
            // lo que significaria que no estaba creado el tipo de ese telefono para ese preso y que no queremos realizar ninguna accion
            if(_actuaMovil){
            //los booleons 'nuevo' nos indican si cuando se cargaron los telefonos ese tenia contendio lo que significaria que ya existe y que 
            //que lo vamos a cambiar de lo contrario no existia y tendremos que realizar unas insercion 
            if(!_nuevoMovil){
                sta.executeUpdate("update table(select telefono from preso where codigo_preso = '"+_preso+"' )set Num_telefono = '"+_telMovil+"' where tipo = 'Movil'");
            } else {
                 sta.executeUpdate("insert into table ( select telefono from preso where  codigo_preso = '"+_preso+"' )  values ( 'Movil' , '"+_telMovil+"' )");
            }
            }
            if(_actuaAbogado){
            if(!_nuevoAbogado){
                sta.executeUpdate("update table(select telefono from preso where codigo_preso = '"+_preso+"' )set Num_telefono = '"+_telAbogado+"' where tipo = 'Abogado'");
            } else {
                 sta.executeUpdate("insert into table ( select telefono from preso where  codigo_preso = '"+_preso+"' )  values ( 'Abogado' , '"+_telAbogado+"' )");
            }
            }
            if(_actuaFamiliar){
            if(!_nuevoFamiliar){
                sta.executeUpdate("update table(select telefono from preso where codigo_preso = '"+_preso+"' )set Num_telefono = '"+_telFamiliar+"' where tipo = 'Familiar'");
            } else {
                 sta.executeUpdate("insert into table ( select telefono from preso where  codigo_preso = '"+_preso+"' )  values ( 'Familiar' , '"+_telFamiliar+"' )");
            }
            }
            
            // Cierra el statement
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:al hacer un Insert");
            ex.printStackTrace();
        }
    }
    
    public String cargarTelefonoPreso(String preso, String tipoTelefono){
        try {

            String telefonoReturn = "";
          //Para ejecutar la consulta
            Statement s = conn1.createStatement();
            //Ejecutamos la consulta que escribimos en la caja de texto
            //y los datos lo almacenamos en un ResultSet
            ResultSet rs = s.executeQuery("select t.Num_telefono from preso p, table(p.telefono)"
                    + " t where p.codigo_preso = '"+preso+"' and t.tipo ='"+tipoTelefono+"'");
            //Obteniendo la informacion de las columnas que estan siendo consultadas
            ResultSetMetaData rsMd = rs.getMetaData();
            //La cantidad de columnas que tiene la consulta
            int cantidadColumnas = rsMd.getColumnCount();
         
            while (rs.next()) {
                    telefonoReturn = (rs.getString(1));               
            }
            rs.close();
            s.close();
            
            return telefonoReturn;
            } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR";
        }
    }
       
    public boolean update(String valores, String id)
    {
        boolean res = false;        
        String q = " UPDATE preso SET " + valores + " WHERE codigo_preso= " + id;
        try {
            PreparedStatement pstm = conn1.prepareStatement(q);
            pstm.execute();
            pstm.close();
            res=true;
         }catch(SQLException e){            
            System.out.println(e);
        }
        return res;
    }

 

    public String cargarDatosAlbum(int _id, String _dato) {
//            int ide = Integer.parseInt(_id);
        String yolo = "";
        int yolos;

        try {
            Statement sta = conn1.createStatement();
            String query = "SELECT * FROM album where id = " + _id;
            ResultSet rs = sta.executeQuery(query);

//           int anno = rs.getInt("publicacion");
//        if(_dato.equals("titulo"))
            yolos = rs.getInt("id");
//        else 
//            yolo = String.valueOf(rs.getInt(_dato));
//          
//          
            rs.close();
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:al consultar");

        }
        return yolo;
    }

    //le pasamos un int columnas eliminadas para en vez de tener que poner la consulta de todos los campos que queremos si queremos 
    //quitarle la ultima columna como en el caso de a la hora de mostrar los presos que no queremos mostrar la url de su foto
    //se lo indicamos y asi se elimina mas facilmente
    public void consulta_Statement(String pregunta, JTable tabla,int columnasEliminadas) {

        try {
            //Para establecer el modelo al JTable
            DefaultTableModel modelo = new DefaultTableModel();
            tabla.setModel(modelo);

            //Para ejecutar la consulta
            Statement s = conn1.createStatement();
            //Ejecutamos la consulta que escribimos en la caja de texto
            //y los datos lo almacenamos en un ResultSet
            ResultSet rs = s.executeQuery(pregunta);
            //Obteniendo la informacion de las columnas que estan siendo consultadas
            ResultSetMetaData rsMd = rs.getMetaData();
            //La cantidad de columnas que tiene la consulta
            int cantidadColumnas = rsMd.getColumnCount() - columnasEliminadas;
            //Establecer como cabezeras el nombre de las colimnas
            for (int i = 1; i <= cantidadColumnas; i++) {

                modelo.addColumn(rsMd.getColumnLabel(i));

            }
            //Creando las filas para el JTable

            while (rs.next()) {

                Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i] = rs.getObject(i+1);
                }

                modelo.addRow(fila);

            }

            rs.close();
            ;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Metodo que borra los datos de las dilas que se tienen seleccionadas en un jtable, si son varias filas las que se van a eliminar
    //se quita el autocommit para que en el caso de que ocurra un error durante la ejecucion del borrados de todas
    //las filas seleccionadas se vuelva al incicio con un rollback
    public void borrarDatos(JTable tabla, String tablaEliminar) {
        DefaultTableModel modeloTabla = (DefaultTableModel) tabla.getModel();
        int[] selectedRow = tabla.getSelectedRows();
        int del = 1;

        if (selectedRow.length >= 1) {

            try {
                Statement sta = conn1.createStatement();
                conn1.setAutoCommit(false);
                for (int j = 0; j < selectedRow.length; j++) {
                    sta.executeUpdate("DELETE from " + tablaEliminar + " where codigo_preso = '" + tabla.getModel().getValueAt(selectedRow[j], 0) + "'");
                    modeloTabla.removeRow(selectedRow[j]);
                    // if para evitar errores a la hora de eliminar las filas de la tabla por el cambio de posiciones
                    // al eliminar index anteriores 
                    if (j < selectedRow.length - 1) {
                        selectedRow[j + 1] = selectedRow[j + 1] - del;
                        del = del + 1;
                    }
                }
                conn1.commit();
                sta.close();
                conn1.setAutoCommit(true);
            } catch (Exception e) {
                 try{
                    if( conn1!=null)
                         conn1.rollback();
                    }catch(SQLException se2){
                         se2.printStackTrace();
                    }
            }

        } else {

            try {
                Statement sta = conn1.createStatement();
                sta.executeUpdate("DELETE from " + tablaEliminar + " where codigo_preso = '" + tabla.getModel().getValueAt(selectedRow[0], 0) + "';");
                modeloTabla.removeRow(selectedRow[0]);
                sta.close();
            } catch (Exception e) {

            }

        }
    }

}
