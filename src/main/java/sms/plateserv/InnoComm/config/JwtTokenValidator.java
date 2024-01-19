/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sms.plateserv.InnoComm.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import sms.plateserv.InnoComm.exception.InvalidTokenRequestException;

@Component
public class JwtTokenValidator {

	private final String jwtSecret;
	private static final Logger LOGGER = LogManager.getLogger(JwtTokenValidator.class);

	@Autowired
	public JwtTokenValidator(@Value("${app.jwt.secret}") String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	/**
	 * Validates if a token satisfies the following properties - Signature is not
	 * malformed - Token hasn't expired - Token is supported - Token has not
	 * recently been logged out.
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);

		} catch (SignatureException ex) {
			LOGGER.error("Invalid JWT signature : {}", ex);
			throw new InvalidTokenRequestException("JWT", authToken, "Incorrect signature");

		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token: {}", ex);
			throw new InvalidTokenRequestException("JWT", authToken, "Malformed jwt token");

		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token: {}", ex);
			throw new InvalidTokenRequestException("JWT", authToken, "Token expired. Refresh required");

		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token: {}", ex);
			throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");

		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty: {}", ex);
			throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
		}
		return true;
	}
}
