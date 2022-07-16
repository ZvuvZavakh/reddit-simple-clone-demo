package zvuv.zavakh.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zvuv.zavakh.reddit.model.Comment;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByUser(User user);
}
