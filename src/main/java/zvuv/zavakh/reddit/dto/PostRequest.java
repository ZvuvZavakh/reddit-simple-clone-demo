package zvuv.zavakh.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private Long id;
    private String subredditName;
    private String title;
    private String url;
    private String description;
}
