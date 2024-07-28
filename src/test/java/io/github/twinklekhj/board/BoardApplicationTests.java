package io.github.twinklekhj.board;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootTest
class BoardApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(BoardApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	void decodeBase64(){
		byte[] keyBytes = new byte[64]; // 64 bytes = 512 bits
		new SecureRandom().nextBytes(keyBytes);

		String base64Key = Base64.getEncoder().encodeToString(keyBytes);
		log.info("Generated Secret Key: {}", base64Key);
	}
}
