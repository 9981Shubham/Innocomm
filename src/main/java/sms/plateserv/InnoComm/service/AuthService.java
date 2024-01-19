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
package sms.plateserv.InnoComm.service;

import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sms.plateserv.InnoComm.config.JwtTokenProvider;
import sms.plateserv.InnoComm.entitys.RefreshToken;
import sms.plateserv.InnoComm.entitys.Role;
import sms.plateserv.InnoComm.entitys.User;
import sms.plateserv.InnoComm.entitys.UserWallet;
import sms.plateserv.InnoComm.enums.RoleName;
import sms.plateserv.InnoComm.model.CustomUserDetails;
import sms.plateserv.InnoComm.model.LoginRequest;
import sms.plateserv.InnoComm.model.RegistrationRequest;
import sms.plateserv.InnoComm.repository.RoleRepository;
import sms.plateserv.InnoComm.repository.UserRepository;
import sms.plateserv.InnoComm.repository.UserWalletRepository;

@Service
public class AuthService {

	private static final Logger LOGGER = LogManager.getLogger(AuthService.class);
	private final JwtTokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final RoleRepository roleRepository;
	private final UserWalletRepository userWalletRepository;
	private final UserRepository userRepository;

	@Autowired
	public AuthService(JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService,
			PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository,
			UserWalletRepository userWalletRepository, UserRepository userRepository) {
		this.tokenProvider = tokenProvider;
		this.refreshTokenService = refreshTokenService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.roleRepository = roleRepository;
		this.userWalletRepository = userWalletRepository;
		this.userRepository = userRepository;
	}

	public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
		LOGGER.debug("User is authenticated in authenticateUser() method");
		return Optional.ofNullable(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())));
	}

	public User createUser(RegistrationRequest registerRequest) {
		LOGGER.debug("Creatring user");
		User newUser = new User();
		newUser.setEmail(registerRequest.getEmail());
		newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		newUser.setUsername(registerRequest.getUsername());
		Set<Role> role = roleRepository.findByRoleName("ROLE_ADMIN");
		if (role.size() == 0) {
			Role newRole = new Role();
			newRole.setRole(RoleName.ROLE_ADMIN);
			role.add(roleRepository.save(newRole));
		}
		newUser.addRoles(role);
		UserWallet wallet = new UserWallet();
		wallet.setBalance(Double.valueOf(100));
		UserWallet savedWallet = userWalletRepository.save(wallet);
		newUser.setUserWallet(savedWallet);
		newUser.setActive(true);
		userRepository.save(newUser);
		return newUser;
	}

	public String generateToken(CustomUserDetails customUserDetails) {
		LOGGER.debug("Token is generated in generateToken() method");
		return tokenProvider.generateToken(customUserDetails);
	}

	public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication,
			LoginRequest loginRequest) {
		LOGGER.debug("refresh token is created in createAndPersistRefreshTokenForDevice() method");
		User currentUser = (User) authentication.getPrincipal();
		RefreshToken refreshToken = refreshTokenService.createRefreshToken();
		refreshToken = refreshTokenService.save(refreshToken);
		return Optional.ofNullable(refreshToken);
	}
}
