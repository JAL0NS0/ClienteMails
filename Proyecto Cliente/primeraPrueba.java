import db.*;
/**
 * Ejemplo de uso de la clase DB
 */
public class primeraPrueba{
	public static void main(String[] args) {
	 try{
		DB myDb = new DB("testDb.db");//create one DB connection object
		if(!myDb.connect()){//create actual connection to db
			System.out.println("Error en db"+myDb.getError());
			System.exit(0);
		}

		//insert statement, notice i use executeNonQuery method
		System.out.println("executing insert");
		System.out.println(myDb.executeNonQuery("INSERT INTO t1 (name,last) VALUES ('juan','perez')"));
	}catch(Exception e){
						System.out.println(e.getClass());
						System.out.println(e.getMessage());
	}
}
}
