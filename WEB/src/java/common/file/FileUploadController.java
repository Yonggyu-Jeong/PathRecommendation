package common.file;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import common.collection.ABox;
import common.collection.ABoxList;
import common.util.CryptoMo;

@Component
public class FileUploadController {

	// service 클래스 내부에서 쓸 함수.
	public static ABoxList<ABox> upload(MultipartHttpServletRequest multipartRequest) throws Exception { 

		String userId = "";
		String newFileName = "";
		String imagePath = "";

		ABox returnbox = new ABox();
		ABox map = new ABox();
		ABoxList<ABox> mapList = new ABoxList<ABox>();

		multipartRequest.setCharacterEncoding("utf-8");
		Enumeration<?> enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			map.set(name, value);
		}
		userId = map.get("publicId").toString();

		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String mFileName = mFile.getName();

			if (!mFileName.contains("badge")&&mFile.getSize() > 2000000) {
				returnbox.set("check", "fail");
				returnbox.set("check2", "size_fail");

				mapList.add(returnbox);
				return mapList;
			}

			String originalFileName = mFile.getOriginalFilename();
			String originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

			switch (originalFileExtension) {
			case "jpg":
				newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "." + originalFileExtension;
				break;

			case "jpeg":
				newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "." + originalFileExtension;
				break;

			case "png": 
				newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "." + originalFileExtension;
				break;

			default:
				returnbox.set("check", "fail");
				returnbox.set("check2", "type_fail");

				mapList.add(returnbox);
				return mapList;
			}

			returnbox.set("mFileName", mFileName);

			switch (mFileName) {
			case "profile1":
				imagePath = "/usr/local/apache-tomcat-8.5.70/webapps/examples/d456406745d816a45cae554c788e754/"
						+ CryptoMo.hashSha1(userId) + "/7D97481B1FE66F4B51DB90DA7E794D9F/";
				returnbox.set("originFileName1", newFileName);
				break;

			default:
				returnbox.set("check", "fail");
				returnbox.set("check2", mFileName);
				mapList.add(returnbox);
				return mapList;
			}

