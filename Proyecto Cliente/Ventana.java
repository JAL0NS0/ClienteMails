import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ventana extends JFrame implements ActionListener{
  JPanel top,right,mails,send,contacts,newcontact;
  JLabel titulo,titmail,titsend,titcontact,titnew;
  JButton butmails, butsend,butcontacs,butnewcontact,butsalir,butenviar,butremails,butcancelar,butguardar;
  JTextArea cuerpo;
  JTextField destino,asunto,nombre,servidor;
  public Ventana(){
    setLayout(null);
    setSize(450,500);
    setDefaultCloseOperation(3);
    setLocationRelativeTo(null);
    setResizable(false);
    iniciarComponentes();

  }
  public void iniciarComponentes(){
    top=new JPanel();
    top.setBackground(new Color(0,158,171));
    top.setBounds(0,0,this.getWidth(),60);
    top.setLayout(null);
    this.getContentPane().add(top);

    right=new JPanel();
    right.setLayout(null);
    right.setBackground(new Color(214,163,64));
    right.setBounds(0,60,115,410);
    this.getContentPane().add(right);

    cargarMails();
    cargarSend();
    cargarContacts();
    cargarNewContact();

    titulo=new JLabel("MAIL CLIENT");
    titulo.setBounds(top.getWidth()/2-60, top.getHeight()/2-20, 120,40);
    titulo.setFont(new Font("arial",Font.PLAIN,15));
    top.add(titulo);

    butmails=new JButton("MAILS");
    butmails.setLayout(null);
    butmails.setBounds(10,10,right.getWidth()-20,20);
    butmails.setFont(new Font("arial",Font.PLAIN,10));
    butmails.addActionListener(this);
    right.add(butmails);

    butsend=new JButton("ENVIAR");
    butsend.setBounds(10,35,right.getWidth()-20,20);
    butsend.setFont(new Font("arial",Font.PLAIN,10));
    butsend.addActionListener(this);
    right.add(butsend);

    butcontacs=new JButton("CONTACTOS");
    butcontacs.setBounds(10,60,right.getWidth()-20,20);
    butcontacs.setFont(new Font("arial",Font.PLAIN,9));
    butcontacs.addActionListener(this);
    right.add(butcontacs);

    butsalir= new JButton("SALIR");
    butsalir.setBounds(10,right.getHeight()-30,right.getWidth()-20,20);
    right.add(butsalir);

  }
  @Override
  public void actionPerformed​(ActionEvent e){
    if(e.getSource()== butmails){
      mails.setVisible(true);
      send.setVisible(false);
      contacts.setVisible(false);
      newcontact.setVisible(false);
    }
    if(e.getSource()== butsend){
      mails.setVisible(false);
      send.setVisible(true);
      contacts.setVisible(false);
      newcontact.setVisible(false);
    }
    if(e.getSource()== butcontacs|| e.getSource()==butcancelar){
      mails.setVisible(false);
      send.setVisible(false);
      contacts.setVisible(true);
      newcontact.setVisible(false);
    }
    if(e.getSource()== butnewcontact){
      mails.setVisible(false);
      send.setVisible(false);
      contacts.setVisible(false);
      newcontact.setVisible(true);
    }
  }
  public void cargarMails(){
    mails=new JPanel();
    mails.setLayout(null);
    mails.setBackground(new Color(175,178,192));
    mails.setBounds(right.getWidth(),top.getHeight(),335,410);
    mails.setVisible(true);
    this.getContentPane().add(mails);

    titmail= new JLabel("MAILS NUEVOS");
    titmail.setBounds(mails.getWidth()/2-60, 10, 120,40);
    titmail.setFont(new Font("arial",Font.PLAIN,15));
    mails.add(titmail);
  }
  public void cargarSend(){
    send=new JPanel();
    send.setLayout(null);
    send.setBackground(new Color(175,178,192));
    send.setBounds(right.getWidth(),top.getHeight(),335,410);
    send.setVisible(false);
    this.getContentPane().add(send);

    titsend=new JLabel("ENVIAR CORREO");
    titsend.setBounds(100, 10 , 300,40);
    titsend.setFont(new Font("arial",Font.PLAIN,15));
    send.add(titsend);

    butenviar=new JButton("ENVIAR enviar");
    butenviar.setBounds(200,360,100,30);
    send.add(butenviar);

    destino=new JTextField("destino");
    destino.setBounds(20,50,300,20);
    send.add(destino);

    asunto=new JTextField("asunto");
    asunto.setBounds(20,75,300,20);
    send.add(asunto);

    cuerpo= new JTextArea("Hola, como estas");
    cuerpo.setBounds(20,100,300,200);
    send.add(cuerpo);
  }
  public void cargarContacts(){
    contacts=new JPanel();
    contacts.setLayout(null);
    contacts.setBackground(new Color(175,178,192));
    contacts.setBounds(right.getWidth(),top.getHeight(),335,410);
    contacts.setVisible(false);
    this.getContentPane().add(contacts);

    titcontact=new JLabel("CONTACTOS");
    titcontact.setBounds(contacts.getWidth()/2-60,10, 120,40);
    titcontact.setFont(new Font("arial",Font.PLAIN,15));
    contacts.add(titcontact);

    butnewcontact=new JButton("NUEVO CONTACTO");
    butnewcontact.setBounds(200,360,100,30);
    butnewcontact.addActionListener(this);
    contacts.add(butnewcontact);
  }
  public void cargarNewContact(){
    newcontact=new JPanel();
    newcontact.setLayout(null);
    newcontact.setBackground(new Color(175,178,192));
    newcontact.setBounds(right.getWidth(),top.getHeight(),335,410);
    newcontact.setVisible(false);
    this.getContentPane().add(newcontact);

    titnew=new JLabel("AGREGAR CONTACTO");
    titnew.setBounds(newcontact.getWidth()/2-100, top.getHeight()/2-20, 200,40);
    titnew.setFont(new Font("arial",Font.PLAIN,15));
    newcontact.add(titnew);

    nombre=new JTextField("Nombre del contacto");
    nombre.setBounds(20,50,200,20);
    newcontact.add(nombre);

    servidor=new JTextField("Servidor del contacto");
    servidor.setBounds(20,80,200,20);
    newcontact.add(servidor);

    butcancelar=new JButton("CANCELAR");
    butcancelar.setBounds(50,360,100,30);
    butcancelar.addActionListener(this);
    newcontact.add(butcancelar);

    butguardar=new JButton("GUARDAR");
    butguardar.setBounds(200,360,100,30);
    newcontact.add(butguardar);

  }
}
