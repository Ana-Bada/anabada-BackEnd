package com.anabada.anabadaBackend.redis;

import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@RedisHash("Chat")
public class RedisChat implements Serializable {
    @Id
    @Indexed
    private String id;

    private ChatRoomEntity room;

    private UserEntity user;

    private String message;

    @Indexed
    private String createdAt;

    public RedisChat(ChatRoomEntity room, String message, UserEntity user, String createdAt) {
        this.room = room;
        this.message = message;
        this.user = user;
        this.createdAt = createdAt;
    }
}
