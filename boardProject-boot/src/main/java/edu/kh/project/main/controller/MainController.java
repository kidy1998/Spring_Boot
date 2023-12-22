package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;

@Controller
@SessionAttributes({"loginMember"})
public class MainController {
	
	@RequestMapping("/")
	public String mainForward(Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember
			) {
		
		model.addAttribute("name", "홍길동");
		
		// Spring MVC : /webapp/WEB-INF/views/common/main.jsp
		
		// Spring Boot (+ thymeleaf 템플릿 엔진)
		// src/main/resources/templates/common/main.html
		
		return "common/main";
	}
	
	@GetMapping("/loginError")
	public String loginError(RedirectAttributes ra) {
		
		ra.addFlashAttribute("message", "로그인 후 이용해주세요");
		return "redirect:/";
	}
	
}
