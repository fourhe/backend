package com.letter2sea.be.menu.dto;

import com.letter2sea.be.member.Member;

public record MenuInfoResponse(int receivedThankCount, boolean firstLogin, String emailAddress, boolean notificationEnabled, int trashCount) {

    public MenuInfoResponse(Member member, int trashCount) {
        this(member.getThankCount(), member.isFirstLogin(), member.getEmail(),
            member.isNotificationEnabled(), trashCount);
    }
}
