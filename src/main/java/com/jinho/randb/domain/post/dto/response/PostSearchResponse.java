package com.jinho.randb.domain.post.dto.response;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostSearchResponse {

    private boolean nextPage;
    private List<PostDto> posts;
}
