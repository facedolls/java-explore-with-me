package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public List<CommentDto> getAllCommentsByEvent(long eventId, Pageable pageable) {
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageable);
        return commentMapper.fromCommentListToDto(comments);
    }
}
