package zvuv.zavakh.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zvuv.zavakh.reddit.dto.SubredditDto;
import zvuv.zavakh.reddit.service.SubredditService;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
public class SubredditController {

    private final SubredditService subredditService;

    @Autowired
    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @PostMapping
    public ResponseEntity<SubredditDto> create(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> get(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(subredditService.get(id));
    }
}
