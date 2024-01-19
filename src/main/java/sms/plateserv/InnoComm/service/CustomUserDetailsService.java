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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sms.plateserv.InnoComm.entitys.User;
import sms.plateserv.InnoComm.model.CustomUserDetails;
import sms.plateserv.InnoComm.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LogManager.getLogger(AuthService.class);
	
	@Autowired
	public UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> dbUser = userRepository.findByEmail(email);
		LOGGER.info("Fetched user : " + dbUser + " by " + email);
		return dbUser.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(
				"Couldn't find a matching user email in the database for " + email));
	}

	public UserDetails loadUserById(Long id) {
		Optional<User> dbUser = userRepository.findById(id);
		LOGGER.info("Fetched user : " + dbUser + " by " + id);
		return dbUser.map(CustomUserDetails::new).orElseThrow(
				() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
	}
}
