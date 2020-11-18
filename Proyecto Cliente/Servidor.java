import java.io.*;
import java.net.*;
import java.util.*;
public class Servidor{
  public static final int PORTC = 1400;
  public static final int PORTS = 1500;
  protected static ServerSocket socketClient;
  protected static ServerSocket socketServer;
  static Socket client;
  //protected ServerThread serverThread;
  public static void main(String[] args){
    BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
    try{
      socketClient = new ServerSocket(PORTC);
      socketServer = new ServerSocket(PORTS);
      System.out.println("El servidor se esta ejecutando en el puerto " + PORTC);

      while(true){
        client = socketClient.accept();
        System.out.println("Nuevo cliente aceptado");
        if(teclado.readLine().equals("close")){
          break;
        }
      }
      socketServer.close();
      socketClient.close();
    }catch(Exception e){}
  }
}
