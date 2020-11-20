El paquete de base de datos le ayudara a poder ejecutar consultas de forma mas facil
para compilar este paquete debe ejecutar la siguiente linea dado que es un paquete de java

javac db/DB.java

y en su codigo recuerde que debe importarlo consultas

import db.*;

El archivo DbTest.java lo debe compilar luego de haber compilado el paquete de base de datos 
y para ejecutarlo usted debe agregar al classpath el .jar de la siguiente forma

java -cp sqlitejdbc.jar:. DbTest

De igual forma si usted crea un nuevo programa y quiere utilizar este paquete debe colocar la bandera
-cp sqlitejdbc.jar:. al momento de ejecutar su clase.

