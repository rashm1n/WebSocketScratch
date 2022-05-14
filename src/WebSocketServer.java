import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketServer extends Thread{
    private int port;
    private ServerSocket serverSocket;
    private boolean running = false;

    public WebSocketServer(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.start();
    }

    public void stopServer()
    {
        running = false;
        this.interrupt();
    }

    @Override public void run() {
        running = true;
        while( running )
        {
            try
            {
                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
