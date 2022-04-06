package common.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import common.collection.ABox;
import common.collection.ALog;
import common.collection.ASession;
import common.dao.CommonDaoIF;

/**
 * <pre>
 * 모든 Controller 클래스의 부모클래스로써 공통기능 및 멤버 변수를 선언함.
 * </pre>
 */
@Controller
public class SuperController {

	@Autowired
	protected HttpSession httpSession; // HttpSession 객체 선언
	
	@Autowired
	private HttpServletRequest request; // request 객체 선언
	
	@Autowired
	protected MappingJackson2JsonView jsonView;

	@Autowired
	protected ALog aLog; // aLog 객체 선언

	@Autowired
	protected ASession aSession; // aSession 객체 선언
	
	@Autowired
	protected CommonDaoIF commonDao; // CommonDao 객체 선언

	/**
	 * <pre>
	 * 초기화 파라미터를 인터셉터로 부터 전달받는 메소드
	 * </pre>
	 * 
	 * @return ABox 형태의 파라미터 객체
	 */
	@ModelAttribute("initBoxs")
	public ABox initBoxs() {
		return (ABox) request.getAttribute("aBox");
	}
}
