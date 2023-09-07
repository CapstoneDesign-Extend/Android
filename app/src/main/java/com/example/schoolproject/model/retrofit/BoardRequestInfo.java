package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class BoardRequestInfo {
    // save boardKind when requesting (for HomeRecyclerView's item:homeBoard)
    public final Single<List<Board>> single;
    public final BoardKind boardKind;

    public BoardRequestInfo(Single<List<Board>> single, BoardKind boardKind) {
        this.single = single;
        this.boardKind = boardKind;
    }
}
