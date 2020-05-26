import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.corba.se.impl.ior.ByteBuffer;

class Node 
{ 
	String key; 
	int value; 

	Node next; 

	public Node(String key, int value) 
	{ 
		this.key = key; 
		this.value = value; 
	} 
} 

public class hashquery {
	public static final int RECORD_SIZE = 368; 
	static final String BUCKET_DELIMITER = "*";
	static final String NODE_DELIMITER= "///";

	ArrayList<Node> bucketArray = new ArrayList<Node>();
	public int numBuckets=0;
	
	
	
	public static void main(String[] args) throws IOException {

		hashquery ri = new hashquery();
		long startTime = System.currentTimeMillis();
		String pageSize = args[args.length - 1];
		ri.readFile(pageSize);
		
		//extracting building name from argument
		String buildingName=ri.getBuildingName(args);
		
		//loading heap file
		File heapFile = new File("heap."+pageSize);
		
		//search for records
		ri.searchForInput(buildingName,ri.get(buildingName), heapFile);
		
		//timer
		long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime + " ms");
        System.out.println((stopTime - startTime) * 0.001 + " sec");
	
		
	}
	public void searchForInput(String name,ArrayList<Integer> list, File heap) throws IOException {
	
		RandomAccessFile heapRaf = new RandomAccessFile(heap, "r");
		byte[] record = new byte[368];
		
		for(int i:list) {
			heapRaf.seek(i); 
		    heapRaf.read(record, 0, 368);
		   
		    
		    byte[] bname = Arrays.copyOfRange(record, 16, 65);
		    
		    String nameCheck = new String(bname).trim();
		    
if (nameCheck.equals(name)) {
	        	
	        	byte[] census_year = Arrays.copyOfRange(record, 0, 4);
	            byte[] block_id = Arrays.copyOfRange(record, 4, 8);
	            byte[] property_id = Arrays.copyOfRange(record, 8, 12);
	            byte[] base_property_id = Arrays.copyOfRange(record, 12, 16);
	            byte[] building_name = Arrays.copyOfRange(record, 16, 81);
	            byte[] street_address = Arrays.copyOfRange(record, 81, 116);
	            byte[] clue_small_area = Arrays.copyOfRange(record, 116, 146);
	            byte[] construction_year = Arrays.copyOfRange(record, 146, 150);
	            byte[] refurbished_year = Arrays.copyOfRange(record, 150, 154);
	            byte[] number_of_floors = Arrays.copyOfRange(record, 154, 158);
	            byte[] predominant_space_use = Arrays.copyOfRange(record, 158, 198);
	            byte[] accessibility_type = Arrays.copyOfRange(record, 198, 233);
	            byte[] accessibility_type_description = Arrays.copyOfRange(record, 233, 318);
	            byte[] accessibility_rating = Arrays.copyOfRange(record, 318, 322);
	            byte[] bicycle_spaces = Arrays.copyOfRange(record, 322, 326);
	            byte[] has_showers = Arrays.copyOfRange(record, 326, 330);
	            byte[] x_coordinate = Arrays.copyOfRange(record, 330, 334);
	            byte[] y_coordinate = Arrays.copyOfRange(record, 334, 338);
	            byte[] location = Arrays.copyOfRange(record, 338, 368);
	            
	            int Scensus_year = ByteBuffer.wrap(census_year).getInt();
	            String Sblock_id = new String(block_id);
	            int Sproperty_id = ByteBuffer.wrap(property_id).getInt();
	            String Sbase_property_id = new String(base_property_id);
	            String Sbuilding_name = new String(building_name);
	            String Sstreet_address = new String(street_address);
	            String Sclue_small_area = new String(clue_small_area);
	            String Sconstruction_year = new String(construction_year);
	            String Srefurbished_year = new String(refurbished_year);
	            String Snumber_of_floors = new String(number_of_floors);
	            String Spredominant_space_use = new String(predominant_space_use);
	            String Saccessibility_type = new String(accessibility_type);
	            String Saccessibility_type_description = new String(accessibility_type_description);
	            String Saccessibility_rating = new String(accessibility_rating);
	            String Sbicycle_spaces = new String(bicycle_spaces);
	            String Shas_showers = new String(has_showers);
	            String Sx_coordinate = new String(x_coordinate);
	            String Sy_coordinate = new String(y_coordinate);
	            String Slocation =new String(location);
	            
	            System.out.println("Census year: "+Scensus_year);
	            System.out.println("Property ID: "+Sproperty_id);
	            System.out.println("Building name: "+Sbuilding_name);
	            System.out.println("Small area: "+Sclue_small_area);
	            System.out.println("Address: "+Sstreet_address);
	            
	        	}
	        System.out.println("------------------------------");
	       }
	}
	
public void readFile(String pageSize) throws NumberFormatException, IOException {
		
		File file=new File("hash."+pageSize);    // new file instance  
		FileReader fr=new FileReader(file);   //read file  
		BufferedReader br=new BufferedReader(fr);  //buffer character input stream  
		
		String line;  
		String name="";
		int offset=0;
		
		bucketArray.add(null);
		
		//loop until reaching end of file
		while((line=br.readLine())!=null)  
		{  
			//increment number of buckets and add a null node to the array list when bucket delimiter is encountered
			if(line.equalsIgnoreCase(BUCKET_DELIMITER)) {
				bucketArray.add(null);
				numBuckets++;
			}
			//parse the node and add it to existing bucket/overflow bucket
			else {
				name=line.split(NODE_DELIMITER)[0];
				offset=Integer.parseInt(line.split(NODE_DELIMITER)[1]);
				//System.out.println(offset);
				Node head = this.bucketArray.get(numBuckets);
				Node newNode = new Node(name,offset);
				newNode.next=head;
				bucketArray.set(numBuckets, newNode);
				
			} 

		}  
		fr.close();    //closes the stream and release the resources  
		
	}
	
	}

	
