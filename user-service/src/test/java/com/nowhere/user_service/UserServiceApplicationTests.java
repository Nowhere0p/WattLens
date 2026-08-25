package com.nowhere.user_service;

import com.nowhere.user_service.entity.User;
import com.nowhere.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class UserServiceApplicationTests {

	private  static  final int USERS = 10;
	@Autowired
	private UserRepository userRepository;
	@Test
	void contextLoads() {
	}
	@Disabled
	@Test
	void AddUsersToDb(){
		for(int i= 1;i<=USERS;i++){
			var user = User.builder()
					.firstName("USER")
					.lastName(""+i)
					.address("Address"+i)
					.email("user"+i+"@example.com")
					.alerting(i%2==0)
					.energyAlertingThreshold(1000.0 *i)
					.build();
			userRepository.save(user);
		}
		log.info("User Repository populated successfully");
	}

}
