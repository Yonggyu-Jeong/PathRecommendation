package common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import common.collection.ABox;

@Component
public class weather {
	
	private String Url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=ylrtQRwyrNEOiDeYbjmIuc66x%2BQM12FUCWgpRESzX2woU1MlnU%2FvMAu0vN70zHWXtNFNAvcQdOJibf2Ed%2FbqQQ%3D%3D&numOfRows=10&pageNo=1&base_date=20220423&base_time=0830&nx=55&ny=127";
	
	@SuppressWarnings("unchecked")
	public String weather() {	
		String result = "";		
	    BufferedReader in = null;
		try {
			   URL obj = new URL(Url); 
	           HttpURLConnection con = (HttpURLConnection)obj.openConnection();
	 
	            con.setRequestMethod("GET");
	 
	            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

	            while((result = in.readLine()) != null) {
	            	
	            }
	            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
        }

		return result;
	}
}
