package vn.com.irtech.core.framework.mail.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	TemplateEngine templateEngine;

	public void prepareAndSend(String from, String title, String subject, String recipient,
			Map<String, Object> variables, String template) throws MessagingException {
		// Prepare the evaluation context
		Context ctx = new Context();
		ctx.setVariables(variables);
		String htmlContent = templateEngine.process(template, ctx);
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		try {
			helper.setFrom(from, title);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		helper.setSubject(subject);
		helper.setTo(InternetAddress.parse(recipient)); // send multiple recipients
		helper.setText(htmlContent, true);
		// Send email
		mailSender.send(mimeMessage);
	}

	public void prepareAndSend(String from, String title, String subject, String recipient,
			Map<String, byte[]> fileAttachmentMap, Map<String, Object> variables, String template)
			throws MessagingException {
		// Prepare the evaluation context
		Context ctx = new Context();
		ctx.setVariables(variables);
		String htmlContent = templateEngine.process(template, ctx);
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		try {
			helper.setFrom(from, title);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		helper.setSubject(subject);
		helper.setTo(InternetAddress.parse(recipient)); // send multiple recipients
		helper.setText(htmlContent, true);
		if (fileAttachmentMap != null) {
			for (Map.Entry<String, byte[]> entry : fileAttachmentMap.entrySet()) {
				helper.addAttachment(entry.getKey(), new ByteArrayResource(entry.getValue()));
			}
		}
		mailSender.send(mimeMessage); // Send email
	}
}
