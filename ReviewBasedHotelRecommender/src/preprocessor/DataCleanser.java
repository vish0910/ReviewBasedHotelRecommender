package preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataCleanser {

//	public static void main(String args[]){
//		tripAdvisor();
//	}
	static void tripAdvisor(){
		String review;
		String reviewFile= "input";
		System.out.println("CorpusWebis: "+reviewFile);
		try{
			BufferedReader br;
			BufferedWriter bw;
			//Reading List of Files
			CharSequence cs = ".dat";
		    ArrayList<String> listOfFiles = new ArrayList<String>();
		    HashMap<String, Integer> hmLocation = new HashMap<String, Integer>();
		    Files.walk(Paths.get("input")).forEach(filePath -> {
		        if (Files.isRegularFile(filePath) && filePath.toString().contains(cs)) {
		            System.out.println(filePath);    
		            listOfFiles.add(filePath.toString());
		        }
		    });
		    
		    for(String fileName: listOfFiles){
		    	System.out.println("File being processed: "+ fileName);
		    	br = new BufferedReader(new FileReader(fileName));
		    	String outputFile = fileName.substring(6, fileName.length()-4);
		    	bw = new BufferedWriter(new FileWriter("output/"+outputFile+".txt"));
		    	String hotelId = outputFile.split("_")[1];
		    	br.readLine();// Overall Rating
		    	String line = br.readLine(); //Avg price
		    	System.out.println(line);
		    	int price= Integer.parseInt(line.split("\\$")[1]);
		    	line=br.readLine();
		    	String [] url = line.split("\\-");
		    	String hotelName = url[4];
		    	System.out.println(hotelName);
		    	String location = url[5].split("\\.")[0]; //html truncated 
		    	Integer locCount = hmLocation.get(location);
		    	if(locCount!=null){
		    		locCount++;
		    		hmLocation.put(location, locCount);
		    	}
		    	else{
		    		hmLocation.put(location, 1);
		    	}
		    	System.out.println(location);
		    	line = br.readLine();
//		    	bw.write("hello");
		    	String author = "";
		    	String content = "";
		    	while(line != null){
		    			if(line.contains("<Author>")){
		    				author= line.split("<Author>")[1];
		    			}
		    			else if(line.contains("<Content>")){
		    				content = line.split("<Content>")[1];
		    				bw.write(hotelId+"|"+hotelName+"|"+location+"|"+price+"|"+author+"|"+content+"\n");
		    			}
		    			line = br.readLine();
		    	}
		    	bw.close();
		    	br.close();
		    }
			
			
			
			
			
			
//			
//			Object obj = jparser.parse(new FileReader("res/"+reviewFile));
//			PrintWriter pr = new PrintWriter("out/"+reviewFile);
//			
//			//Type casting Object
//			JSONObject jsonobject =(JSONObject) obj;
//			review = (String) jsonobject.get("text");
//			
//			pr.println(review);
////			JSONObject json2 = (JSONObject)jsonobject.get("attributes");
////			JSONObject json3 = (JSONObject)json2.get("de.webis.attribute.Author");
////			String author_name= (String) json3.get("name");
//
//			String author_name= (String) ((JSONObject) ((JSONObject) jsonobject.get("attributes")).get("de.webis.attribute.Author")).get("name");
//			pr.println("|"+author_name);
//			pr.close();
		    //printing the hashmap
		    ArrayList<String> keys = new ArrayList<String>(hmLocation.keySet());
		    for(String key: keys){
		    	System.out.println(key+":"+hmLocation.get(key));
		    }
		}
		catch(Exception e){
			System.out.println("Exception"+e);
		}
	
	}
}
