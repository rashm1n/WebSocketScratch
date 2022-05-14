import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread{
    private Socket socket;
    RequestHandler( Socket socket )
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedInputStream clientInputStream = new BufferedInputStream(socket.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();

            while (clientInputStream.available() > 0) {
                stringBuilder.append((char) clientInputStream.read());
            }

            String[] ss = stringBuilder.toString().split("\n");

            Map<String, String> headers = new HashMap<>();

            for (int i = 0; i < 5; i++) {
                if (ss[i].contains("Connection")) {
                    String value = ss[i].split(":")[1].trim();
                    headers.put("Connection",value);
                } else if (ss[i].contains("Upgrade")) {
                    String value = ss[i].split(":")[1].trim();
                    headers.put("Upgrade",value);
                } else if (ss[i].contains("Host")) {
                    String value = ss[i].split(" ")[1].trim();
                    headers.put("Host",value);
                } else if (ss[i].contains("Sec-WebSocket-Key")) {
                    String value = ss[i].split(":")[1].trim();
                    headers.put("Sec-WebSocket-Key",value);
                }
            }

            if (headers.containsValue("websocket") && headers.containsValue("Upgrade") && headers.containsKey("Sec-WebSocket-Key")) {
                initConnection(headers, socket);
            } else {
                System.out.println(stringBuilder);
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] createHandshakeResponse(Map<String,String> headers)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return  ("HTTP/1.1 101 Switching Protocols\r\n"
                + "Connection: Upgrade\r\n"
                + "Upgrade: websocket\r\n"
                + "Sec-WebSocket-Accept: "
                + Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-1").digest((headers.get("Sec-WebSocket-Key") + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(
                        StandardCharsets.UTF_8)))
                + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);
    }

    private static void initConnection(Map<String, String> headers, Socket socket)
            throws IOException, NoSuchAlgorithmException {
        byte[] response = createHandshakeResponse(headers);
        socket.getOutputStream().write(response, 0, response.length);
    }
}
