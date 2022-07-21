package org.codewithoutus.tgbotusers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test", "bot"})
class TgBotUsersApplicationTests {

	@Test
	void contextLoads() {
	}

}
