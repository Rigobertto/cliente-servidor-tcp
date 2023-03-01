package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;

    public Client(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(name);
            System.out.println(name + " conectado ao servidor na porta " + port);
            new Thread(new Receiver()).start();
            new Thread(new Sender()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Receiver implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Sender implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = console.readLine()) != null) {
                    out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

