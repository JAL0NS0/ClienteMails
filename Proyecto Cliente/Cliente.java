import java.util.*;
import java.net.*;
import java.io.*;

public class Cliente{
  static final int PUERTO=1200;
  String ipservidor;
  String respuesta;
  MiSocket socket;
  String[] dividido;
  String usuario;
  String contrasena;
  String nombreServidor;
  MiBaseDeDatos miBaseDatos;

  public Cliente(MiBaseDeDatos miBD ){
    miBaseDatos=miBD;
  }
  public void crearTablaIpsServidores(String nombre,String ip){
    miBaseDatos.agregarServidor(nombre,ip);
  }
  //Verifica el nombre del servidor e inicia la conexion con el socket
  public byte Verificar(String nom, String contra){
    System.out.println("Este es el usuario@server: "+nom);
    System.out.println("Este es la contrasena: "+contra);
    contrasena= contra;
    dividido=nom.split("@");
    System.out.println("Este es el usuario: "+dividido[0]);
    usuario=dividido[0];
    nombreServidor=dividido[1];
    if(miBaseDatos.buscarServidor(nombreServidor)){
      this.ipservidor= miBaseDatos.obtenerIpServidor(nombreServidor);
      System.out.println("servidor: "+ipservidor);
        try{
          socket= new MiSocket(ipservidor,PUERTO);
        }catch(Exception e){
          System.out.println("Error al crear el socket1");
        }
        try{
          socket.getOut().println(Comandos.LOGIN + usuario+" "+contrasena);
        }catch(Exception e){
          System.out.println("Error al hablar");
        }
        try{
          respuesta= socket.getIn().readLine();
          System.out.println(respuesta);
        }catch(Exception e){
          System.out.println("Error al oir");
        }
        if(respuesta.equals(Comandos.OK_LOGIN)){
          return 0;
        }else if(respuesta.substring(0,Comandos.LOGIN_ERROR.length()).equals(Comandos.LOGIN_ERROR)){
          if(respuesta.substring(Comandos.LOGIN_ERROR.length(),respuesta.length()).equals("101")) {
            return 2;
          }else if(respuesta.substring(Comandos.LOGIN_ERROR.length(),respuesta.length()).equals("102")) {
            return 3;
          }
        }
    }
      return 1;
  }
  //Hace un log out con el servidor
  public boolean cerrarSecion(){
    try{
      socket.out.println(Comandos.LOGOUT);
      System.out.println("Cliente: "+Comandos.LOGOUT);
      String respuesta=socket.getIn().readLine();
      System.out.println("Servidor: "+respuesta);
      if(respuesta.equals(Comandos.OK_LOGOUT)){
        miBaseDatos.cerrarConexion();
        socket.closeIn();
        socket.closeOut();
        socket.closeMiSocket();
        return true;
      }else{
        System.out.println("No se ha podido hacer LOG OUT");
        return false;
      }
    }catch(Exception e){
      System.out.println("Exception Log out");
      return false;
    }
  }
  //Obtiene los nuevos mails y los devuelve en una lista como remitente,asunto,texto;
  public void getNuevosMails(){
    try{
    socket.getOut().println(Comandos.GETNEWMAILS + usuario);
    System.out.println("Cliente: GETNEWMAILS "+usuario);
    String mensaje="";
    String todo;
    String comando;
    String remitente;
    String asunto;
    String body;
    while(true){
        String[] aux= new String[3];
        todo=socket.getIn().readLine();
        System.out.println("Servidor: "+todo);
        if(todo==null){
          break;
        }
        if(todo.equals(Comandos.NO_MAILS)){
        }
        comando=todo.substring(0,Comandos.OK_GETNEWMAILS.length());
        if(comando.equals(Comandos.OK_GETNEWMAILS)){
          mensaje= todo.substring(Comandos.OK_GETNEWMAILS.length(),todo.length());
          int finalRemitente = mensaje.indexOf(" ");
          int finalAsunto = mensaje.indexOf(" ",finalRemitente+1);
          aux[0]=mensaje.substring(0,finalRemitente);
          aux[1]=mensaje.substring(finalRemitente+1,finalAsunto);
          aux[2]=mensaje.substring(finalAsunto+1,mensaje.length());
          if(mensaje.charAt(mensaje.length()-1)==('*')){
            miBaseDatos.guardarNuevoMensaje(usuario,aux[0],aux[1],aux[2]);
            break;
          }else{
            miBaseDatos.guardarNuevoMensaje(usuario,aux[0],aux[1],aux[2]);
          }
        }
      }
    }catch(Exception e){
      e.printStackTrace();
      System.out.println("Error al leer el comando Mails");
    }
  }

