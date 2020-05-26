import java.io.File;
import java.io.FileNotFoundException;
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
		 
		 public void add(String key, int value) {
			 int nodecounter=0;
			 
			 int bucketIndex = getBucketIndex(key);
			 
			 HashNode head = bucketArray.get(bucketIndex);
			 
			 while (head != null) { 	 
				   nodecounter++;
				   if(nodecounter>17) {
					   break;
				   }
				   head = head.next; 
			   }
			 if(nodecounter>17) {
				   HashNode newhead = bucketArray.get(bucketArray.size()-1);
				   HashNode newOverflowNode = new HashNode(key, value); 
				   newOverflowNode.next=newhead;
				   bucketArray.set(bucketArray.size()-1, newOverflowNode);
				   
			   } else {
				   head = bucketArray.get(bucketIndex); 
				   HashNode newNode = new HashNode(key, value); 
				   newNode.next = head; 
				   bucketArray.set(bucketIndex, newNode); 	   
			   }       
			 }
		 
		 public void hashFunction(int pageSize) {
			 File heapFile = new File("heap." + pageSize);
			 
			 trailingWhiteSpace = pageSize - (11 * RECORD_SIZE);
			 
			 boolean isNextPage = true;
			 try {
				   FileInputStream fis = new FileInputStream(heapFile);
				         
				   //run loop until there is no page left
				   while(isNextPage) {
					   byte[] page = new byte[pageSize];
					   fis.read(page, 0, pageSize);
					   String pageCheck = new String(page);
					   if(pageCheck.trim().length() == 0) {
						   isNextPage = false;
						   break;
					   }
					   for(int i=0; i<page.length; i+=RECORD_SIZE) {
						   if(i == Math.floor(pageSize/RECORD_SIZE)*RECORD_SIZE) {
							   position+=trailingWhiteSpace;
							   break;
						   } else {
							   position+=RECORD_SIZE;
						   }
						   byte[] bName = new byte[BUILDINGNAME_SIZE];
						   System.arraycopy(page, i+BNAME_OFFSET, bName, 0, 65);
						   String name = new String(bName).trim();
						   
						   if(!(name.equalsIgnoreCase("null")) && !(name.equalsIgnoreCase(""))) {
							   add(name, position);
						   }
							   
					   }
				   }
				   fis.close();

				   System.out.println("Completed");

			   } catch (FileNotFoundException e) {
				   e.printStackTrace();
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
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
