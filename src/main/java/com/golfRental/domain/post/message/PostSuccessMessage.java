package com.golfRental.domain.post.message;

public final class PostSuccessMessage {

    public final static String POST_CREATED = "게시물이 생성되었습니다.";
    public final static String POST_GET_ALL = "게시물 목록이 조회되었습니다.";
    public final static String POST_GETS = "게시물 상세 조회에 성공하였습니다.";
    public final static String POST_GET_MY = "내 게시물 조회에 성공하였습니다.";
    public final static String POST_UPDATED = "게시물 수정에 성공하였습니다.";
    public final static String POST_UPDATED_STATUS = "게시물 거래 상태 수정에 성공하였습니다.";
    public final static String POST_DELETED = "게시물 삭제에 성공하였습니다.";

    private PostSuccessMessage() {
    }
}
