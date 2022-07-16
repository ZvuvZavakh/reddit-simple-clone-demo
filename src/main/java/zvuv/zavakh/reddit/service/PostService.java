package zvuv.zavakh.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zvuv.zavakh.reddit.dto.PostRequest;
import zvuv.zavakh.reddit.dto.PostResponse;
import zvuv.zavakh.reddit.exception.RedditException;
import zvuv.zavakh.reddit.mapper.PostMapper;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.Subreddit;
import zvuv.zavakh.reddit.model.User;
import zvuv.zavakh.reddit.repository.PostRepository;
import zvuv.zavakh.reddit.repository.SubredditRepository;
import zvuv.zavakh.reddit.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Autowired
    public PostService(
            PostRepository postRepository,
            SubredditRepository subredditRepository,
            UserRepository userRepository,
            AuthService authService,
            PostMapper postMapper
    ) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.postMapper = postMapper;
    }

    public PostResponse save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new RedditException("Subreddit not found"));
        User user = authService.getCurrentUser();
        Post post = postMapper.mapToEntity(postRequest, subreddit, user);
        postRepository.save(post);

        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RedditException("Post not found"));

        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RedditException("Subreddit not found"));

        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RedditException("User not found"));

        return postRepository.findAllByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
