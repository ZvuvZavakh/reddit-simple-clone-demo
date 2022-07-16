package zvuv.zavakh.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import zvuv.zavakh.reddit.dto.CommentDto;
import zvuv.zavakh.reddit.model.Comment;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Comment mapDtoToEntity(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    CommentDto mapEntityToDto(Comment comment);
}
