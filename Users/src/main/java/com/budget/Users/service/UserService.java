package com.budget.Users.service;

import com.budget.Users.LogUtil;
import com.budget.Users.model.AuthResponse;
import com.budget.Users.model.User;
import com.budget.Users.model.UserLogin;
import com.budget.Users.repository.UserRepository;
import com.budget.Users.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    LogUtil log;
    @Autowired
    JwtUtil jwtUtil;

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

    public AuthResponse login (UserLogin loginUser){
        try {
            User retrievedUser = userRepository.getUserByEmail(loginUser.getEmail());
            if (encoder.matches(loginUser.getPassword(), retrievedUser.getPassword())) {
                String accessToken = jwtUtil.generateToken(retrievedUser.getIdentifier());
                String refreshToken = jwtUtil.generateRefreshToken(retrievedUser.getIdentifier());
                log.loginInfo(loginUser, "successful");
                return new AuthResponse(accessToken, refreshToken);
            }else{
                log.loginInfo(loginUser, "failed (invalid password: "+loginUser.getPassword()+")");
                throw new IllegalArgumentException();
            }
        }catch (EmptyResultDataAccessException e) {
            log.loginInfo(loginUser, "failed (invalid E-mail)");
            throw new IllegalArgumentException();
        }catch (IncorrectResultSizeDataAccessException e){
            log.warnGeneral(e, "Duplicate E-mail found in DB" + loginUser.getEmail());
            throw e;
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
        try {
            byte[] hashedBytes = MessageDigest.getInstance("SHA-256").digest(email.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        return encoder.encode(email);
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
