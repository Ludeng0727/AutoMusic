package one.design.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.design.domain.Song;
import one.design.dto.MusicDto;
import one.design.repository.SongRepository;
import one.design.service.SongService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/song")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class SongController {

    static boolean isWorking = false;
    private final SongService songService;
    private final SongRepository songRepository;

    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> deleteSong(@PathVariable String fileName, Authentication authentication){
        String userId = authentication.getName();
        String path = "C:\\Users\\Administrator\\Desktop\\study\\design\\src\\main\\resources\\static\\music/"+ userId + "_" + fileName +".wav";

        File file = new File(path);

        if (file.exists()){
            boolean delete = file.delete();
            if (delete){
                songRepository.deleteByFileName(fileName);
                List<Song> all = songRepository.findAllByUserId(userId);
                return ResponseEntity.ok(all);
            }
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody MusicDto musicDto, Authentication authentication) throws IOException, InterruptedException {
        if (isWorking){
            return ResponseEntity.internalServerError().build();
        }
        isWorking = true;

        String uuid = UUID.randomUUID().toString();
        String userId = authentication.getName();
        Song song = new Song(uuid, userId);
        songService.createSong(musicDto, uuid, userId);
        Song insertedSong = songRepository.save(song);
        isWorking = false;

        return new ResponseEntity<>(insertedSong, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Song>> mySongs(Authentication authentication){
        String userId = authentication.getName();
        return ResponseEntity.ok(songRepository.findAllByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<Song>> getSongs(){

        List<Song> all = songRepository.findAll();
        Song sample = songRepository.findByFileName("sample").get();
        all.remove(sample);

        return ResponseEntity.ok(all);

    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Song> getSong(@PathVariable String fileName){
        Optional<Song> song = songRepository.findByFileName(fileName);
        if(song.isPresent()){
            return ResponseEntity.ok(song.get());
        }
        else{
            log.info("404");
            return ResponseEntity.notFound().build();
        }
    }
}
