import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    private static ArrayList<BufferedWriter>clients;
    private static ServerSocket server;
    private String name;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    public Server(Socket con){
        this.con = con;

        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            clients.add(bfw);
            name = msg = bfr.readLine();

            while (!"Sair".equalsIgnoreCase(msg) && msg!= null){
                msg = bfr.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException{
        BufferedWriter bws;

        for (BufferedWriter bw : clients){
            bws = (BufferedWriter)bw;
            if (!(bwSaida == bws)){
                bw.write(name + " ->" + msg + "\r\n");
                bw.flush();
            }
        }
    }

    public static void main(String[] args) {
        try {
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("12345");
            Object[] texts = {lblMessage, txtPorta};
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            clients = new ArrayList<BufferedWriter>();
            JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " +
                                          txtPorta.getText());
            while (true){
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado com sucesso...");
                Thread t = new Server(con);
                t.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }




}
