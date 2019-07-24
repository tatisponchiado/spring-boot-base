package info.agilite.spring.base.bugsnag;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;

@Configuration
@Import(BugsnagSpringConfiguration.class)
public class BugsnagConfig {
	@Bean
	public Bugsnag bugsnag() {
		return new Bugsnag("5bbd346badb90ce8c05d23e5b01281a1");
	}
}