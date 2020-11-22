import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import java.util.*;
public class Ventana extends JFrame{
  JScrollPane scrollMail;
  Cliente cliente;
  ArrayList<String[]> nMails;
  ArrayList<JButton> listaBotones;
  ArrayList<String> listaContactos;
  String formato= new String("[\\w]+@[\\w]+");
  JPanel login;
  JPanel encabezado;
  JPanel menu;
  JPanel mails;
  JPanel panelInteriorMail;
  JPanel enviar;
  JPanel contactos;
  JPanel panelInteriorContactos;
  JPanel nuevoContact;
  JLabel titulo_login;
  JLabel instruccionUsuario;
  JLabel instruccionContrasena;
  JLabel instruccionDestinatario;
  JLabel instruccionAsunto;
  JLabel instruccionCuerpo;
  JLabel titulo;
  JLabel titulo_mail;
  JLabel titulo_enviar;
  JLabel titulo_contactos;
  JLabel titulo_nuevo;
  JLabel errorLogin;
  JLabel errorEnviar;
  JButton botonlog;
  JButton botonMenuMails;
  JButton botonMenuEnviar;
  JButton botonMenuContactos;
  JButton botonMenuSalir;
  JButton botonNuevo;
  JButton botonEnviar;
  JButton botonRecargarMails;
  JButton botonCancelar;
  JButton botonGuardar;
  JTextArea cuerpo;
  JTextField usuario_server;
  JPasswordField contrasena;
  JTextField destino;
  JTextField asunto;
  JTextField nombreNuevo;
  JTextField servidorNuevo;
  //Crea una nueva ventana
  public Ventana(Cliente cliente, MiBaseDeDatos datos){
    this.cliente=cliente;
    setLayout(null);
    setSize(750,500);
    setDefaultCloseOperation(3);
    setLocationRelativeTo(null);
    setResizable(false);
    cargarLogin();

    this.setVisible(true);
  }
  //Inicia los componentes de la ventana
  public void iniciarComponentes(){
    encabezado=new JPanel();
    encabezado.setBackground(new Color(0,158,171));
    encabezado.setBounds(0,0,this.getWidth(),60);
    encabezado.setLayout(null);
    this.getContentPane().add(encabezado);

    titulo=new JLabel("MAIL CLIENT");
    titulo.setBounds(encabezado.getWidth()/2-150, encabezado.getHeight()/2-20, 300,40);
    titulo.setFont(new Font("purisa",Font.PLAIN,30));
    encabezado.add(titulo);

    menu=new JPanel();
    menu.setLayout(null);
    menu.setBackground(new Color(214,163,64));
    menu.setBounds(0,60,115,410);
    this.getContentPane().add(menu);

    botonMenuMails=new JButton("MAILS");
    botonMenuMails.setLayout(null);
    botonMenuMails.setBounds(10,10,menu.getWidth()-20,20);
    botonMenuMails.setFont(new Font("arial",Font.PLAIN,10));
    botonMenuMails.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        mails.setVisible(true);
        enviar.setVisible(false);
        contactos.setVisible(false);
        nuevoContact.setVisible(false);

      }
    });
    menu.add(botonMenuMails);

    botonMenuEnviar=new JButton("ENVIAR");
    botonMenuEnviar.setBounds(10,35,menu.getWidth()-20,20);
    botonMenuEnviar.setFont(new Font("arial",Font.PLAIN,10));
    botonMenuEnviar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mails.setVisible(false);
        enviar.setVisible(true);
        contactos.setVisible(false);
        nuevoContact.setVisible(false);
        errorEnviar.setVisible(false);
      }
    });
    menu.add(botonMenuEnviar);

    botonMenuContactos=new JButton("CONTACTOS");
    botonMenuContactos.setBounds(10,60,menu.getWidth()-20,20);
    botonMenuContactos.setFont(new Font("arial",Font.PLAIN,9));
    botonMenuContactos.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mails.setVisible(false);
        enviar.setVisible(false);
        contactos.setVisible(true);
        nuevoContact.setVisible(false);
      }
    });
    menu.add(botonMenuContactos);

    botonMenuSalir= new JButton("CERRAR SESION");
    botonMenuSalir.setFont(new Font("arial",Font.PLAIN,7));
    botonMenuSalir.setBounds(10,menu.getHeight()-50,menu.getWidth()-20,30);
    botonMenuSalir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(cliente.cerrarSecion()){
          mails.setVisible(false);
          enviar.setVisible(false);
          contactos.setVisible(false);
          nuevoContact.setVisible(false);
          menu.setVisible(false);
          login.setVisible(true);
          errorLogin.setVisible(false);
        }
      }
    });
    menu.add(botonMenuSalir);

    cargarMails();
    cargarEnviar();
    cargarContacts();
    cargarNuevoContacto();
    }
  public boolean cargarLogin(){
    login=new JPanel();
    login.setLayout(null);
    login.setBackground(new Color(0, 158, 171));
    login.setBounds(0,0,this.getWidth(),this.getHeight());
    this.getContentPane().add(login);

    titulo_login=new JLabel("INICIAR SESION",SwingConstants.CENTER);
    titulo_login.setLayout(null);
    titulo_login.setOpaque(true);
    titulo_login.setForeground(Color.WHITE);
    titulo_login.setBackground(new Color(81, 90, 98));
    titulo_login.setBounds(0,50,this.getWidth(),40);
    titulo_login.setFont(new Font("arial",Font.PLAIN,20));
    login.add(titulo_login);

    usuario_server= new JTextField();
    usuario_server.setSize(300,20);
    usuario_server.setLocation(login.getWidth()/2-usuario_server.getWidth()/2,130);
    login.add(usuario_server);

    contrasena=new JPasswordField();
    contrasena.setSize(300,20);
    contrasena.setLocation(login.getWidth()/2-contrasena.getWidth()/2,180);
    login.add(contrasena);

    instruccionUsuario=new JLabel("Ingrese usuario@servidor: ");
    instruccionUsuario.setLayout(null);
    instruccionUsuario.setBounds(usuario_server.getX(),105,200,15);
    instruccionUsuario.setFont(new Font("arial",Font.PLAIN,15));
    login.add(instruccionUsuario);

    instruccionContrasena=new JLabel("Ingrese contrasena: ");
    instruccionContrasena.setLayout(null);
    instruccionContrasena.setBounds(contrasena.getX(),160,200,15);
    instruccionContrasena.setFont(new Font("arial",Font.PLAIN,15));
    login.add(instruccionContrasena);


    errorLogin = new JLabel("Error al Iniciar Secion");
    errorLogin.setLayout(null);
    errorLogin.setSize(200,15);
    errorLogin.setLocation(login.getWidth()/2-errorLogin.getX()/2,210);
    errorLogin.setForeground(Color.RED);
    errorLogin.setFont(new Font("arial",Font.PLAIN,15));
    errorLogin.setVisible(false);
    login.add(errorLogin);

    botonlog=new JButton("Entrar");
    botonlog.setBounds(login.getWidth()/2-75,250,150,25);
    botonlog.setFont(new Font("arial",Font.PLAIN,15));
    botonlog.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String nombre=usuario_server.getText();
        String contra= new String(contrasena.getPassword());
        if(Pattern.matches(formato,nombre)){
          if(cliente.Verificar(nombre,contra)==0){
            listaContactos=cliente.getContactos();
            cliente.getNuevosMails();
            nMails=cliente.cargarListaNuevos();
            iniciarComponentes();
            menu.setVisible(true);
            login.setVisible(false);
            usuario_server.setText("");
            contrasena.setText("");
          }else if(cliente.Verificar(nombre,contra)==1){
            errorLogin.setText("Servidor Desconocido");
            errorLogin.setVisible(true);
          }else{
            errorLogin.setText("Usuario o contrasena invalido");
            errorLogin.setBounds(130,210,220,15);
            errorLogin.setVisible(true);
          }
        }else{
          errorLogin.setText("Formato incorrecto usuario@server");
          errorLogin.setLocation(login.getWidth()/2-errorLogin.getX()/2,15);
          errorLogin.setVisible(true);
        }
      }
    });
    login.add(botonlog);

    return true;
  }
  public void cargarMails(){
    mails=new JPanel();
    mails.setLayout(null);
    mails.setBackground(new Color(175,178,192));
    mails.setSize(this.getWidth()-menu.getWidth(),this.getHeight()-(encabezado.getHeight()+30));
    mails.setLocation(menu.getWidth(),encabezado.getHeight());
    mails.setVisible(true);
    this.getContentPane().add(mails);

    titulo_mail= new JLabel("MAILS NUEVOS");
    titulo_mail.setBounds(mails.getWidth()/2-60, 10, 120,20);
    titulo_mail.setFont(new Font("arial",Font.PLAIN,15));
    mails.add(titulo_mail);

    panelInteriorMail=new JPanel();
    panelInteriorMail.setLayout(new GridLayout(21,1));
    panelInteriorMail.setBackground(new Color(175,178,192));
    panelInteriorMail.setBounds(10,30,mails.getWidth()-20,mails.getHeight()-50);
    for(String[] x:nMails){
      JButton boton = new JButton(x[1]+" "+x[2]);
      boton.setForeground(Color.BLUE);
      boton.setFont(new Font("arial",Font.PLAIN,12));
      boton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null,x[1]+"\n"+x[2]+"\n"+x[3]+"\n");
          boton.setForeground(Color.BLACK);
          cliente.marcarLeido(x[0]);
          panelInteriorMail.setSize(mails.getWidth()-20,panelInteriorMail.getHeight()+10);
          panelInteriorMail.updateUI();
        }
      });
      panelInteriorMail.add(boton);
    }

    // scrollMail=new JScrollPane();
    // scrollMail.setLocation(10,30);
    // scrollMail.setSize(mails.getWidth()-20,mails.getHeight()-40);
    // mails.add(scrollMail);

    mails.add(panelInteriorMail);
  }
  public void cargarEnviar(){
    enviar=new JPanel();
    enviar.setLayout(null);
    enviar.setBackground(new Color(175,178,192));
    enviar.setSize(this.getWidth()-menu.getWidth(),this.getHeight()-encabezado.getHeight());
    enviar.setLocation(menu.getWidth(),encabezado.getHeight());
    enviar.setVisible(false);
    this.getContentPane().add(enviar);

    titulo_enviar=new JLabel("ENVIAR CORREO");
    titulo_enviar.setBounds(enviar.getWidth()/2-150, 10 , 300,18);
    titulo_enviar.setFont(new Font("arial",Font.PLAIN,15));
    enviar.add(titulo_enviar);

    errorEnviar=new JLabel("ERROR AL ENVIAR");
    errorEnviar.setLayout(null);
    errorEnviar.setSize(150,20);
    errorEnviar.setLocation(10,370);
    errorEnviar.setForeground(Color.RED);
    errorEnviar.setFont(new Font("arial",Font.PLAIN,15));
    errorEnviar.setVisible(false);
    enviar.add(errorEnviar);

    botonEnviar=new JButton("ENVIAR");
    botonEnviar.setBounds(200,370,90,20);
    botonEnviar.setFont(new Font("arial",Font.PLAIN,10));
    botonEnviar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String destinatario= destino.getText();
        String asuntoParaEnviar=asunto.getText();
        String mensajeAEnviar=cuerpo.getText();
        if(cliente.enviarMensaje(destinatario,asuntoParaEnviar,mensajeAEnviar)){
          destino.setText("");
          asunto.setText("");
          cuerpo.setText("");
          errorEnviar.setText("ENVIO EXITOSO");
          errorEnviar.setForeground(new Color(83, 122, 0));
          errorEnviar.setVisible(true);
        }else{
          errorEnviar.setVisible(true);
        }
      }
    });
    enviar.add(botonEnviar);

    instruccionDestinatario= new JLabel("Ingresar destinatario(s) usuario@servidor");
    instruccionDestinatario.setLayout(null);
    instruccionDestinatario.setBounds(20,35,250,13);
    instruccionDestinatario.setFont(new Font("arial",Font.PLAIN,10));
    enviar.add(instruccionDestinatario);

    instruccionAsunto= new JLabel("Ingresar Asunto");
    instruccionAsunto.setLayout(null);
    instruccionAsunto.setBounds(20,75,200,13);
    instruccionAsunto.setFont(new Font("arial",Font.PLAIN,10));
    enviar.add(instruccionAsunto);

    instruccionCuerpo= new JLabel("Ingresar mensaje");
    instruccionCuerpo.setLayout(null);
    instruccionCuerpo.setBounds(20,115,200,15);
    instruccionCuerpo.setFont(new Font("arial",Font.PLAIN,10));
    enviar.add(instruccionCuerpo);

    destino=new JTextField("");
    destino.setFont(new Font("arial",Font.PLAIN,10));
    destino.setBounds(20,50,300,20);
    enviar.add(destino);

    asunto=new JTextField("");
    asunto.setFont(new Font("arial",Font.PLAIN,10));
    asunto.setBounds(20,90,300,20);
    enviar.add(asunto);

    cuerpo= new JTextArea("");
    cuerpo.setLineWrap(true);
    cuerpo.setWrapStyleWord(true);
    cuerpo.setFont(new Font("arial",Font.PLAIN,10));
    cuerpo.setBounds(20,130,300,220);
    enviar.add(cuerpo);
  }
  public void cargarContacts(){
    contactos=new JPanel();
    contactos.setLayout(null);
    contactos.setBackground(new Color(175,178,192));
    contactos.setSize(this.getWidth()-menu.getWidth(),this.getHeight()-encabezado.getHeight());
    contactos.setLocation(menu.getWidth(),encabezado.getHeight());
    contactos.setVisible(false);
    this.getContentPane().add(contactos);

    titulo_contactos=new JLabel("CONTACTOS");
    titulo_contactos.setBounds(contactos.getWidth()/2-60,10, 120,20);
    titulo_contactos.setFont(new Font("arial",Font.PLAIN,15));
    contactos.add(titulo_contactos);

    panelInteriorContactos=new JPanel();
    panelInteriorContactos.setLayout(new GridLayout(22,1));
    panelInteriorContactos.setBackground(new Color(175,178,192));
    panelInteriorContactos.setBounds(20,35,mails.getWidth()-40,330);
    for(String x:listaContactos){
      panelInteriorContactos.add(new JButton(x));
    }

    contactos.add(panelInteriorContactos);

    botonNuevo=new JButton("NUEVO CONTACTO");
    botonNuevo.setBounds(165,370,150,25);
    botonNuevo.setFont(new Font("arial",Font.PLAIN,10));
    botonNuevo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mails.setVisible(false);
        enviar.setVisible(false);
        contactos.setVisible(false);
        nuevoContact.setVisible(true);
      }
    });
    contactos.add(botonNuevo);
  }
  public void cargarNuevoContacto(){
    nuevoContact=new JPanel();
    nuevoContact.setLayout(null);
    nuevoContact.setBackground(new Color(175,178,192));
    nuevoContact.setSize(this.getWidth()-menu.getWidth(),this.getHeight()-encabezado.getHeight());
    nuevoContact.setLocation(menu.getWidth(),encabezado.getHeight());
    nuevoContact.setVisible(false);
    this.getContentPane().add(nuevoContact);

    titulo_nuevo=new JLabel("AGREGAR CONTACTO");
    titulo_nuevo.setBounds(nuevoContact.getWidth()/2-100, 10, 200,40);
    titulo_nuevo.setFont(new Font("arial",Font.PLAIN,15));
    nuevoContact.add(titulo_nuevo);

    nombreNuevo=new JTextField("");
    nombreNuevo.setFont(new Font("arial",Font.PLAIN,10));
    nombreNuevo.setBounds(20,50,200,20);
    nuevoContact.add(nombreNuevo);

    servidorNuevo=new JTextField("");
    servidorNuevo.setBounds(20,80,200,20);
    servidorNuevo.setFont(new Font("arial",Font.PLAIN,10));
    nuevoContact.add(servidorNuevo);

    botonCancelar=new JButton("CANCELAR");
    botonCancelar.setBounds(20,360,100,30);
    botonCancelar.setFont(new Font("arial",Font.PLAIN,10));
    botonCancelar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mails.setVisible(false);
        enviar.setVisible(false);
        contactos.setVisible(true);
        nuevoContact.setVisible(false);

      }
    });
    nuevoContact.add(botonCancelar);

    botonGuardar=new JButton("GUARDAR");
    botonGuardar.setBounds(200,360,100,30);
    botonGuardar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cliente.guardarContacto(nombreNuevo.getText(),servidorNuevo.getText());
      }
    });
    nuevoContact.add(botonGuardar);
  }
}
