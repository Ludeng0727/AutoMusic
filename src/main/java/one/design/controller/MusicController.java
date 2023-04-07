package one.design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MusicController {

    @GetMapping("/getMusic")
    public void getMusic(){
        //MidiController 에 요청


    }

}
