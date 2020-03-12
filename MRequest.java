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
