package Task_Management_service.service;

import Task_Management_service.dto.request.CommentReq;
import Task_Management_service.dto.request.TeamMembersReq;
import Task_Management_service.dto.response.CommentRes;
import Task_Management_service.dto.response.TeamMembersRes;

import java.util.List;

public interface CommentService {
    CommentRes createComment(CommentReq commentReq);
    CommentRes getCommentById(Long id);
    List<CommentRes> getAllComments();
    void deleteCommentById(Long id);
}
