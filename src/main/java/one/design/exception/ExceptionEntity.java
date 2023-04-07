package one.design.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionEntity {

    private String errorCode;
    private String errorMsg;
}
