package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.user.dto.ProfileimageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false/*, unique = true*/)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileImg;

    public UserEntity(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImg = "https://applesong.s3.ap-northeast-2.amazonaws.com/97b77cab-712c-422c-b114-2d9faea04a2b_%EC%9C%A0%EC%A0%80%EA%B8%B0%EB%B3%B8%ED%94%84%EC%82%AC.JPG";
    }

    public void updateProfileImage(ProfileimageRequestDto profileimageRequestDto) {
        this.profileImg = profileimageRequestDto.getProfileImg();
    }

}
