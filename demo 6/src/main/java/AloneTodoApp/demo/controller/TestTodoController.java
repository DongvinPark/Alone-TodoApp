package AloneTodoApp.demo.controller;

import AloneTodoApp.demo.dto.ResponseDTO;
import AloneTodoApp.demo.dto.TodoDTO;
import AloneTodoApp.demo.dto.TodoReplyDTO;
import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.service.TestTodoReplyService;
import AloneTodoApp.demo.service.TestTodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/testTodo")
public class TestTodoController {

    @Autowired
    private TestTodoService service;

    @Autowired
    private TestTodoReplyService replyService;



    @GetMapping("/getTodo")
    public ResponseEntity<?> getTodoLists(){

        String tempUserId = "temporary-user"; //for TEST

        List<TodoEntity> toDoEntities = service.retrieve(tempUserId);

        List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(tempUserId);
        log.info("리플라이엔티티 리스트 길이 보기 : " + replyEntities.size());
        for(TodoReplyEntity ret : replyEntities){
            log.info("리플라이 엔티티들의 parentTodoId 보기 : " + ret.getParentTodoId());
        }

        List<TodoDTO> dtos = toDoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());

        addReplies(replyEntities, dtos);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }//func




    @PostMapping("/createTodo")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO){
        try{
            String tempUserId = "temporary-user"; //for TEST

            TodoEntity entity = TodoDTO.toEntity(todoDTO);

            entity.setId(null);

            entity.setUserId(tempUserId);

            List<TodoEntity> toDoentities = service.create(entity);

            List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(tempUserId);

            List<TodoDTO> dtos = toDoentities.stream().map(TodoDTO::new).collect(Collectors.toList());

            addReplies(replyEntities, dtos);

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }//try
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    } //func





    @PostMapping("/makeReply")
    public ResponseEntity<?> createTodoReply( @RequestBody TodoDTO todoDTO ){
        try{
            String tempUserId = "temporary-user"; //for TEST

            String title = todoDTO.getReplies().get(todoDTO.getReplies().size()-1).getTitle();

            replyService.createReply(tempUserId, todoDTO.getId(), title);

            return getTodoLists();
        }
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }




    @PutMapping("/updateTodo")
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO todoDTO){

        String tempUserId = "temporary-user";

        TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);

        todoEntity.setUserId(tempUserId);

        List<TodoEntity> todoEntities = service.update(todoEntity);

        List<TodoDTO> dtos = todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());

        List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(tempUserId);

        addReplies(replyEntities, dtos);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }//func




    @PutMapping("/updateReply")
    public ResponseEntity<?> updateReply(@RequestBody TodoReplyDTO todoReplyDTO){

        /*
        * 로직을 좀 세워보자. 이놈은 특정한 Todo 튜플 하나에 존재하는 TodoReply 항목중 하나만을 딱 찍어서 타이틀을 수정하는 것이다.
        * 프런트 엔드에서 무엇을 입력으로 받아야 하지? 결국 딱 찍어서 수정하고자 하는 TodoReply하나만 있으면 된다.
        * 그래서 최종적으로 무엇을 유저에게 리턴해야 하지? >>> 결과물은 결국 getTodo의 결과물과 일치해야 한다!!
        * */

        String tempUserId = "temporary-user";

        TodoReplyEntity replyEntity = TodoReplyDTO.toEntity(todoReplyDTO);

        replyEntity.setUserId(tempUserId);

        List<TodoReplyEntity> listOfRepliesAfterUpdate = replyService.updateReply(replyEntity);

        List<TodoEntity> todoEntities = service.retrieve(tempUserId);

        List<TodoDTO> dtos = todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());

        addReplies(listOfRepliesAfterUpdate, dtos);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }//func




    @DeleteMapping("/deleteTodo")
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO todoDTO){
        return null;
    }//func




    @DeleteMapping("/deleteReply")
    public ResponseEntity<?> deleteReply(@RequestBody TodoDTO todoDTO){
        return null;
    }//func



    //************>>>>>>>>> HELPER METHOD AREA <<<<<<<<<<<************





    private void addReplies(List<TodoReplyEntity> replyEntities, List<TodoDTO> dtos) {
        for(TodoDTO dto : dtos){
            for(TodoReplyEntity todoReplyEntity : replyEntities){

                log.info("Todo 아이디 : " + dto.getId());
                log.info("TodoReply의 parentTodo 아이디 : " + todoReplyEntity.getParentTodoId());

                if(dto.getId().equals(todoReplyEntity.getParentTodoId())){
                    log.info("todoDTO에 리플라이 넣음");
                    log.info(todoReplyEntity.getParentTodoId());
                    dto.getReplies().add(todoReplyEntity);
                }
            }//inner for
        }//outer for
    }//func

}//end of class
