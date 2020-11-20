import db.*;
public class MiBaseDeDatos{
  DB miDataBase;
  public MiBaseDeDatos(String direccion){
    try{
 		DB miDataBase = new DB("tablaPrueba.db");//create one DB connection object
 		if(!miDataBase.connect()){//create actual connection to db
 			System.out.println("Error en db"+miDataBase.getError());
 			System.exit(0);
 		}

 		//insert statement, notice i use executeNonQuery method
 		System.out.println("executing insert");
 		System.out.println(miDataBase.executeNonQuery("INSERT INTO ipServidor ('servidor','ips') VALUES ('servidor2','127.0.0.3');"));
   	}catch(Exception e){
   						System.out.println(e.getClass());
   						System.out.println(e.getMessage());
   	}
  }

}
