import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Cliente extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou ;
    private Writer ouw;
    private BufferedWriter bfw;
    private JTextField txtIP;
    private JTextField txtPorta;
    private JTextField txtNome;


    public Client() throws IOException{

        JLabel lblMessage = new JLabel("Check!");
        txtIP = new JTextField("127.0.0.1");
        txtPort = new JTextField("12345");
        txtName = new JTextField("Client");
        Object[] texts = {lblMessage, txtIP, txtPort, txtPort};
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        text = new JTextArea(10,20);
        text.setEditable(false);
        text.setBackground(new Color(240,240,240));
        txtMsg = new JTextField(20);
        lblHistoric = new JLabel("Historic");
        lblMsg = new JLabel("Message");
        btnSend = new JButton("Send");
        btnSend.setToolTipText("Send Message");
        btnExit = new JButton("Sair");
        btnExit.setToolTipText("Sair do Chat");
        btnSend.addActionListener(this);
        btnExit.addActionListener(this);
        btnSend.addKeyListener(this);
        txtMsg.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(text);
        text.setLineWrap(true);
        pnlContent.add(lblHistoric);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);
        pnlContent.add(txtMsg);
        pnlContent.add(btnExit);
        pnlContent.add(btnSend);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        text.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        setTitle(txtName.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250,300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void connect() throws IOException{
        socket = new Socket(txtIP.getText(), Integer.parseInt(txtPort.getText()));
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        bfw.write(txtName.getText() + "\r\n");
        bfw.flush();
    }

    public void sendMessage(String msg) throws IOException{
        if (msg.equals("Sair")){
            bfw.write("Desconectado \r\n");
            text.append("Desconectado \r\n");
        } else {
            bfw.write(msg + "\r\n");
            text.append(txtName.getText() + " say ->" + txtMsg.getText() + "\r\n");
        }
        bfw.flush();
        txtMsg.setText("");
    }

    public void listen() throws IOException{

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while (!"Sair".equalsIgnoreCase(msg)){
            if (bfr.ready()){
                msg = bfr.readLine();
                if (msg.equals("Sair"))
                    text.append("Sevidor caiu! \r\n");
                else
                    text.append(msg + "\r\n");
            }
        }
    }

    public void exit() throws IOException{
        sendMessage("Sair");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }

    public void actionPerfomed(ActionEvent e){
        try{
            if(e.getActionCommand().equals(btnSend.getActionCommand()))
                sendMessage(txtMsg.getText());
            else
                if (e.getActionCommand().equals(btnExit.getActionCommand()))
                    exit();
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                sendMessage(txtMsg.getText());
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }

    public void keyReleased(KeyEvent arg0){
    }

    public void keyTyped(KeyEvent arg0){
    }

    public static void main(String[] args) {
        Client app = new Client();
        app.connect();
        app.listen();
    }
}


