package info.agilite.spring.base.security;

import java.util.Optional;

public interface SecurityUserCacheByToken {
	Optional<String> get(String token);
	void put(String token, String user);
	void remove(String token);
}
