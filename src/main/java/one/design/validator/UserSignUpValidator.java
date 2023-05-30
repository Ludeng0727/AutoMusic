package one.design.validator;

import lombok.RequiredArgsConstructor;
import one.design.dto.UserSignUpDto;
import one.design.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserSignUpValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserSignUpDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSignUpDto userSignUpDto = (UserSignUpDto) target;

        if (userSignUpDto.getUserId().length() < 8){
            errors.rejectValue("userId", "userId","아이디는 최소 8자 이상이어야 합니다.");
        }
        if (userService.findByUserId(userSignUpDto.getUserId()).isPresent()){
            errors.rejectValue("userId","userId", "중복된 아이디입니다.");
        }
        if (userSignUpDto.getPw().length() < 8){
            errors.rejectValue("pw", "pw","패스워드는 최소 8자 이상이어야 합니다.");
        }
        if(userSignUpDto.getNickname().length() < 2){
            errors.rejectValue("nickname", "nickname","닉네임은 최소 2자 이상이어야 합니다.");
        }

    }
}
