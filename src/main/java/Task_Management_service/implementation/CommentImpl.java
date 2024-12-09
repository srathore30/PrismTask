package Task_Management_service.implementation;

import Task_Management_service.constant.ApiErrorCodes;
import Task_Management_service.dto.request.CommentReq;
import Task_Management_service.dto.response.CommentRes;
import Task_Management_service.entity.CommentEntity;
import Task_Management_service.entity.TaskEntity;
import Task_Management_service.entity.UserEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.repository.CommentRepo;
import Task_Management_service.repository.TaskRepo;
import Task_Management_service.repository.UserRepo;
import Task_Management_service.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentImpl implements CommentService {
    private final CommentRepo commentRepo;
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    public CommentImpl(CommentRepo commentRepo, TaskRepo taskRepo, UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    @Override
    public CommentRes createComment(CommentReq commentReq) {
        TaskEntity task = taskRepo.findById(commentReq.getTaskId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TASK_NOT_FOUND.getErrorCode(),ApiErrorCodes.TASK_NOT_FOUND.getErrorMessage()));
        UserEntity user = userRepo.findById(commentReq.getUserId())
                .orElseThrow(() -> new NoSuchElementFoundException(
                        ApiErrorCodes.USER_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.USER_NOT_FOUND.getErrorMessage()
                ));
        CommentEntity commentEntity = mapToEntity(commentReq, task, user);
        CommentEntity savedComment = commentRepo.save(commentEntity);
        return mapToDto(savedComment);
    }

    @Override
    public CommentRes getCommentById(Long id) {
        CommentEntity commentEntity = commentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(
                        ApiErrorCodes.COMMENT_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.COMMENT_NOT_FOUND.getErrorMessage()
                ));

        return mapToDto(commentEntity);
    }

    @Override
    public List<CommentRes> getAllComments() {
        List<CommentEntity> commentEntityList = commentRepo.findAll();
        return commentEntityList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommentById(Long id) {
        CommentEntity commentEntity = commentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(
                        ApiErrorCodes.COMMENT_NOT_FOUND.getErrorCode(),
                        ApiErrorCodes.COMMENT_NOT_FOUND.getErrorMessage()
                ));
        commentRepo.delete(commentEntity);
    }

    private CommentRes mapToDto(CommentEntity commentEntity) {
        CommentRes commentRes = new CommentRes();
        commentRes.setId(commentEntity.getId());
        commentRes.setContent(commentEntity.getContent());
        commentRes.setUserId(commentEntity.getUser().getId());
        commentRes.setUserName(commentEntity.getUser().getUsername());
        commentRes.setTaskId(commentEntity.getTask().getId());
        commentRes.setTaskTitle(commentEntity.getTask().getTitle());

        return commentRes;
    }

    private CommentEntity mapToEntity(CommentReq commentReq, TaskEntity task, UserEntity user) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentReq.getContent());
        commentEntity.setTask(task);
        commentEntity.setUser(user);

        return commentEntity;
    }
}
