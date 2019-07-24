package info.agilite.spring.base.security;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenService {
	private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

	String secretKey;

	public TokenService(@Value("${jwt.secret:secret}") final String secret) {
		super();
		this.secretKey = BASE64.encode(requireNonNull(secret));
	}

	public String permanent(final Map<String, Object> value) {
		return Jwts.builder()
				.addClaims(value)
				.claim("time", System.currentTimeMillis())
				.signWith(HS256, secretKey)
				.compressWith(COMPRESSION_CODEC)
				.compact();
	}
	
	public String expiration(final Map<String, Object> value, int expirationMinutes) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MINUTE, expirationMinutes);
		
		return Jwts.builder()
				.addClaims(value)
				.claim("time", System.currentTimeMillis())
				.signWith(HS256, secretKey)
				.setExpiration(cal.getTime())
				.compressWith(COMPRESSION_CODEC)
				.compact();
	}
	
	public Claims decode(final String jwt){
		return Jwts.parser()         
	       .setSigningKey(secretKey)
	       .parseClaimsJws(jwt)
	       .getBody();
	}
}
