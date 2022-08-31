package com.anabada.anabadaBackend.beach;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class BeachWeatherData {
    @Value("${weather-key}")
    private String weatherkey;

    private final BeachRepository beachRepository;

    @Scheduled(fixedDelay = 10800000, initialDelay = 5000)
    public void getBeachesWeather() throws IOException, ParseException {
        HashMap<String, String> hm = new HashMap<>();
        List<BeachEntity> beachList = beachRepository.findAll();

        String nowDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        String nowTime = DateTimeFormatter.ofPattern("HH").format(LocalTime.now());
        int n = Integer.parseInt(nowTime);
        switch ( n % 3) {
            case 0:
                n -= 1;
                break;
            case 1:
                n -= 2;
                break;
            case 2: break;
        }
        if((n+"00").length() == 3)
            nowTime = "0" + n + "00";
        else nowTime = n + "00";
        for(BeachEntity beach : beachList) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/BeachInfoservice/getVilageFcstBeach"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + weatherkey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("beach_num", "UTF-8") + "=" + URLEncoder.encode(beach.getBeachNum(), "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(nowDate, "UTF-8")); /*‘21년 6월 28일발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(nowTime, "UTF-8")); /*05시 발표*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(sb.toString());
            JSONObject parse_response = (JSONObject) obj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            JSONArray parse_item = (JSONArray) parse_items.get("item");

            String category;
            JSONObject weather;
            for (int i = 0; i < parse_item.size(); i++) {
                weather = (JSONObject) parse_item.get(i);
                category = (String) weather.get("category");
                if (category.equals("TMP") || category.equals("WAV") || category.equals("WSD")
                        || category.equals("POP") || category.equals("PCP") || category.equals("PTY") || category.equals("SKY")) {
                    String fcstValue = (String) weather.get("fcstValue");
                    hm.put(category, fcstValue);
                }
                beach.updateBeach(hm.get("TMP"), hm.get("WAV"), hm.get("WSD"), hm.get("POP"), hm.get("PCP"), hm.get("PTY"), hm.get("SKY"));
                beachRepository.save(beach);
            }
        }
    }
}
