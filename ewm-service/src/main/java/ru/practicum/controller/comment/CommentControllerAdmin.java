package ru.practicum.controller.comment;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.service.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentControllerAdmin {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(@Valid @RequestBody UpdateCommentDto commentRequest, @PathVariable Long commentId) {
        commentRequest.setId(commentId);
        log.info("Admin update comment {}", commentRequest);
        return commentService.updateByAdmin(commentRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByIdAdmin(@PathVariable Long commentId) {
        log.info("Admin delete comment by id {}", commentId);
        commentService.deleteCommentByIdAdmin(commentId);
    }
}
