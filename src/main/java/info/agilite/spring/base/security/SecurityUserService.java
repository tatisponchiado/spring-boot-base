package info.agilite.spring.base.security;

import java.util.Optional;

public interface SecurityUserService {

	  /**
	   * Efetua o login através de {@code username} e {@code password}.
	   *
	   * @param username
	   * @param password
	   * @return um {@link Optional} com o usuário caso o login seja bem sucedido
	   */
	  Optional<SecurityUser> login(String username, String password);

	  /**
	   * Localiza o usuário a partir do token
	   *
	   * @param token JWT
	   * @return
	   */
	  Optional<SecurityUser> findByToken(String token);
	  
	  /**
	   * Salva o usuário no repositório quando o token é alterado pelo login
	   *
	   * @param token JWT
	   * @return
	   */
	  void saveUser(SecurityUser user, String newToken);
	  
	  
	  /**
	   * Configura user token
	   * @param user
	   * @return
	   */
	  String createNewToken(SecurityUser user);

}
