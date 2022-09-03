package com.anabada.anabadaBackend.chatRoom.dto;

import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RoomResponseDto {
    private Long roomId;
    private String senderNickname;
    private String senderProfileImg;
    private String receiverNickname;
    private String receiverProfileImg;

    public RoomResponseDto (ChatRoomEntity chatRoom, UserEntity user) {
        if(!chatRoom.getSender().getNickname().equals(user.getNickname())){
            this.roomId = chatRoom.getId();
            this.senderNickname = chatRoom.getReceiver().getNickname();
            this.senderProfileImg = chatRoom.getReceiver().getProfileImg();
            this.receiverNickname = chatRoom.getSender().getNickname();
            this.receiverProfileImg = chatRoom.getSender().getProfileImg();
        }
        this.roomId = chatRoom.getId();
        this.senderNickname = chatRoom.getSender().getNickname();
        this.senderProfileImg = chatRoom.getSender().getProfileImg();
        this.receiverNickname = chatRoom.getReceiver().getNickname();
        this.receiverProfileImg = chatRoom.getReceiver().getProfileImg();
    }
}
