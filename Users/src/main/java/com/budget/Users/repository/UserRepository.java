package com.budget.Users.repository;

import com.budget.Users.LogUtil;
import com.budget.Users.model.User;
import com.budget.Users.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    LogUtil log;

    private final String TABLE = "users";
    public void save(User user){
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO " + TABLE + " (email, hashed_email, uuid, password, role) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getHashedEmail());
                ps.setString(3, user.getIdentifier());
                ps.setString(4, user.getPassword());
                ps.setString(5, user.getRole());
                return ps;
            }, keyHolder);
    }

    public void changePassword (String password, String uuid){
        String sql = "UPDATE " + TABLE + " SET password = ? WHERE uuid = ?";
    }
//
//    public User getUserByEmail(String hashedMail) {
//        System.out.println("HASHED EMAIL = " + hashedMail);
//        String sql = "SELECT * FROM " + TABLE + " WHERE hashed_email = ?";
//        return jdbcTemplate.queryForObject(sql, new UserMapper(), hashedMail);
////        return jdbcTemplate.queryForObject(sql, new Object[]{hashedMail}, String.class);
//    }
//
    public String getUserByEmail(String hashedMail){
        System.out.println("HASHED EMAIL = " + hashedMail);
        String sql = "SELECT password FROM " + TABLE + " WHERE hashed_email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{hashedMail}, String.class);
    }
}