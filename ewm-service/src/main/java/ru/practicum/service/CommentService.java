package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public CommentDto createByAuthor(NewCommentDto commentDto, Long userId, Long eventId) {
        User author = getUserOrElseThrow(userId);
        Event event = getEventOrElseThrow(eventId);

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.fromCommentToDto(savedComment);
    }

    public List<CommentDto> getAllCommentsByEvent(Long eventId, Pageable pageable) {
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageable);
        return commentMapper.fromCommentListToDto(comments);
    }

    public void deleteByCommentIdByAuthor(Long userId, Long commentId) {
        getUserOrElseThrow(userId);
        Comment comment = getCommentOrElseThrow(commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotFoundException("Comment for author not found");
        }
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getAllCommentsByAuthor(Long userId, Pageable pageable) {
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, pageable);
        return commentMapper.fromCommentListToDto(comments);
    }

    public CommentDto updateByAuthor(UpdateCommentDto commentRequest, Long userId) {
        getUserOrElseThrow(userId);
        Comment comment = getCommentOrElseThrow(commentRequest.getId());

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotFoundException("Comment not found");
        }
        Optional.ofNullable(commentRequest.getText()).ifPresent(comment::setText);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.fromCommentToDto(updatedComment);
    }

    private Comment getCommentOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment id " + commentId + " not found"));
    }

    private Event getEventOrElseThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id " + eventId + " not found"));
    }

    private User getUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id " + userId + " found"));
    }

}
