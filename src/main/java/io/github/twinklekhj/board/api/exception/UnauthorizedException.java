package io.github.twinklekhj.board.api.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("권한이 없는 사용자입니다.");
    }
}