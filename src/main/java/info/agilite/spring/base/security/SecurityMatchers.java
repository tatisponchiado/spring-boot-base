package info.agilite.spring.base.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

public interface SecurityMatchers {
	RequestMatcher getPublicMatcher();
	RequestMatcher getProtectedMatcher();
}
