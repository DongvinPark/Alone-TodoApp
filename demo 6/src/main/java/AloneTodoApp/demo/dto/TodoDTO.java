package AloneTodoApp.demo.dto;

import AloneTodoApp.demo.Exception.AloneTodoAppException;
import AloneTodoApp.demo.Exception.AloneTodoErrorCode;
import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static AloneTodoApp.demo.Exception.AloneTodoErrorCode.NO_DATE_ENTERED;
import static AloneTodoApp.demo.Exception.AloneTodoErrorCode.NO_TITLE_ENTERED;

@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {

    private String id;

    private String title;

    private boolean done;

    //private boolean isFailed;

    private List<TodoReplyEntity> replies;


    //프론트 엔드 쪽에서는 dueDate 부분을 백엔드에 넘겨줄 때, "2022-06-02"와 같은 스트링 타입 리터럴로 보내줘야 한다.
    private String dueDate;

    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
        //this.isFailed = entity.isFailed();
        this.replies = new LinkedList<>();
    }


    //여기서는 굳이 클라이언트(즉, 사용자)에게 userId 를 보낼 필요가 없기에 .userId(dto.getUserId()); 가 없는 것이다.
    public static TodoEntity toEntity(final TodoDTO dto) {

        log.info("투듀디티오 투엔티티 메서드 진입");

        if(dto.getTitle()==null || dto.getTitle().equals("")){
            throw new AloneTodoAppException(NO_TITLE_ENTERED);
        }

        if(dto.getDueDate()==null || dto.getDueDate().equals("")){
            throw new AloneTodoAppException(NO_DATE_ENTERED);
        }

        log.info("투듀디티오 투엔티티 유효성 검사 통과");

        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                //.isFailed(dto.isFailed())
                .dueDate(LocalDate.parse(dto.getDueDate()))
                .build();
    }
}//end of TodoDTO class
