package com.letter2sea.be.trash;

import com.letter2sea.be.auth.jwt.JwtProvider;
import com.letter2sea.be.trash.dto.TrashDetailResponse;
import com.letter2sea.be.trash.dto.TrashListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trash")
public class TrashController {

    private final JwtProvider jwtProvider;
    private final TrashService trashService;

    @GetMapping
    public List<TrashListResponse> getList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Long memberId = jwtProvider.decode(authorization);
        return trashService.findList(memberId);
    }

    @GetMapping("/{id}")
    public TrashDetailResponse getDetail(@PathVariable Long id,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        Long memberId = jwtProvider.decode(authorization);
        return trashService.findDetail(id, memberId);
    }
}
