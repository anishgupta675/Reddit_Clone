package org.fsp.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fsp.springredditclone.model.VoteType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {

    private VoteType voteType;
    private Long postId;
}