  public ArrayList<String> getContactos(){
    ArrayList<String> listaContactos=new ArrayList<String>();
    try{
    socket.getOut().println(Comandos.CLIST + usuario);
    System.out.println("Cliente: CLIST "+usuario);
    String aux;
    String comando;
    while(true){
        aux=socket.in.readLine();
        comando=aux.substring(0,Comandos.OK_CLIST.length());
        System.out.println("Servidor: "+aux);
        if(comando.equals(Comandos.OK_CLIST)){
          aux= aux.substring(Comandos.OK_CLIST.length(),aux.length());
          if(aux.charAt(aux.length()-1)==('*')){
            aux=aux.substring(0,aux.length()-1);
            if(!miBaseDatos.existeContacto(usuario,aux)){
              miBaseDatos.agregarContacto(usuario,aux);
            }
            listaContactos.add(aux);
            break;
          }else{
            if(!miBaseDatos.existeContacto(usuario,aux)){
              miBaseDatos.agregarContacto(usuario,aux);
            }
            listaContactos.add(aux);
          }
        }
      }
    }catch(Exception e){
      System.out.println("Error al leer el comando");
    }
    return listaContactos;
  }
  public boolean enviarMensaje(String destinatarios,String asunto,String cuerpo){
    try{
        System.out.println("CLIENTE: SEND MAIL");
        socket.getOut().println("SEND MAIL");
        String[] destino=destinatarios.split(" ");
        for(String x:destino){
          if(!x.equals(destino[destino.length-1])){
            System.out.println("Cliente: MAIL TO "+x);
            socket.getOut().println("MAIL TO "+x);
          }else{
            System.out.println("Cliente: MAIL TO "+x+"*");
            socket.getOut().println("MAIL TO "+x+"*");
          }
        }
        System.out.println("Cliente: MAIL SUBJECT "+ asunto);
        socket.getOut().println("MAIL SUBJECT "+ asunto);
        System.out.println("Cliente: MAIL BODY "+ cuerpo);
        socket.getOut().println("MAIL BODY "+ cuerpo);
        System.out.println("Cliente: END SEND MAIL");
        socket.getOut().println("END SEND MAIL");
        String respuesta=socket.getIn().readLine();
        System.out.println("Servidor: "+respuesta);
        if(respuesta.equals(Comandos.OK_SEND)){
          System.out.println("Correo enviado exitosamente");
          return true;
        }else{
          System.out.println("Erro al envia correo");
          return false;
        }
    }catch(Exception e){
      e.printStackTrace();
      System.out.println("Error al enviar un mensaje");
      return false;
    }
  }
  public boolean guardarContacto(String nombreNuevo,String servidorNuevo){
    try{
      String nuevo=nombreNuevo+"@"+servidorNuevo;
      socket.getOut().println("NEWCONT "+nuevo);
      String respuesta=socket.getIn().readLine();
      System.out.println("Cliente: "+respuesta);
      String contacto= respuesta.substring(12,respuesta.length());
      String aux=respuesta.substring(0,11);
      System.out.println(aux+"+");
      if(aux.equals(Comandos.OK_NEWCONT)){
        System.out.println("Contacto "+contacto + " Guardado exitosamente");
        return true;
      }else{
        System.out.println("Error al guardar contacto");
        return false;
      }
    }catch(Exception e){
      System.out.println("Fallo enviando el contacto al servidor");
      e.printStackTrace();
      return false;
    }
  }
  public ArrayList<String[]> cargarListaNuevos(){
    return miBaseDatos.cargarListaNuevos(usuario);
  }
}
