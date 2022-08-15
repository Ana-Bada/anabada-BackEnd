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

//    @Scheduled(cron = "0 0/1 * * * *")
    @Scheduled(cron = "0 0 0/1 * * *")
    public void getBeachesWeather() throws IOException, ParseException {
        HashMap<String, String> hm = new HashMap<>();
        List<BeachEntity> beachList = beachRepository.findAll();

        String nowDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        String nowTime = DateTimeFormatter.ofPattern("HH00").format(LocalTime.now());
        if (Integer.parseInt(nowTime) > 0 && Integer.parseInt(nowTime) <= 500) {
            nowDate = Integer.parseInt(nowDate) - 1 + "";
            nowTime = Integer.parseInt(nowTime) + 2400 + "";
        }
        String page = (Integer.parseInt(nowTime) / 100) - 5 + "";

        for(BeachEntity beach : beachList) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
            String x = Math.round(beach.getX()) + "";
            String y = Math.round(beach.getY()) + "";
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + weatherkey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(nowDate, "UTF-8")); /*‘21년 6월 28일발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8")); /*05시 발표*/
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(x.split("\\.")[0], "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(y.split("\\.")[0], "UTF-8")); /*예보지점의 Y 좌표값*/
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
                        || category.equals("POP") || category.equals("PCP")) {
                    String fcstValue = (String) weather.get("fcstValue");
                    hm.put(category, fcstValue);
                }
                beach.updateBeach(hm.get("TMP"), hm.get("WAV"), hm.get("WSD"), hm.get("POP"), hm.get("PCP"));
                beachRepository.save(beach);
            }
        }
    }
}
