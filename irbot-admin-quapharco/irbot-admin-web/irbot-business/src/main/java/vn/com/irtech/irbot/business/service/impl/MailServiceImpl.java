package vn.com.irtech.irbot.business.service.impl;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import vn.com.irtech.irbot.business.dto.MailTemplate;
import vn.com.irtech.irbot.business.service.IMailService;
import vn.com.irtech.irbot.business.type.Charset;


@Service
public class MailServiceImpl implements IMailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine templateEngine;

	@Value("${spring.mail.username:@null}")
	private String fromAddress;

	@Value("${spring.mail.personal:@null}")
	private String fromPersonal;

	@Override
	public void sendHtmlMail(MailTemplate template, String templateName, String fileName, ByteArrayResource input) throws Exception {
		// TODO Auto-generated method stub
		MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, Charset.UTF_8.value());

        Context context = new Context();
        context.setVariables(template.getProps());

        String html = templateEngine.process(templateName, context);
        helper.setFrom(fromAddress, fromPersonal);
        helper.setTo(template.getTo());
        helper.setSubject(template.getSubject());
        helper.setText(html, true);
        helper.addAttachment(fileName, input);

        mailSender.send(message);
		
	}

}
