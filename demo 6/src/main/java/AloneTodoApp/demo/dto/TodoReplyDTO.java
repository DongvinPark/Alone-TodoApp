package AloneTodoApp.demo.dto;

import AloneTodoApp.demo.model.TodoReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoReplyDTO {

    private String id; //ID of this object. 릴레이션 내에서 주키 역할을 한다.
    private String userId;// ID of user who created this reply. 이게 있어야 내가 작성한 답글만을 볼 수 있다.
    private String parentTodoId;//이 답글의 부모 Todo의 아이디가 필요하다.
    private String title;//답글의 내용을 담아야 한다.

    public TodoReplyDTO(TodoReplyEntity replyEntity){
        this.id = replyEntity.getId();
        this.userId = replyEntity.getUserId();
        this.parentTodoId = replyEntity.getParentTodoId();
        this.title = replyEntity.getTitle();
    }

    public static TodoReplyEntity toEntity(final TodoReplyDTO replyDTO){
        return TodoReplyEntity.builder()
                .id(replyDTO.getId())
                .userId(replyDTO.getUserId())
                .parentTodoId(replyDTO.getParentTodoId())
                .title(replyDTO.getTitle())
                .build();
    }

}//end of class
