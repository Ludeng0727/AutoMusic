package one.design.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.design.dto.MusicDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class SongService {

    private String url = "http://automusic.iptime.org:8000/auto_music";

    public void createSong(MusicDto musicDto, String fileName, String userId) throws JsonProcessingException, FileNotFoundException {
        String filePath = "C:\\Users\\Administrator\\Desktop\\study\\design\\src\\main\\resources\\static\\music/"+ userId + "_" + fileName +".wav";
        ObjectMapper mapper = new ObjectMapper();

        // Python FastAPI 서버로 GET 요청을 보냅니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Token","automusic321");
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(musicDto), headers);
        ResponseEntity<Map> response = new RestTemplate().exchange(url, HttpMethod.POST, entity, Map.class);

        // Python FastAPI 서버로부터 받은 응답에서 직렬화된 MIDI 데이터를 추출합니다.
        String b64_midi_data = (String) response.getBody().get("midi_data");
        byte[] decodedMidiData = Base64.getDecoder().decode(b64_midi_data);
        InputStream inputStream = new ByteArrayInputStream(decodedMidiData);
        OutputStream outputStream = new FileOutputStream(filePath);
        convertWav(inputStream, outputStream);

    }

    private void convertWav(InputStream inputStream, OutputStream saveto){
        try{
            AudioInputStream stream = AudioSystem.getAudioInputStream(inputStream);
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, saveto);
            stream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}