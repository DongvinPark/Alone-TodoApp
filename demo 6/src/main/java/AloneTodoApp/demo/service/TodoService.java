package AloneTodoApp.demo.service;

import AloneTodoApp.demo.Exception.AloneTodoAppException;
import AloneTodoApp.demo.Exception.AloneTodoErrorCode;
import AloneTodoApp.demo.dto.TodoDTO;
import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.persistence.TodoReplyRepository;
import AloneTodoApp.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static AloneTodoApp.demo.Exception.AloneTodoErrorCode.*;

@Slf4j
@Service
public class TodoService {

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

        validateEmptyTodoTile(entity);
        validateDate(entity);

        repository.save(entity);

        log.info("Entity Id : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());

    }//func




    @Transactional
    public List<TodoEntity> update(TodoEntity entity){

        validateEmptyTodoTile(entity);
        validateDate(entity);

        Optional<TodoEntity> original = repository.findById(entity.getId());

        if(original.isPresent()){
            TodoEntity newEntity = original.get();
            newEntity.setTitle(entity.getTitle());
            //newEntity.setFailed(entity.isFailed());
            newEntity.setDone(entity.isDone());
            newEntity.setDueDate(entity.getDueDate());

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
                log.info("리플라이 엔티티 DB상 제거  완료");
            }
        }
        catch(Exception e){
            log.error("error deleting todo Entity", todoEntity.getId(), e);
            throw new RuntimeException("error deleting entity" + todoEntity.getId());
        }

        return retrieve(todoEntity.getUserId());
    }//func


    //************>>>>>>>>> HELPER METHOD AREA <<<<<<<<<<<************

    private void validateDate(TodoEntity todoEntity){

        /*if(todoEntity.getDueDate() == null || String.valueOf(todoEntity.getDueDate()) == null ){
            throw new AloneTodoAppException(NO_DATE_ENTERED);
        }*/

        if(todoEntity.getDueDate().isBefore(LocalDate.now())){
            throw new AloneTodoAppException(INVALID_DATE);
        }

    }//func





    private void validateEmptyTodoTile(TodoEntity todoEntity){
        if(todoEntity.getTitle().equals("") || todoEntity.getTitle() == null){
            throw new AloneTodoAppException(NO_TITLE_ENTERED);
        }
    }//func

}//end of class
