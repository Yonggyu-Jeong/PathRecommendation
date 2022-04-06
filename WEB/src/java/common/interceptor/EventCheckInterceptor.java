package common.interceptor;

import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import common.collection.ABox;
import common.collection.ASession;

/**
 * <pre>
 * Controller 호출 전 Handler를 통한 Filter Interceptor Class
 * </pre>
 */
@Controller
public class EventCheckInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	protected ASession aSession; // aSession 객체 선언

	@Autowired
	protected HttpSession httpSession; // HttpSession 객체 선언

	/**
	 * <pre>
	 *  Controller 호출전 실행 메소드
	 * </pre>
	 * 
	 * @param request
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("##### EVENT CHECK INTERCEPTOR #####");
		
		ABox aBox = new ABox();
		String uri = request.getRequestURI();
		Map<String, String[]> requestMap = request.getParameterMap();
		Iterator<String> it = requestMap.keySet().iterator();
		
		// [1] Request 객체로 부터 Parameter 정보를 ABox로 셋팅함.
		while (it.hasNext()) {
			try {
				String key = (String) it.next();
				
				if ((requestMap.get(key) instanceof String[])) {
					String[] values = (String[]) requestMap.get(key);

					if (values != null && values.length == 1) {
						aBox.set(key, values[0]);
					} else {
						aBox.set(key, values);
					}
					
				} else {
					aBox.set(key, requestMap.get(key));
				}
				
			} catch (Exception ex) { }
		}
		
		// [2] Device 구분을 통해 해당되는 URL로 redirect
		Device device = DeviceUtils.getCurrentDevice(request);
		
		// clientDevice 구분
		String client_device = "Unknown";
		if (null != device && (device.isMobile() || device.isTablet())) {
			client_device = "Mobile";
		} else if(null != device && device.isNormal()) {
			client_device = "PC";
		} 
		aBox.set("clientDevice", client_device);
		
		// [3] Header 정보를 읽어와 ABox에 설정함.
		Enumeration<String> headerNames = request.getHeaderNames();
		
		ABox header = new ABox();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			header.set(headerName, request.getHeader(headerName));
		}

		// [4] 필요정보 aBox객체에 저장함
		aBox.set("remoteContextPath", request.getContextPath()); // CLient 요청 URL의 Context Path
		aBox.set("remoteDomain", request.getScheme() + "://"+request.getServerName() + (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":"+request.getServerPort() : ""));
		aBox.set("remoteHost", request.getRemoteHost()); // Client Host
		aBox.set("remoteAddr", request.getRemoteAddr()); // Client Host IP
		
		aBox.set("remoteURI", uri); // Client 요청 URI
		aBox.set("remoteMethod", request.getMethod()); // Client 요청 Method (GET, POST)
		
		// 세션 가져오기
		httpSession = request.getSession(false);
		
		// [5] cookie 있을 시 세션에 cookie 정보 저장
		if (httpSession == null && request.getCookies() != null) {
			// "_EBM_pu" 이름의 cookie 받아오기
			Optional<String> cookies = Arrays.stream(request.getCookies()).filter(c -> "_WF_pu".equals(c.getName())).map(Cookie::getValue).findAny();
			if (cookies.isPresent()) {
				String cookie_value = cookies.get();
				
				// Base64 Decoding
				byte[] decoded_bytes = Base64.getDecoder().decode(cookie_value);
				String decoded_cookie = new String(decoded_bytes);
				
				ABox public_user_info = new ABox();
				public_user_info.setJson(decoded_cookie);
				
				// 세션 생성
				httpSession = request.getSession();
				
				// 세션 정보 설정
				httpSession.setAttribute("loginPublicSeq", public_user_info.getString("loginPublicSeq"));
				httpSession.setAttribute("loginSNSIntegSeq", public_user_info.getString("loginSNSIntegSeq"));
				httpSession.setAttribute("loginIntegTarget", public_user_info.getString("loginIntegTarget"));
				httpSession.setAttribute("loginPublicId", public_user_info.getString("loginPublicId"));
				httpSession.setAttribute("loginName", public_user_info.getString("loginName"));
				httpSession.setAttribute("loginPhoneNum", public_user_info.getString("loginPhoneNum"));
				httpSession.setAttribute("loginProfileImg", public_user_info.getString("loginProfileImg"));

			}
		}
		
		// [6] session 정보 중 ABox에 저장할 정보 추출
		if (httpSession != null) {
			// HttpSession 객체를 ASession 객체로 변환함.
			aSession.setSession(this.httpSession);
			
			aBox.setIfEmpty("loginPublicSeq", httpSession.getAttribute("loginPublicSeq"));
			aBox.setIfEmpty("loginSNSIntegSeq", httpSession.getAttribute("loginSNSIntegSeq"));
			aBox.setIfEmpty("loginIntegTarget", httpSession.getAttribute("loginIntegTarget"));
			aBox.setIfEmpty("loginPublicId", httpSession.getAttribute("loginPublicId"));
			aBox.setIfEmpty("loginName", httpSession.getAttribute("loginName"));
			aBox.setIfEmpty("loginPhoneNum", httpSession.getAttribute("loginPhoneNum"));
			aBox.setIfEmpty("loginProfileImg", httpSession.getAttribute("loginProfileImg"));
		}
	
		// [8] HISTORY Table에 저장 		
		String userAgent = header.getString("user-agent");
		String browserType = "";
		if (userAgent.contains("NAVER(inapp;")) {
			browserType += "NAVER";		
		} else if (userAgent.contains("MSIE")) {
			browserType += "IE";			
			if (userAgent.contains("Trident/4.0")) {
				browserType += "8";				
			} else if (userAgent.contains("Trident/5.0")) {
				browserType += "9";			
			} else if (userAgent.contains("Trident/6.0")) {
				browserType += "10";				
			} else if (userAgent.contains("MSIE 7")) {
				browserType += "7";				
			} else if (userAgent.contains("MSIE 6")) {
				browserType += "6";				
			}
		} else if (userAgent.contains("Firefox")) {
			browserType += "Firefox";			
		} else if (userAgent.contains("Opera")) {
			browserType += "Opera";			
		} else if (userAgent.contains("Chrome")) {
			browserType += "Chrome";			
		} else if (userAgent.contains("Safari")) {
			browserType += "Safari";			
		} else if (userAgent.contains("Trident/7.0")) {
			browserType += "IE 11";			
		} else if (userAgent.contains("Dalvik")) {
			browserType += "Dalvik";			
		} else if (userAgent.contains("KAKAOTALK")) {
			browserType += "KAKAOTALK";			
		} else if (userAgent.contains("facebookexternalhit")) {
			browserType += "facebook";			
		} else if (userAgent.contains("AndroidDownloadManager")) {
			browserType += "AndroidDownloadManager";			
		} else {
			browserType += "Unknown Browser";
		}
		
		aBox.set("browser",browserType);
		aBox.set("userAgent",userAgent);
		aBox.set("serverName",request.getServerName());		
		
		
		
		aBox.println();
		// 접속 HISTORY DB INSERT
//		int insertCnt = commonDao.insert("mybatis.common.util.util_mapper.insertAccessHistory", aBox);
//		
//		if (insertCnt > 0) {
//			System.out.println("접속 히스토리 성공");
//		} else {
//			System.out.println("접속 히스토리 실패");
//		}
//
//		// [9] requset 영역에 aBox를 저장하여 Controller로 보냄
//		request.setAttribute("aBox", aBox);

		return true;
	}
}