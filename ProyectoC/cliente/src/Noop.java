import java.net.Socket;
import java.util.TimerTask;

public class Noop extends TimerTask {
    //Socket socket;
    public Socket socket;
    public Noop(Socket socket){
        super();
        this.socket = socket;
    }
    @Override
    public void run(/*Socket socket*/) {

    }
}
