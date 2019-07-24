package info.agilite.spring.base.mail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.InputStreamSource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level=AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
public class Mail {
    final String to;
    final String subject;
    final String templateName;
    String from;
    String fromName;
    
    @Builder.Default()
    Map<String, Object> model = new HashMap<>();
    
    @Builder.Default()
    Map<String, InputStreamSource> attachments = new HashMap<>();
    
    public Mail putModel(String key, Object value) {
    	this.model.put(key, value);
    	return this;
    }
    
    public Mail attach(String nome, InputStreamSource attachment) {
    	this.attachments.put(nome, attachment);
    	return this;
    }
}