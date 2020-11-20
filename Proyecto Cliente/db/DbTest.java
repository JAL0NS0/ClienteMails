import db.*;
/**
 * Ejemplo de uso de la clase DB
 */
public class DbTest{
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
		
		//execute selectQuery using executeQuery method, I will save this query's results in 'rs1'
		System.out.println(myDb.executeQuery("SELECT * FROM t1","rs1"));

		//next method makes tuple(row) fetch, if there are no more tuples method returns false
		while(myDb.next("rs1"))
			System.out.print("id= "+myDb.getString("id","rs1")+" name= "+myDb.getString("name","rs1")+" last= "+myDb.getString("last","rs1")+"\n");
		
		System.out.println("executing update");
		System.out.println(myDb.executeNonQuery("UPDATE t1 SET last='lopez' WHERE name='juan';"));

		//execute selectQuery using executeQuery method, I will save this query's results in 'rs2'
		System.out.println(myDb.executeQuery("SELECT * FROM t1","rs2"));

		// next method makes tuple fetch, if there are no more tuples method returns false
		while(myDb.next("rs2"))
			System.out.print("id= "+myDb.getString("id","rs2")+" name= "+myDb.getString("name","rs2")+" last= "+myDb.getString("last","rs2")+"\n");
		
		System.out.println("executing delete");
		System.out.println(myDb.executeNonQuery("DELETE FROM t1 WHERE name='juan'"));
		
		// save results from query on 'rs3'
		myDb.executeQuery("SELECT * FROM t1","rs3"); 
		while(myDb.next("rs3"))
			System.out.print("id= "+myDb.getString("id","rs3")+" name= "+myDb.getString("name","rs3")+" last= "+myDb.getString("last","rs3")+"\n");
			
		myDb.close(); //close connection
	 }catch(Exception e){
             System.out.println(e.getClass());
             System.out.println(e.getMessage());}
	}	
}
