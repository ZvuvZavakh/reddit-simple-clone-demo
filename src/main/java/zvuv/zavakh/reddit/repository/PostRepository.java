package zvuv.zavakh.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.Subreddit;
import zvuv.zavakh.reddit.model.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findAllByUser(User user);
}
