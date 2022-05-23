package AloneTodoApp.demo.controller;

import AloneTodoApp.demo.dto.ResponseDTO;
import AloneTodoApp.demo.dto.TodoDTO;
import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.service.TestTodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/testTodo")
public class TestTodoController {

    @Autowired
    private TestTodoService service;


    @GetMapping
    public ResponseEntity<?> getTodoLists(){

        String tempUserId = "temporary-user"; //for TEST

        List<TodoEntity> entities = service.retrieve(tempUserId);

        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

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

            List<TodoEntity> entities = service.create(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    } //func


}//end of class
