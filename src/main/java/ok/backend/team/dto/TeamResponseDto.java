package ok.backend.team.dto;
import lombok.*;
import ok.backend.team.domain.entity.Team;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDto {
    private Long id;

    private String name;

    private Long creatorId;

    private LocalDateTime createAt;

    private String imageUrl;

    public TeamResponseDto(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.creatorId = team.getCreatorId();
        this.createAt = team.getCreateAt();
        this.imageUrl = team.getImageUrl();

    }
}
