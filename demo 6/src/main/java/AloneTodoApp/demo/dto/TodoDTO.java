package AloneTodoApp.demo.dto;

import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    //private List<TodoReplyEntity> replies;

    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
        this.isFailed = entity.isFailed();
        //this.replies = entity.getReplies();
    }


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
