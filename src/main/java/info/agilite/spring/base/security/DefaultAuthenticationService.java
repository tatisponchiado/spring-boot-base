package info.agilite.spring.base.security;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import info.agilite.spring.base.session.UserSessionCacheByToken;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
/**
 * Serviço default de autenticação
 * @author Rafael Battazza
 *
 */

@Service
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService{
	@NonNull SecurityUserService userService;
	@NonNull TokenService tokenService;
	@NotNull SecurityUserCacheByToken cache;
	@NotNull UserSessionCacheByToken sessions;

	@Override
	public Optional<SecurityUser> login(String username, String password) {
		Optional<SecurityUser> optionalUser = userService.login(username, password);
		optionalUser.ifPresent(user -> {
			if(user.getToken() != null) {
				cache.remove(user.getToken());
			}
			String jwt = userService.createNewToken(user);
			
			user.setToken(jwt);
			
			cache.put(jwt, user.getUsername());
			sessions.put(jwt, user);
			
			userService.saveUser(user, jwt);
		});
		
		return optionalUser;
	}

	@Override
	public Optional<SecurityUser> retrieveByToken(String token) {
		try {
			tokenService.decode(token);
		}catch (io.jsonwebtoken.ExpiredJwtException exc) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Authorization header expired"); 
		}catch (Exception exc) {
			throw new RuntimeException("Invalid authorization header", exc);
		}
		
		Optional<String> cached = cache.get(token);
		if(!cached.isPresent())throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User has been disconnected");
		
		Optional<SecurityUser> optionalSecurityUser = sessions.get(token);
		if(optionalSecurityUser.isPresent())return optionalSecurityUser;
		 
		Optional<SecurityUser> secureUser = userService.findByToken(token);
		secureUser.ifPresent(u -> {
			sessions.put(u.getToken(), u);	
		});
				
		return secureUser;
	}

	@Override
	public void logout(SecurityUser user) {
		cache.remove(user.getToken());
		userService.saveUser(user, null);
	}
}
