//Firstly decide on how to make HTTP(S)requests
/*
Can use:
1.HTTPURLConnection
2.Apache HTTP Client 
3. Java 11 HTTP/2 HTTP Client supports earlier HTTP/1.1 Too

Based off what I read option number 3 is common and recommended while option 2 is reasonable but older 

Language: decided on Java since test is for Java position
*/

//Next import Libraries for Java
import java.util.*; 
import java.lang.Math; 
import java.io.*;
import java.net.URI;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//Libraries imported

//Next to structure request method

//put bulk of work in main
//while loop is good

public static void main(String[] args){
MRequest m_request = new MRequest();
 try{
  System.out.println ("\nType in how many records would you like to see?");
		Scanner scan = new Scanner (System.in);
		while (!scan.hasNextInt()){
	        scan.nextLine(); //clears any wrong input before prompting again
	        System.out.print("Please enter a valid number (no fractions/decimals, text or negative numbers)");
	    }//while
		int numRecords = scan.nextInt();
		scan.close();
               int i=0;
		 //For latitude (1)
		 LinkedHashMap<String, Object> lmap1 = new LinkedHashMap<String, Object>();
		   //The declaration above was Hashmap not LinkedHashmap
		   ObjectMapper mapper1 = new ObjectMapper(); 
		  //For longitude
		   LinkedHashMap<String, Object> lmap2 = new LinkedHashMap<String, Object>();
		   ObjectMapper mapper2 = new ObjectMapper();
		   
       ArrayList<Double>li1= new ArrayList<Double>();
       ArrayList<Double>li2= new ArrayList<Double>();
       ArrayList <Double> resultD= new ArrayList<Double>();
       BufferedWriter writer1 = new BufferedWriter(new FileWriter("/home/mbans8a1/Final_Work/Test_Folder_Sean/People_living_in_London.txt"));
       BufferedWriter writer2 = new BufferedWriter(new FileWriter("/home/mbans8a1/Final_Work/Test_Folder_Sean/People_within_50_miles_of_London.txt"));
  
  int i=0;
  
  
  while(i<numRecords){
  //numRecords ->get user input (maybe Scanner)
  HttpRequest req1 = HttpRequest.newBuilder().uri(URI.create("https://bpdts-test-app.herokuapp.com/user/"+Integer.toString(i+1)))
		  .version(HttpClient.Version.HTTP_2)
		  .header("Accept","application/json")
		  .timeout(Duration.ofSeconds(10))
		  .GET()
		  .setHeader("User-Agent", "Java 11 HttpClient Bot")
		  .build(); 
          //.timeout will time the request out after 10 seconds and in case of internet issues
		  //create a proxy to use the requester to handle the response
		  HttpResponse<String> resp1 = HttpClient.newBuilder()
		  .proxy(ProxySelector.getDefault())
		  .followRedirects(HttpClient.Redirect.ALWAYS)
		  .build()
		  .send(req1,HttpResponse.BodyHandlers.ofString());
	  
	  //System.out.println("Status Code:"+resp1.statusCode());
		  //System.out.println("Response Body:"+resp1.body());
		  //convert the JSON body to a map
		   HashMap<String, Object> map = new HashMap<String, Object>();
		   ObjectMapper mapper = new ObjectMapper();
		   map = mapper.readValue(resp1.body(), new TypeReference<Map<String, Object>>(){});
		   //Type reference was Map
		   for(String s : map.keySet()) {
			   if(map.containsValue("London")) {
				   System.out.println(s+":" + map.get(s));
				   writer1.write(s+":" + map.get(s));
				   writer1.write("\n\n");
			   }//for
		  }//for-each loop
		   lmap1 = mapper1.readValue(resp1.body(), new TypeReference<LinkedHashMap<String, Object>>(){});
		   lmap2 = mapper2.readValue(resp1.body(), new TypeReference<LinkedHashMap<String, Object>>(){});
		    li1.add(Double.valueOf((lmap1.get("latitude")).toString()));
		    li2.add(Double.valueOf((lmap2.get("longitude")).toString()));
		            
			 LinkedHashMap<String, Object> lmap3 = new LinkedHashMap<String, Object>();
				   ObjectMapper mapper3 = new ObjectMapper();
				   lmap3 = mapper3.readValue(resp1.body(), new TypeReference<LinkedHashMap<String, Object>>(){});
				   resultD =m_request.Haversine(li2, li1,51.507322,-0.127647);
				   Iterator<Double> itr1 =resultD.iterator();
				   /*London Coordinates
				    *longitude:-0.127647
				    *latitude:51.507322
				    *These will be parameters in Haversine 
				    *Function(the call to it you see above)
				    */
				   while(itr1.hasNext()) {
				    	if(itr1.next()<50.0) {
				    		for(Map.Entry<String,Object> entry:lmap3.entrySet()){
								   
				    			if(entry.getValue()!="London") {
								 System.out.println(entry.getKey()+":" +entry.getValue());
								 writer2.write(entry.getKey()+":" +entry.getValue());
								 writer2.write("\n\n");
				    			}
							}//inner for-each
				    		System.out.println("\n");
				    		
				    	 }//if
				    	
				    }//while
				    
		  //counter to move to the next record	   
		  i = i+1;
    
  }//while
  writer1.close();
  writer2.close();
	   System.out.println("Arraylist Latitudes "+li1);
	   System.out.println("Arraylist Longitudes "+li2);
	  }//try
	 catch(ArrayIndexOutOfBoundsException e) {
		 System.out.println(e);
	 }
	 catch(Exception e){
	 e.printStackTrace();
	}//catch
}//main

//Haversine Formula for getting distance between two locations (points) using the Earth's shortest circular distance in km
//Return an Array List of distances
/*London Coordinates
				    *longitude:-0.127647
				    *latitude:51.507322
				    *These will be parameters in Haversine 
				    *Function(the call to it you see above)
				    */
public ArrayList<Double> Haversine(ArrayList<Double> lng, ArrayList<Double> lat, double lonLat, double lonLong) {
	  final double R = 6372.8; //in kilometres
	  double latitudeLonR = Math.toRadians(lonLat);
	  ArrayList<Double> lngDiff= new ArrayList<Double>();
	  ArrayList<Double> latDiff= new ArrayList<Double>();
	  ArrayList<Double> latconvertRad= new ArrayList<Double>();
	  ArrayList<Double> aParam= new ArrayList<Double>();
	  ArrayList<Double> cParam= new ArrayList<Double>();
	  ArrayList<Double> finalDKilom= new ArrayList<Double>();  
	  for(int i=0; i<lng.size(); i++) {
		  lngDiff.add(i, Math.toRadians(lng.get(i)-lonLong));
		  latDiff.add(i, Math.toRadians(lat.get(i)-lonLat));
		  latconvertRad.add(i, Math.toRadians(lat.get(i)));
		  aParam.add(i,Math.pow(Math.sin(latDiff.get(i) / 2),2) + Math.pow(Math.sin(lngDiff.get(i) / 2),2) * Math.cos(latitudeLonR) * Math.cos(latconvertRad.get(i)));
		  cParam.add(i, 2 * Math.atan2(Math.sqrt(aParam.get(i)), Math.sqrt(1-aParam.get(i))));
		  finalDKilom.add(i,(((R*cParam.get(i))*4.97097)/8.0));//answers in miles
		  
	  }//for
	      return finalDKilom;
  }//Haversine
