package zvuv.zavakh.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zvuv.zavakh.reddit.dto.PostRequest;
import zvuv.zavakh.reddit.dto.PostResponse;
import zvuv.zavakh.reddit.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.save(postRequest));
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable("id") Long id) {
        return postService.get(id);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAll());
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getBySubreddit(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getBySubreddit(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponse>> getByUsername(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getByUsername(username));
    }
}
