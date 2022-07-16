package zvuv.zavakh.reddit.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import zvuv.zavakh.reddit.dto.SubredditDto;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.Subreddit;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapEntityToDto(Subreddit subreddit);

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToEntity(SubredditDto subredditDto);

    default Integer mapPosts(List<Post> posts) {
        return posts.size();
    }
}
