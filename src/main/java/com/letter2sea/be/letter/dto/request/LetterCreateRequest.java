package com.letter2sea.be.letter.dto.request;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LetterCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public Letter toEntity(Member member) {
       return Letter.builder()
           .writer(member)
           .title(title)
           .content(content)
           .build();
    }
}

