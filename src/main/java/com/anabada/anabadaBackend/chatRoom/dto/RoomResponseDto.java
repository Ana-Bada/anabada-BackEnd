package com.anabada.anabadaBackend.chatRoom.dto;

import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
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

    public RoomResponseDto (ChatRoomEntity chatRoom) {
        this.roomId = chatRoom.getId();
        this.senderNickname = chatRoom.getSender().getNickname();
        this.senderProfileImg = chatRoom.getSender().getProfileImg();
        this.receiverNickname = chatRoom.getReceiver().getNickname();
        this.receiverProfileImg = chatRoom.getReceiver().getProfileImg();
    }
}
