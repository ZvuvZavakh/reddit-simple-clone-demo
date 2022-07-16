package zvuv.zavakh.reddit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import zvuv.zavakh.reddit.dto.PostRequest;
import zvuv.zavakh.reddit.dto.PostResponse;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.Subreddit;
import zvuv.zavakh.reddit.model.User;
import zvuv.zavakh.reddit.repository.CommentRepository;
import zvuv.zavakh.reddit.repository.VoteRepository;
import zvuv.zavakh.reddit.service.AuthService;

import java.time.Instant;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(getCreatedDate())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "description", source = "postRequest.description")
    public abstract Post mapToEntity(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Instant getCreatedDate() {
        return Instant.now();
    }

    Integer commentCount(Post post) {
        return commentRepository.findAllByPost(post)
                .size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
