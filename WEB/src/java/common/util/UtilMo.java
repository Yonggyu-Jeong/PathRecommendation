package common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import common.collection.ABox;
import common.collection.ABoxList;

/**
 * <pre>
 * 	UtilMo 공통 모듈 Class
 * </pre>
 */
public class UtilMo {

	/**
	 * <pre>
	 * 정해진 길이만큼 랜덤 숫자 생성 메서드
	 * </pre>
	 */
	public static String getRandomInt(int randomNumber) {
		Random random = new Random();
		String result = "";
		
		for (int i=0; i<randomNumber; i++) {
			result += random.nextInt(10);
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * 대문자, 소문자, 숫자 포함하여 정해진 길이만큼 랜덤 문자열 생성 메서드
	 * </pre>
	 */
	public static String getRandomString(int randomNumber) {
		Random random = new Random();
		String result = "";
		
		for (int i=0; i<randomNumber; i++) {
			int choice = random.nextInt(3);
			
			// 대문자
			if (choice == 0) {
				char bch = (char) (random.nextInt(26) + 65);
				result += Character.toString(bch);
				
			// 소문자
			} else if (choice == 1) {
				char sch = (char) (random.nextInt(26) + 97);
				result += Character.toString(sch);
				
			// 숫자
			} else {
				result += random.nextInt(10);
			}
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * 대문자, 숫자 포함하여 정해진 길이만큼 랜덤 문자열 생성 메서드
	 * </pre>
	 */
	public static String getRandomUpperString(int randomNumber) {
		Random random = new Random();
		String result = "";
		
		for (int i = 0; i < randomNumber; i++) {
			int choice = random.nextInt(2);
			
			// 대문자
			if (choice == 0) {
				char bch = (char) (random.nextInt(26) + 65);
				result += Character.toString(bch);
				
			}
			// 숫자
			else {
				result += random.nextInt(10);
			}
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * 영문 숫자 포함 난수 발생후 유일한 Token값 생성 메소드
	 * </pre>
	 * 
	 * @param size
	 *            발생시킬 난수의 개수
	 * @return 입력받은 개수만큼 난수를 통해 생성된 숫자와 영문의 조합값
	 */
	public static String getToken(int randomNumber) {
		Random rand = new Random();
		String token = "";

		int i = 50;
		while (i-- > 0) {
			char ch = (char) (rand.nextInt(26) + 97);
			int in = rand.nextInt(9);
			token += Character.toString(ch) + in;
		}
		return token.substring(0, randomNumber);
	}

	/**
	 * <pre>
	 * String을 ABox로 변환하는 메서드
	 * </pre>
	 * 
	 * @param ABoxParam
	 *            : ABox로 변환하고 싶은 String ex) key[value] : userId[test], userEmail[test@test.com], ...
	 * @return String을 변환한 ABox 객체
	 */
	public static ABox convertABoxToString(String ABoxParam) {
		ABox result = new ABox();
		String[] convertArr = ABoxParam.split(",");

		for (String convertStr : convertArr) {
			int locationIdx = convertStr.indexOf("[");

			result.set(convertStr.substring(0, locationIdx), convertStr.substring(locationIdx + 1, convertStr.length() - 1));
		}

		return result;
	}

	/**
	 * <pre>
	 *   유니코드 파일을 특수문자로 맵핑함.
	 * </pre>
	 * 
	 * @param str
	 *            : unicode 문자열
	 * @return 특수문자 맵핑 문자열
	 */
	public static String mappingUnicode(String str) {

		// 파일명에 사용되는 특수문자
		char[] ch = { '~', '!', '@', '#', '$', '%', '&', '(', ')', '=', ';', '[', ']', '{', '}', '^', '-' };
		try {
			for (char c : ch) {
				String encodeData = URLEncoder.encode(c + "", "UTF-8");
				str = str.replaceAll(encodeData, "\\" + c);
			}
			str = str.replaceAll("%2B", "+"); // 띄워쓰기 의 경우 치환함
			str = str.replaceAll("%2C", "_"); // 콤마의 경우 언더바로 치환함
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * <pre>
	 *   ABox를 Html에서 사용할 JSON String으로 변환
	 * </pre>
	 * 
	 * @param box
	 *            : 변환할 ABox
	 * @return Json String
	 */
	public static String boxToJsonStringForHtml(ABox box) {
		return new JSONObject(box).toString().replaceAll("'", "&#39;").replaceAll("\"", "&#34;");
	}
	
	/**
	 * <pre>
	 *   ABoxList를 Html에서 사용할 JSON String으로 변환
	 * </pre>
	 * 
	 * @param boxList
	 *            : 변환할 ABoxList
	 * @return Json String
	 */
	public static String boxToJsonStringForHtml(ABoxList<ABox> boxList) {
		return new JSONArray(boxList).toString().replaceAll("'", "&#39;").replaceAll("\"", "&#34;");
	}
}
