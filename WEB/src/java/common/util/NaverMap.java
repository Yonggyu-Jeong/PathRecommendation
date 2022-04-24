package common.util;

import java.io.IOException;

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
public class NaverMap {
	private String ncloudSENSSMSUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";	
	private String clientID = "z61zcmt5wp";
	private String clientSecretKey = "bdfimj2GgICqHHEcNJoUpLkAm58Zu6zJn7XJpROF";
	
	@SuppressWarnings("unchecked")
	public String sendSensNcloudSMS(ABox param) {	
		String result = "";
		CloseableHttpClient httpClient = null;
		
		
		try {
			// http client 생성
			httpClient = HttpClients.createDefault();

			// post 메서드와 URL 설정
			HttpPost httpPost = new HttpPost(ncloudSENSSMSUrl);

			// agent 정보 설정
			httpPost.addHeader("accept", "application/json");
			httpPost.addHeader("X-NCP-APIGW-API-KEY-ID", clientID);
			httpPost.addHeader("X-NCP-APIGW-API-KEY", clientSecretKey);
			httpPost.addHeader("content-type", "application/json;");

			
			JSONObject msgObj = new JSONObject();
			/*
			Request Position Format
			필수 항목(경도, 위도)과 옵션 항목(이름)을 , 문자로 연결한 문자열입니다.
			옵션 항목은 항목에 맞는 prefix가 필요합니다.
			옵션 항목값은 ,, : 또는 | 문자를 포함할 수 없습니다.
			숫자가 아닌 문자는 URL 인코딩이 필요합니다.
			옵션 항목 간의 순서는 상관 없습니다.
			다음은 request position format의 사용 예입니다.

			기본 예: 127.12345,37.12345
			이름 옵션 항목을 추가한 예: 127.12345,37.12345,name=출발지이름
			*/
			//msgObj.put("start", param.getString("start"));	// Request Position Format
			
			/*
			Multiple Request Position Format
			request position format 여러 개를 : 문자로 연결한 문자열입니다.
			waypoints의 각 항목은 독립적인 경유지이므로 name을 사용한 여러 개의 request position format을 사용할 수 있습니다.
			waypoints 예 1: 127.12345,37.12345:128.12345,38.12345
			waypoints 예 2: 127.12345,37.12345:128.12345,38.12345,name=장소이름1
			waypoints 예 3: 127.12345,37.12345,name=장소이름1:128.12345,38.12345,name=장소이름2
			---
			Multiple Request Position Format list
			multiple request position format 여러 개를 | 문자로 연결한 리스트 문자열입니다. 경유지 파라미터인 waypoints가 이 형식을 사용합니다.
			경유지가 두 개이고 각 경유지에 좌표가 두 개씩 있는 경우의 예: 127.12345,37.12345:127.23456,37.23456|128.12345,38.12345:128.23456,38.23456
			*/	
			//msgObj.put("start", param.getString("start"));	// Request Position Format
			//msgObj.put("goal", param.getString("goal"));	// Multiple Request Position Format
			
			
			
			msgObj.put("goal", param.getString("127.12345,37.12345"));	// Multiple Request Position Format
			msgObj.put("waypoints", param.getString("127.12345,37.12345:128.12345,38.12345"));	// Multiple Request Position Format
			msgObj.put("waypoints", param.getString("127.12345,37.12345:127.23456,37.23456|128.12345,38.12345:128.23456,38.23456"));	// Multiple Request Position Format
			
			httpPost.setEntity(new StringEntity(msgObj.toString(), "UTF-8"));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
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
