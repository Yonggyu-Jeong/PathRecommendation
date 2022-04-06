package common.collection;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * ArrayList를 상속받아 구현한 SBoxList
 * 
 * @param <E>
 */
@Component
@Qualifier("aBoxList")
public class ABoxList<E> extends ArrayList<E> {

	/**
	 * serialVersionID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <pre>
	 * ABoxList 생성자
	 * </pre>
	 * 
	 */
	public ABoxList() {
		super();
	}

	/**
	 * <pre>
	 * List 를 가져와 ABoxList로 변환하는 오버로딩 된 생성자
	 * </pre>
	 * 
	 * @param list
	 *            변환할 대상의 List 객체
	 */
	@SuppressWarnings("unchecked")
	public ABoxList(List<?> list) {
		if (list != null) {
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				this.add((E) it.next());
			}
		}
	}

	/**
	 * <pre>
	 * ABoxList에 Object 셋팅하는 메소드
	 * </pre>
	 * 
	 * @param obj
	 *            SBoxList에 셋팅할 값
	 */
	public void set(E obj) {
		super.add(obj);
	}

	/**
	 * <pre>
	 * ABoxList 에 Object Array 셋팅하는 메소드
	 * </pre>
	 * 
	 * @param objs
	 *            : 추가할 Object Array
	 */
	public void set(E[] objs) {
		for (E obj : objs) {
			super.add(obj);
		}
	}

	/**
	 * <pre>
	 * JSON 형식의 Data 를 SBox를 포함한 ABoxList Data로 Settting 함
	 * </pre>
	 * 
	 * @param jsonData
	 *            : json 형식의 Data
	 */
	@SuppressWarnings("unchecked")
	public void setJson(String jsonData) {

		Object obj = JSONValue.parse(jsonData);
		JSONArray array = (JSONArray) obj;

		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) instanceof JSONObject) {
				ABox sBox = new ABox();
				sBox.putAll((JSONObject) array.get(i));
				this.set((E) sBox);
			}

		}

	}

	/**
	 * <pre>
	 * ABoxList 를 String 으로 출력하는 메소드
	 * </pre>
	 * 
	 * @return SBoxList 출력 String
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer();

		if (this != null) {
			Iterator<E> itList = this.iterator();
			while (itList.hasNext()) {
				E obj = itList.next();
				if (obj instanceof ABox) {
					sb.append(obj.toString());
				} else if (obj instanceof ABoxList) {
					sb.append("SBoxList Object");
				} else if (obj instanceof String[]) {
					String temp = "";
					for (int i = 0; i < ((String[]) obj).length; i++) {
						temp += ((String[]) obj)[i];
						temp += ",";
					}
					if (((String[]) obj).length > 0) {
						sb.append(temp.substring(0, temp.length() - 1));
					}
				} else if (obj instanceof String) {
					sb.append((String) obj);
				} else {
					sb.append(obj);
				}
				sb.append(",");
			}
		} 
		return sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) : "";
	}

	/**
	 * <pre>
	 * ABoxList 를 줄바꿈 하여 String 으로 출력하는 메소드
	 * </pre>
	 * 
	 * @return ABoxList 출력 String
	 */
	public String println() {

		StringBuffer sb = new StringBuffer();

		if (this != null) {
			Iterator<E> itList = this.iterator();
			while (itList.hasNext()) {
				E obj = itList.next();
				if (obj instanceof ABox) {
					sb.append(obj.toString());
				} else if (obj instanceof ABoxList) {
					sb.append("SBoxList Object");
				} else if (obj instanceof String[]) {
					String temp = "";
					for (int i = 0; i < ((String[]) obj).length; i++) {
						temp += ((String[]) obj)[i];
						temp += ",";
					}
					if (((String[]) obj).length > 0) {
						sb.append(temp.substring(0, temp.length() - 1));
					}
				} else if (obj instanceof String) {
					sb.append((String) obj);
				} else {
					sb.append(obj);
				}
				sb.append(",");
				sb.append("\n");
			}
		}
		return sb.length() > 0 ? sb.toString().substring(0, sb.length() - 2) : "";
	}

	/**
	 * <pre>
	 * ABoxList 를 JSON 타입으로 변환하는 메소드
	 * </pre>
	 * 
	 * @return JSON 변환 문자열
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String toJSON() throws IOException {
		ABoxList<Object> cloneObj = this.recursiveEncode((ABoxList<Object>) ((ABoxList<Object>) this).clone(), "euc-kr");
		return this.recursiveChangeJson(cloneObj);
	}

	/**
	 * <pre>
	 * ABoxList 를 JSON 타입으로 변환하는 메소드
	 * </pre>
	 * 
	 * @param encode
	 *            = 인코딩
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String toJSON(String encode) throws IOException {
		ABoxList<Object> cloneObj = this.recursiveEncode((ABoxList<Object>) ((ABoxList<Object>) this).clone(), encode);
		return this.recursiveChangeJson(cloneObj);
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
				String result = ((java.sql.Timestamp) list.get(i)).toString()
						.substring(0, ((java.sql.Timestamp) list.get(i)).toString().indexOf("."));
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
	 * @param ABox
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
				String result = ((java.sql.Timestamp) aBox.get(key)).toString().substring(0,
						((java.sql.Timestamp) aBox.get(key)).toString().indexOf("."));
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
	 * @param aBox
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String recursiveChangeJson(ABox aBox) {
		StringBuffer sb = new StringBuffer();

		if (aBox != null && aBox.size() != 0) {
			sb.append("{");

			Iterator<String> it = aBox.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				sb.append("\"" + key + "\"");
				sb.append(":");
				if (aBox.get(key) instanceof ABox) {
					sb.append(this.recursiveChangeJson((ABox) aBox.get(key)));
					sb.append(",");

				} else if (aBox.get(key) instanceof ABoxList) {
					sb.append(this.recursiveChangeJson((ABoxList<Object>) aBox.get(key)));
					sb.append(",");
				} else if (aBox.get(key) instanceof String[]) {
					sb.append("[");
					int len = ((String[]) aBox.get(key)).length;
					String[] temp = new String[len];
					System.arraycopy((String[]) aBox.get(key), 0, temp, 0, len);
					for (int j = 0; j < temp.length; j++) {
						sb.append("\"" + temp[j] + "\"");
						sb.append(",");
					}
					sb.replace(sb.length() - 1, sb.length(), "");
					sb.append("],");

				} else {
					sb.append("\"" + aBox.get(key) + "\"");
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
	 * @param ABoxList
	 *            Data
	 * @return : ABoxList 내부 데이터를 String,String의 형태로 출력함.
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

    public ABoxList<ABox> jsonToABoxList(JsonArray jsonArray){
        Gson gson = new Gson();
        ABoxList<ABox> aBoxList = new ABoxList<ABox>();
        aBoxList = gson.fromJson(jsonArray.toString(), new TypeToken<ABoxList<ABox>>(){}.getType());
        
        return aBoxList;
    }

    public ABoxList<ABox> jsonToABoxList(String jsonArray){
        Gson gson = new Gson();
        ABoxList<ABox> aBoxList = new ABoxList<ABox>();
        aBoxList = gson.fromJson(jsonArray, new TypeToken<ABoxList<ABox>>(){}.getType());
        
        return aBoxList;
    }
}