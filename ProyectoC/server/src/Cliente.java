import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cliente {

  public static void main(String[] args) {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Socket socket;
    byte[] mensaje_bytes = new byte[256];
    String mensaje = "";
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date fecha = new Date();
    try {
      socket = new Socket("127.0.0.1",1500);
      DataInputStream inSer = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      do {
        System.out.print("Conexion establecida\n");
        mensaje = in.readLine();
        out.writeUTF(mensaje);
        mensaje=inSer.readUTF();
        System.out.println("\n"+mensaje);
      } while (!(mensaje.toUpperCase().equals("EXIT")));
      System.out.println("Deteniendo cliente...");
    } catch(Exception e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
