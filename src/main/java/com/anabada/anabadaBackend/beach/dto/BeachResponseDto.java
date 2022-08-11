package com.anabada.anabadaBackend.beach.dto;

import com.anabada.anabadaBackend.beach.BeachEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

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
    private List<Boolean> amenity;

    public BeachResponseDto(BeachEntity beach, String tmp, String wav) {
        this.beachId = beach.getBeachId();
        this.beachName = beach.getBeachName();
        this.x = beach.getX();
        this.y = beach.getY();
        this.tmp = tmp;
        this.wav = wav;
    }

    public BeachResponseDto(BeachEntity beach, HashMap<String, String> hm) {
        this.beachId = beach.getBeachId();
        this.beachName = beach.getBeachName();
        this.tmp = hm.get("TMP");
        this.wav = hm.get("WAV");
        this.wsd = hm.get("WSD");
        this.pop = hm.get("POP");
        this.pcp = hm.get("PCP");
    }
}
