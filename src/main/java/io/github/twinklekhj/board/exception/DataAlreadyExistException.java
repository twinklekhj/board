package io.github.twinklekhj.board.exception;

public class DataAlreadyExistException extends RuntimeException{
    public DataAlreadyExistException(String message){
        super(message);
    }
}
