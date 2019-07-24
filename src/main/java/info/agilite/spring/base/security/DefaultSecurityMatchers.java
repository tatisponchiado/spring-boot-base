package info.agilite.spring.base.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class DefaultSecurityMatchers implements SecurityMatchers{
	private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(new AntPathRequestMatcher("/public/**"), new AntPathRequestMatcher("/error/**"));
	private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
	
	@Override
	public RequestMatcher getPublicMatcher() {
		return PUBLIC_URLS;
	}

	@Override
	public RequestMatcher getProtectedMatcher() {
		return PROTECTED_URLS;
	}
}
