package com.letter2sea.be.member.dto;

import jakarta.validation.constraints.Email;

public record MemberUpdateRequest(@Email String email) {
}
