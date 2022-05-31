package shadow.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import common.collection.ABox;
import common.collection.ABoxList;
import common.util.NaverMap;
import shadow.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private NaverMap naverMap;

	@RequestMapping(value = "/user/test", method = RequestMethod.GET, produces = "application/json; charset=utf8")
	public ResponseEntity<String> test() throws Exception{
		ABox resultBox = new ABox();

		try{
			ABox jsonBox = new ABox();
			String json="{\"goal\":\"37.31689239364219,126.84627011464801,name=\\\"안산종합여객자동차터미널\\\"\",\"start\":\"37.3627151749158,126.73882379220626,name=\\\"오이도역 수인분당선\\\"\",\"waypoints\":\"37.350593833,126.7582752593,name=생금집:37.319893151,126.8229310131,name=안산패션일번가:37.3204546056,126.8541872996,name=노적봉 공원\"}";
			jsonBox.setJson(json);
			resultBox.setJson(naverMap.sendNaverMap(jsonBox));

		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.OK);
	}
		
	@RequestMapping(value = "/insert/locate", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> insertLocate(@RequestBody String json) throws Exception{
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
	public ResponseEntity<String> getLocation(@RequestBody String json) throws Exception{
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
	public ResponseEntity<String> findLocation(@RequestBody String json) throws Exception{
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
	public ResponseEntity<String> testLocation() throws Exception{
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
	public ResponseEntity<String> getPathList(@RequestBody String json) throws Exception{
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);
		try {
			result = naverMap.sendNaverMap(jsonBox);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/category/list", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> getCategoryList(@RequestBody String json) throws Exception{
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);

		try {
			result = userService.getCategoryList(jsonBox).aBoxToJsonObject().toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
