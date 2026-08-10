package com.interviewcoach.service;
import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.interviewcoach.dto.UserRequestDto;
import com.interviewcoach.entity.User;
import com.interviewcoach.exception.UserNotFoundException;
import com.interviewcoach.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User createUser(User user) {            
        return userRepository.save(user);       
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + id));
    }

    public User updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = findById(id);
        existingUser.setFirstName(userRequestDto.getFirstName());
        existingUser.setLastName(userRequestDto.getLastName());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setPassword(userRequestDto.getPassword());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User existingUser = findById(id);
        userRepository.delete(existingUser);
    }

}
