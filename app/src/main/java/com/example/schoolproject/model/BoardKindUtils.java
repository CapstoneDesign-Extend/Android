package com.example.schoolproject.model;

public class BoardKindUtils {
    public static String getBoardTitleByEnum(BoardKind boardKind) {
        switch (boardKind) {
            case ISSUE:
                return "학과소식";
            case TIP:
                return "학과꿀팁";
            case REPORT:
                return "신문고";
            case QNA:
                return "Q&A";
            case MARKET:
                return "장터게시판";
            case FREE:
                return "자유게시판";
            case FRESH:
                return "새내기게시판";
            case FOSSIL:
                return "졸업생게시판";
            case INFO:
                return "정보게시판";
            case CAREER:
                return "취업·진로";
            default:
                return "";
        }
    }
    public static String getBoardTitleByString(String boardName) {
        try {
            BoardKind boardKind = BoardKind.valueOf(boardName);
            return getBoardTitleByEnum(boardKind);
        } catch (IllegalArgumentException e) {
            // 해당 enum 이 없을 경우
            return "";
        }
    }
}
