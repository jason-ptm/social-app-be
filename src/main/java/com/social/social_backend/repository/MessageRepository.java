// MessageRepository.java
package com.social.social_backend.repository;

import com.social.social_backend.model.Message;
import com.social.social_backend.model.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MessageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int sendMessage(MessageRequest request) {
        String sql = "INSERT INTO SOCIAL_UD.MESSAGE (MESSAGE_ID, PARENT_MESSAGE_ID, GROUP_ID, " +
                "SENDER_USER_ID, RECEIVER_USER_ID, MESSAGE_DATE) " +
                "VALUES (SOCIAL_UD.MESSAGE_ID_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                request.getParentMessageId(),
                request.getGroupId(),
                request.getSenderUserId(),
                request.getReceiverUserId(),
                Timestamp.valueOf(LocalDateTime.now()));
    }

    public Integer sendMessageWithContent(MessageRequest request) {
        // Primero insertar el mensaje
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String messageSql = "INSERT INTO SOCIAL_UD.MESSAGE (MESSAGE_ID, PARENT_MESSAGE_ID, GROUP_ID, " +
                "SENDER_USER_ID, RECEIVER_USER_ID, MESSAGE_DATE) " +
                "VALUES (SOCIAL_UD.MESSAGE_ID_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(messageSql, new String[]{"MESSAGE_ID"});
            ps.setObject(1, request.getParentMessageId());
            ps.setObject(2, request.getGroupId());
            ps.setString(3, request.getSenderUserId());
            ps.setString(4, request.getReceiverUserId());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        Integer messageId = keyHolder.getKey().intValue();

        // Obtener el CONTENT_TYPE_ID dinámicamente
        String contentTypeId = jdbcTemplate.queryForObject(
                "SELECT CONTENT_TYPE_ID FROM SOCIAL_UD.CONTENT_TYPE WHERE TYPE_DESCRIPTION = ?",
                new Object[]{request.getContentTypeName()},
                String.class
        );

        // Luego insertar el contenido
        if (messageId != null && request.getContent() != null && contentTypeId != null) {
            String contentSql = "INSERT INTO SOCIAL_UD.CONTENT (MESSAGE_ID, CONTENT_ID, CONTENT_DESCRIPTION, CONTENT_TYPE_ID) " +
                    "VALUES (?, SEQ_CONTENT_ID.NEXTVAL, ?, ?)";

            jdbcTemplate.update(contentSql, messageId, request.getContent(), contentTypeId);
        }

        return messageId;
    }

    public List<Message> getMessages(String userId1, String userId2) {
        String sql = "SELECT m.MESSAGE_ID, m.SENDER_USER_ID, m.RECEIVER_USER_ID, m.GROUP_ID, " +
                "c.CONTENT_DESCRIPTION AS CONTENT, m.MESSAGE_DATE " +
                "FROM SOCIAL_UD.MESSAGE m " +
                "JOIN SOCIAL_UD.CONTENT c ON m.MESSAGE_ID = c.MESSAGE_ID " +
                "WHERE ((m.SENDER_USER_ID = ? AND m.RECEIVER_USER_ID = ?) " +
                "OR (m.SENDER_USER_ID = ? AND m.RECEIVER_USER_ID = ?)) " +
                "AND m.GROUP_ID IS NULL " +
                "ORDER BY m.MESSAGE_DATE DESC";

        return jdbcTemplate.query(sql, new Object[]{userId1, userId2, userId2, userId1}, messageRowMapper());
    }

    public List<Message> getGroupMessages(Integer groupId) {
        String sql = "SELECT m.MESSAGE_ID, m.SENDER_USER_ID, m.RECEIVER_USER_ID, m.GROUP_ID, " +
                "c.CONTENT_DESCRIPTION AS CONTENT, m.MESSAGE_DATE " +
                "FROM SOCIAL_UD.MESSAGE m " +
                "JOIN SOCIAL_UD.CONTENT c ON m.MESSAGE_ID = c.MESSAGE_ID " +
                "WHERE m.GROUP_ID = ? " +
                "ORDER BY m.MESSAGE_DATE DESC";

        return jdbcTemplate.query(sql, new Object[]{groupId}, messageRowMapper());
    }

    private RowMapper<Message> messageRowMapper() {
        return (rs, rowNum) -> {
            Message message = new Message();
            message.setMessageId(rs.getInt("MESSAGE_ID"));
            message.setSenderId(rs.getString("SENDER_USER_ID"));
            message.setReceiverId(rs.getString("RECEIVER_USER_ID"));
            message.setGroupId(rs.getInt("GROUP_ID"));
            if (rs.wasNull()) message.setGroupId(null);
            message.setContent(rs.getString("CONTENT"));
            message.setMessageDate(rs.getTimestamp("MESSAGE_DATE").toLocalDateTime());
            return message;
        };
    }
}