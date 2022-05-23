package AloneTodoApp.demo.service;

import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TestTodoService {

    @Autowired
    private TodoRepository repository;



    public List<TodoEntity> retrieve(String userId) {
        return repository.findByUserId(userId);
    }//func



    public List<TodoEntity> create(TodoEntity entity) {

        repository.save(entity);

        log.info("Entity Id : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());

    }//func

}//end of class
