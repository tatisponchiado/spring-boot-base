package info.agilite.spring.base.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;

import info.agilite.spring.base.RestMapping;
import info.agilite.spring.base.security.AuthenticationService;
import info.agilite.spring.base.security.SecurityUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestMapping("/logout")
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
@AllArgsConstructor(access=AccessLevel.PACKAGE)
public class LogoutController {
	AuthenticationService service;
	
	/**
	 * Método responsável pelo logout no SpringSecurity
	 */
	@PostMapping
	public void logout(@AuthenticationPrincipal SecurityUser user){
		service.logout(user);
	}
}
