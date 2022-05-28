package AloneTodoApp.demo.service;

import AloneTodoApp.demo.dto.TodoDTO;
import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.persistence.TodoReplyRepository;
import AloneTodoApp.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TestTodoService {

    @Autowired
    private TodoRepository repository;

    @Autowired
    private TodoReplyRepository replyRepository;




    @Transactional(readOnly = true)
    public List<TodoEntity> retrieve(String userId) {
        return repository.findByUserId(userId);
    }//func





    @Transactional
    public List<TodoEntity> create(TodoEntity entity) {

        repository.save(entity);

        log.info("Entity Id : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());

    }//func




    @Transactional
    public List<TodoEntity> update(TodoEntity entity){

        Optional<TodoEntity> original = repository.findById(entity.getId());

        if(original.isPresent()){
            TodoEntity newEntity = original.get();
            newEntity.setTitle(entity.getTitle());
            newEntity.setFailed(entity.isFailed());
            newEntity.setDone(entity.isDone());

            repository.save(newEntity);
        }

        return retrieve(entity.getUserId());
    }//func




    @Transactional
    public List<TodoEntity> delete(final TodoEntity todoEntity, TodoDTO todoDTO) {
        try{
            repository.delete(todoEntity);
            for(TodoReplyEntity replyEntity : todoDTO.getReplies()){
                replyRepository.delete(replyEntity);
            }
        }
        catch(Exception e){
            log.error("error deleting todo Entity", todoEntity.getId(), e);
            throw new RuntimeException("error deleting entity" + todoEntity.getId());
        }

        return retrieve(todoEntity.getUserId());
    }//func


    //************>>>>>>>>> HELPER METHOD AREA <<<<<<<<<<<************


}//end of class
