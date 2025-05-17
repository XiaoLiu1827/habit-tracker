package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/statistics")
@Controller
public class StatisticsController {

	@GetMapping("/view")
	public String home(Model model) {
		return "statistics";
	}
}
