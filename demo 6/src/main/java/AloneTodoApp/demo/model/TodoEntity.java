package AloneTodoApp.demo.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@Entity
@Table(name = "Todo")
public class TodoEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    private String id; //ID of this object. 릴레이션에서 주키 역할을 한다.

    private String userId; // ID of user who created this object

    private String title; // Title of ToDo list. ex) : 운동하기.

    private boolean done;//할 일을 다 마쳤는지 확인한다. 여기가 true되면 isFailed 는 절대로 true 될 수 없다. 이 부분은 별도의 validation 이 필요할 것이다.

    private boolean isFailed;//현재 시각이 dead line 보다 큰 값일 경우 여기가 true되면서 Failed 처리 된다.

    private LocalDate dueDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TodoEntity that = (TodoEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}//end of class
