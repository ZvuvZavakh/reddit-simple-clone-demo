package zvuv.zavakh.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.User;
import zvuv.zavakh.reddit.model.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User user);
}
