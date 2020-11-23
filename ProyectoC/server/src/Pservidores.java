public class Pservidores {
    public static void main(String[] args) {
        ClienteServidor m1 = new ClienteServidor();
        ServidorServidor m2 = new ServidorServidor();
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m2);
        t1.start();
        t2.start();
    }
}