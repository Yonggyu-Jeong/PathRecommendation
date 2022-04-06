package common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import common.collection.ABox;

import java.security.Key;

/**
 * <pre>
 * 	Date 공통 모듈 Class
 * </pre>
 */
public class CryptoMo {

	/**
	 * <pre>
	 * MD5 암호화 메소드
	 * </pre>
	 * 
	 * @param data 암호화 대상 문자열
	 * @return 암호화 후 반환 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashMd5(String data) throws NoSuchAlgorithmException {
		String result = data;
		if (data != null) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			if ((result.length() % 2) != 0) {
				result = "0" + result;
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * SHA-1 암호화 메소드
	 * </pre>
	 * 
	 * @param data 암호화 대상 문자열
	 * @return 암호화 후 반환 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashSha1(String data) throws NoSuchAlgorithmException {
		String result = data;
		if (data != null) {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(data.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			if ((result.length() % 2) != 0) {
				result = "0" + result;
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * SHA-1 암호화 메소드
	 * </pre>
	 * 
	 * @param data 암호화 대상 문자열
	 * @return 암호화 후 반환 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashSha512(String data) throws NoSuchAlgorithmException {
		String result = data;
		if (data != null) {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(data.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			if ((result.length() % 2) != 0) {
				result = "0" + result;
			}
		}
		return result;
	}

	public static ABox AES128(String key) {
		ABox aBox = new ABox();
		try {
			byte[] keyBytes = new byte[16];
			byte[] b = key.getBytes("UTF-8");
			System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			aBox.set("ips", key.substring(0, 16));
			aBox.set("keySpec", keySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aBox;
	}

	public static String encryptAES128(ABox aBox, String bString) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, aBox.get("keySpec"),
					new IvParameterSpec(aBox.getString("ips").getBytes()));

			byte[] encrypted = cipher.doFinal(bString.getBytes("UTF-8"));
			String Str = new String(Base64.encodeBase64(encrypted));
			return Str;

		} catch (Exception e) {
			return null;
		}
	}

	public static String decryptAES128(ABox aBox, String aString) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, aBox.get("keySpec"),
					new IvParameterSpec(aBox.getString("ips").getBytes("UTF-8")));
			byte[] byteStr = Base64.decodeBase64(aString.getBytes());
			String Str = new String(cipher.doFinal(byteStr), "UTF-8");
			return Str;

		} catch (Exception e) {
			return null;
		}
	}

}
