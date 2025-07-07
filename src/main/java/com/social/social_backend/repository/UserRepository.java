// UserRepository.java (extender para incluir validación)
package com.social.social_backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean userExists(String userId) {
        String sql = "SELECT COUNT(*) FROM SOCIAL_UD.SOCIAL_USER WHERE USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }
}