			try {
				File file = new File(imagePath + newFileName);
				if (mFile.getSize() != 0) { // File Null Check
					if (!file.exists()) { // 경로에 파일이 없으면
						if (file.getParentFile().mkdirs()) { // 그 경로에 해당하는 디렉터리를 만든 후
							file.createNewFile(); // 파일을 생성
						}
					}
					mFile.transferTo(new File(imagePath + newFileName)); // 임시로 저장된 multipartFile을 실제 파일로 전송
				}
				returnbox.set("check", "ok");
				mapList.add(returnbox);
				

			} catch (Exception e) {
				returnbox.set("check", "fail");
				returnbox.set("check2", e.toString());
				mapList.add(returnbox);
			}
		}
		return mapList;

	}
	
	public static ABoxList<ABox> upload(MultipartHttpServletRequest multipartRequest, int uploadType) throws Exception { //uploadType==0 -> profile image, uploadType==1 -> badge image

		String userId = "";
		String newFileName = "";
		String imagePath = "";

		ABox returnbox = new ABox();
		ABox map = new ABox();
		ABoxList<ABox> mapList = new ABoxList<ABox>();

		multipartRequest.setCharacterEncoding("utf-8");
		Enumeration<?> enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			map.set(name, value);
		}
		userId = map.get("publicId").toString();

		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String mFileName = mFile.getName();

			if (!mFileName.contains("badge")&&mFile.getSize() > 2000000) {
				returnbox.set("check", "fail");
				returnbox.set("check2", "size_fail");

				mapList.add(returnbox);
				return mapList;
			}

			String originalFileName = mFile.getOriginalFilename();
			String originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

			switch (originalFileExtension) {
			case "jpg":
				newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "." + originalFileExtension;
				break;

			case "jpeg":
				newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "." + originalFileExtension;
				break;

			case "png": 
				newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "." + originalFileExtension;
				break;

			default:
				returnbox.set("check", "fail");
				returnbox.set("check2", "type_fail");

				mapList.add(returnbox);
				return mapList;
			}

			returnbox.set("mFileName", mFileName);
				if(uploadType==0) {
					switch (mFileName) {
					case "profile1":
						imagePath = "/usr/local/apache-tomcat-8.5.70/webapps/examples/d456406745d816a45cae554c788e754/"
								+ CryptoMo.hashSha1(userId) + "/7D97481B1FE66F4B51DB90DA7E794D9F/";
						returnbox.set("originFileName1", newFileName);
						break;

					case "badge24":
						imagePath = "/usr/local/apache-tomcat-8.5.70/webapps/examples/d456406745d816a45cae554c788e754/"
								+ CryptoMo.hashSha1(userId) + "/DCDD1274F35AC5A8573E07B0582AD92C/"
								+ multipartRequest.getParameter("badge_type24") + "/"
								+ multipartRequest.getParameter("badge_detail24") + "/";
						returnbox.set("filename24", newFileName);
						break;
					
					default:
						returnbox.set("check", "fail");
						mapList.add(returnbox);
						return mapList;
					}
				}
				
			try {
				File file = new File(imagePath + newFileName);
				if (mFile.getSize() != 0) { // File Null Check
					if (!file.exists()) { // 경로에 파일이 없으면
						if (file.getParentFile().mkdirs()) { // 그 경로에 해당하는 디렉터리를 만든 후
							file.createNewFile(); // 파일을 생성
						}
					}
					mFile.transferTo(new File(imagePath + newFileName)); // 임시로 저장된 multipartFile을 실제 파일로 전송
				}
				returnbox.set("check", "ok");
				mapList.add(returnbox);
				

			} catch (Exception e) {
				returnbox.set("check", "fail");
				returnbox.set("check2", e.toString());
				mapList.add(returnbox);
			}
		}
		return mapList;

	}
	
	public static ABoxList<ABox> upload(MultipartHttpServletRequest multipartRequest, String userId, String condition)
			throws Exception {
		String newFileName = "";
		String imagePath = "";
		ABox returnbox = new ABox();
		ABoxList<ABox> mapList = new ABoxList<ABox>();

		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();

			if (fileName.equals(condition)) {
				MultipartFile mFile = multipartRequest.getFile(fileName);
				String mFileName = mFile.getName();

				if (mFile.getSize() > 1048576) {
					returnbox.set("check", "fail");
					mapList.add(returnbox);
					return mapList;
				}

				String originalFileName = mFile.getOriginalFilename();
				String originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

				switch (originalFileExtension) {
				case "jpg":
					newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "."
							+ originalFileExtension;
					break;

				case "jpeg":
					newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "."
							+ originalFileExtension;
					break;

				case "png":
					newFileName = mFileName + "_" + userId + "_" + System.currentTimeMillis() + "."
							+ originalFileExtension;
					break;

				default:
					returnbox.set("check", "fail");
					mapList.add(returnbox);
					return mapList;
				}

				returnbox.set("mFileName", mFileName);

				if (condition.equals("profile1")) {
					imagePath = "/usr/local/apache-tomcat-8.5.70/webapps/examples/d456406745d816a45cae554c788e754/"
							+ CryptoMo.hashSha1(userId) + "/7D97481B1FE66F4B51DB90DA7E794D9F/";
					returnbox.set("shadowFileName", newFileName);
					
				} else {
					returnbox.set("check", "fail");
					mapList.add(returnbox);
					return mapList;
				}

				try {
					File file = new File(imagePath + newFileName);
					if (mFile.getSize() != 0) { // File Null Check
						if (!file.exists()) { // 경로에 파일이 없으면
							if (file.getParentFile().mkdirs()) { // 그 경로에 해당하는 디렉터리를 만든 후
								file.createNewFile(); // 파일을 생성
							}
						}
						mFile.transferTo(new File(imagePath + newFileName)); // 임시로 저장된 multipartFile을 실제 파일로 전송
					}
					returnbox.set("check", "true");
					mapList.add(returnbox);

				} catch (Exception e) {
					returnbox.set("check", "false");
					mapList.add(returnbox);
				}
			}
		}
		return mapList;
	}
}
