package shadow.user.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import common.collection.ABox;
import common.collection.ABoxList;
import common.util.NaverMap;
import shadow.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/test", method = RequestMethod.GET, produces = "application/json; charset=utf8")
	public ResponseEntity<String> test() throws Exception {
		String json = "{\"goal\":{\"lng\":\"126.73882379220626\",\"lat\":\"37.3627151749158\"},\"start\":{\"lng\":\"126.9784147336912\",\"lat\":\"37.56667197782809\"},\"user\":{\"password\":\"1\",\"id\":\"hello\"}}";
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);

		try {
			result = userService.findLocation(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/test2", method = RequestMethod.GET, produces = "application/json; charset=utf8")
	public ResponseEntity<String> test2() throws Exception {
		String result = "";
		try {
			result = userService.test().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/insert/locate", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> insertLocate(@RequestBody String json) throws Exception {
		String result = "";
		ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();
		jsonBoxList.setJson(json);
		try {
			result = userService.insertLocation(jsonBoxList).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/get-location", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> getLocation(@RequestBody String json) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);
		try {
			result = userService.getLocation(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/find", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> findLocation(@RequestBody String json) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);

		try {
			result = userService.findLocation(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/testData", method = RequestMethod.GET)
	public ResponseEntity<String> testLocation() throws Exception {
		String result = "";
		String trash = "{\"goal\":{\"lng\":\"126.73882379220626\",\"lat\":\"37.3627151749158\"},\"start\":{\"lng\":\"126.9784147336912\",\"lat\":\"37.56667197782809\"},\"user\":{\"password\":\"1\",\"id\":\"hello\"}}";
		ABox jsonBox = new ABox();
		jsonBox.setJson(trash);

		try {
			result = userService.findLocation(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/path", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> getPathList(@RequestBody String json) throws Exception {
		String result = "";
		String parsingJson = "";
		ABox jsonBox = new ABox();
		ABox jsonBox2 = new ABox();
		ABox jsonBox3 = new ABox();
		ABox resultBox = new ABox();
		ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();
		NaverMap naverMap = new NaverMap();

		jsonBox.setJson(json);
		try {
			if (jsonBox.containsKey("start") && jsonBox.containsKey("goal")) {
				try {
					jsonBox2.setJson(naverMap.sendNaverMap(jsonBox));
					if (jsonBox2.getString("message").equals("길찾기를 성공하였습니다.")) {
						jsonBox2.setJson(jsonBox2.get("route").toString());
						jsonBoxList.setJson(jsonBox2.get("traoptimal").toString()); // 추가 기능
						jsonBox3 = jsonBoxList.get(0);
						parsingJson = jsonBox3.get("path").toString();

						result = parsingJson.substring(1, parsingJson.length() - 1).replaceAll("\\],\\[", "\\]&\\[");
						resultBox.set("result", result);
						if (result.length() > 0) {
							resultBox.set("check", "ok");
						} else {
							resultBox.set("check", "fail");
							resultBox.set("input", jsonBox.toString());
							resultBox.set("check_message", "error1");
							resultBox.set("message", jsonBox2.getString("message"));
						}

					} else {
						resultBox.set("check", "fail");
						resultBox.set("input", jsonBox.toString());
						resultBox.set("check_message", "error2");
						resultBox.set("message", jsonBox2.getString("message"));
					}
				} catch (Exception e) {
					resultBox.set("check", "fail");
					resultBox.set("input", jsonBox.toString());
					resultBox.set("check_message", "error3");
					resultBox.set("message", jsonBox2.getString("message"));
					e.printStackTrace();
				}
			} else {
				resultBox.set("check", "fail");
				resultBox.set("input", jsonBox.toString());
				resultBox.set("check_message", "error4");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.SERVICE_UNAVAILABLE);

		} finally {
			result = null;
			parsingJson = null;
			jsonBox = null;
			jsonBox2 = null;
			jsonBox3 = null;
			naverMap = null;
			jsonBoxList = null;
		}
		return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/category/list/{category}", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public ResponseEntity<String> getCategoryList(@PathVariable("category") String category) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.set("category", category);
		try {
			result = userService.getCategoryList(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
