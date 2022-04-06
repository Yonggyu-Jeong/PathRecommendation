package common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
/**
 * <pre>
 * 	EnDecodingMO 공통 모듈 Class
 * </pre>
 */

@Component
public class EnDecodingMO {
	/**
	 * <pre>
	 * UTF-8 인코딩
	 * </pre>
	 * 
	 * @param data : 입력값
	 * 
	 * @return UTF-8 인코딩 후의 값
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeUTF8(String data) throws Exception {
		return URLEncoder.encode(data, "UTF-8");
	}

	/**
	 * <pre>
	 * UTF-8 디코딩
	 * </pre>
	 * 
	 * @param data : 입력값
	 * 
	 * @return UTF-8 디코딩 후의 값
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeUTF8(String msg) throws Exception {
		return URLDecoder.decode(msg, "UTF-8");
	}

}
