package one.design.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.design.dto.MusicDto;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SongServiceTest {

    @Test
    void createSong() throws FileNotFoundException, JsonProcessingException {
        MusicDto musicDto = new MusicDto(1,
                0,
                80,
                1,
                Map.of("inst1",22,"inst2",14,"inst3",16,"inst4",18,"inst5",45));



        SongService songService = new SongService();
        songService.createSong(musicDto,"test", "test");
    }
}