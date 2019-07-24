package info.agilite.spring.base.security;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.HttpClientErrorException;

import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PRIVATE, makeFinal = true)
final class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	TokenAuthenticationFilter(final RequestMatcher requiresAuth) {
		super(requiresAuth);
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {
			super.doFilter(req, res, chain);
		} catch (HttpClientErrorException e) {
			HttpServletResponse resp = (HttpServletResponse)res;
			resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			resp.setStatus(e.getStatusCode().value());
			String msg = e.getStatusCode().value() + " " + e.getStatusText();
			resp.getOutputStream().println("{\"message\":\"" + msg + "\"}");
		}
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
		final String token = ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
				.map(String::trim)
				.orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Authorization header not found"));

		final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
		return getAuthenticationManager().authenticate(auth);
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		boolean result = super.requiresAuthentication(request, response);
		
		return result;
	}
}
