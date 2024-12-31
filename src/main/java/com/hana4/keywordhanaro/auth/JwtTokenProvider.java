package com.hana4.keywordhanaro.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * 서버 전용 JWT 발행 및 검증
 */
@Component
public class JwtTokenProvider {
	@Value("${jwt.secret}")
	private String SECRET_KEY; // JWT Secret Key
	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간 (밀리초)

	public String createToken(String userId) {
		return Jwts.builder()
			.setSubject(userId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String getUserIdFromToken(String token) {
		Jws<Claims> jws = Jwts.parser()
			.verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
			.build()
			.parseSignedClaims(token);
		return jws.getPayload().getSubject();
	}

}
