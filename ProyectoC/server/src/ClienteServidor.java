import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import db.*;

public class ClienteServidor implements  Runnable{
  private String user = "";

  private String validarLogin(String mensaje, DB myDb) throws Exception{
    HashMap<String,String> basedatos = new HashMap<>();
    Scanner in = new Scanner(mensaje);
    in.useDelimiter(" ");
    if (new StringTokenizer(mensaje, " ").countTokens()!=3) {
      return null;
    }else{
      in.next();
      String username = in.next();
      String password = in.next();
      myDb.executeQuery("SELECT * FROM users","rs1");
      while(myDb.next("rs1"))
        basedatos.put(myDb.getString("nombre","rs1"),myDb.getString("password","rs1"));
      if(basedatos.containsKey(username) && basedatos.get(username).equals(password)){
        return username;
      }else{
        return null;
      }
    }
  }

  private LinkedList<String> getContactList(String mensaje, DB myDb) throws Exception{
    LinkedList<String> contactos = new LinkedList<>();
    Scanner in = new Scanner(mensaje);
    in.useDelimiter("");
    if (new StringTokenizer(mensaje, " ").countTokens()!=2) {
      return null;
    }else{
      in.next();
      String usuario = in.next();
      myDb.executeQuery("SELECT * FROM contacts","rs1");
      while(myDb.next("rs1"))
        if(myDb.getString("contactoDe","rs1").equals(user)){
          contactos.add(myDb.getString("nombre","rs1")+"@"+myDb.getString("servidor","rs1"));
        }
      return contactos;
    }
  }

  private LinkedList<String> getMails(String mensaje, DB myDb) throws Exception{
    LinkedList<String> mails = new LinkedList<>();
    Scanner in = new Scanner(mensaje);
    if (new StringTokenizer(mensaje," ").countTokens()!=2){
      return null;
    }else{
      in.next();
      String username = in.next();
      myDb.executeQuery("SELECT * FROM mails","rs1");
      while(myDb.next("rs1"))
        if(myDb.getString("fromU","rs1").equals(username)){
          mails.add(myDb.getString("sender","rs1")+" \""+myDb.getString("subject","rs1")+"\" \""+myDb.getString("body","rs1")+"\"");
        }
      return mails;
    }
  }

  private String agregarContacto(String mensaje,  DB myDb) throws Exception{
    LinkedList<String> users = new LinkedList<>();
    Scanner sc = new Scanner(mensaje);
    sc.useDelimiter(" ");
    sc.next();
    sc = new Scanner(sc.next());
    sc.useDelimiter("@");
    String contacto = sc.next();
    String server = sc.next();
    if (new StringTokenizer(mensaje,"@").countTokens()!=2){
      System.out.println("NEWCONT ERROR 109 "+contacto+"@"+server);
      return "NEWCONT ERROR 109 "+contacto+"@"+server;
    }else{
      myDb.executeQuery("SELECT * FROM users","rs1");
      while(myDb.next("rs1"))
        users.add(myDb.getString("nombre","rs1"));
      System.out.println(contacto);
      System.out.println(server);
      System.out.println(users.toString());
      if (users.contains(contacto)){
        System.out.println("NEWCONT ERROR 110 "+contacto+"@"+server);
        return "NEWCONT ERROR 110 "+contacto+"@"+server;
      }else {
        System.out.println("INSERT INTO contacts (nombre,servidor,contactoDe) VALUES ('"+contacto+"','"+server+"','"+user+"')");
        myDb.executeNonQuery("INSERT INTO contacts (nombre,servidor,contactoDe) VALUES ('"+contacto+"','"+server+"','"+user+"')");
        return "OK NEWCONT "+contacto+"@"+server;
      }
    }
  }

  private void insert(LinkedList<String> destCorreos,String subject,String body,String mensaje,DB myDb)throws Exception{
    for(String s : destCorreos){
      myDb.executeNonQuery("INSERT INTO mails (sender,subject,body,fromU) VALUES ('"+s+"','"+subject+"','"+body+"','"+user+"')");
    }
  }

