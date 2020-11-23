import java.util.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.event.ListSelectionEvent;

public class Cliente{
  static final int PUERTO=1400;
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
    contrasena= contra;
    dividido=nom.split("@");
    usuario=dividido[0];
    nombreServidor=dividido[1];
    if(miBaseDatos.buscarServidor(nombreServidor)){
      this.ipservidor= miBaseDatos.obtenerIpServidor(nombreServidor);
        try{
          socket= new MiSocket(ipservidor,PUERTO);
        }catch(Exception e){
          System.out.println("Error al crear el socket1");
        }
        try{
          System.out.println("Cliente: "+"LOGIN "+ usuario+" "+contrasena);
          socket.getOut().writeUTF(Comandos.LOGIN + usuario+" "+contrasena);
        }catch(Exception e){
          System.out.println("Error al hablar");
        }
        try{
          respuesta= socket.getIn().readUTF();
          System.out.println("Servidor: "+respuesta);
        }catch(Exception e){
          System.out.println("Error al oir");
        }
        if(respuesta.equals("OK LOGIN")){
          return 0;
        }else if(respuesta.substring(0,12).equals(Comandos.LOGIN_ERROR)){
          if(respuesta.substring(12,respuesta.length()).equals("101")) {
            miBaseDatos.cerrarConexion();
            return 2;
          }else if(respuesta.substring(12,respuesta.length()).equals("102")) {
            miBaseDatos.cerrarConexion();
            return 3;
          }
        }
    }
      miBaseDatos.cerrarConexion();
      return 1;
  }
  //Hace un log out con el servidor
  public boolean cerrarSecion(){
    try{
      socket.getOut().writeUTF(Comandos.LOGOUT);
      System.out.println("Cliente: "+Comandos.LOGOUT);
      String respuesta=socket.getIn().readUTF();
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
    socket.getOut().writeUTF(Comandos.GETNEWMAILS + usuario);
    System.out.println("Cliente: GETNEWMAILS "+usuario);
    String mensaje="";
    String todo;
    String comando;
    String remitente;
    String asunto;
    String body;
    String submensaje;
    while(true){
        String[] aux= new String[3];
        todo=socket.getIn().readUTF();
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
          aux[0]=mensaje.substring(0,finalRemitente);
          submensaje=mensaje.substring(finalRemitente+2,mensaje.length());
          int finalAsunto = submensaje.indexOf("\"");
          aux[1]=submensaje.substring(0,finalAsunto);
          aux[2]=submensaje.substring(finalAsunto+2,submensaje.length());
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

  public String[] getContactos(){
    return miBaseDatos.getContactos(usuario);
    }

    public void pedirContactos(){
    try{
    socket.getOut().writeUTF(Comandos.CLIST + usuario);
    System.out.println("Cliente: CLIST "+usuario);
    String aux;
    String comando;
    while(true){
        aux=socket.getIn().readUTF();
        comando=aux.substring(0,Comandos.OK_CLIST.length());
        System.out.println("Servidor: "+aux);
        if(comando.equals(Comandos.OK_CLIST)){
          aux= aux.substring(Comandos.OK_CLIST.length(),aux.length());
          if(aux.charAt(aux.length()-1)==('*')){
            aux=aux.substring(0,aux.length()-1);
            if(!miBaseDatos.existeContacto(usuario,aux)){
              miBaseDatos.agregarContacto(usuario,aux);
            }
            break;
          }else{
            if(!miBaseDatos.existeContacto(usuario,aux)){
              miBaseDatos.agregarContacto(usuario,aux);
            }
          }
        }
      }
    }catch(Exception e){
      System.out.println("Error al leer el comando");
    }
  }

  public boolean enviarMensaje(String destinatarios,String asunto,String cuerpo){
    try{
        System.out.println("CLIENTE: SEND MAIL");
        socket.getOut().writeUTF("SEND MAIL");
        String[] destino=destinatarios.split(" ");
        for(String x:destino){
          if(!x.equals(destino[destino.length-1])){
            System.out.println("Cliente: MAIL TO "+x);
            socket.getOut().writeUTF("MAIL TO "+x);
          }else{
            System.out.println("Cliente: MAIL TO "+x+" *");
            socket.getOut().writeUTF("MAIL TO "+x+ " *");
          }
        }
        System.out.println("Cliente: MAIL SUBJECT "+ asunto);
        socket.getOut().writeUTF("MAIL SUBJECT "+ asunto);
        Thread.sleep(20);
        System.out.println("Cliente: MAIL BODY "+ cuerpo);
        socket.getOut().writeUTF("MAIL BODY "+ cuerpo);
        Thread.sleep(20);
        System.out.println("Cliente: END SEND MAIL");
        socket.getOut().writeUTF("END SEND MAIL");
        String respuesta=socket.getIn().readUTF();
        System.out.println("Servidor: "+respuesta);
        if(respuesta.equals(Comandos.OK_SEND)){
          System.out.println("Correo enviado exitosamente");
          return true;
        }else{
          System.out.println("Erro al envia correo");
          return false;
        }
    }catch(Exception e){
      System.out.println("Error al enviar un mensaje");
      return false;
    }
  }
  public boolean guardarContacto(String nombreNuevo,String servidorNuevo){
    try{
      String nuevo=nombreNuevo+"@"+servidorNuevo;
      socket.getOut().writeUTF("NEWCONT "+nuevo);
      System.out.println("Cliente: NEWCONT "+nuevo);
      String respuesta=socket.getIn().readUTF();
      System.out.println("Servidor: "+respuesta);
      String contacto= respuesta.substring(11,respuesta.length());
      String aux=respuesta.substring(0,11);
      if(aux.equals(Comandos.OK_NEWCONT)){
        System.out.println("Contacto "+contacto + " Guardado exitosamente");
        miBaseDatos.agregarContacto(usuario,nuevo);
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
  public String[] cargarListaNuevos(){
    return miBaseDatos.cargarListaNuevos(usuario,"0");
  }
  public String[] cargarListaLeidos(){
    return miBaseDatos.cargarListaNuevos(usuario,"1");
  }
  public void marcarLeido(String id){
    miBaseDatos.marcarLeido(usuario,id);
  }
  public String[] getMensajeCompleto(String id){
    return miBaseDatos.getMensaje(id);
  }
}
