package AloneTodoApp.demo.dto;

import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
    private String id;
    private String title;
    private boolean done;
    private boolean isFailed;
    private List<String> replies;

    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
        this.isFailed = entity.isFailed();
        this.replies = new LinkedList<>();
    }


    //여기서는 굳이 클라이언트(즉, 사용자)에게 userId 를 보낼 필요가 없기에 .userId(dto.getUserId()); 가 없는 것이다.
    public static TodoEntity toEntity(final TodoDTO dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .isFailed(dto.isFailed())
                //.replies(dto.getReplies())
                .build();
    }
}//end of TodoDTO class
