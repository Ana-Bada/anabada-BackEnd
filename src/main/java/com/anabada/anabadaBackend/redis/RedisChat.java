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
    private String id;

    @Indexed
    private String roomId;

    private String senderNickname;

    private String message;


    public RedisChat(ChatRoomEntity room, String message, UserEntity user) {
        this.roomId = Long.toString(room.getId());
        this.message = message;
        this.senderNickname = user.getNickname();
    }
}
