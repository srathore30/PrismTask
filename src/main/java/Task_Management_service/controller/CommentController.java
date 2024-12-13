package Task_Management_service.Controller;

import Task_Management_service.dto.request.CommentReq;
import Task_Management_service.dto.request.TeamReq;
import Task_Management_service.dto.response.CommentRes;
import Task_Management_service.dto.response.TeamRes;
import Task_Management_service.services.CommentService;
import Task_Management_service.services.TeamServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<CommentRes> createComment(@RequestBody CommentReq commentReq) {
        CommentRes reqs = commentService.createComment(commentReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(reqs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentRes> getCommentById(@PathVariable Long id ){
        CommentRes resp = commentService.getCommentById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentRes>> getAllComments() {
        List<CommentRes> commentResList = commentService.getAllComments();
        return new ResponseEntity<>(commentResList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
