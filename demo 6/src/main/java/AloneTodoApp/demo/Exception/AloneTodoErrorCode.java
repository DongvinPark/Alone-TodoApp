package AloneTodoApp.demo.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AloneTodoErrorCode {

    NO_DATE_ENTERED("날짜를 입력해주세요!"),
    NO_TITLE_ENTERED("제목을 입력해주세요!"),
    INVALID_DATE("오늘 보다 앞선 날짜는 입력할 수 없어요...");

    private final String message;
}//end of class
