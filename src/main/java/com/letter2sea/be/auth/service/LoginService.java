package com.letter2sea.be.auth.service;

import com.letter2sea.be.member.Member;
import com.letter2sea.be.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long login(Member oauthMember) {
        Optional<Member> optionalMember = memberRepository.findByKakaoId(oauthMember.getKakaoId());

        if (optionalMember.isEmpty()) {
            memberRepository.save(oauthMember);
            return oauthMember.getId();
        }
        return optionalMember.get().getId();
    }

    @Transactional
    public void updateRefreshToken(String jwtRefreshToken, Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        findMember.updateRefreshToken(jwtRefreshToken);
        findMember.updateLastLoginTime();
    }

    @Transactional
    public void deleteRefreshToken(Long decodedMemberId) {
        Member findMember = memberRepository.findById(decodedMemberId)
            .orElseThrow(RuntimeException::new);

        findMember.updateRefreshToken(null);
    }
}
