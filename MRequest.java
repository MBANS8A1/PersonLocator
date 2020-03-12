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
//Libraries imported

//Next to structure request method

//put bulk of work in main
//while loop is good

public static void main(String[] args){
MRequest m_request = new MRequest();
  System.out.println ("\nType in how many records would you like to see?");
		Scanner scan = new Scanner (System.in);
		while (!scan.hasNextInt()){
	        scan.nextLine(); //clears any wrong input before prompting again
	        System.out.print("Please enter a valid number (no fractions/decimals, text or negative numbers)");
	    }//while
		int numRecords = scan.nextInt();
		scan.close();
  
  
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
    
  }//while

}//main

//Haversine Formula for getting distance between two locations (points) using the Earth's shortest circular distance in km
//Return an Array List of distances

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
