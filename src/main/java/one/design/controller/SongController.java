package one.design.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.design.domain.Song;
import one.design.dto.SongInputDto;
import one.design.dto.SongOutputDto;
import one.design.repository.SongRepository;
import one.design.service.SongService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<SongOutputDto>> deleteSong(@PathVariable String fileName, Authentication authentication){
        String userId = authentication.getName();
        songRepository.deleteByFileName(fileName);
        List<SongOutputDto> allSong = songRepository.findAllByUserId(userId).stream()
                .map(song -> new SongOutputDto(song.getUserId(), song.getFileName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(allSong);
    }

    @PostMapping
    public ResponseEntity<SongOutputDto> createSong(@RequestBody SongInputDto songInputDto, Authentication authentication) throws IOException, InterruptedException {
        if (isWorking){
            return ResponseEntity.internalServerError().build();
        }
        isWorking = true;

        String uuid = UUID.randomUUID().toString();
        String userId = authentication.getName();

        Song createdSong = songService.createSong(songInputDto, uuid, userId);

        isWorking = false;

        return new ResponseEntity<>(new SongOutputDto(createdSong.getUserId(), createdSong.getFileName()), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<SongOutputDto>> mySongs(Authentication authentication){
        String userId = authentication.getName();
        return ResponseEntity.ok(songRepository.findAllByUserId(userId)
                .stream()
                .map(song -> new SongOutputDto(song.getUserId(), song.getFileName()))
                .collect(Collectors.toList())

        );
    }

    @GetMapping
    public ResponseEntity<List<SongOutputDto>> getSongs(){

        List<SongOutputDto> all = songRepository.findAll().stream()
                .map(song -> new SongOutputDto(song.getUserId(), song.getFileName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(all);

    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getSong(@PathVariable String fileName){
        Optional<Song> find = songRepository.findByFileName(fileName);
        if(find.isPresent()){
            Song song = find.get();
            byte[] data = song.getData();
            return ResponseEntity.ok(data);
        }
        else{
            log.info("404");
            return ResponseEntity.notFound().build();
        }
    }
}
