package com.letter2sea.be.letter.dto.request;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.member.Member;
import lombok.Getter;

@Getter
public class ReplyCreateRequest {

    private String title;
    private String content;

    public Letter toEntity(Member member, Letter letter) {
        return Letter.builder()
            .writer(member)
            .title(title)
            .content(content)
            .replyLetterId(letter.getId())
            .build();
    }
}
