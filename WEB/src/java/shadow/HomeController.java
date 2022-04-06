package shadow;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import common.collection.ABox;
import common.controller.SuperController;

@RestController
public class HomeController extends SuperController {

	@RequestMapping(value = { "", "/", "/test" }, method = { RequestMethod.GET })
	public ModelAndView index(@ModelAttribute("initBoxs") ABox aBox, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("web/test");
		mav.addObject("aBox", aBox);
		return mav;
	}	

}