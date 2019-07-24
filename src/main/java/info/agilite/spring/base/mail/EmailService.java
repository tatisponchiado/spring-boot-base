package info.agilite.spring.base.mail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.base.Splitter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import info.agilite.utils.StringUtils;

@Service
public class EmailService {
	@Value("${spring.mail.from:#{null}}")
	private String from;
	
	@Value("${spring.mail.from-name:#{null}}")
	private String fromName;
	
    @Autowired(required=false)
    private JavaMailSender emailSender;

    @Autowired
    private Configuration freemarkerConfig;

    public void sendSimpleMessage(Mail mail) throws MessagingException, IOException, TemplateException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        for(String name : mail.getAttachments().keySet()) {
        	helper.addAttachment(name, mail.getAttachments().get(name));
        }

        Template t = freemarkerConfig.getTemplate(mail.getTemplateName()+".ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

        List<String> tos = Splitter.on(";").trimResults().splitToList(mail.getTo());
        for(String to : tos) {
        	if(StringUtils.isNullOrEmpty(to))continue;
        	helper.addTo(to);
        }
        
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        if(mail.getFrom() != null) {
        	helper.setFrom(mail.getFrom(), mail.getFromName());
        }else {
        	helper.setFrom(from, fromName);
        }
        
        emailSender.send(message);
    }

}