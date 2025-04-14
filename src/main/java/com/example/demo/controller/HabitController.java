package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repository.HabitRepository;

@Controller
@RequestMapping("/habit")
public class HabitController {
	 @Autowired
	    private HabitRepository habitRepository;

	    @GetMapping("/new")
	    public String newHabitForm() {
	        return "new";
	    }

	    @PostMapping("/new")
	    public String createHabit(@RequestParam String name) {
	        habitRepository.save(name);
	        return "redirect:/";
	    }
}
