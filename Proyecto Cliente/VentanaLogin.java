import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaLogin extends JFrame {
  JPanel panelcompleto;
  JLabel tituloLogin,indicacionusuario,indicacionpassword;
  JButton login;
  JTextField usuario_server;
  JPasswordField password;
  public VentanaLogin(){
    setLayout(null);
    setSize(450,500);
    setDefaultCloseOperation(3);
    setLocationRelativeTo(null);
    setResizable(false);
    iniciarComponentes();
  }
  public void iniciarComponentes(){
    panelcompleto=new JPanel();
    panelcompleto=new JPanel();
    panelcompleto.setBackground(new Color(81,90,98));
    panelcompleto.setBounds(0,0,this.getWidth(),this.getHeight());
    panelcompleto.setLayout(null);
    this.getContentPane().add(panelcompleto);

    tituloLogin = new JLabel("CLIENTE JOIN");
    tituloLogin.setBounds()

    usuario_server=new JTextField("");
    usuario_server.setBounds(20,50,300,20);
    panelcompleto.add(usuario_server);

    password=new JPasswordField();
    password.setBounds(20,75,300,20);
    panelcompleto.add(password);
  }
}
