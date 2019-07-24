package info.agilite.spring.base.security;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

	/**
	 * Efetuar o login do usuário
	 * @param username
	 * @param password
	 * @return
	 */
	Optional<SecurityUser> login(String username, String password);

	/**
	 * Localizar o usuário logado pelo token
	 * @param username
	 * @param password
	 * @return
	 */
	Optional<SecurityUser> retrieveByToken(String token);

	/**
	 * Efetuar logout
	 * @param username
	 * @param password
	 * @return
	 */
	void logout(SecurityUser user);

}