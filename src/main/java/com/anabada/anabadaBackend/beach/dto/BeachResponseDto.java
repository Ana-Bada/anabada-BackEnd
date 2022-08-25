package com.anabada.anabadaBackend.beach.dto;

import com.anabada.anabadaBackend.beach.BeachEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeachResponseDto {
    private Long beachId;
    private String beachName;
    private Double x;
    private Double y;
    private String tmp;
    private String wav;
    private String wsd;
    private String pop;
    private String pcp;
    private String pty;
    private String sky;

    public BeachResponseDto(BeachEntity beach) {
        this.beachId = beach.getBeachId();
        this.beachName = beach.getBeachName();
        this.tmp = beach.getTmp();
        this.wav = beach.getWav();
        this.wsd = beach.getWsd();
        this.pop = beach.getPop();
        this.pcp = beach.getPcp();
        this.pty = beach.getPty();
        this.sky = beach.getSky();
    }
}
