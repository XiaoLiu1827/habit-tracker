package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repository.HabitRepository;

@Controller
public class HomeController {

	@Autowired
	private HabitRepository habitRepository;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("habits", habitRepository.findAll());

		return "home";
	}

    @PostMapping("/habit/{id}/record")
    public String record(@PathVariable Long id, @RequestParam boolean success) {
        habitRepository.addRecord(id, success);
        
        // レコードをコンソールに出力
        System.out.println("--------------------------");
        habitRepository.getRecords(id).forEach(record -> {
            System.out.println(record.getId()+":"+"Habit ID: " + record.getHabitId() + ", Date: " + record.getDate() + ", Success: " + record.isSuccess());
        });
        
        return "redirect:/";
    }
    
//	@GetMapping("/habit/new")
//	public String createHabit() {
//		return "habit_create"; // 今はテンプレートなくてもOK
//	}
//
//	@GetMapping("/habit/streaks")
//	public String viewStreaks() {
//		return "streaks";
//	}
//
//	@GetMapping("/stats")
//	public String viewStats() {
//		return "stats";
//	}
}
