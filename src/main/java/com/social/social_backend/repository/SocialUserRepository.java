package com.social.social_backend.repository;


import com.social.social_backend.model.FriendChat;
import com.social.social_backend.model.GroupChat;
import com.social.social_backend.model.SocialUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SocialUserRepository {

    private final JdbcTemplate jdbcTemplate;


    public SocialUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<SocialUser> rowMapper = (rs, rowNum) -> {
        SocialUser user = new SocialUser();
        user.setUserId(rs.getString("USER_ID"));
        user.setUserName(rs.getString("USER_NAME"));
        user.setUserLastName(rs.getString("USER_LAST_NAME"));
        user.setEmail(rs.getString("EMAIL"));
        return user;
    };
    
    public SocialUser findById(String id) {
        String sql = "SELECT USER_ID, USER_NAME, USER_LAST_NAME, EMAIL, USER_UNIQUE_NAME, PHONE FROM SOCIAL_USER WHERE USER_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    public List<SocialUser> findAll() {
        String sql = "SELECT USER_ID, USER_NAME, USER_LAST_NAME, EMAIL FROM SOCIAL_USER";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public int register(SocialUser user) {
        String sql = """
        INSERT INTO SOCIAL_USER (
            USER_ID, USER_NAME, USER_LAST_NAME, USER_UNIQUE_NAME, EMAIL, PHONE, LOCATION_CODE, REGISTRATION_DATE
        ) VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)
    """;

        return jdbcTemplate.update(sql,
                user.getUserId(),
                user.getUserName(),
                user.getUserLastName(),
                user.getUserUniqueName(),
                user.getEmail(),
                user.getPhone(),
                user.getLocationCode()
        );
    }
    
    public List<FriendChat> findFriends(String userId) {
        String sql = """
        SELECT U.USER_ID, U.USER_NAME, U.EMAIL
        FROM SOCIAL_USER U
        WHERE U.USER_ID IN (
            SELECT CASE
                WHEN USER_A = ? THEN USER_B
                ELSE USER_A
            END
            FROM USER_FRIENDSHIP
            WHERE USER_A = ? OR USER_B = ?
        )
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FriendChat fc = new FriendChat();
            fc.setId(rs.getString("USER_ID"));
            fc.setName(rs.getString("USER_NAME"));
            fc.setAvatar("default_avatar.jpg");
            fc.setLastMessage("");
            fc.setLastMessageTime("");
            fc.setOnline(false);
            return fc;
        }, userId, userId, userId);
    }

    public List<GroupChat> findGroups(String userId) {
        String sql = """
        SELECT G.GROUP_ID, G.GROUP_NAME, G.REGISTRATION_DATE
        FROM SOCIAL_GROUP G
        JOIN GROUP_MEMBERSHIP GM ON G.GROUP_ID = GM.GROUP_ID
        WHERE GM.USER_ID = ?
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            GroupChat gc = new GroupChat();
            gc.setId(rs.getInt("GROUP_ID"));
            gc.setName(rs.getString("GROUP_NAME"));
            gc.setAvatar("default_group_avatar.jpg");
            gc.setLastMessage("");
            gc.setLastMessageTime("");
            gc.setMemberCount(0);
            return gc;
        }, userId);
    }

}
