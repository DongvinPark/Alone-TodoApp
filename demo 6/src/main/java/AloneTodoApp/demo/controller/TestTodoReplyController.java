package AloneTodoApp.demo.controller;


import AloneTodoApp.demo.dto.ResponseDTO;
import AloneTodoApp.demo.dto.TodoDTO;
import AloneTodoApp.demo.model.TodoEntity;
import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.service.TestTodoReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/*
* 생각 해보자. TodoReplyEntity와 관련된 컨트롤러, 서비스가 굳이 필요한가?
* >> 필요하다. 만들고, 수정하고, 삭제하는 작업은 별도의 컨트롤러, 서비스, 레포지토리가 필요하다.
* >> 단, TodoReplyEntity에 대한 별도의 'read' 메서드는 필요치 않다. TodoReplyEntity는 오로지 TodoEntity를 읽어들이는 작업을 통해서만 접근할 수 있기 때문이다.
* */

@RestController
@RequestMapping("/testTodoReply")
public class TestTodoReplyController {



}//end of class
