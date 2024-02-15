package com.letter2sea.be.trash;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.exception.Letter2SeaException;
import com.letter2sea.be.exception.type.CommonExceptionType;
import com.letter2sea.be.trash.dto.TrashDetailResponse;
import com.letter2sea.be.trash.dto.TrashPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trash")
public class TrashController {

    private final JwtProvider jwtProvider;
    private final TrashService trashService;

    @GetMapping
    public TrashPaginatedResponse getList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestParam String type, Pageable pageable) {
        Long memberId = jwtProvider.decode(authorization);
        if (!(type.equals("letter") || type.equals("reply"))) {
            throw new Letter2SeaException(CommonExceptionType.INCORRECT_REQUEST_PARAM);
        }
        return trashService.findList(type, pageable, memberId);
    }

    @GetMapping("/{id}")
    public TrashDetailResponse getDetail(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        Long memberId = jwtProvider.decode(authorization);
        return trashService.findDetail(id, memberId);
    }

    @PostMapping("/{id}")
    public void restore(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        trashService.restore(id, memberId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        trashService.delete(id, memberId);
    }
}
