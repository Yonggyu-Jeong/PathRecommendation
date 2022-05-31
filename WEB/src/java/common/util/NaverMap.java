package common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Component;
import common.collection.ABox;

@Component
public class NaverMap {
	// private String ncloudUrl =
	// "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?";
	//private String ncloudUrl = "https://naveropenapi.apigw-pub.fin-ntruss.com/map-direction/v1/driving";
	private String ncloudUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
	private String clientID = "z61zcmt5wp";
	private String clientSecretKey = "O89vy2cYe04XasNZssFfJspMZqfUzW7qaZ9MwYch";

	public String sendNaverMap(ABox param) throws IOException {
		String result = "";
		HttpURLConnection con = null;
		try {
			// http client 생성

			ncloudUrl += "?start=" + param.getString("start");
			ncloudUrl += "&goal=" + param.getString("goal");
			if (param.containsKey("waypoints")) {
				ncloudUrl += "&waypoints=" + param.getString("waypoints");
			}
			if (param.containsKey("option")) {
				ncloudUrl += "&option=" + param.getString("option");
			}
			ncloudUrl = ncloudUrl.replaceAll(" ", "");
			// ncloudUrl = URLEncoder.encode(ncloudUrl, "UTF-8");

			URL url = new URL(ncloudUrl);
			con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientID);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecretKey);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			result = response.toString();
			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return result;
	}
}
