package zvuv.zavakh.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zvuv.zavakh.reddit.dto.VoteDto;
import zvuv.zavakh.reddit.exception.RedditException;
import zvuv.zavakh.reddit.mapper.VoteMapper;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.User;
import zvuv.zavakh.reddit.model.Vote;
import zvuv.zavakh.reddit.model.VoteType;
import zvuv.zavakh.reddit.repository.PostRepository;
import zvuv.zavakh.reddit.repository.VoteRepository;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    @Autowired
    public VoteService(
            VoteRepository voteRepository,
            PostRepository postRepository,
            AuthService authService,
            VoteMapper voteMapper) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.authService = authService;
        this.voteMapper = voteMapper;
    }

    @Transactional
    public void vote(VoteDto voteDto) {
        User user = authService.getCurrentUser();
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new RedditException("Post not found"));
        Optional<Vote> vote = voteRepository.findTopByPostAndUserOrderByIdDesc(post, user);

        if (vote.isPresent() && vote.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new RedditException("Vote already exist");
        }

        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(voteMapper.mapDtoToEntity(voteDto, post, user));
        postRepository.save(post);
    }
}
