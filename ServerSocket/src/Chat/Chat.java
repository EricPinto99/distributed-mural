package Chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Chat extends Thread{

    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Chat(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            while(true) {
                String nome = in.readObject().toString();
                System.out.println("Aluno - " + nome);

                String msg = in.readObject().toString();
                System.out.println("Mensagem - " + msg);

                System.out.println("=========================");
            }

        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
    }


}
