import java.io.FileWriter;
import java.io.IOException;

public class hashload {
public static void main(String[] args) throws IOException { 
	int pageSize=0;
	
	try{
		pageSize = Integer.parseInt(args[0]);
	   } catch(NumberFormatException e){
		   pageSize = 4096;
		   System.out.println("Invalid page size given. Set to default value of 4096");
	   }
	 hashload map = new hashload();
	 long startTime = System.currentTimeMillis();
	  // map.hashFunction(pageSize);
	 
	 FileWriter myWriter = new FileWriter("hash."+pageSize);

	 myWriter.close();
}
}
