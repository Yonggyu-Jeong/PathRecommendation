package common.collection;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * <pre>
 * HashMap을 상속받아 구현한 ABox 클래스
 * </pre>
 */
@Component
@Qualifier("aBox")
public class ABox extends HashMap<String, Object> {

	/**
	 * SBox seraiID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <pre>
	 * ABox 생성자
	 * </pre>
	 */
	public ABox() {

	}

	/**
	 * <pre>
	 * Map 자료형을 ABox에 Setting 하는 메소드
	 * </pre>
	 * 
	 * @param <E>
	 * @param map
	 */
	public <E> ABox(Map<String, Object> map) {
		if (map != null) {
			Iterator<String> mapIt = map.keySet().iterator();

			while (mapIt.hasNext()) {
				String key = (String) mapIt.next();

				this.set(key, map.get(key));
			}
		}
	}

	/**
	 * <pre>
	 * 입력받은 key, Value 값을 ABox에 셋팅하는 메소드
	 * </pre>
	 * 
	 * @param <E>
	 * @param key
	 *            입력받은 key
	 * @param obj
	 *            입력받은 Object
	 */
	public <E> ABox set(String key, E obj) {
		super.put(key, obj);
		return this;
	}

	/**
	 * <pre>
	 * 입력받은 key, Value 값을 ABox에 셋팅하는 메소드
	 * 
	 * </pre>
	 * 
	 * @param strArray
	 *            : 입력형태 : key:value,key:value...
	 */
	public <E> ABox set(String strArray) {
		String[] splData = strArray.trim().split("\\,");
		for (String data : splData) {
			String[] keyValue = data.trim().split("\\:");
			super.put(keyValue[0].trim(), keyValue[1].trim());
		}
		return this;
	}

