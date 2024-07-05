package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CommentControllerPrivate {
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@Valid @RequestBody NewCommentDto commentDto,
                             @PathVariable Long userId,
                             @PathVariable Long eventId) {
        log.info("Create comment {}", commentDto);
        return commentService.createByAuthor(commentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(@Valid @RequestBody UpdateCommentDto commentRequest,
                             @PathVariable Long userId,
                             @PathVariable Long commentId) {
        commentRequest.setId(commentId);
        log.info("Author update comment {}", commentRequest);
        return commentService.updateByAuthor(commentRequest, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable Long userId,
                                           @RequestParam(defaultValue = DEFAULT_FROM) @Min(0) Integer from,
                                           @RequestParam(defaultValue = DEFAULT_SIZE) Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdDate").descending());
        log.info("Get all comments by author");
        return commentService.getAllCommentsByAuthor(userId, pageable);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long userId,
                           @PathVariable Long commentId) {
        log.info("Author delete comment by id {}", commentId);
        commentService.deleteByCommentIdByAuthor(userId, commentId);
    }
}
