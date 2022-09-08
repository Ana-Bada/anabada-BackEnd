package com.anabada.anabadaBackend.notification;


import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table (name = "notification")
public class NotificationEntity extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @Column
    private String notificationType; //이넘으로 하나...?? int로 정해놓고 해??

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private PostEntity post;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private boolean isBadge;

//    public NotificationEntity(String content, UserEntity user, PostEntity post) {
//        this.content = content;
//        this.user = user;
//        this.post = post;
//    }

    public NotificationEntity(UserEntity user, PostEntity post, String type) {
        this.notificationType = type;
        this.user = user;
        this.post = post;
        this.isRead = false;
        this.isBadge = false;
    }

    public void badgeOff() {
        this.isBadge = true;
    }

    public void readOff() {
        this.isRead = true;
    }

}