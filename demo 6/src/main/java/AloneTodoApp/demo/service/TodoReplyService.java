package AloneTodoApp.demo.service;

import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.persistence.TodoReplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoReplyService {

    @Autowired
    private TodoReplyRepository replyRepository;




    @Transactional(readOnly = true)
    public List<TodoReplyEntity> retrieveReplies(String userId){
        log.info("TodoReply 리포지토리 called");
        return replyRepository.findByUserId(userId);
    }




    @Transactional
    public void createReply(String tempUserId, String todoId, String title) {

        TodoReplyEntity replyEntity = new TodoReplyEntity(null, tempUserId, todoId, title);

        replyRepository.save(replyEntity);
        log.info("리플라이 리포지토리 worked");
    }//func




    @Transactional
    public List<TodoReplyEntity> updateReply(TodoReplyEntity replyEntity){

        Optional<TodoReplyEntity> original = replyRepository.findById(replyEntity.getId());

        if(original.isPresent()){
            TodoReplyEntity newTodoReplyEntity = original.get();
            newTodoReplyEntity.setTitle(replyEntity.getTitle());
            replyRepository.save(newTodoReplyEntity);
        }

        return retrieveReplies(replyEntity.getUserId());
    }//func


    public List<TodoReplyEntity> deleteReply(TodoReplyEntity replyEntity) {
        try{
                replyRepository.delete(replyEntity);
        }
        catch(Exception e){
            log.error("error deleting Todo Reply Entity", replyEntity.getId(), e);
            throw new RuntimeException("error deleting entity" + replyEntity.getId());
        }

        return retrieveReplies(replyEntity.getUserId());
    }//func

}//end of class
