package zvuv.zavakh.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zvuv.zavakh.reddit.dto.SubredditDto;
import zvuv.zavakh.reddit.exception.RedditException;
import zvuv.zavakh.reddit.mapper.SubredditMapper;
import zvuv.zavakh.reddit.model.Subreddit;
import zvuv.zavakh.reddit.repository.SubredditRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Autowired
    public SubredditService(SubredditRepository subredditRepository, SubredditMapper subredditMapper) {
        this.subredditRepository = subredditRepository;
        this.subredditMapper = subredditMapper;
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditMapper.mapDtoToEntity(subredditDto);
        subredditRepository.save(subreddit);
        subredditDto.setId(subreddit.getId());

        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        List<Subreddit> subreddits = subredditRepository.findAll();
        return subreddits.stream()
                .map(subredditMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public SubredditDto get(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RedditException("Subreddit not found"));

        return subredditMapper.mapEntityToDto(subreddit);
    }
}
