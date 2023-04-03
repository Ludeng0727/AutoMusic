package one.design.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

public class MidiController {

    public void getMidiFile() {
        // Python FastAPI 서버의 주소와 포트번호
        String url = "http://192.168.0.12:12000/midi";

        // Python FastAPI 서버로 GET 요청을 보냅니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<Map> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, Map.class);

        // Python FastAPI 서버로부터 받은 응답에서 직렬화된 MIDI 데이터를 추출합니다.
        String b64_midi_data = (String) response.getBody().get("midi_data");
        byte[] decodedMidiData = Base64.getDecoder().decode(b64_midi_data);

        // 디코딩된 MIDI 데이터를 파일로 저장합니다.
        try (FileOutputStream stream = new FileOutputStream("example.mid")) {
            stream.write(decodedMidiData);
        } catch (IOException e) {
            System.out.println("failed");
        }

        System.out.println("success");
    }
}