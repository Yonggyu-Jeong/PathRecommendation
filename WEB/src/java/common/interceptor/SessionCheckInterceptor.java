package common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionCheckInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("##### SESSION CHECK INTERCEPTOR #####");
		boolean result = false;
		String context_path = request.getContextPath();

		try {
			String id = (String) request.getSession().getAttribute("loginPublicSeq");
			if (id == null) {
				if (isAjaxRequest(request)) {
					response.sendError(400);
					return false;
				} else {

					response.sendRedirect(context_path + "/user/login/loginForm.hs");
					result = false;
				}
			} else {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
		return result;
	}

	private boolean isAjaxRequest(HttpServletRequest req) {
		String header = req.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(header)) {
			return true;
		} else {
			return false;
		}
	}

}