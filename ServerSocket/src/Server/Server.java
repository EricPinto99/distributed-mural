package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Chat.Chat;


public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(6074);
            System.out.println("Servidor iniciado com sucesso.\n\n===============================\n"
                    + "Mural dos Alunos\n");

            while(true) {
                Socket client = server.accept();
                Chat chat = new Chat(client);
                chat.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}