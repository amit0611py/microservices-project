package com.report.SendMail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.report.SendMail.modal.EmailVo;
import com.report.SendMail.service.EmailService;

@RestController
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/emailcheck")
	public String homePage() {
		return "Welcome to Send Mail";
	}
	
	@PostMapping("/sendMail")
	public String sendMail(@RequestBody EmailVo email) throws Exception{
		String status = emailService.sendSimpleMail(email);
		
		return status;
	}
	
	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailVo email) throws Exception{
		String status = emailService.sendMailWithAttachment(email);
		
		return status;
	}
	
	

}
