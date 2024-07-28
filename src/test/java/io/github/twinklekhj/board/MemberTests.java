package io.github.twinklekhj.board;

import io.github.twinklekhj.board.api.controller.UserController;
import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.login.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class MemberTests {
    @Autowired
    private MockMvc mockMvc;

    private Member member;

    @BeforeEach
    public void setup() {
        this.member = Member.builder()
                .memberId("admin")
                .name("관리자")
                .password("admin")
                .email("admin@domain.com")
                .role(Role.ADMIN)
                .build();
    }
}
