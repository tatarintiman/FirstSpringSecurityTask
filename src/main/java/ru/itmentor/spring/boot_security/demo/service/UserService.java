package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UsersRepository;

import java.util.List;

@Service
public class UserService {

    private final UsersRepository userRepository;
    @Autowired
    public UserService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findById(long id){
        return userRepository.getOne(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void deleteById(long id){
        userRepository.deleteById(id);
    }
}
