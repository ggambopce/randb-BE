package com.jinho.randb.global.exception.ex.nosuch;

public enum NoSuchErrorType {

    NO_SUCH_ACCOUNT("사용자를 찾을 수 없습니다."),
    NO_SUCH_PROFILE("해당 프로필을 찾을 수 없습니다."),
    NO_SUCH_POST("토론글을 찾을 수 없습니다."),
    NO_SUCH_IMAGE("이미지 파일을 찾을 수 없습니다."),
    NO_SUCH_EMAIL("해당 이메일을 찾을 수 없습니다."),
    NO_SUCH_NOTICE("해당 공지사항을 찾을 수 없습니다."),
    NO_SUCH_QUESTION("해당 문의사항을 찾을 수 없습니다."),
    NO_SUCH_DATA("해당 데이터를 찾을수 업습니다."),
    NO_SUCH_OPINION("해당 의견을 찾을 수 없습니다."),
    NO_SUCH_NOTIFICATION("해당 알림을 찾을 수 없습니다.");


    private final String message;

    NoSuchErrorType(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
