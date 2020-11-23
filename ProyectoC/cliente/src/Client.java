import db.DB;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;


public class Client {


    public static boolean isEnd(String str){

        return (str.charAt(str.length()-1)=='*');
    }

    public static void showEmail(String mail){
        mail=mail.substring(15,mail.length());
        int finDestino=mail.indexOf(" ");
        Scanner sc = new Scanner(mail);
        sc.useDelimiter("\"");
        System.out.println("Sender: " +sc.next().substring(0,finDestino));
        System.out.println("Subject: " +sc.next().replaceAll("\"",""));
        sc.next();
        System.out.println("Body: " +sc.next().replaceAll("\"",""));
    }

    public static void handleError(String msg){

    }

    public static void noop(DataOutputStream out){
        try {
            out.writeUTF("NOOP");
        } catch (Exception e){

        }
    }

    public static void send(DataOutputStream out,String msg){
        try {
            System.out.println("---CLIENTE: "+msg);
            out.writeUTF(msg);

        } catch (Exception e){

        }
    }

    public  static  String receive(DataInputStream in){
        String msg = "";
        try {
            msg=in.readUTF();
            System.out.println("+++SERVIDOR: "+msg);

        } catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    public static void main(String[] args) {
        try {
            String msg = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // usuario@servidor
            System.out.println("Ingrese el usuario y el servidor al que desea conectarse. Utilice el fomato usuario@servidor");
            msg = reader.readLine();
            String[] split = msg.split("@");
            String user = split[0];
            String server = split[1];
            String ip = "";
            String port = "";


            try{
                DB db = new DB("a.db");
                if(!db.connect()){
                    System.out.println("Error conectando a la base de datos: "+db.getError());
                    System.exit(0);
                }
                //System.out.println("asdasd");
                db.executeQuery("SELECT * FROM IPs","rs1");
                LinkedList<String[]> basedatos = new LinkedList<String[]>();
                String[] str = new String[3] ;
                while(db.next("rs1")) {
                    str = new String[]{db.getString("name", "rs1"), db.getString("ip", "rs1"), db.getString("port", "rs1")};
                    basedatos.push(str);
                }

                for (int i = 0; i < basedatos.size(); i++) {;
                   if (basedatos.get(i)[0].equals(server)){
                       ip = basedatos.get(i)[1];
                       port = basedatos.get(i)[2];
                   };
               }


            }catch(Exception e){
                e.printStackTrace();}

            Socket socket;
            socket = new Socket(ip, Integer.parseInt(port));
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            System.out.println("Ingrese la contraseña");
            String password = reader.readLine();;
            send(out,"LOGIN "+user+" "+password);
            msg= receive(in);
           //logueado
            if ("OK LOGIN".equals(msg)){

                TimerTask task = new TimerTask() {
                    @Override
                    public void run(){
                        //noop(out);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, new Date(), 20000);

                //Obtener lista de contactos / CLIST username
                System.out.println("Se ha logueado correctamente.");

                System.out.println("Obteniendo lista de usuarios y nuevos correos...");
                send(out,"CLIST "+user);
                msg= receive(in);

                LinkedList<String> contacts = new LinkedList<String>();

                boolean last = isEnd(msg);
                split = null;
                split = msg.split(" ");
                String contact = split[2];
                contacts.push(contact);
                while (!last) {
                    msg = receive(in);
                    last = isEnd(msg);
                    split = null;
                    split = msg.split(" ");
                    contact = split[2];
                    contacts.push(contact);

                }

                //Obtener nuevos emails / GETNEWMAILS username
                send(out,"GETNEWMAILS "+user);
                msg= receive(in);
                split = null;
                split = msg.split(" ");
                LinkedList<String> mails = new LinkedList<String>();
                boolean newmails = false;

                if(!"NOMAILS".equals(split[2])){
                    newmails = true;
                    last = isEnd(msg);
                    mails.push(msg);
                    while (!last) {
                        msg= receive(in);
                        last = isEnd(msg);
                        mails.push(msg);
                        System.out.println("Pusheo "+msg);
                    }
                    if (newmails) {
                        int mailq = mails.size();
                        System.out.println("Tiene " + mailq + " nuevo(s) correo(s).");
                        for (int i =0;i<mailq;i++){
                            System.out.println("Correo "+ (i+1));
                            System.out.println("");
                            showEmail(mails.pop());
                            System.out.println("");
                        }
                    } else {
                        System.out.println("No hay correos nuevos.");
                    }
                }System.out.println("Inrese el comando deseado");



                //El programa empieza a leer lo que el usuario quiere hacer (SEND/NEWCONT/LOGOUT/GETNEWEMAILS/VER CONTACTOS)

                System.out.println("");
                System.out.println("¿Qué desea hacer? Ingrese el número asociado con la acción.");
                System.out.println("1. Enviar un correo");
                System.out.println("2. Verificar si hay correos nuevos");
                System.out.println("3. Ver Contactos");
                System.out.println("4. Crear contacto");
                System.out.println("5. Salir");
                System.out.println("");

                msg = reader.readLine();

                while (!"5".equals(msg)){
                    switch(msg) {
                        case "1":
                            send(out,"SEND MAIL");
                            System.out.println("Ingrese los usuarios y servidores al cual desea enviar correos de la siguiente forma: usuario@servidor. Cuando sea el último destinatario coloque un * al final.");
                            msg = reader.readLine();
                            send(out,"MAIL TO " + msg);
                            while (!isEnd(msg)) {
                                msg = reader.readLine();
                                send(out,"MAIL TO " + msg);
                            }
                            System.out.println("Ingrese el titulo del correo");
                            msg = reader.readLine();
                            send(out,"MAIL SUBJECT " + msg);
                            System.out.println("Ingrese el cuerpo del correo");
                            msg = reader.readLine();
                            send(out,"MAIL BODY " + msg);
                            send(out,"END SEND MAIL");
                            msg= receive(in);
                            
                            if ("OK SEND MAIL".equals(msg)){
                                System.out.println("Correo enviado exitosamente.");
                                System.out.println("");
                            } else
                                handleError(msg);
                            break;
                        case "2":
                            send(out,"GETNEWMAILS "+user);
                            msg= receive(in);
                            split = null;
                            split = msg.split(" ");
                            mails = null;
                            newmails = false;

                            if(!"NOMAILS".equals(split[2])){
                                newmails = true;
                                last = isEnd(msg);
                                LinkedList<String> nmails = new LinkedList<String>();
                                nmails.push(msg);
                                while (!last) {
                                    msg= receive(in);
                                    last = isEnd(msg);
                                    nmails.push(msg);
                                    System.out.println("Pusheo "+msg);
                                }
                                if (newmails) {
                                    int mailq = mails.size();
                                    System.out.println("Tiene " + mailq + " nuevo(s) correo(s).");
                                    for (int i =0;i<mailq;i++){
                                        System.out.println("Correo "+ (i+1));
                                        System.out.println("");
                                        showEmail(mails.pop());
                                        System.out.println("");
                                    }
                                } else {
                                    System.out.println("No hay correos nuevos.");
                                }
                            }
                            break;
                        case "3":
                            if (contacts.size()>0){
                                for (int i =0;i<contacts.size();i++){
                                    System.out.println("");
                                    System.out.println("Contacto "+(i+1)+": "+contacts.get(i));
                                    System.out.println("");
                                }
                            }
                            break;
                        case "4":
                            System.out.println("Ingrese el nuevo contacto con el formato usuario@servidor");
                            msg = reader.readLine();
                            send(out,"NEWCONT " + msg);
                            msg= receive(in);
                            if ("OK NEWCONT".equals(msg.substring(0,10))){
                                System.out.println("");
                                System.out.println("Contacto añadido exitosamente.");
                                System.out.println("");
                            } else
                                handleError(msg);
                            break;
                        default:
                            System.out.println("Ingrese una opción válida.");
                    }
                    System.out.println("");
                    System.out.println("¿Qué desea hacer? Ingrese el número asociado con la acción.");
                    System.out.println("1. Enviar un correo");
                    System.out.println("2. Verificar si hay correos nuevos");
                    System.out.println("3. Ver Contactos");
                    System.out.println("4. Crear contacto");
                    System.out.println("5. Salir");
                    System.out.println("");
                    msg = reader.readLine();
                }
                send(out,"LOGOUT");
                msg = receive(in);
                System.out.println("Saliendo de la aplicación");

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
