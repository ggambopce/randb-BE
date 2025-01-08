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

    public static MainPagePostResponse of(List<PostDto> list){ // 서비스로직에서 dto를 응답dto로 변환
        return new MainPagePostResponse(list);
    }
}
