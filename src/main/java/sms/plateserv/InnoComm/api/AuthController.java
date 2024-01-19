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
package sms.plateserv.InnoComm.api;

import java.util.Set;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.plateserv.InnoComm.config.JwtAuthenticationResponse;
import sms.plateserv.InnoComm.config.JwtTokenProvider;
import sms.plateserv.InnoComm.config.JwtTokenValidator;
import sms.plateserv.InnoComm.entitys.RefreshToken;
import sms.plateserv.InnoComm.entitys.Role;
import sms.plateserv.InnoComm.enums.ErrorMessages;
import sms.plateserv.InnoComm.exception.BaseException;
import sms.plateserv.InnoComm.exception.UserLoginException;
import sms.plateserv.InnoComm.model.CustomUserDetails;
import sms.plateserv.InnoComm.model.LoginRequest;
import sms.plateserv.InnoComm.model.LoginResponse;
import sms.plateserv.InnoComm.model.RegistrationRequest;
import sms.plateserv.InnoComm.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger logger = LogManager.getLogger(AuthController.class);
	private final AuthService authService;
	private final JwtTokenProvider tokenProvider;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	JwtTokenValidator jwtTokenValidator;

	@Autowired
	public AuthController(AuthService authService, JwtTokenProvider tokenProvider,
			ApplicationEventPublisher applicationEventPublisher) {
		this.authService = authService;
		this.tokenProvider = tokenProvider;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@PostMapping("/login")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authService.authenticateUser(loginRequest)
				.orElseThrow(() -> {
					UserLoginException e = new UserLoginException("Couldn't login user [" + loginRequest + "]");
					logger.error("Exception occurred while authenticating user {}", e);
					return e;
					});

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
				.map(RefreshToken::getToken).map(refreshToken -> {
					String jwtToken = authService.generateToken(customUserDetails);
					JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(jwtToken,
							refreshToken, tokenProvider.getExpiryDuration());
					LoginResponse loginResponse = new LoginResponse();
					loginResponse.setJwtAuthenticationResponse(jwtAuthenticationResponse);
					loginResponse.setEmployeeId(customUserDetails.getId());
					Set<Role> roles = customUserDetails.getRoles();
					loginResponse.setRoles(roles);
					return ResponseEntity.ok(loginResponse);
				})
				.orElseThrow(() -> { 
					UserLoginException e = new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]");
					logger.error("Exception occurred while creating refresh token {}", e);
					return e;
				});
	}

	@PostMapping("/register")
	public ResponseEntity registerUser(@RequestBody RegistrationRequest registrationRequest) {
		try {
			authService.createUser(registrationRequest);
			return ResponseEntity.ok("User registered successfully.");
		} catch (Exception e) {
			logger.error("Exception occurred while registring {}", e);
			throw new BaseException(ErrorMessages.INVALID_DATA, "Missing user object in database");
		}
	}

}
