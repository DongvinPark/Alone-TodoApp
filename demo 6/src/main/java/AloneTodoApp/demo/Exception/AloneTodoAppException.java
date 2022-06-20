package AloneTodoApp.demo.Exception;

import lombok.Getter;

@Getter
public class AloneTodoAppException extends RuntimeException{

    private AloneTodoErrorCode errorCode;
    private String detailMessage;

    public AloneTodoAppException(AloneTodoErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

    public AloneTodoAppException(AloneTodoErrorCode errorCode, String detailMessage){
        super(detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

}//end of class
