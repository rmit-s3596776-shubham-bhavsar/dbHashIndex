import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Building a hash index file using separate chaining
 * 
 * A node of key value pair and next pointer is used to store the building name and offset
 * 
 * If collision occurs, the node will be added to end of the existing node
 * 
 * If overflow of bucket occurs, the element will be added to the last bucket of infinite size called overflow bucket
 * 
 * An array list of linked lists will be used to build the hash table
 * 
 * 19000 buckets with bucket size of 17 will be used
 * 
 */


//Node for our linked list
class HashNode 
{ 
	//Building name
	String key; 
	//offset
	int value; 

	// next pointer 
	HashNode next; 

	public HashNode(String key, int value) 
	{ 
		this.key = key; 
		this.value = value; 
	} 
} 

public class hashload {
	   static final int RECORD_SIZE = 368;
	   static final int BUILDINGNAME_SIZE = 65;
	   static final int BNAME_OFFSET = 16; // to handle the business name in front of each record
	   static final String NODE_DELIMITER= "///";
	   static final String BUCKET_DELIMITER = "*";
	   int position=-368;
	   static int currentPositionInHeap = 0; // where the current pointer in the heap file is
	   static boolean lastRecordFlag = false; // To flag when its the last record in the 
	   static int trailingWhiteSpace; // used to store the trailing white space calculated
	   static int numOfRecords=0;

	   int hcounter=0;
	   
	   // Array list to store our buckets
	   private ArrayList<HashNode> bucketArray; 

		 
	   //number of buckets in the hash table 
	   private int numBuckets; 
 

	   // initializing array list with null values
	   public hashload() { 
		   bucketArray = new ArrayList<>(); 
		   
		   numBuckets = 19000; 	 
			
		   // Create empty chains 
		   for(int i = 0; i < numBuckets; i++) 
			   bucketArray.add(null); 
	   } 
				 
	   // function to get the hash index using hash code and modulus value of number of buckets 
	   private int getBucketIndex(String key) 
	   { 
		   int hashCode = key.hashCode(); 
		   int index = Math.abs(hashCode) % numBuckets; 
		  
		   return index; 
	   } 
 
	   public void add(String key, int value) { 
		   int nodecounter=0;
		   //get hash index
		   int bucketIndex = getBucketIndex(key);
		   //get head using hash index
		   HashNode head = bucketArray.get(bucketIndex); 
		  
		   //get size of bucket
		   while (head != null) { 	 
			   nodecounter++;
			   if(nodecounter>17) {
				   break;
			   }
			   head = head.next; 
		   } 
			  
		   //if more than 17 nodes, add node to overflow bucket
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
	   
	   
	   //function to add element to the hashtable from the heap file	 
	   public void hashFunction(int pageSize) {
			      
		   File heapFile = new File("heap." + pageSize);
		   
		   // Used to store the empty space at the end of each page.
		   trailingWhiteSpace = pageSize - (numOfRecords * RECORD_SIZE);
			      
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

	   public static void main(String[] args) throws IOException 
	   { 
		   int pageSize=0;
		   try{
			   //get the pagesize from input
			   pageSize = Integer.parseInt(args[0]);
		   } catch(NumberFormatException e){
			   pageSize = 4096;
			   System.out.println("Invalid page size given. Set to default value of 4096");
		   }

		   hashload map = new hashload();

		   long startTime = System.currentTimeMillis();
		   numOfRecords=(int) Math.floor(pageSize/RECORD_SIZE);
		   map.hashFunction(pageSize);
		   
		   //writing each node in each linked list of the array list to a file
		   FileWriter myWriter = new FileWriter("hash."+pageSize);

		   for(int i=0;i<map.bucketArray.size();i++) {
			   HashNode head = map.bucketArray.get(i);

			   while(head!=null) {
				   //using delimiters for parsing purposes
				   myWriter.write(head.key+NODE_DELIMITER+head.value);
				   myWriter.write("\n");
				   head=head.next;
			   }
			   myWriter.write(BUCKET_DELIMITER);
			   myWriter.write("\n");

		   }

		   myWriter.close();

		   long stopTime = System.currentTimeMillis();

		   System.out.println(stopTime - startTime + " ms");
		   System.out.println((stopTime - startTime) * 0.001 + " sec");

		  
	   } 

}
