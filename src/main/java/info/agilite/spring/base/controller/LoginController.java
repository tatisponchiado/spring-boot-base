package info.agilite.spring.base.controller;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import com.google.common.base.Splitter;

import info.agilite.spring.base.RestMapping;
import info.agilite.spring.base.security.AuthenticationService;
import info.agilite.utils.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestMapping("${api-login-url:/public/login}")
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
@AllArgsConstructor(access=AccessLevel.PACKAGE)
public class LoginController {
	AuthenticationService service;
	
	/**
	 * Método responsável pelo login no SpringSecurity
	 * @param username Login do usuário
	 * @param senha Senha
	 * @return SecurityUser
	 */
	@PostMapping
	@Transactional
	public ResponseEntity<Map<String, Object>> login(
			@RequestParam("username") final String username, 
			@RequestParam("senha") final String senha){
		return service.login(username, senha)
				.map(user -> 
					ResponseEntity.status(HttpStatus.OK)
						.body(
								Utils.mapByPattern(
										"token, data", 
										user.getToken(), 
										user.getDataToSendOnLogin()))
			).orElseThrow(()-> new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválido"));
	}
	
	@PostMapping("{code}")
	@Transactional
	public ResponseEntity<Map<String, Object>> login(@PathVariable("code") final String code){
		String loginSenha = new String(Base64.getUrlDecoder().decode(code));
		List<String> splitter = Splitter.on("|").splitToList(loginSenha);
		return service.login(splitter.get(0), splitter.get(1))
				.map(user -> 
					ResponseEntity.status(HttpStatus.OK)
						.body(
								Utils.mapByPattern(
										"token, data", 
										user.getToken(), 
										user.getDataToSendOnLogin()))
			).orElseThrow(()-> new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválido"));
	}
}
