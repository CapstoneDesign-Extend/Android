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
        } catch (NullPointerException e){
            return "error:BoardName is NULL!";
        }
    }
    public static BoardKind getBoardKindByKorean(String koreanBoardName){
        switch (koreanBoardName){
            case "학과소식":
                return BoardKind.ISSUE;
            case "학과꿀팁":
                return BoardKind.TIP;
            case "신문고":
                return BoardKind.REPORT;
            case "Q&A":
                return BoardKind.QNA;
            case "장터게시판":
                return BoardKind.MARKET;
            case "자유게시판":
                return BoardKind.FREE;
            case "새내기게시판":
                return BoardKind.FRESH;
            case "졸업생게시판":
                return BoardKind.FOSSIL;
            case "정보게시판":
                return BoardKind.INFO;
            case "취업·진로":
                return BoardKind.CAREER;
            default:
                return null;
        }
    }
}
