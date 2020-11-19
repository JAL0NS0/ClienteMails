import java.net.*;
import java.io.*;
import java.util.*;
public class MiSocket{
  ArrayList<String> nMails= new ArrayList<String>();
  Socket conexion;
  InputStreamReader isr;
  BufferedReader in;
  PrintWriter out;
  public MiSocket(String ip, int puerto) throws Exception{
    conexion= new Socket(ip,puerto);
  }
  public BufferedReader getIn()throws Exception{
    isr = new InputStreamReader(conexion.getInputStream());
    in = new BufferedReader(isr);
    return in;
  }

  public PrintWriter getOut()throws Exception{
    out = new PrintWriter(conexion.getOutputStream(), true);
    return out;
  }
  public void closeIn()throws Exception{
    in.close();
  }
  public void closeOut() throws Exception{
    out.close();
  }
  public void closeMiSocket()throws Exception{
    conexion.close();
  }
}
