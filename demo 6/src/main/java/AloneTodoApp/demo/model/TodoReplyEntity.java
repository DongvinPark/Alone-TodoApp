package AloneTodoApp.demo.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TodoReply")
public class TodoReplyEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    private String id; //ID of this object. DB 내에서 주키 역할을 한다.

    private String userId;// ID of user who created this reply. 이게 있어야 내가 작성한 답글만을 볼 수 있다.

    private String parentTodoId;//이 답글의 부모 Todo의 아이디가 필요하다.

    private String title;//답글의 내용을 담아야 한다.

}//end of class
