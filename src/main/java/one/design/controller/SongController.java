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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/song")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final SongRepository songRepository;

    @PostMapping
    public ResponseEntity<?> createSong(@RequestBody MusicDto musicDto, @RequestBody String fileName, Authentication authentication) throws IOException {
        String userId = authentication.getName();
        Song song = new Song(fileName,userId);
        songService.createSong(musicDto, fileName, userId);

        Song insertedSong = songRepository.save(song);

        return new ResponseEntity<>(insertedSong, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Song>> mySongs(Authentication authentication){
        String userId = authentication.getName();
        return ResponseEntity.ok(songRepository.findAllByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<Song>> getSongs(){
        return ResponseEntity.ok(songRepository.findAll());
    }


    @GetMapping("/{fileName}")
    public ResponseEntity<Song> getSong(@PathVariable String fileName){
        Optional<Song> song = songRepository.findByFileName(fileName);
        if(song.isPresent()){
            return ResponseEntity.ok(song.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }

    }
}
