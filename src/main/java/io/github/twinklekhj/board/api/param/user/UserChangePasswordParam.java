package io.github.twinklekhj.board.api.param.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserChangePasswordParam {
    private String password;
    private String newPassword;
    private String newPasswordConfirm;

    public boolean validate(){
        return !password.equals(newPassword) && newPassword.equals(newPasswordConfirm);
    }
}
