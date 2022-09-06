package com.anabada.anabadaBackend.redis;

import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@RedisHash("Chat")
public class RedisChat implements Serializable {

    @Id
    private String id;

    @Indexed
    private String roomId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    private String senderNickname;

    private String message;

    private String createdAt;


    public RedisChat(ChatRoomEntity room, String message, UserEntity user) {
        this.roomId = Long.toString(room.getId());
        this.message = message;
        this.senderNickname = user.getNickname();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss:sss"));
    }
}
