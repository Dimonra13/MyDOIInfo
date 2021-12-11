package tfg.urjc.mydoiinfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.User;
import tfg.urjc.mydoiinfo.domain.repositories.UserRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private final HashSet<String> ADMIN_AUTHORITIES = new HashSet<>(Arrays.asList("ADMIN","USER"));
    private final HashSet<String> USER_AUTHORITIES = new HashSet<>(Arrays.asList("USER"));

    public User registerNewUser(String username, String password, String type){
        if (username == null || username.length()<8 || userRepository.findFirstByUsername(username)!=null
                || password==null || password.length()<8 || (!type.equals("ADMIN") && !type.equals("USER"))){
            return null;
        }
        User registeredUser = type.equals("ADMIN") ? new User(username,password, ADMIN_AUTHORITIES) :
                                        new User(username,password, USER_AUTHORITIES);
        return userRepository.save(registeredUser);
    }
}
