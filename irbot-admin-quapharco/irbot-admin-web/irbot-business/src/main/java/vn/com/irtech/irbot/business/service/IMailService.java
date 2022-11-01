package vn.com.irtech.irbot.business.service;

import org.springframework.core.io.ByteArrayResource;

import vn.com.irtech.irbot.business.dto.MailTemplate;

/**
 * Utility class used to send email
 * 
 * @author thiennv
 *
 */
public interface IMailService {
	
	public void sendHtmlMail(MailTemplate template, String templateName, String fileName, ByteArrayResource input) throws Exception;

}
