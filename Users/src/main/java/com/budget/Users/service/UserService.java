package com.budget.Users.service;

import com.budget.Users.LogUtil;
import com.budget.Users.model.User;
import com.budget.Users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    LogUtil log;

    final private Pattern validEmailRegex = Pattern.compile("^[a-zA-Z0-9-_~+.]{2,30}@[a-zA-Z0-9]{2,10}(\\.[a-zA-Z]{2,3}){1,2}$");
    final private Pattern validPasswordRegex = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-!@#$%^&*()_./]).{8,}$");
    public void newUser (User user){
        try {
            validateRegistration(user.getEmail(), user.getPassword());
            user.setIdentifier(uuidGenerator(user.getEmail()));
            user.setHashedEmail(hashGenerator(user.getEmail()));
            user.setPassword(hashPassword(user.getPassword()));
            userRepository.save(user);
        }catch (DuplicateKeyException e){
            String uuid = log.warnGeneral(e, "Attempt to register with existing E-mail: " + user.getHashedEmail());
            throw new IllegalArgumentException("Unable to create user ref.: " + uuid);
            // OPTIONALLY: send informational E-mail to given address with "report abuse" link
        }
    }

    private String uuidGenerator (String email){
        try {
            String userPortion = email.split("@")[0].toLowerCase();
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(email.getBytes(StandardCharsets.UTF_8));
            int suffix = ((hash[0]) & 0xFF) % 100;
            return userPortion + suffix;
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("SHA-256 not available");
        }
    }

    private String hashGenerator (String email){
            return encoder.encode(email);
    }

    private String hashPassword (String password){
        return encoder.encode(password);
    }
    private void validateRegistration (String email, String password){
        if(!validEmailRegex.matcher(email).matches())
            throw new IllegalArgumentException("Invalid E-mail");
        if (!validPasswordRegex.matcher(password).matches())
            throw new IllegalArgumentException("Invalid password");
    }
}
