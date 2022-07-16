package zvuv.zavakh.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String description;
    private String url;
    private String subredditName;
    private String username;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
}
