import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cliente implements ActionListener{
  public static final int PORT = 1200;
  public static HashMap<String, String> servidores;
  protected static Socket socketServer;
  static BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
  static Ventana app;
  static VentanaLogin log;
  public static void loadGUIComponents(){
    System.out.println("Cargando ventana login...");
    log = new VentanaLogin();
    log.setVisible(true);

    System.out.println("Cargando ventana app...");
    app = new Ventana();
    app.setVisible(true);
  }
    public static void NewMails(BufferedReader in,PrintWriter out,String usuario)throws Exception{
      out.println("GETNEWMAILS " + usuario);
      System.out.println("Cliente: GETNEWMAILS " + usuario);
      String texto;
      while(true){
          texto = in.readLine();
          System.out.println("Servidor: " + texto);
          if(texto.charAt(texto.length()-1) == '*'){
              break;
          }
      }
    }
    public static boolean LogOut(BufferedReader in,PrintWriter out,String usuario)throws Exception{
      out.println("LOGOUT");
      System.out.println("Cliente: LOGOUT");
      String respuesta=in.readLine();
      System.out.println("Servidor: "+respuesta);
      if(respuesta.equals("OK LOGOUT")){
        return true;
      }else{
        System.out.println("No se ha podido hacer LOG OUT");
        return false;
      }
    }
  public static void SendMail(BufferedReader in,PrintWriter out,String usuario)throws IOException{
    System.out.print("\t Ingrese usuario@server: ");
    String destinatarios = teclado.readLine();
    String[] x=destinatarios.split(" ");
    System.out.print("\t Ingrese subject: ");
    String subject = teclado.readLine();
    System.out.print("\t Ingrese mensaje: ");
    String mensaje = teclado.readLine();
    System.out.println("SEND MAIL");
    out.println("SEND MAIL");
    for(String destino:x){
      if(!destino.equals(x[x.length-1])){
        System.out.println("Cliente: MAIL TO "+destino);
        out.println("MAIL TO "+destino);
      }else{
        System.out.println("Cliente: MAIL TO "+destino+"*");
        out.println("MAIL TO "+destino+"*");
      }
    }
    System.out.println("Cliente: MAIL SUBJECT "+ subject);
    out.println("MAIL SUBJECT "+ subject);
    System.out.println("Cliente: MAIL BODY "+ mensaje);
    out.println("MAIL BODY "+ mensaje);
    System.out.println("Cliente: END SEND MAIL");
    out.println("END SEND MAIL");
    String respuesta=in.readLine();
    System.out.println("Servidor: "+respuesta);
    if(respuesta.equals("OK SEND MAIL")){
      System.out.println("Correo enviado exitosamente");
    }else{
      System.out.println("Erro al envia correo");
    }
  }
  public static void ContacList(BufferedReader in,PrintWriter out,String usuario)throws Exception{
    out.println("CLIST " + usuario);
    System.out.println("Cliente: CLIST " + usuario);
    String texto;
    while(true){
        texto = in.readLine();
        System.out.println("Servidor: " + texto);
        if(texto.charAt(texto.length()-1) == '*'){
            break;
        }
    }
  }
  public static void NuevoContacto(BufferedReader in,PrintWriter out,String usuario)throws Exception{
    System.out.print("Ingrese usuario@server: ");
    String nuevo = teclado.readLine();
    out.println("NEWCONT "+nuevo);
    String respuesta=in.readLine();
    System.out.println("Cliente: "+respuesta);
    if(respuesta.substring(0,10).equals("OK NEWCONT")){
      System.out.println("Contacto Guardado exitosamente");
    }else{
      System.out.println("Error al guardar contacto");
    }

  }
  public static void main(String[] args)throws IOException{
    HashMap<String, String> servidores = new HashMap<String, String>();
    servidores.put("localhost", "127.0.0.1");
    servidores.put("127.0.0.1", "127.0.0.1");
    servidores.put("serv3", "127.0.0.2");
    servidores.put("serv4", "127.0.0.3");
    String[] user_server;
    String user;
    String server;
    String password;
    InputStreamReader isr;
    BufferedReader in;
    PrintWriter out;
    String comando;
    loadGUIComponents();
    try{
    while(true){
        System.out.print("Ingrese usuario@server: ");
        user = teclado.readLine();
        user_server=user.split("@");
        user=user_server[0];
        server=user_server[1];
        System.out.print("Ingrese password: ");
        password = teclado.readLine();
        if(servidores.containsKey(server)||servidores.containsValue(server)){
          socketServer=new Socket(server,PORT);
          isr = new InputStreamReader(socketServer.getInputStream());
          in = new BufferedReader(isr);
          // es importante el segundo argumento (true) para que tenga autoflush al hacer print
          out = new PrintWriter(socketServer.getOutputStream(), true);
          out.println("LOGIN "+user+" "+password);
          comando=in.readLine();
          System.out.println("Servidor: "+comando);
          if(comando.equals("OK LOGIN")){
            ContacList(in,out,user);
            NewMails(in,out,user);
            break;
          }else{
            in.close();
            out.close();
            // socketServer.close();
          }
        }
      }
    while(true){
      System.out.print("Ingrese un comando: ");
      comando = teclado.readLine();
      if(comando.equals("CLIST")){
         ContacList(in,out,user);
      }else if(comando.equals("GETNEWMAILS")){
         NewMails(in,out,user);
    }else if(comando.equals("SEND MAIL")){
       SendMail(in,out,user);
  }else if(comando.equals("NEWCONT")){
     NuevoContacto(in,out,user);
  }else if(comando.equals("LOGOUT")){
      if(LogOut(in,out,user)){
        System.out.println("Saliendo...");
        break;
      }
  }else{
        out.println(comando);
      }
    }
    System.out.println("Llegando a cerrar");
    in.close();
    out.close();
    socketServer.close();
    System.out.println("Cerrado");
    }catch(Exception e){
    }
    System.out.println("Ultima parte");
  }

  @Override
  public void actionPerformed​(ActionEvent e){
    if(e.getSource()==app.butmails){
      System.out.println("Ingreso a los mails");
    }
  }
}
