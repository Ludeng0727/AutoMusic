package one.design.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MusicDto {
    int atDetail;
    int atmosphere;
    int bpm;
    int highlow;
    Map<String,Integer> instrument;
}
