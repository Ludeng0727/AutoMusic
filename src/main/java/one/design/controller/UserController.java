package one.design.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.design.domain.User;
import one.design.dto.UserLoginDto;
import one.design.dto.UserSignUpDto;
import one.design.error.SimpleError;
import one.design.service.UserService;
import one.design.validator.UserLoginValidator;
import one.design.validator.UserSignUpValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserSignUpValidator userSignUpValidator;
    private final UserLoginValidator userLoginValidator;

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<Object> signUp(@RequestBody UserSignUpDto userSignUpDto, BindingResult bindingResult){
        userSignUpValidator.validate(userSignUpDto, bindingResult);
        if(bindingResult.hasErrors()){
            List<SimpleError> code = bindingResult.getAllErrors().stream()
                    .map(objectError -> new SimpleError(objectError.getCode(), objectError.getDefaultMessage())).collect(Collectors.toList());
            return new ResponseEntity<>(code, HttpStatus.BAD_REQUEST);
        }

        User newUser = new User(userSignUpDto.getUserId(), userSignUpDto.getPw(), userSignUpDto.getNickname());
        userService.signUp(newUser);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/signIn")
    public ResponseEntity<Object> signIn(@RequestBody UserLoginDto userLoginDto, BindingResult bindingResult){
        userLoginValidator.validate(userLoginDto, bindingResult);
        if (bindingResult.hasErrors()){
            List<SimpleError> code = bindingResult.getAllErrors().stream()
                    .map(objectError -> new SimpleError(objectError.getCode(), objectError.getDefaultMessage())).collect(Collectors.toList());
            return new ResponseEntity<>(code, HttpStatus.BAD_REQUEST);
        }

        String userId = userLoginDto.getUserId();
        String token = userService.signIn(userId);
        HashMap<String,String> result = new HashMap<>();
        result.put("token", token);

        return ResponseEntity.ok().body(result);
    }

}
