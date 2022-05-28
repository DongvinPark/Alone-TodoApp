package AloneTodoApp.demo.controller;

import AloneTodoApp.demo.dto.ResponseDTO;
import AloneTodoApp.demo.dto.TodoDTO;
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


    @GetMapping
    public ResponseEntity<?> getTodoLists(){

        String tempUserId = "temporary-user"; //for TEST

        List<TodoEntity> toDoEntities = service.retrieve(tempUserId);
        //TodoService 쪽의 retrieve 메서드를 고칠 필요는 없다. 어차피 내가 각각의 TodoEntity에 리플라이 리스트를 추가해 줘야 하기 때문이다.
        // todo 엔티티를 하나 찾을 때마다 그 엔티티에 대응되는 리플라이들도 붙여서 반환해야 하기 때문이다.

        List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(tempUserId);
        log.info("리플라이엔티티 리스트 길이 보기 : " + replyEntities.size());
        for(TodoReplyEntity ret : replyEntities){
            log.info("리플라이 엔티티들의 parentTodoId 보기 : " + ret.getParentTodoId());
        }

        List<TodoDTO> dtos = toDoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());

        extracted(replyEntities, dtos);

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }//func


    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO){
        try{
            String tempUserId = "temporary-user"; //for TEST

            TodoEntity entity = TodoDTO.toEntity(todoDTO);

            entity.setId(null);

            entity.setUserId(tempUserId);

            List<TodoEntity> toDoentities = service.create(entity);//TodoServece 쪽의 create 메서드를 수정해야 한다.
            //이 메서드에서 todo 엔티티 하나 찾을 때마다, 그 엔티티에 대응되는 리플라이들도 붙여서 반환해야 하기 때문이다.
            //그런데, 어차피 서비스 계층에서는 리포지토리가 기본제공하는 메서드밖에 사용하지 못한다.
            //즉, 여기에서 List<TodoEntity> entities 가 만들어질 때, 중간에 리플라이들을 각 Todo 엔티티의 고유 아이디에 맞는 것으로 선별해서 맵핑해주는 작업을 한 수가 없다는 뜻이다.

            List<TodoReplyEntity> replyEntities = replyService.retrieveReplies(tempUserId);

            //그 맵핑 작업은 결국 여기서 해야 한다. 왜냐면 엔티티의 리스트를 그냥 반환하는 것이 아니라 여기에서 추가로 리프라이 리스트를 만들어서 반환해야 하니까.
            List<TodoDTO> dtos = toDoentities.stream().map(TodoDTO::new).collect(Collectors.toList());

            extracted(replyEntities, dtos);

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }//try
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    } //func

    //한 개의 Todo 튜플의 DTO와 그 튜플의 고유 id 문자열을 입력으로 받아서 Reply를 만든다.
    //그러면 이놈은 뭘 반환해야 하지?
    // 현재 만든 리플라이만 반환하는 것이 아니라, 그 리플라이를 포함하고 있는 부모 todo 엔티티, 그리고 다른 todo 엔티티도 모두 같이 반환해야 하지 않나?
    //결과적으로 이놈은 현재 TestTodoController클래스의 getTodoLists()메서드 동작을 똑같이 따라해야 한다!!
    @PostMapping("/makeReply")
    public ResponseEntity<?> createTodoReply(
            @RequestBody TodoDTO todoDTO
    ){
        try{
            String tempUserId = "temporary-user"; //for TEST
            //String parentTodoId = todoId;
            //todoDTO.getReplies().size()-1 이 부분은 나중에 validation이 필요하다. todoDTO 내의 replies 리스트가 빈 리스트면 작동을 하지 않기 때문이다.
            String title = todoDTO.getReplies().get(todoDTO.getReplies().size()-1);

            replyService.createReply(tempUserId, todoDTO.getId(), title);

            return getTodoLists();
        }
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    //>>>>>>>>> Helper Methods Area <<<<<<<<<<<

    private void extracted(List<TodoReplyEntity> replyEntities, List<TodoDTO> dtos) {
        for(TodoDTO dto : dtos){
            for(TodoReplyEntity todoReplyEntity : replyEntities){

                log.info("Todo 아이디 : " + dto.getId());
                log.info("TodoReply의 parentTodo 아이디 : " + todoReplyEntity.getParentTodoId());

                if(dto.getId().equals(todoReplyEntity.getParentTodoId())){
                    log.info("todoDTO에 리플라이 넣음");
                    log.info(todoReplyEntity.getParentTodoId());
                    dto.getReplies().add(todoReplyEntity.getTitle());
                }
            }//inner for
        }//outer for
    }

}//end of class
