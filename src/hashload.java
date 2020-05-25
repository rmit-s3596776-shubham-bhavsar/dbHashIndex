import java.io.FileWriter;
import java.io.IOException;

public class hashload {

	static final int RECORD_SIZE = 368;
	   static final int BUILDINGNAME_SIZE = 65;
	   static final int BNAME_OFFSET = 16; 
	   static final String NODE_DELIMITER= ":::";
	   static final String BUCKET_DELIMITER = "#";
	   int position=-368;
	   static int currentPositionInHeap = 0; 
	   static boolean lastRecordFlag = false;  
	   static int trailingWhiteSpace;
	
	   int hcounter=0;
	   
	     private ArrayList<HashNode> bucketArray; 
		
		 private int numBuckets; 
	   
		 public hashload() { 
			   bucketArray = new ArrayList<>(); 
			   
			   numBuckets = 19000; 	 
				
			  
			   for(int i = 0; i < numBuckets; i++) 
				   bucketArray.add(null); 
		   } 
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
