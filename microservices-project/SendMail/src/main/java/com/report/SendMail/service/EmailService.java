package com.report.SendMail.service;

import com.report.SendMail.modal.EmailVo;

public interface EmailService {
	
	String sendSimpleMail(EmailVo details);
	
	String sendMailWithAttachment(EmailVo detail) throws Exception;

}
