package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto fromCommentToDto(Comment comment);

    List<CommentDto> fromCommentListToDto(List<Comment> comments);
}
