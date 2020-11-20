import db.*;
public class MiBaseDeDatos{
  DB miBaseDatos;
  String query;
  public MiBaseDeDatos(String direccion){
    try{
 		miBaseDatos = new DB("tablaPrueba.db");//create one DB connection object
 		if(!miBaseDatos.connect()){//create actual connection to db
 			System.out.println("Error en db"+miBaseDatos.getError());
 			System.exit(0);
 		}

   	}catch(Exception e){
   						System.out.println(e.getClass());
   						System.out.println(e.getMessage());
   	}
  }
  //Agrega el nombre e ip de un servidor a la Base de datos tabla ipServidor;
  public boolean agregarServidor(String nombre,String ip){
    query="INSERT INTO ipServidor ('servidor','ips') VALUES ('%s','%s');";
    System.out.print("Agregando servidor "+nombre+" "+ip+": ");
    query= String.format(query,nombre,ip);
    try{
      if(miBaseDatos.executeNonQuery(query)){
      System.out.println("Servidor agregado exitosamente");
      return true;
      }else{
        System.out.println("Error al agregar servidor");
        return false;
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return false;
  }
  //Devuelve true si el servidor se encuentra en la tabla ipServidor;
  public boolean buscarServidor(String nomServidor){
    query="SELECT COUNT(*) as contador FROM ipServidor WHERE servidor='%s';";
    query= String.format(query,nomServidor);
    try{
      if(miBaseDatos.executeQuery(query,"respuesta")){
        miBaseDatos.next("respuesta");
        String aparece = miBaseDatos.getString("contador","respuesta");
        System.out.println(aparece);
        if(aparece.equals("1")){
          System.out.println("funciona");
          return true;
        }else{
          return false;
        }
      }else{
        System.out.println("No se pudo hacer la consulta");
        return false;
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
      return false;
    }
  }
  //Devuelve el ip del servidor con el nombre nombreServidor, si no existe devuelve null;
  public String obtenerIpServidor(String nombreServidor){
    query="SELECT ips FROM ipServidor Where servidor='%s';";
    query= String.format(query,nombreServidor);
    try{
      if(miBaseDatos.executeQuery(query,"respuesta")){
        miBaseDatos.next("respuesta");
        String ip = miBaseDatos.getString("ips","respuesta");
        System.out.println(ip);
        return ip;
      }else{
        System.out.println("No se pudo hacer la consulta");
        return null;
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }
  //Devuelve true si el contacto se encuentra en la tabla contactos;
  public boolean existeContacto(String usuario,String contacto){
    query="select count(*) as cantidad from contactos where nombre='%s' and contacto='%s';";
    query= String.format(query,usuario,contacto);
    try{
      if(miBaseDatos.executeQuery(query,"respuesta")){
        miBaseDatos.next("respuesta");
        String aparece = miBaseDatos.getString("cantidad","respuesta");
        System.out.println(aparece);
        if(aparece.equals("1")){
          System.out.println("existe");
          return true;
        }else{
          System.out.println("no existe");
          return false;
        }
      }else{
        System.out.println("No se pudo hacer la consulta");
        return false;
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
      return false;
    }
  }
  public void agregarContacto(String usuario,String contactoN){

    query="INSERT INTO contactos (nombre,contacto) VALUES ('%s','%s');";
    query= String.format(query,usuario,contactoN);
    System.out.println(query);
    try{
      if(miBaseDatos.executeNonQuery(query)){
      System.out.println("Contacto agregado exitosamente");
      }else{
        System.out.println("No se pudo agregar contacto");
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
  public void cerrarConexion(){
    try{
      System.out.println("Cerrando conexion");
      miBaseDatos.close(); //close connection
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
    }
  }

}