  public void run() {
    LinkedList<String> destCorreos = new LinkedList<>();
    ServerSocket socket;
    boolean terminarConexion = false;
    try {
      DB myDb = new DB("Server.db");
      if(!myDb.connect()){
        System.out.println("Error en db"+myDb.getError());
        System.exit(0);
      }
      socket = new ServerSocket(1400);
      Socket socket_cli = socket.accept();
      DataInputStream in = new DataInputStream(socket_cli.getInputStream());
      DataOutputStream out = new DataOutputStream(socket_cli.getOutputStream());
      String mensaje = "";
      do {
        mensaje= in.readUTF();
        System.out.println("\nCliente: "+mensaje+"\n");
        if ("".equals(user)&& !mensaje.startsWith("LOGIN")){
          out.writeUTF("Inicie sesión");
          System.out.println("Inicie sesion");
        }else if("".equals(user) && mensaje.startsWith("LOGIN")){
          String res = validarLogin(mensaje, myDb);
          if (res!=null){
            this.user = res;
            System.out.println("Inicio sesion el usuario: "+user);
            out.writeUTF("OK LOGIN");
            System.out.println("OK LOGIN");
          }else{
            out.writeUTF("LOGIN ERROR 101");
            System.out.println("LOGIN ERROR 101");
          }
        }else if(!("".equals(user)) && mensaje.startsWith("CLIST")){
          LinkedList<String> contactos = getContactList(mensaje, myDb);
          if (contactos!=null){
            for (int i = 0; i < contactos.size(); i++) {
              if(i==contactos.size()-1){
                out.writeUTF("OK CLIST "+contactos.get(i) +" *");
                System.out.println("OK CLIST "+contactos.get(i) +" *\n");
              }else{
                out.writeUTF("OK CLIST "+contactos.get(i) +" ");
                System.out.println("OK CLIST "+contactos.get(i) +" \n");
              }
            }
          }else{
            out.writeUTF("CLIST ERROR 103");
            System.out.println("CLIST ERROR 103");
          }
        }else if(!("".equals(user)) && mensaje.startsWith("GETNEWMAILS")){
          LinkedList<String> mails = getMails(mensaje, myDb);
          if (mails!=null && mails.size()>0){
            for (int i = 0; i < mails.size(); i++) {
              if(i==mails.size()-1){
                out.writeUTF("OK GETNEWMAILS " +mails.get(i) +" *");
                System.out.println("OK GETNEWMAILS " +mails.get(i) +" *");
              }else{
                out.writeUTF("OK GETNEWMAILS " +mails.get(i) );
                System.out.println("OK GETNEWMAILS " +mails.get(i) );
              }
            }
          }else if (mails!=null){
            out.writeUTF("OK GETNEWMAILS NOMAILS");
            System.out.println("OK GETNEWMAILS NOMAILS");
          }else{
            out.writeUTF("error GETNEWMAILS");
            System.out.println("error GETNEWMAILS");
          }
        }else if(!("".equals(user)) && mensaje.startsWith("NEWCONT")){
          out.writeUTF(agregarContacto(mensaje,myDb));
        }else if(!("".equals(user)) && mensaje.startsWith("LOGOUT")){
          this.user = "";
          out.writeUTF("OK LOGOUT");
        }else if(!("".equals(user)) && mensaje.equals("SEND MAIL")){
          System.out.println("Recibiendo primer mensaje");
          mensaje = in.readUTF();
          System.out.println("\nCliente: "+mensaje+"\n");
          int cont = 0;
          while (mensaje.startsWith("MAIL TO ") ){
            cont++;
            Scanner sc = new Scanner(mensaje);
            if (new StringTokenizer(mensaje," ").countTokens()==4){
              sc.next();
              sc.next();
              destCorreos.add(sc.next());
              break;
            }else if (new StringTokenizer(mensaje," ").countTokens()==3){
              sc.next();
              sc.next();
              destCorreos.add(sc.next());
              mensaje = in.readUTF();
              System.out.println(mensaje);
            }else{
              cont = 0;
              System.out.println("ERROOOOOOOOOOOOOOOOOOR");
              break;
            }
          }
          System.out.println("Esperando Asunto");
          mensaje = in.readUTF();
          System.out.println("\nCliente: "+mensaje+"\n");
          Scanner sc = new Scanner(mensaje);
          sc.next();
          sc.next();
          String subject = "";
          while(sc.hasNext()){
            subject = subject + sc.next()+" ";
          }
          System.out.println("Esperando Cuerpo");
          mensaje = in.readUTF();
          System.out.println("\nCliente: "+mensaje+"\n");
          sc = new Scanner(mensaje);
          sc.next();
          sc.next();
          String body = "";
          while(sc.hasNext()){
            body = body + sc.next()+" ";
          }
          System.out.println("Esperando END SEND MAIL");
          mensaje = in.readUTF();
          System.out.println("\nCliente: "+mensaje+"\n");
          System.out.println(destCorreos.toString()+" "+subject+" "+body+" "+mensaje);
          if(cont==0){
            out.writeUTF("Error de protocolo");
            System.out.println("Error de protocolo");
          }else{
            System.out.println("Llegamos al insert");
            insert(destCorreos,subject,body,mensaje,myDb);
            out.writeUTF("OK SEND MAIL");
            System.out.println("OK SEND MAIL");
          }
        }else{
          out.writeUTF("Comando invalido");
          System.out.println("Comando invalido");
        }
      } while (!(mensaje.toUpperCase().equals("EXIT")));
      System.out.println("Deteniendo servidor");
    } catch(Exception e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
