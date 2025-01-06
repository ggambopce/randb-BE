package com.jinho.randb.domain.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore // Post 직렬화 방지
    private Post post;

    private int redVotes; // RED 투표수

    private int blueVotes; // BLUE 투표수

    private String winningVoteType; // 승리한 의견 (RED 또는 BLUE)

    private double redVotePercentage; // RED 비율

    private double blueVotePercentage; // BLUE 비율

}
