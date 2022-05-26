package AloneTodoApp.demo.service;

import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.persistence.TodoReplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TestTodoReplyService {

    @Autowired
    private TodoReplyRepository replyRepository;

    public List<TodoReplyEntity> retrieveReplies(String userId){
        log.info("TodoReply 리포지토리 called");
        return replyRepository.findByUserId(userId);
    }


    public void createReply(String tempUserId, String todoId, String title) {

        TodoReplyEntity replyEntity = new TodoReplyEntity(null, tempUserId, todoId, title);

        replyRepository.save(replyEntity);
        log.info("리플라이 리포지토리 worked");
    }//func


}//end of class
