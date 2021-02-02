package com.leaf.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GlobalExceptionMessage {

    INVALID_PARAMETER(10001, "올바르지 않은 파라메터 입니다."),
    INVALID_TOKEN_OWNER(10002, "토큰 생성자는 캐시를 받을 수 없습니다."),
    INVALID_TOKEN_ALREADY(10003, "토큰이 모두 사용되었습니다"),
    INVALID_USER(10004, "올바르지 않은 사용자 입니다."),
    INVALID_TOKEN_ROOM(10005, "해당 채팅방에서 발급된 토큰이 아닙니다."),
    INVALID_ROOM(10006, "올바르지 않은 채팅방 입니다."),
    INVALID_USER_ROOM(10007, "올바르지 않은 채팅방 혹은 사용자입니다."),
    INVALID_USER_BY_TOKEN_OWNER(10008, "토큰 생성자는 캐시를 받을 수 없습니다."),
    INVALID_TOKEN(10009, "유효한 토큰이 아닙니다."),
    INVALID_DUPLICATE_TOKEN(10010, "중복으로 토큰 캐시를 받을 수 없습니다."),
    INVALID_DUPLICATE_USER(10011, "채팅방에 이미 존재하는 사용자 아이디입니다."),
    INVALID_SERVER_TOKEN(10012, "요청이 정상적으로 처리되지 않았습니다. 다시 시도해주세요."),
    INVALID_TOKEN_OWNER2(10013, "토큰를 생성한 사용자가 아닙니다."),
    INVALID_TOKEN_DATE(10014, "발급 7일 이내의 건 만 조회가 가능합니다.")
    ;

    private int code;
    private String message;

    public GlobalException exception() {
        return new GlobalException(this);
    }
}
