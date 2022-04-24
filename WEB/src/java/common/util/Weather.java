package common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.springframework.stereotype.Component;
import common.collection.ABox;

@Component
public class Weather {
		
	@SuppressWarnings("unchecked")
	public ABox getWeather() throws IOException {	
		String xml = null;
		StringBuilder sBuffer = new StringBuilder();
        ABox aBox = new ABox();
        
        try {
            String urlAddr = "http://www.kma.go.kr/weather/forecast/mid-term-xml.jsp?stnId=109";
            URL url = new URL(urlAddr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    while (true) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        sBuffer.append(line);
                    }
                    br.close();
                    conn.disconnect();
                    xml = sBuffer.toString();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
 
        }
        try {
            if (xml != null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
 
                InputStream is = new ByteArrayInputStream(xml.getBytes());
                Document doc = documentBuilder.parse(is);
                Element element = doc.getDocumentElement();
                
                NodeList items1 = element.getElementsByTagName("wf");
                for (int i = 1; i < 2; i++) {
                    Node node = items1.item(i);
                    Node temp = node.getFirstChild();
                    String value = temp.getNodeValue();
                    aBox.set("wf", value);
                }
                
                NodeList items2 = element.getElementsByTagName("tmn");
                for (int i = 0; i < 1; i++) {
                    Node node = items2.item(i);
                    Node temp = node.getFirstChild();
                    String value = temp.getNodeValue();
                    aBox.set("tmn", value);
                }
                
                NodeList items3 = element.getElementsByTagName("tmx");
                for (int i = 0; i < 1; i++) {
                    Node node = items3.item(i);
                    Node temp = node.getFirstChild();
                    String value = temp.getNodeValue();
                    aBox.set("tmx", value);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } 
		return aBox;
	}
}
