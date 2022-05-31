package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class test {

	public static void main(String[] args) throws IOException {

		

/*
		String jsonReader = "";
		try{
			File file = new File("C:\\Users\\YongGyu\\Downloads\\file\\모범음식점현황.json");
	        FileReader file_reader = new FileReader(file);
	         int cur = 0;
	         while((cur = file_reader.read()) != -1){
	        	jsonReader += (char)cur;
	         }
	         file_reader.close();
	        }catch (FileNotFoundException e) {
	            e.getStackTrace();
	        }catch(IOException e){
	            e.getStackTrace();
	    }
*/

		ABox aBox = new ABox();
		ABox aBox2 = new ABox();
		ABox aBox3 = new ABox();

		/*
		aBox.set("category", "CS01");
		aBox2.set("lat", 37.361927530);
		aBox2.set("lng", 126.738518735);

		aBox3.set("lat", 37.5006048572);
		aBox3.set("lng", 126.7637176203);
		aBox.set("start", aBox2);
		aBox.set("goal", aBox3);
*/
//{"goal":"37.31689239364219,126.84627011464801,name=\"안산종합여객자동차터미널\"","start":"37.3627151749158,126.73882379220626,name=\"오이도역 수인분당선\"","waypoints":"37.350593833,126.7582752593,name=생금집:37.319893151,126.8229310131,name=안산패션일번가:37.3004311958,126.8258812153,name=안산호수공원"}

		
		aBox.set("accessKey", "UvtUQzC55CYXzgM25aZAzNB7J22AzWX5Tw8WLjtKJHzF6pKpqbQM4ikYbik4RawM");
		System.out.println("<<<<  "+aBox.toString());
		String jsonReader = aBox.aBoxToJsonObject().toString();
		
		URL url = new URL("http://localhost:8090/SHADOW/path");
//		URL url = new URL("http://localhost:8080/SHADOW/search/");
//		URL url = new URL("http://101.101.208.123:8080/category/list");
//		URL url = new URL("http://localhost:8090/SHADOW/find");
//		URL url = new URL("http://101.101.208.123:8080/locate/find");
//

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json;utf-8");
		con.setDoOutput(true);

		// JSON Output stream
		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonReader.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		// Response data
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println(">>>>  "+response.toString());
		}


	}
}

