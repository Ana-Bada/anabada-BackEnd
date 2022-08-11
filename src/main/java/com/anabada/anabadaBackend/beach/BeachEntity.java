package com.anabada.anabadaBackend.beach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
public class BeachEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long beachId;

    @Column(nullable = false)
    private String beachName;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    public BeachEntity(String beachName, Double x, Double y){
        this.beachName = beachName;
        this.x = x;
        this.y = y;
    }
}
