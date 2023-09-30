package com.example.schoolproject.model;

import java.util.List;

public class LikeStatus {
    private boolean isLikedBoard;
    private List<Long> likedCommentIds;

    public boolean isLikedBoard() {
        return isLikedBoard;
    }

    public void setIsLikedBoard(boolean likedBoard) {
        isLikedBoard = likedBoard;
    }

    public List<Long> getLikedCommentIds() {
        return likedCommentIds;
    }

    public void setLikedCommentIds(List<Long> likedCommentIds) {
        this.likedCommentIds = likedCommentIds;
    }
}
