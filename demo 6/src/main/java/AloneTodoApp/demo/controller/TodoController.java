package AloneTodoApp.demo.controller;

import AloneTodoApp.demo.dto.ResponseDTO;
import AloneTodoApp.demo.dto.TodoDTO;
import AloneTodoApp.demo.dto.TodoReplyDTO;
import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.service.TodoReplyService;
import AloneTodoApp.demo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/Todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @Autowired
    private TodoReplyService replyService;



    @GetMapping("/getTodo")
    public ResponseEntity<?> getTodoLists(@AuthenticationPrincipal String userId){

        //String tempUserId = "temporary-user"; //for TEST

        List<TodoEntity> toDoEntities = service.retrieve(userId);

        List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(userId);
        log.info("리플라이엔티티 리스트 길이 보기 : " + replyEntities.size());
        for(TodoReplyEntity ret : replyEntities){
            log.info("리플라이 엔티티들의 parentTodoId 보기 : " + ret.getParentTodoId());
        }

        List<TodoDTO> dtos = makeDtoListFromEntityList(toDoEntities);

        addReplies(replyEntities, dtos);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }//func




    @PostMapping("/createTodo")
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId , @RequestBody TodoDTO todoDTO){
        try{

            log.info("투듀 컨트롤러 크리에이트 투듀 메서드 진입");

            //String tempUserId = "temporary-user"; //for TEST

            TodoEntity entity = TodoDTO.toEntity(todoDTO);

            entity.setId(null);

            entity.setUserId(userId);

            List<TodoEntity> toDoentities = service.create(entity);

            List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(userId);

            List<TodoDTO> dtos = makeDtoListFromEntityList(toDoentities);

            addReplies(replyEntities, dtos);

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }//try
        catch (Exception e) {
            log.info("투듀 컨트롤러 크리에이트 투듀 메서드 캣치 파트 진입");
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    } //func





    @PostMapping("/makeReply")
    public ResponseEntity<?> createTodoReply(@AuthenticationPrincipal String userId , @RequestBody TodoDTO todoDTO ){

        log.info("makeReply method entered");

        try{
            //String tempUserId = "temporary-user"; //for TEST

            //String title = todoDTO.getReplies().get(todoDTO.getReplies().size()-1).getTitle();

            replyService.createReply(userId, todoDTO.getId(), "");

            return getTodoLists(userId);
        }
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }




    @PutMapping("/updateTodo")
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO todoDTO){
        try {
            //String tempUserId = "temporary-user";

            TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);

            todoEntity.setUserId(userId);

            List<TodoEntity> todoEntities = service.update(todoEntity);

            List<TodoDTO> dtos = makeDtoListFromEntityList(todoEntities);

            List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(userId);

            addReplies(replyEntities, dtos);

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }//func




    @PutMapping("/updateReply")
    public ResponseEntity<?> updateReply(@AuthenticationPrincipal String userId, @RequestBody TodoReplyDTO todoReplyDTO){

        /*
        * 로직을 좀 세워보자. 이놈은 특정한 Todo 튜플 하나에 존재하는 TodoReply 항목중 하나만을 딱 찍어서 타이틀을 수정하는 것이다.
        * 프런트 엔드에서 무엇을 입력으로 받아야 하지? 결국 딱 찍어서 수정하고자 하는 TodoReply하나만 있으면 된다.
        * 그래서 최종적으로 무엇을 유저에게 리턴해야 하지? >>> 결과물은 결국 getTodo의 결과물과 일치해야 한다!!
        * */

        log.info("updateReply entered");

        try {
            //String tempUserId = "temporary-user";

            TodoReplyEntity replyEntity = TodoReplyDTO.toEntity(todoReplyDTO);

            replyEntity.setUserId(userId);

            List<TodoReplyEntity> listOfRepliesAfterUpdate = replyService.updateReply(replyEntity);

            List<TodoEntity> todoEntities = service.retrieve(userId);

            List<TodoDTO> dtos = makeDtoListFromEntityList(todoEntities);

            addReplies(listOfRepliesAfterUpdate, dtos);

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }//func




    //Todo 아이템이 사라지면, 그 아이템 밑에 존재하던 리플라이들도 전부 없애야 한다!!
    //이것을 구현하는 로직을 Todo Service 계층의 delete 메서드에서 구현해야 한다.
    @DeleteMapping("/deleteTodo")
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO todoDTO){

        //String tempUserId = "temporary-user";

        TodoEntity todoEntity = TodoDTO.toEntity(todoDTO);

        todoEntity.setUserId(userId);

        List<TodoEntity> todoEntities = service.delete(todoEntity, todoDTO);

        List<TodoDTO> dtos = makeDtoListFromEntityList(todoEntities);

        List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(userId);

        addReplies(replyEntities, dtos);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }//func




    //이놈은 특정한 Todo 아이템에 속해 있는 특정한 리플라이 하나를 골라서 삭제하는 것이다.
    @DeleteMapping("/deleteReply")
    public ResponseEntity<?> deleteReply(@AuthenticationPrincipal String userId, @RequestBody TodoReplyDTO todoReplyDTO){

        //String tempUserId = "temporary-user";

        TodoReplyEntity replyEntity = TodoReplyDTO.toEntity(todoReplyDTO);

        replyEntity.setUserId(userId);

        List<TodoReplyEntity> listOfRepliesAfterDelete = replyService.deleteReply(replyEntity);

        List<TodoEntity> todoEntities = service.retrieve(userId);

        List<TodoDTO> dtos = makeDtoListFromEntityList(todoEntities);

        addReplies(listOfRepliesAfterDelete, dtos);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
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





    private List<TodoDTO> makeDtoListFromEntityList( List<TodoEntity> todoEntities ){
        List<TodoDTO> todoDTOList = new ArrayList<>();

        for(TodoEntity todoEntity : todoEntities){
            TodoDTO todoDTO = TodoDTO.builder()
                    .id(todoEntity.getId())
                    .title(todoEntity.getTitle())
                    .done(todoEntity.isDone())
                    //.isFailed(todoEntity.isFailed())
                    .replies(new LinkedList<TodoReplyEntity>())
                    .dueDate(String.valueOf(todoEntity.getDueDate()))
                    .build();

            todoDTOList.add(todoDTO);
        }//for

        return todoDTOList;
    }

}//end of class
