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
import shadow.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/test/{temp}", method = RequestMethod.GET)
	public ResponseEntity<String> test(@PathVariable("temp") String temp) throws Exception{
		try{
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(userService.test().toString(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(userService.test().toString(), HttpStatus.OK);
	}
	
	//특정 조건의 사용자 리스트 조회
	@RequestMapping(value = "user/search", method = RequestMethod.POST)
	public ResponseEntity<String> searchUser(@RequestBody ABox userFilter) throws Exception{
		ABox searched_userList = null;
		String result = "";
		try{
			searched_userList = userService.searchUserList(userFilter);
			result = searched_userList.aBoxToJsonObject().toString();
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	//회원정보 수정
	@RequestMapping(value = "user/modify", method = RequestMethod.PUT)
	public ResponseEntity<String> modifyUser(@RequestBody ABox userInfo) throws Exception {
		String answer = null;
		try {
			answer = userService.modifyUser(userInfo);
		} catch (Exception e) {
			return new ResponseEntity<String>(answer, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(answer, HttpStatus.OK);
	}

	
	//프로필 사진 변경
	@RequestMapping(value = "user/modify/profile_image", method = RequestMethod.POST, headers="Content-Type=multipart/form-data")
	public ResponseEntity<String> modifyProfileImage(MultipartHttpServletRequest multipartRequest,
			@RequestPart(value = "file", required = false) MultipartFile multipartFile) throws Exception {
		String answer = null;
		try {
			answer = userService.modifyProfileImage(multipartRequest); //win
			
		} catch (Exception e) {
			return new ResponseEntity<String>(answer, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(answer, HttpStatus.OK);
	}	
	
	
//	//회원 삭제
//	@RequestMapping(value = "/delete/{publicId}", method = RequestMethod.DELETE)
//	public ResponseEntity<String> deleteAccount(@PathVariable("userID") String userID) throws Exception {
//	
//		String answer = null;
//		try {
//			ABox user_ID = new ABox();
//			user_ID.set("userID", userID);
//			answer = userService.deleteAccount(user_ID);
//			
//		} catch (Exception e) {
//			return new ResponseEntity<String>(answer, HttpStatus.SERVICE_UNAVAILABLE);
//		}
//		return new ResponseEntity<String>(answer, HttpStatus.OK);
//	}
	
	//jsp 양식 보기(웹)
	@RequestMapping(value = "/*Form", method = RequestMethod.GET)
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName(request));
		return mav;
	}

	private String getViewName(HttpServletRequest request) throws Exception {
		// 요청명 구하는 함수. 첫번째 요청명까지 포함. /user/join.do면 -> user/join만 추출

		String contextPath = request.getContextPath();
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
		}

		int begin = 0;
		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length();
		}

		int end;
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}

		String viewName = uri.substring(begin, end);
		if (viewName.indexOf(".") != -1) {
			viewName = viewName.substring(0, viewName.lastIndexOf("."));
		}
		if (viewName.lastIndexOf("/") != -1) {
			viewName = viewName.substring(viewName.lastIndexOf("/", 1), viewName.length());
		}
		return viewName;
	}
}
