import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.*;
import java.util.*;

public class ServidorPrueba {
    static int port = 1400;
    public static void main(String[] args) {
        String[] auxiliares;
        String formato= new String("[\\w]+@[\\w]+");
        HashMap<String, String> usuarios = new HashMap<String, String>();
        HashMap<String, String> servidores = new HashMap<String, String>();
        usuarios.put("javier", "1234");
        usuarios.put("joaquin", "alonso19");
        servidores.put("oscar@", "127.0.0.2");
        servidores.put("rodrigo@", "127.0.0.3");
        servidores.put("antonio@","200.20.0.27");
        servidores.put("isaura@","2.3.4.5");
        servidores.put("pedro@","sever23");

        try {
          ServerSocket server = new ServerSocket(port);
          System.out.println("Escuchando conexion por el puerto " + port + " ...");
          ciclo:
          while(true){
            Socket socketClient = server.accept();
            System.out.println("Conexion aceptada!");
            InputStreamReader isr = new InputStreamReader(socketClient.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
            String usa;
            while(true){
              usa = in.readLine();
              if(usa != null){
                System.out.println("Cliente: " + usa);
                String[] flag = usa.split(" ");
                if((flag[0]).equals("LOGIN")){
                  System.out.println(flag[1]);
                  if(usuarios.containsKey(flag[1])){
                    if(usuarios.get(flag[1]).equals(flag[2])){
                      System.out.println("Servidor: OK LOGIN");
                      out.println("OK LOGIN");
                    } else{
                      out.println(Comandos.LOGIN_ERROR+"102");
                      System.out.println(Comandos.LOGIN_ERROR+"102");
                      in.close();
                      out.close();
                      socketClient.close();
                      continue ciclo;
                    }

                  }else {
                    out.println(Comandos.LOGIN_ERROR+"101");
                    System.out.println(Comandos.LOGIN_ERROR+"101");
                    in.close();
                    out.close();
                    socketClient.close();
                    continue ciclo;
                  }

                } else if((flag[0].equals("CLIST"))){
                  if(usuarios.containsKey(flag[1])){

                    int contador = 0;
                    for(String i : servidores.keySet()){
                      contador++;
                      if(contador != servidores.size()){
                        System.out.println("Servidor: OK CLIST " + i + servidores.get(i));
                        out.println("OK CLIST " + i + servidores.get(i));
                      } else {
                        System.out.println("Servidor: OK CLIST " + i + servidores.get(i) + " *");
                        out.println("OK CLIST " + i + servidores.get(i) + "*");
                      }
                    }
                    Thread.sleep(10);
                  }
                  //}
                } else if((flag[0].equals("GETNEWMAILS"))){
                  if(usuarios.containsKey(flag[1])){
                    int contador = 0;
                    for(String i : servidores.keySet()){
                      contador++;
                      if(contador != servidores.size()){
                        System.out.println("Servidor: OK GETNEWMAILS " + i + servidores.get(i) + " \"Hola\" \"Como estas,como vendras\" ") ;
                        out.println("OK GETNEWMAILS " + i + servidores.get(i) + " \"Hola Esta es un aprueba\" \"Como estas,como vendras\" ");
                      } else {
                        System.out.println("Servidor: OK GETNEWMAILS " + i + servidores.get(i) + " \"Buenas\" \"Mi nombre es Pepe\" " +  " *");
                        out.println("OK GETNEWMAILS " + i + servidores.get(i) + " \"Buenas\" \"Mi nombre es Pepe\" " + " *");
                      }
                      Thread.sleep(10);
                    }
                  }
                }else if((flag[0].equals("LOGOUT"))){
                  out.println("OK LOGOUT");
                  System.out.println("Servidor: OK LOGOUT");
                  in.close();
                  out.close();
                  socketClient.close();
                  continue ciclo;
                }else if((flag[0].equals("SEND"))){
                  ArrayList<String> contactos= new ArrayList<String>();
                  String contacto;
                  int contador=0;
                  while(true){
                    contacto=in.readLine();
                    char ultimo=contacto.charAt(contacto.length()-1);
                    if( ultimo == '*'){
                      System.out.println("Recibido ultimo contacto");
                      contacto=contacto.substring(9,contacto.length()-1);
                      System.out.println("\""+contacto+"\"");
                      if(Pattern.matches(formato,contacto)){
                        break;
                      }else{
                        System.out.println(contacto+" formato invalido");
                        contador+=1;
                        break;
                      }
                    }else{
                      contacto=contacto.substring(9,contacto.length());
                      System.out.println("\""+contacto+"\"");
                      if(Pattern.matches(formato,contacto)){
                        break;
                      }else{
                        System.out.println(contacto+" formato invalido");
                        contador+=1;
                        break;
                      }
                    }
                  }
                  String asunto= in.readLine();
                  String cuerpo= in.readLine();
                  String enviar= in.readLine();
                  if(contador==0){
                    System.out.println("Servidor: OK SEND MAIL");
                    out.println("OK SEND MAIL");
                  }else{
                    System.out.println("Servidor: "+Comandos.SEND_ERROR);
                    System.out.println(contador+" contactos con formato invalido");
                    out.println(Comandos.SEND_ERROR);
                  }

                }else if ((flag[0].equals("NEWCONT"))){
                  String contacto=flag[1];
                  System.out.println(contacto);
                  auxiliares=contacto.split("@");
                  servidores.put(auxiliares[0]+"@",auxiliares[1]);
                  System.out.println("Se guardo el contacto "+contacto);
                  out.println("OK NEWCONT "+contacto);
                }else{
                  System.out.println("INVALID COMMAND ERROR");
                }
              }

            }
          }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
