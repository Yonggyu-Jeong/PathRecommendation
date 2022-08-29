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

		// String json =
		// "{\"goal\":\"126.97095271218643,37.57920379891076,name=\\\"인왕여성사우나\\\"\",\"start\":\"126.6885861477842,37.34732011129632,name=\\\"정동진\\\"\",\"waypoints\":\"126.7009730989,37.3518070533,name=옥구공원:126.7447471013,37.3718045017,name=뒷방울저수지:126.8157177668,37.5051268321,name=부천자연생태공원\"}";
		String json = "";
		ABox jsonBox = new ABox();
		ABox naverJsonBox = new ABox();
		ABox routeBox = new ABox();
		ABox summaryBox = new ABox();
		ABox resultBox = new ABox();
		ABoxList<ABox> routeList = new ABoxList<ABox>();
		ABoxList<ABox> guideList = new ABoxList<ABox>();
		NaverMap naverMap = new NaverMap();

		jsonBox.setJson(json);
		try {
			String option = jsonBox.getString("option");
			try {
				// naverJsonBox.setJson(naverMap.sendNaverMap(jsonBox));
				naverJsonBox = jsonBox;
				if (naverJsonBox.getString("message").equals("길찾기를 성공하였습니다.")) {
					naverJsonBox.setJson(naverJsonBox.get("route").toString());
					routeList.setJson(naverJsonBox.get(option).toString()); // 추가 기능
					routeBox = routeList.get(0);
					summaryBox.setJson(routeBox.get("summary").toString());
					guideList.setJson(routeBox.get("guide").toString());
					resultBox.set("naverJson", naverJsonBox);
					//resultBox.set("guidList", guideList);
					//resultBox.set("cost", summaryBox.getInt("fuelPrice")+summaryBox.getInt("fuelPrice"));
					//resultBox.set("duration", summaryBox.getInt("duration"));
					//resultBox.set("distance", summaryBox.getInt("distance"));
					//resultBox.set("path", routeBox.get("path").toString().substring(1, routeBox.get("path").toString().length() - 1).replaceAll("\\],\\[", "\\]&\\["));
	
				} else {
					resultBox.set("check", "fail");
					//resultBox.set("input", jsonBox.toString());
					resultBox.set("check_message", "error2");
					resultBox.set("message", naverJsonBox.getString("message"));
				}
			} catch (Exception e) {
				resultBox.set("check", "fail");
				//resultBox.set("input", jsonBox.toString());
				resultBox.set("check_message", "error3");
				resultBox.set("message", naverJsonBox.getString("message"));
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.SERVICE_UNAVAILABLE);

		} finally {
			jsonBox = null;
			naverMap = null;
		}
		return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.OK);
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
		ABox jsonBox = new ABox();
		ABox naverJsonBox = new ABox();
		ABox routeBox = new ABox();
		ABox traBox = new ABox();
		ABox resultBox = new ABox();
		ABox summaryBox = new ABox();
		ABoxList<ABox> routeList = new ABoxList<ABox>();
		ABoxList<ABox> guideList = new ABoxList<ABox>();
		NaverMap naverMap = new NaverMap();
		jsonBox.setJson(json);
		try {
			String option = jsonBox.getString("option");
			if (jsonBox.containsKey("start") && jsonBox.containsKey("goal")) {
				try {
					naverJsonBox.setJson(naverMap.sendNaverMap(jsonBox));
					if (naverJsonBox.getString("message").equals("길찾기를 성공하였습니다.")) {
						routeBox.setJson(naverJsonBox.get("route").toString());						
						routeList.setJson(routeBox.get(option).toString());
						traBox = routeList.get(0);
						summaryBox.setJson(traBox.get("summary").toString());
						guideList.setJson(traBox.get("guide").toString());
						resultBox.set("guidList", guideList);
						resultBox.set("cost", summaryBox.getInt("fuelPrice")+summaryBox.getInt("fuelPrice"));
						resultBox.set("duration", summaryBox.getInt("duration"));
						resultBox.set("distance", summaryBox.getInt("distance"));
						resultBox.set("result", traBox.get("path").toString().substring(1, traBox.get("path").toString().length() - 1).replaceAll("\\],\\[", "\\]&\\["));

						if (traBox.get("path").toString().length() > 0) {
							resultBox.set("check", "ok");
						} else {
							resultBox.set("check", "fail");
							resultBox.set("input", jsonBox.toString());
							resultBox.set("check_message", "error1");
							resultBox.set("message", naverJsonBox.getString("message"));
						}

					} else {
						resultBox.set("check", "fail");
						resultBox.set("input", jsonBox.toString());
						resultBox.set("check_message", "error2");
						resultBox.set("message", naverJsonBox.getString("message"));
					}
				} catch (Exception e) {
					resultBox.set("check", "fail");
					resultBox.set("input", jsonBox.toString());
					resultBox.set("check_message", "error3");
					resultBox.set("message", naverJsonBox.getString("message"));
					resultBox.set("check-", e.getStackTrace());
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
			jsonBox = null;
			naverJsonBox = null;
			routeBox = null;
			traBox = null;
			summaryBox = null;
			naverMap = null;
			routeList = null;
			guideList = null;
		}
		return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/category/list/{category}", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public ResponseEntity<String> getCategoryList(@PathVariable("category") String category) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		if(!category.equals("CS99")) {
			jsonBox.set("category", category);			
		}
		try {
			result = userService.getCategoryList(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/list/{option}", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public ResponseEntity<String> getCategoryUserList(@PathVariable("option") String option) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.set("option", option);			
		try {
			result = userService.getCategoryUserList(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/list2/{option}", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public ResponseEntity<String> getCategoryUserList2(@PathVariable("option") String option) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.set("option", option);			
		try {
			result = userService.getCategoryUserList2(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/addMap/{name}/{option}", produces = "application/json; charset=utf8", method = RequestMethod.GET)
	public ResponseEntity<String> addMap(@PathVariable("name") String name, @PathVariable("option") String option) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.set("name", name);
		if(option.equals("CH01")) {
			jsonBox.set("option", "CH01");			
		} else {
			jsonBox.set("option", "CH02");			
		}
		try {
			
			result = userService.addMap(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
