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
import common.collection.ABox;
import common.collection.ABoxList;
import shadow.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/login/{temp}", method = RequestMethod.GET)
	public ResponseEntity<String> test(@PathVariable("temp") String temp) throws Exception{
		ABox resultBox = new ABox();
		try{
			resultBox = userService.test();
			
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
	
	@RequestMapping(value = "/insert/review", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> insertReview(@RequestBody String json) throws Exception{
		String result = "";
		ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();
		//jsonBoxList.setJson(json);
		try {
			result = userService.insertReview(jsonBoxList).toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/locate/find", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> findLocation(@RequestBody String json) throws Exception{
		String result = "";
		ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();
		jsonBoxList.setJson(json);
		try {
			result = userService.findLocation(jsonBoxList).aBoxToJsonObject().toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
	}
	
}
