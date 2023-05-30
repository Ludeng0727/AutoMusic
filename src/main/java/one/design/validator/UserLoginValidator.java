package one.design.validator;

import lombok.RequiredArgsConstructor;
import one.design.domain.User;
import one.design.dto.UserLoginDto;
import one.design.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserLoginValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserLoginDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserLoginDto userLoginDto = (UserLoginDto) target;

        if (userLoginDto.getUserId().length() == 0){
            errors.rejectValue("userId", "userId", "아이디를 입력해주세요");
        }
        if (userLoginDto.getPw().length() == 0){
            errors.rejectValue("pw","pw", "패스워드를 입력해주세요");
        }

        Optional<User> findUser = userService.findByUserId(userLoginDto.getUserId());
        if(findUser.isEmpty()){
            errors.rejectValue("userId", "userId","로그인 실패");
        }

        if(findUser.isPresent()){
            if(!Objects.equals(findUser.get().getPw(), userLoginDto.getPw())){
                errors.rejectValue("pw", "pw","로그인 실패");
            }
        }
    }
}
