package one.design.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.design.domain.User;
import one.design.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<User> signUp(@RequestBody User newUser){
        if (userService.findByUserId(newUser).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복 아이디");
        }

        userService.signUp(newUser);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/signIn")
    public ResponseEntity<User> signIn(@RequestBody User user, HttpServletRequest request){
        if (userService.findByUserId(user).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 실패");
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", user);
        return ResponseEntity.ok(user);
    }

}
