package one.design.service;


import lombok.RequiredArgsConstructor;
import one.design.domain.User;
import one.design.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signUp(User user){
        userRepository.save(user);
    }

    public Optional<User> findByUserId(User user){
        return userRepository.findByUserId(user.getUserId());
    }



}
