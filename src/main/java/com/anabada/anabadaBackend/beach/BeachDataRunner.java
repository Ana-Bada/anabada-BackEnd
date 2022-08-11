package com.anabada.anabadaBackend.beach;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class BeachDataRunner implements ApplicationRunner {
    private final BeachRepository beachRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        beachRepository.save(new BeachEntity("38해수욕장",38.005,128.731));
        beachRepository.save(new BeachEntity("가진해수욕장",38.373,128.509));
        beachRepository.save(new BeachEntity("강문해수욕장",37.794,128.918));
        beachRepository.save(new BeachEntity("갯마을해수욕장",37.745,128.781));
        beachRepository.save(new BeachEntity("격포해수욕장",35.63,126.469));
        beachRepository.save(new BeachEntity("경포대해수욕장",33.451,128.91));
        beachRepository.save(new BeachEntity("곽지과물해수욕장",37.803,126.304));
        beachRepository.save(new BeachEntity("광안리해수욕장",35.153,129.118));
        beachRepository.save(new BeachEntity("사천해수욕장",37.831,128.878));
    }

    public void getWeather(String x, String y) throws IOException, ParseException {
        LocalDate now = LocalDate.now(ZoneId.of("Asis/Seoul"));
        System.out.println(now);
//        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=Gkq3x%2BhZLTP7L2d7zyEr5j3yIDbHiIelfcjNUUbz2S%2FR0WzvQ3GiDZ31JBA%2Btq6pv4tdNK8q6lco7qyT9C5MZg%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
//        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode("20220811", "UTF-8")); /*‘21년 6월 28일발표*/
//        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8")); /*05시 발표*/
//        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(x, "UTF-8")); /*예보지점의 X 좌표값*/
//        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(y, "UTF-8")); /*예보지점의 Y 좌표값*/
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        BufferedReader rd;
//        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        String result = "";
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//        System.out.println(sb.toString());
//        JSONParser parser = new JSONParser();
//        JSONObject obj = (JSONObject) parser.parse(sb.toString());
//        JSONObject parse_response = (JSONObject) obj.get("response");
//        JSONObject parse_body = (JSONObject) parse_response.get("body");
//        JSONObject parse_items = (JSONObject) parse_body.get("items");
//        JSONArray parse_item = (JSONArray) parse_items.get("item");
//        String category;
//        JSONObject weather;
//        for (int i = 0; i < parse_item.size(); i++) {
//            weather = (JSONObject) parse_item.get(i);
//            category = (String) weather.get("category");
//            if(category.equals("TMP") || category.equals("WAV") || category.equals("WSD")
//                    || category.equals("POP") || category.equals("PCP")) {
//                String fcstValue = (String) weather.get("fcstValue");
//                System.out.println(category + " " + fcstValue);
//            }
//        }
    }
}