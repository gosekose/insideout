package com.insideout.exception

enum class BusinessErrorCause(
    val message: String,
) {
    UNAUTHORIZED("권한이 없습니다."),
    NOT_FOUND("정보를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS("이메일이 이미 존재합니다."),
    MEMBER_NOT_FOUND("회원이 존재하지 않습니다."),
    MEMORY_MARBLE_NOT_FOUND("기억구슬이 존재하지 않습니다."),
    DISTRIBUTE_LOCK_TRY_FAILED("잠시 후에 다시 시도해주세요."),
    INTERNAL_SERVER_ERROR("고객센터에 문의해주세요."),
}
