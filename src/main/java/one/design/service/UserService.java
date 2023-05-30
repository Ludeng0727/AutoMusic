package one.design.service;


import lombok.RequiredArgsConstructor;
import one.design.domain.User;
import one.design.repository.UserRepository;
import one.design.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 30l;
    private final UserRepository userRepository;


    public void signUp(User user){
        userRepository.save(user);
    }
    public String signIn(String userId){
        return JwtUtil.createJwt(userId, secretKey, expiredMs);

    }

    public Optional<User> findByUserId(String userId){
        return userRepository.findByUserId(userId);
    }

}
