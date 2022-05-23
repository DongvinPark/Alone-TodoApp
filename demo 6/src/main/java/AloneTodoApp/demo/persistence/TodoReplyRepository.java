package AloneTodoApp.demo.persistence;

import AloneTodoApp.demo.model.TodoReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoReplyRepository extends JpaRepository<TodoReplyEntity, String> {
    List<TodoReplyEntity> findByParentTodoId(String parentTodoId);
}
