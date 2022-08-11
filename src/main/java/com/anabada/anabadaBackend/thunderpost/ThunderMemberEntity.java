package com.anabada.anabadaBackend.thunderpost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
public class ThunderMemberEntity {
    @GeneratedValue
    @Id
    private Long ThunderMemberId;
}
