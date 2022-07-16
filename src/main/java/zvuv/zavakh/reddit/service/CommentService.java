package zvuv.zavakh.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zvuv.zavakh.reddit.dto.CommentDto;
import zvuv.zavakh.reddit.exception.RedditException;
import zvuv.zavakh.reddit.mapper.CommentMapper;
import zvuv.zavakh.reddit.model.Comment;
import zvuv.zavakh.reddit.model.NotificationEmail;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.User;
import zvuv.zavakh.reddit.repository.CommentRepository;
import zvuv.zavakh.reddit.repository.PostRepository;
import zvuv.zavakh.reddit.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            AuthService authService,
            CommentMapper commentMapper,
            MailContentBuilder mailContentBuilder,
            MailService mailService
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.commentMapper = commentMapper;
        this.mailContentBuilder = mailContentBuilder;
        this.mailService = mailService;
    }

    public CommentDto save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RedditException("Post not found"));
        User user = authService.getCurrentUser();
        Comment comment = commentMapper.mapDtoToEntity(commentDto, post, user);

        commentRepository.save(comment);
        commentDto.setId(comment.getId());
        commentDto.setUsername(comment.getUser().getUsername());
        commentDto.setCreatedDate(comment.getCreatedDate());
        sendCommentNotification(post, user);

        return commentDto;
    }

    private void sendCommentNotification(Post post, User user) {
        String message = mailContentBuilder.build(
                user.getUsername() + " posted a comment on your post: " + post.getUrl()
        );
        mailService.sendMail(new NotificationEmail("New comment", post.getUser().getEmail(), message));
    }

    public List<CommentDto> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RedditException("Post not found"));

        return commentRepository.findAllByPost(post)
                .stream()
                .map(commentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RedditException("User not found"));

        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
