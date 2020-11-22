import java.net.*;
import java.io.*;
import java.util.*;
public class MiSocket{
  ArrayList<String> nMails= new ArrayList<String>();
  Socket conexion;
  InputStreamReader isr;
  DataInputStream in;
  DataOutputStream out;
  public MiSocket(String ip, int puerto) throws Exception{
    conexion= new Socket(ip,puerto);
  }
  public DataInputStream getIn()throws Exception{
    in = new DataInputStream(conexion.getInputStream());
    return in;
  }

  public DataOutputStream getOut()throws Exception{
    out = new DataOutputStream(conexion.getOutputStream());
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
