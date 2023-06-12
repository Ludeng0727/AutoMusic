package one.design.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import one.design.domain.Song;
import one.design.dto.SongInputDto;
import one.design.repository.SongRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    private String url = "http://automusic.iptime.org:8000/auto_music";

    public Song createSong(SongInputDto songInputDto, String fileName, String userId) throws JsonProcessingException{

        //String filePath = "C:\\Users\\Administrator\\Desktop\\study\\design\\src\\main\\resources\\static\\music/"+ userId + "_" + fileName +".wav";
        ObjectMapper mapper = new ObjectMapper();

        // Python FastAPI 서버로 GET 요청을 보냅니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Token","automusic321");
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(songInputDto), headers);
        ResponseEntity<Map> response = new RestTemplate().exchange(url, HttpMethod.POST, entity, Map.class);

        // Python FastAPI 서버로부터 받은 응답에서 직렬화된 MIDI 데이터를 추출합니다.
        String b64_midi_data = (String) response.getBody().get("midi_data");
        byte[] decodedMidiData = Base64.getDecoder().decode(b64_midi_data);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedMidiData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try{
            AudioSystem.write(AudioSystem.getAudioInputStream(inputStream),
                    AudioFileFormat.Type.WAVE,outputStream);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        byte[] audio = outputStream.toByteArray();
        Song song = new Song(null,userId, fileName, audio);

        return songRepository.save(song);
    }

}