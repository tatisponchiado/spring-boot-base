package info.agilite.spring.base.bugsnag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@Import(BugsnagSpringConfiguration.class)
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
@AllArgsConstructor(access=AccessLevel.PACKAGE)
public class BugsnagConfig {
	@Value("${info.agilite.spring.base.crash-report-key:#{null}}")
	String bugsnagKey;
	
	@Bean
	public Bugsnag bugsnag() {
		return new Bugsnag(bugsnagKey);
	}
}
