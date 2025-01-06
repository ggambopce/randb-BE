package com.jinho.randb.domain.post.dto.response;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.Data;

import java.util.List;

@Data
public class MainPagePostResponse {

    private List<PostDto> posts;

    private  MainPagePostResponse(List<PostDto> list) {
        this.posts = list;
    }

    public static MainPagePostResponse of(List<PostDto> list){
        return new MainPagePostResponse(list);
    }
}
