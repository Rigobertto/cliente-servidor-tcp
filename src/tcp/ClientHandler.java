package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private ArrayList<ClientHandler> clients;
    private PrintWriter out;
    private String name;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            name = in.readLine();
            System.out.println(name + " conectado ao servidor.");
            broadcast(name + " entrou no servidor");
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/")) {
                    handleCommand(message);
                } else {
                    unicast(name, message);
                }
            }
            System.out.println(name + " desconectado do servidor");
            broadcast(name + " saiu do servidor.");
            clientSocket.close();
            clients.remove(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCommand(String message) {
        String[] parts = message.split(" ");
        String command = parts[0];
        switch (command) {
            case "/list":
                listClients();
                break;
            case "/all":
            	broadcast(name + ": " + removeCommand(message).trim());
                break;
            case "/quit":
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                out.println("Comando inválido: " + command);
                break;
        }
    }

    private void listClients() {
        StringBuilder sb = new StringBuilder();
        sb.append("Clientes conectados:\n");
        for (ClientHandler client : clients) {
            sb.append(client.name + "\n");
        }
        out.println(sb.toString());
    }

    private void unicast(String sender, String message) {
        String[] parts = message.split(" ");
        String recipientName = parts[0];
        for (ClientHandler client : clients) {
            if (client.name.equals(recipientName)) {
                client.out.println(sender + ": " + message.substring(recipientName.length() + 1));
            }
        }
    }

    private void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.out.println(message);
        }
    }
    
//    private String removeCommand(String s) {
//
//    	String message[] = s.split(" ");
//    	String newS = "";
//    	for(int i = 1 ; i < message.length ;i++) {
//    		newS = message[i] + " ";
//    	}
//    	return newS;
//    }
    
    private String removeCommand(String s) {
    	char message[] = s.toCharArray();
    	char newMessage[] = new char[message.length - 4];
    	for(int i = 0; i < newMessage.length; i++) {
    		newMessage[i] = message[i+4];
    	}
    	return new String(newMessage);
    }
}

