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

    @Column
    private String tmp;

    @Column
    private String wav;

    @Column
    private String wsd;

    @Column
    private String pop;

    @Column
    private String pcp;

    public BeachEntity(String beachName, Double x, Double y){
        this.beachName = beachName;
        this.x = x;
        this.y = y;
    }

    public void updateBeach(String tmp, String wav, String wsd, String pop, String pcp) {
        this.tmp = tmp;
        this.wav = wav;
        this.wsd = wsd;
        this.pop = pop;
        this.pcp = pcp;
    }
}
