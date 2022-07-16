package zvuv.zavakh.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zvuv.zavakh.reddit.model.VoteType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    private Long postId;
    private VoteType voteType;
}
