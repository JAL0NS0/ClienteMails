import db.*;
import java.util.*;
public class MiBaseDeDatos{
  int contador;
  DB miBaseDatos;
  String query;
  public MiBaseDeDatos(String direccion){
    conectar();
  }
  public void conectar(){
    try{
 		miBaseDatos = new DB("tablaPrueba.db");//create one DB connection object
 		if(!miBaseDatos.connect()){//create actual connection to db
 			System.out.println("Error en db"+miBaseDatos.getError());
 			System.exit(0);
 		}
    System.out.println("Conectado");

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
    conectar();
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
  public void guardarNuevoMensaje(String receptor,String emisor,String asunto,String mensaje){
    query="INSERT INTO mensajes (destinatario,remitente,asunto,cuerpo,leido) VALUES ('%s','%s','%s','%s',0);";
    query= String.format(query,receptor,emisor,asunto,mensaje);
    try{
      if(miBaseDatos.executeNonQuery(query)){
      System.out.println("Mensaje guardado exitosamente");
      }else{
        System.out.println("No se pudo guardar el mensaje");
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
  public String[] cargarListaNuevos(String usuario,String leido){
    Integer numero=contarMensajes(usuario,0);
    String[] nMails= new String[numero];
    query="select id,remitente,asunto,cuerpo from mensajes where leido=%s and destinatario='%s';";
    query= String.format(query,leido,usuario);
    try{
      if(numero==0){
        return nMails;
      }else{
        if(miBaseDatos.executeQuery(query,"respuesta")){
          contador=0;
          while(miBaseDatos.next("respuesta")){
            String aux="";
            aux= aux + miBaseDatos.getString("id","respuesta")+". ";
            aux= aux + miBaseDatos.getString("remitente","respuesta")+"   ";
            aux= aux + miBaseDatos.getString("asunto","respuesta");
            nMails[contador]=aux;
            contador+=1;
          }
        }else{
          System.out.println("No se pudo hacer la consulta");
        }
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return nMails;
  }
  public void marcarLeido(String usuario,String id){
    query="UPDATE mensajes SET leido=1 WHERE destinatario='%s' and id=%s;";
    query=String.format(query,usuario,id);
    try{
      if(miBaseDatos.executeNonQuery(query)){
        System.out.println("Mensaje leido");
      }else{
        System.out.println("Error al marcar el mensaje");
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
  public Integer contarMensajes(String usuario,int leido){
    query="SELECT COUNT(*) as contador FROM mensajes WHERE leido=%d and destinatario='%s';";
    query=String.format(query,leido,usuario);
    try{
      if(miBaseDatos.executeQuery(query,"respuesta")){
        miBaseDatos.next("respuesta");
        String aparece = miBaseDatos.getString("contador","respuesta");
        return Integer.parseInt(aparece);
      }else{
        System.out.println("No se pudo hacer la consulta");
        return 0;
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
      return 0;
    }
  }
  public String[] getMensaje(String id){
    String[] mensajeAImprimir= new String[4];
    query="select * from mensajes where id='%s';";
    query=String.format(query,id);
    System.out.println(query);
    try{
      if(miBaseDatos.executeQuery(query,"respuesta")){
        miBaseDatos.next("respuesta");
        mensajeAImprimir[0]=miBaseDatos.getString("id","respuesta");
        mensajeAImprimir[1]=miBaseDatos.getString("remitente","respuesta");
        mensajeAImprimir[2]=miBaseDatos.getString("asunto","respuesta");
        mensajeAImprimir[3]=miBaseDatos.getString("cuerpo","respuesta");
        return mensajeAImprimir;
      }else{
        System.out.println("No se pudo hacer la consulta");
        return mensajeAImprimir;
      }
    }catch(Exception e){
      System.out.println(e.getClass());
      System.out.println(e.getMessage());
      e.printStackTrace();
      return mensajeAImprimir;
    }
  }
}
