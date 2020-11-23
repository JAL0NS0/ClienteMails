import db.DB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ServidorServidor  implements  Runnable{
    private String user = "";

    private void insert(LinkedList<String> destCorreos,String subject,String body,String mensaje,DB myDb)throws Exception{
        for(String s : destCorreos){
            myDb.executeNonQuery("INSERT INTO mails (sender,subject,body,fromU) VALUES ('"+s+"','"+subject+"','"+body+"','"+user+"')");
        }
    }
    @Override
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
            socket = new ServerSocket(1500);
            Socket socket_cli = socket.accept();
            DataInputStream in = new DataInputStream(socket_cli.getInputStream());
            DataOutputStream out = new DataOutputStream(socket_cli.getOutputStream());
            String mensaje = "";
            do {
                mensaje= in.readUTF();
                System.out.println("\nCliente: "+mensaje+"\n");
                if(mensaje.equals("SEND MAIL")){
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
                        }else{
                            cont = 0;
                            System.out.println("ERROOOOOOOOOOOOOOOOOOR");
                            break;
                        }
                    }
                    mensaje = in.readUTF();
                    System.out.println("\nCliente: "+mensaje+"\n");
                    Scanner sc = new Scanner(mensaje);
                    sc.next();
                    sc.next();
                    String subject = "";
                    while(sc.hasNext()){
                        subject = subject + sc.next();
                    }
                    mensaje = in.readUTF();
                    System.out.println("\nCliente: "+mensaje+"\n");
                    sc = new Scanner(mensaje);
                    sc.next();
                    sc.next();
                    String body = "";
                    while(sc.hasNext()){
                        body = body + sc.next();
                    }
                    mensaje = in.readUTF();
                    System.out.println("\nCliente: "+mensaje+"\n");
                    System.out.println(destCorreos.toString()+" "+subject+" "+body+" "+mensaje);
                    if(cont==0){
                        out.writeUTF("Error de protocolo");
                        System.out.println("Error de protocolo");
                    }else{
                        insert(destCorreos,subject,body,mensaje,myDb);
                        out.writeUTF("OK SEND EMAIL");
                        System.out.println("OK SEND EMAIL");
                    }
                }else if(mensaje.startsWith("CHECK CONTACT")){
                    LinkedList<String> contactos = new LinkedList<>();
                    Scanner sc = new Scanner(mensaje);
                    sc.useDelimiter(" ");
                    if (new StringTokenizer(mensaje, " ").countTokens()!=3) {
                        out.writeUTF("CHECK ERROR 206");
                    }else{
                        sc.next();
                        sc.next();
                        String usuario = sc.next();
                        sc = new Scanner(usuario);
                        sc.useDelimiter("@");
                        usuario = sc.next();
                        myDb.executeQuery("SELECT * FROM contacts","rs1");
                        while(myDb.next("rs1"))
                            if(myDb.getString("nombre","rs1").equals(usuario)){
                                contactos.add(myDb.getString("nombre","rs1")+"@"+myDb.getString("servidor","rs1"));
                            }
                        if (contactos.size()>0){
                            out.writeUTF("OK CHECK CONTACT");
                        }else{
                            out.writeUTF("CHECK ERROR 205");
                        }
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
