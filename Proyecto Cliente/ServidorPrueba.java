import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServidorPrueba {
    static int port = 1200;
    public static void main(String[] args) {

        HashMap<String, String> usuarios = new HashMap<String, String>();
        HashMap<String, String> servidores = new HashMap<String, String>();
        usuarios.put("javier", "1234");
        usuarios.put("joaquin", "alonso19");
        servidores.put("oscar@", "127.0.0.2");
        servidores.put("rodrigo@", "127.0.0.3");
        servidores.put("andres@","127.9.9.9");
        servidores.put("juan@","179.2.0.0");

        try {
          ServerSocket server = new ServerSocket(port);
          System.out.println("Escuchando conexion por el puerto " + port + " ...");
          ciclo:
          while(true){
            Socket socketClient = server.accept();
            System.out.println("Conexion aceptada!");

            InputStreamReader isr = new InputStreamReader(socketClient.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            // es importante el segundo argumento (true) para que tenga autoflush al hacer print
            PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);

            //out.println("Bienvenido al servidor de cc2 :)");

            while(true){
              String usa = in.readLine();
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
                        out.println("OK CLIST " + i + servidores.get(i) + " *");
                      }
                      Thread.sleep(10);
                    }
                  }
                  //}
                } else if((flag[0].equals("GETNEWMAILS"))){
                  if(usuarios.containsKey(flag[1])){
                    int contador = 0;
                    for(String i : servidores.keySet()){
                      contador++;
                      if(contador != servidores.size()){
                        System.out.println("Servidor: OK GETNEWMAILS " + i + servidores.get(i) + " \"Hola\" \"Como estas,como vendras\" ") ;
                        out.println("OK GETNEWMAILS " + i + servidores.get(i) + " \"Hola\" \"Como estas,como vendras\" ");
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
                  while(true){
                    contacto=in.readLine();
                    contactos.add(contacto);
                    System.out.println(contacto);
                    if(contacto.charAt(contacto.length()-1) == '*'){
                      System.out.println("Recibido ultimo contacto");
                      break;
                    }
                  }
                  String asunto= in.readLine();
                  String cuerpo= in.readLine();
                  String enviar= in.readLine();
                  System.out.println("Servidor: OK SEND MAIL");
                  out.println("OK SEND MAIL");

                }else if ((flag[0].equals("NEWCONT"))){
                  String contacto=flag[1];
                  System.out.println(contacto);
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
