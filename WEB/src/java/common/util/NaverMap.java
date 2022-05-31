package common.util;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import common.collection.ABox;

@Component
public class NaverMap {
	//private String ncloudUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?";	
	private String ncloudUrl = "https://naveropenapi.apigw-pub.fin-ntruss.com/map-direction/v1/driving?";
	private String clientID = "z61zcmt5wp";
	private String clientSecretKey = "bdfimj2GgICqHHEcNJoUpLkAm58Zu6zJn7XJpROF";
	
	@SuppressWarnings("unchecked")
	public String sendNaverMap(ABox param) {	
		String result = "";
		CloseableHttpClient httpClient = null;
		
		try {
			// http client 생성
			httpClient = HttpClients.createDefault();

			ncloudUrl += "start="+param.getString("start");
			ncloudUrl += "&goal="+param.getString("goal");
			if(param.containsKey("waypoints")) {
				ncloudUrl += "&waypoints="+param.getString("waypoints");
			}
			if(param.containsKey("option")) {
				ncloudUrl += "&option="+param.getString("option");
			}
			HttpGet httpGet = new HttpGet(ncloudUrl);

			// agent 정보 설정
			httpGet.addHeader("X-NCP-APIGW-API-KEY-ID", clientID);
			httpGet.addHeader("X-NCP-APIGW-API-KEY", clientSecretKey);

			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			result = json;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
