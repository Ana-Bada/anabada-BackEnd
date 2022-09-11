package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.user.dto.ProfileimageRequestDto;
import com.anabada.anabadaBackend.user.dto.SignupRequestDto;
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
@Table (name = "user")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profileImg;

    public UserEntity(SignupRequestDto requestDto, String password, String profileImg) {
        this.email = requestDto.getEmail();
        this.nickname = requestDto.getNickname();
        this.password = password;
        this.profileImg = profileImg;
    }

    public UserEntity(String email, String nickname, String password, String profileImg) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImg = profileImg;
    }

    public void updateProfileImage(ProfileimageRequestDto profileimageRequestDto) {
        this.profileImg = profileimageRequestDto.getProfileImg();
    }

}
