
public class loadGUI{
  public static void main(String[] args){
    MiBaseDeDatos bd= new MiBaseDeDatos("tablaPrueba.db");
    Cliente c= new Cliente(bd);
    Ventana ventana= new Ventana(c,bd);
  }
}
