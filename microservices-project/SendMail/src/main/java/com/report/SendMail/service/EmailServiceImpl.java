package com.report.SendMail.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.mail.SimpleMailMessage;

import com.report.SendMail.modal.EmailVo;
import com.report.SendMail.modal.EmailVo.Templates;


import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
	Configuration fmConfiguration;

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String sender;
	
//	@Value("${attachments.base.path}")
//	private String attachmentBasePath;
	
	
	@Override
	public String sendSimpleMail(EmailVo email) {
		// TODO Auto-generated method stub
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setFrom(sender);
		mailMessage.setTo(email.getRecipients());
		mailMessage.setText(email.getMsgBody());
		mailMessage.setSubject(email.getSubject());
		
		javaMailSender.send(mailMessage);
		
		return "Mail Sent Successfully";
	}

	@Override
	public String sendMailWithAttachment(EmailVo email) throws Exception {
		// TODO Auto-generated method stub
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		
		
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			if(email.getRecipients().length > 0) 
				mimeMessageHelper.setTo(email.getRecipients());
			if(email.getCarbonCopy().size() > 0 ) {
				InternetAddress[] ccArray = email.getCarbonCopy()
						.stream()
						.map(cc -> {
							try {
								return new InternetAddress(cc);
							}catch(AddressException e){
								e.printStackTrace();
								return e.getLocalizedMessage();
							}
						})
						.toArray(InternetAddress[]::new);
				mimeMessageHelper.setCc(ccArray);
			}
			if(email.getBcc().size() > 0 ) {
				InternetAddress[] bccArray = email.getBcc()
						.stream()
						.map(bcc -> {
							try {
								return new InternetAddress(bcc);
							}catch(AddressException e){
								e.printStackTrace();
								return e.getLocalizedMessage();
							}
						})
						.toArray(InternetAddress[]::new);
				mimeMessageHelper.setCc(bccArray);
			}
			
			email.setMsgBody(getContentFromTemplate(email.getTemplateId(),email.getTemplateParams()));
			mimeMessageHelper.setText(email.getMsgBody(), true);
			mimeMessageHelper.setSubject(email.getSubject());
			
			if(!email.getTemplateParams().getReplyTo().equals(""))
				mimeMessageHelper.setReplyTo(email.getTemplateParams().getReplyTo());
			
			
//			String attachmentPath = attachmentBasePath + email.getAttachment();
//            FileSystemResource file = new FileSystemResource(new File(attachmentPath));

			
			
			FileSystemResource file = new FileSystemResource(new File(email.getAttachment()));
			if(file.getFilename().endsWith(".xlsx"))
				mimeMessageHelper.addAttachment(file.getFilename(), file);
			
			if((email.getNoDataReportRequired().equals("Y")) ||
					(email.getNoDataReportRequired().equals("N") && (!email.getTemplateParams().getRecordCount().equals("0")))) {
				javaMailSender.send(mimeMessage);
				return "Mail Sent Successfully";
			}
			return "No data reportis set as not required.";
			
		}
		catch(MessagingException e){
			e.printStackTrace();
			return "Error while sending Mail!";
		}
		
	}

	private String getContentFromTemplate(String templateId, Templates model) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		// TODO Auto-generated method stub
		StringBuffer content = new StringBuffer();
		System.out.println("Model Name: "+ model.getReportName() +", " + "Date As On: "+ model.getDataAsOn());
		
		if(model.getReportLink().equals(null) || model.getReportLink().isEmpty()) {
			Map<String, Object> modelMap = new HashMap<>();
			modelMap.put("model", model); // Put the Templates object under the key "model"
			content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate("no_data_available.html"), modelMap));
			return content.toString();
		}
		
		
//		content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateId), model));
		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("model", model); // Put the Templates object under the key "model"
		content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateId), modelMap));
		return content.toString();
	}
	
	

}