	/**
	 * <pre>
	 * JSON 형식의 Data를 SBox 형태로 출력함.
	 * </pre>
	 * 
	 * @param jsonData
	 *            : json Object 형태의 String Data
	 */
	@SuppressWarnings("unchecked")
	public <E> void setJson(String jsonData) {

		try {
			JSONParser parser = new JSONParser();

			this.putAll(((JSONObject) parser.parse(jsonData)));

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 입력받은 key 통해 Object 반환한다
	 * </pre>
	 * 
	 * @param <E>
	 * @param key
	 *            입력받은 Key 값
	 * @return 반환할 Object
	 */
	@SuppressWarnings("unchecked")
	public <E> E get(String key) {
		return (E) super.get(key);
	}

	/**
	 * <pre>
	 * key 에 해당하는 값이 NULL 또는 공백인 경우에는 replaceObj로 대체한다
	 * </pre>
	 * 
	 * @param <E>
	 * @param key
	 *            대체할 SBox의 Key값
	 * @param replaceobj
	 *            대체할 Object
	 */
	public <E> void setIfEmpty(String key, E replaceobj) {

		if (this.get(key) == null || this.get(key).equals("")) {
			this.set(key, replaceobj);
		}
	}

	/**
	 * <pre>
	 * 해당 Key 값의 Null 또는 공백시 true를 반환한다.
	 * </pre>
	 * 
	 * @param key
	 *            Null체크할 키값
	 * @return true(NULL 또는 공백) / false(데이터존재함)
	 */
	public boolean isEmpty(String key) {
		boolean bTF = false;
		if (this.get(key) == null || this.getString(key).equals("")) {
			bTF = true;
		}
		return bTF;
	}

	/**
	 * <pre>
	 * 입력 받은 Key 통해 String 형 변환 메소드
	 * </pre>
	 * 
	 * @param key
	 *            입력값
	 * @return 메소드 내에 정의된 여러 자료형을 String으로 변환한 값
	 */
	public String getString(String key) {
		Object obj = this.get(key);
		String result = "";
		if (obj != null) {
			if (obj instanceof String) {
				result = (String) obj;
			} else if (obj instanceof Integer) {
				result = obj.toString();
			} else if (obj instanceof Long) {
				result = obj.toString();
			} else if (obj instanceof Float) {
				result = obj.toString();
			} else if (obj instanceof Double) {
				result = obj.toString();
			} else if (obj instanceof Boolean) {
				result = obj.toString();
			} else if (obj instanceof Short) {
				result = obj.toString();
			} else if (obj instanceof Date) {
				result = obj.toString();
			} else if (obj instanceof BigInteger) {
				result = ((BigInteger) obj).toString();
			} else if (obj instanceof BigDecimal) {
				result = ((BigDecimal) obj).toString();
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 입력받은 Key 통해 integer 형 변환 메소드
	 * </pre>
	 * 
	 * @param key
	 *            입력값
	 * @return integer 변환값
	 */
	public int getInt(String key) {
		Object obj = this.get(key);
		int result = 0;
		if (obj != null) {
			if (obj instanceof String) {
				result = Integer.parseInt((String) obj);
			} else if (obj instanceof Integer) {
				result = (Integer) obj;
			} else if (obj instanceof Long) {
				result = ((Long) obj).intValue();
			} else if (obj instanceof Double) {
				result = ((Double) obj).intValue();
			} else if (obj instanceof Float) {
				result = ((Float) obj).intValue();
			} else if (obj instanceof Short) {
				result = ((Short) obj).intValue();
			} else if (obj instanceof BigInteger) {
				result = ((BigInteger) obj).intValue();
			} else if (obj instanceof BigDecimal) {
				result = ((BigDecimal) obj).intValue();
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 입력받은 Key 통해 Long 형 변환 메소드
	 * </pre>
	 * 
	 * @param key
	 *            입력값
	 * @return long 변환값
	 */
	public long getLong(String key) {
		Object obj = this.get(key);
		long result = 0L;
		if (obj != null) {
			if (obj instanceof String) {
				result = Long.parseLong((String) obj);
			} else if (obj instanceof Long) {
				result = (Long) obj;
			} else if (obj instanceof Integer) {
				result = ((Integer) obj).longValue();
			} else if (obj instanceof BigInteger) {
				result = ((BigInteger) obj).longValue();
			} else if (obj instanceof BigDecimal) {
				result = ((BigDecimal) obj).longValue();
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 입력받은 Key 통해 Float 형 변환 메소드
	 * </pre>
	 * 
	 * @param key
	 *            입력값
	 * @return Float 변환값
	 */
	public float getFloat(String key) {
		Object obj = this.get(key);
		float result = 0f;
		if (obj != null) {
			if (obj instanceof String) {
				result = Float.parseFloat((String) obj);
			} else if (obj instanceof Float) {
				result = (Float) obj;
			} else if (obj instanceof BigDecimal) {
				result = ((BigDecimal) obj).floatValue();
			} else {
				result = (Float) obj;
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 입력받은 Key 통해 Double 형 변환 메소드
	 * </pre>
	 * 
	 * @param key
	 *            입력값
	 * @return Double 변환값
	 */
	public double getDouble(String key) {
		Object obj = this.get(key);
		double result = 0D;
		if (obj != null) {
			if (obj instanceof String) {
				result = Double.parseDouble((String) obj);
			} else if (obj instanceof Double) {
				result = (Double) obj;
			} else if (obj instanceof BigDecimal) {
				result = ((BigDecimal) obj).doubleValue();
			} else {
				result = (Double) obj;
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * Generic 내부의 데이터를 key=value&amp;data Get Parameter 형식으로 변환하여 출력한다.
	 * </pre>
	 * 
	 * @return key=value 형식의 String Data
	 */
	public String toParamString() {
		StringBuffer sb = new StringBuffer();
		Iterator<String> itMap = this.keySet().iterator();
		while (itMap.hasNext()) {
			String key = (String) itMap.next();
			if (this.get(key) != null) {
				sb.append(key);
				sb.append("=");
				sb.append(this.get(key).toString());
				sb.append("&");
			}
		}
		return sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) : "";
	}

	/**
	 * <pre>
	 * ABox 내부의 데이터를 key=value (\n) 값 형식으로 변환하여 출력한다.
	 * </pre>
	 * 
	 * @return key=value (\n) 형식의 String Data
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this != null) {
			Iterator<String> itMap = this.keySet().iterator();
			while (itMap.hasNext()) {
				String key = (String) itMap.next();
				Object obj = this.get(key);
				if (obj != null) {
					sb.append(key);
					sb.append("[");
					if (obj instanceof ABoxList) {
						sb.append(obj.toString());
					} else if (obj instanceof String[]) {
						String temp = "";
						for (int i = 0; i < ((String[]) obj).length; i++) {
							temp += ((String[]) obj)[i];
							temp += ",";
						}
						if (((String[]) obj).length > 0) {
							sb.append(temp.substring(0, temp.length() - 1));
						}
					} else {
						sb.append(obj);
					}
					sb.append("],");
				}
			}
		}

		return sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) : "";
	}

	/**
	 * <pre>
	 * ABox 내부의 데이터를 key=value (\n) 값 형식으로 줄바꿈을 하며 출력한다.
	 * </pre>
	 * 
	 * @return key=value (\n) 형식의 String Data
	 */
	public String println() {
		StringBuffer sb = new StringBuffer();
		if (this != null) {
			Iterator<String> itMap = this.keySet().iterator();
			while (itMap.hasNext()) {
				String key = (String) itMap.next();
				Object obj = this.get(key);
				if (obj != null) {
					sb.append(key);
					sb.append("[");
					if (obj instanceof ABoxList) {
						sb.append(obj.toString());
					} else if (obj instanceof String[]) {
						String temp = "";
						for (int i = 0; i < ((String[]) obj).length; i++) {
							temp += ((String[]) obj)[i];
							temp += ",";
						}
						if (((String[]) obj).length > 0) {
							sb.append(temp.substring(0, temp.length() - 1));
						}
					} else {
						sb.append(obj);
					}
					sb.append("],");
					sb.append("\n");
				}
			}
		}

		return sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) : "";
	}

	/**
	 * <pre>
	 * ABox 내부의 데이터를 key=value (\n) 값 형식으로 변환하여 출력한다.
	 * 출력시에는 keySet의 내용을 파싱 하여 부분적으로 출력한다.
	 * </pre>
	 * 
	 * @param keySet
	 *            key1,key2,key3...
	 * @return key=value (\n) 형식의 String Data
	 */
	public String toString(String keySet) {
		StringBuffer sb = new StringBuffer();
		if (this != null) {
			Iterator<String> itMap = this.keySet().iterator();
			while (itMap.hasNext()) {
				String key = (String) itMap.next();

				String[] keySetArray = keySet.split("\\,");
				for (int k = 0; k < keySetArray.length; k++) {

					if (key.equals(keySetArray[k])) {
						key = keySetArray[k];
						Object obj = this.get(key);
						if (obj != null) {
							sb.append(key);
							sb.append("[");
							if (obj instanceof ABoxList) {
								sb.append(obj.toString());
							} else if (obj instanceof String[]) {
								String temp = "";
								for (int i = 0; i < ((String[]) obj).length; i++) {
									temp += ((String[]) obj)[i];
									temp += ",";
								}
								if (((String[]) obj).length > 0) {
									sb.append(temp.substring(0, temp.length() - 1));
								}
							} else {
								sb.append(obj);
							}
							sb.append("],");
						}

						break;
					}
				}
			}
		}

		return sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) : "";
	}

	/**
	 * <pre>
	 * ABox 를 JSON 타입으로 변환하는 메소드
	 * </pre>
	 * 
	 * @return JSON 변환 문자열
	 */
	public String toJSON() throws IOException {

		return this.recursiveChangeJson(this);

	}

	/**
	 * <pre>
	 * ABox 를 JSON 타입으로 변환시 특수문자는 인코딩을 한다.
	 * </pre>
	 * 
	 * @return JSON 변환 문자열
	 * @throws IOException
	 */
	public String toJSON(String encode) throws IOException {

		ABox cloneBox = this.recursiveEncode((ABox) this.clone(), encode);

		return this.recursiveChangeJson(cloneBox);
	}

	/**
	 * <pre>
	 * ABoxList 내의 함수 인코딩 메소드.
	 * 재귀호출 함수로써 오버로딩하여 사용된다.
	 * </pre>
	 * 
	 * @param list
	 *            Source Object
	 * @param encode
	 *            Encode Type
	 * @return Result Object(SBoxList)
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private ABoxList<Object> recursiveEncode(ABoxList<Object> list, String encode) throws IOException {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof ABox) {
				list.set(i, this.recursiveEncode((ABox) list.get(i), encode));
			} else if (list.get(i) instanceof ABoxList) {
				list.set(i, this.recursiveEncode((ABoxList<Object>) list.get(i), encode));
			} else if (list.get(i) instanceof String) {
				list.set(i, URLEncoder.encode((String) list.get(i), encode));
			} else if (list.get(i) instanceof String[]) {
				int len = ((String[]) list.get(i)).length;
				String[] temp = new String[len];
				System.arraycopy((String[]) list.get(i), 0, temp, 0, len);
				for (int j = 0; j < temp.length; j++) {
					temp[j] = URLEncoder.encode(temp[j], encode);
				}
				list.set(i, temp);
			} else if (list.get(i) instanceof java.sql.Time) {
				list.set(i, URLEncoder.encode(((java.sql.Time) list.get(i)).toString(), encode));
			} else if (list.get(i) instanceof java.sql.Date) {
				list.set(i, URLEncoder.encode(((java.sql.Date) list.get(i)).toString(), encode));
			} else if (list.get(i) instanceof java.sql.Timestamp) {
				String result = ((java.sql.Timestamp) list.get(i)).toString().substring(0, ((java.sql.Timestamp) list.get(i)).toString().indexOf("."));
				list.set(i, URLEncoder.encode(result, encode));
			}
		}
		return list;
	}

	/**
	 * <pre>
	 * ABox 내의 함수 인코딩 메소드.
	 * 재귀호출 함수로써 오버로딩하여 사용된다.
	 * </pre>
	 * 
	 * @param sBox
	 *            Source Object
	 * @param encode
	 *            Encode Type
	 * @return Result Object(SBox)
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private ABox recursiveEncode(ABox aBox, String encode) throws IOException {

		Iterator<String> it = aBox.keySet().iterator();

		while (it.hasNext()) {
			String key = (String) it.next();

			if (aBox.get(key) instanceof ABox) {
				aBox.set(key, recursiveEncode((ABox) aBox.get(key), encode));
			} else if (aBox.get(key) instanceof ABoxList) {
				aBox.set(key, this.recursiveEncode((ABoxList<Object>) aBox.get(key), encode));
			} else if (aBox.get(key) instanceof String) {
				aBox.set(key, URLEncoder.encode(aBox.getString(key), encode));
			} else if (aBox.get(key) instanceof String[]) {
				int len = ((String[]) aBox.get(key)).length;
				String[] temp = new String[len];
				System.arraycopy((String[]) aBox.get(key), 0, temp, 0, len);
				for (int j = 0; j < temp.length; j++) {
					temp[j] = URLEncoder.encode(temp[j], encode);
				}
				aBox.set(key, temp);
			} else if (aBox.get(key) instanceof java.sql.Time) {
				aBox.set(key, URLEncoder.encode(((java.sql.Time) aBox.get(key)).toString(), encode));
			} else if (aBox.get(key) instanceof java.sql.Date) {
				aBox.set(key, URLEncoder.encode(((java.sql.Date) aBox.get(key)).toString(), encode));
			} else if (aBox.get(key) instanceof java.sql.Timestamp) {
				String result = ((java.sql.Timestamp) aBox.get(key)).toString().substring(0, ((java.sql.Timestamp) aBox.get(key)).toString().indexOf("."));
				aBox.set(key, URLEncoder.encode(result, encode));
			}

		}

		return aBox;
	}

	/**
	 * <pre>
	 * ABox를 JSON 타입으로 재귀호출하며 문자열로 변환한다.
	 * </pre>
	 * 
	 * @param sBox
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String recursiveChangeJson(ABox sBox) {
		StringBuffer sb = new StringBuffer();

		if (sBox != null && sBox.size() != 0) {
			sb.append("{");

			Iterator<String> it = sBox.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				sb.append("\"" + key + "\"");
				sb.append(":");
				if (sBox.get(key) instanceof ABox) {
					sb.append(this.recursiveChangeJson((ABox) sBox.get(key)));
					sb.append(",");

				} else if (sBox.get(key) instanceof ABoxList) {
					sb.append(this.recursiveChangeJson((ABoxList<Object>) sBox.get(key)));
					sb.append(",");
				} else if (sBox.get(key) instanceof String[]) {
					sb.append("[");
					int len = ((String[]) sBox.get(key)).length;
					String[] temp = new String[len];
					System.arraycopy((String[]) sBox.get(key), 0, temp, 0, len);
					for (int j = 0; j < temp.length; j++) {
						sb.append("\"" + temp[j] + "\"");
						sb.append(",");
					}
					sb.replace(sb.length() - 1, sb.length(), "");
					sb.append("],");

				} else {
					sb.append("\"" + sBox.get(key) + "\"");
					sb.append(",");
				}
			}
			sb.replace(sb.length() - 1, sb.length(), "");
			sb.append("}");
		}

		return sb.toString();
	}

	/**
	 * <pre>
	 * ABoxList를 JSON 타입으로 재귀호출하며 문자열로 변환한다.
	 * </pre>
	 * 
	 * @param SBoxList
	 *            Data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String recursiveChangeJson(ABoxList<Object> list) {
		StringBuffer sb = new StringBuffer();

		if (list != null && list.size() != 0) {

			sb.append("[");
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof ABox) {
					sb.append(this.recursiveChangeJson((ABox) list.get(i)));
					sb.append(",");
				} else if (list.get(i) instanceof ABoxList) {
					sb.append(this.recursiveChangeJson((ABoxList<Object>) list.get(i)));
					sb.append(",");
				} else if (list.get(i) instanceof String[]) {
					sb.append("[");
					int len = ((String[]) list.get(i)).length;
					String[] temp = new String[len];
					System.arraycopy((String[]) list.get(i), 0, temp, 0, len);
					for (int j = 0; j < temp.length; j++) {
						sb.append("\"" + temp[j] + "\"");
						sb.append(",");
					}
					sb.replace(sb.length() - 1, sb.length(), "");
					sb.append("],");

				} else {
					sb.append("\"" + list.get(i) + "\"");
					sb.append(",");
				}
			}
			sb.replace(sb.length() - 1, sb.length(), "");
			sb.append("]");

		}

		return sb.toString();
	}

    public ABox jsonToABox(JsonObject jsonObject){
        Gson gson = new Gson();
        ABox aBox = new ABox();
        aBox = gson.fromJson(jsonObject, aBox.getClass());
        
        return aBox;
    }

    public ABox jsonToABox(String jsonObject){
        Gson gson = new Gson();
        ABox aBox = new ABox();
        aBox = gson.fromJson(jsonObject, aBox.getClass());
        
        return aBox;
    }
    
    public JsonObject aBoxToJsonObject(){
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(this).getAsJsonObject();
        
        return json;
    }
}