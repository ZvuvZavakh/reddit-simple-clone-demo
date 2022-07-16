package zvuv.zavakh.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import zvuv.zavakh.reddit.dto.VoteDto;
import zvuv.zavakh.reddit.model.Post;
import zvuv.zavakh.reddit.model.User;
import zvuv.zavakh.reddit.model.Vote;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Vote mapDtoToEntity(VoteDto voteDto, Post post, User user);
}
