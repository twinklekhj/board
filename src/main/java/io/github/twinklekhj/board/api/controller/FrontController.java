package io.github.twinklekhj.board.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontController {
    @RequestMapping(path = {
        "/", // 홈 화면
        "/signup", "/me", // 사용자 관리 화면
        "/boards/**", // 게시판 화면
    })
    public String redirect() {
        return "forward:/index.html";
    }
}
