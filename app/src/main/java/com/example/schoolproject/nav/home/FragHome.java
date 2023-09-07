package com.example.schoolproject.nav.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.BoardKindUtils;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.model.retrofit.BoardRequestInfo;
import com.example.schoolproject.mypage.MyPageActivity;
import com.example.schoolproject.search.SearchActivity;
import com.example.schoolproject.test.DataBaseHelper;
import com.example.schoolproject.R;
import com.example.schoolproject.notification.NotificationActivity;
import com.example.schoolproject.model.ui.DataHomeBoard;
import com.example.schoolproject.model.ui.DataHomeDynamicMorning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;

public class FragHome extends Fragment {
    private View view;
    private List<Object> dataList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private CompositeDisposable disposables = new CompositeDisposable();  // to manage subscriptions

    @Override
    public void onResume() {
        super.onResume();

        // add testData to ViewHolder1 (DynamicMorning) ::
        String MorningTitle, MorningLecture1, MorningLecture2;
        MorningTitle = "오늘은 강의가 2개 있어요.";
        MorningLecture1 = "09:00 캡스톤디자인";
        MorningLecture2 = "13:00 모바일프로그래밍";

        DataHomeDynamicMorning data0 = new DataHomeDynamicMorning(MorningTitle, MorningLecture1, MorningLecture2);

        // Load data and set adapter
//        dataList.clear();
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
//        DataHomeBoard data1 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "자유게시판", "2");
//        DataHomeBoard data2 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "졸업생게시판", "2");
//        DataHomeBoard data3 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "새내기게시판", "2");
//        DataHomeBoard data4 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "정보게시판", "2");
//        DataHomeBoard data5 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "취업·진로", "2");
//
//        dataList.add(data0);
//        dataList.add(data1);
//        dataList.add(data2);
//        dataList.add(data3);
//        dataList.add(data4);
//        dataList.add(data5);

//        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);
        setHasOptionsMenu(true);

        // setting ToolBar
        Toolbar toolbar = view.findViewById(R.id.toolbar_home);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // setting Multi-View RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_home);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        dataList = new ArrayList<>();  // // initialize empty data
        adapter = new HomeRecyclerViewAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);

        // add testData to ViewHolder1 (DynamicMorning) ::
        String MorningTitle, MorningLecture1, MorningLecture2;
        MorningTitle = "오늘은 강의가 2개 있어요.";
        MorningLecture1 = "09:00 캡스톤디자인";
        MorningLecture2 = "13:00 모바일프로그래밍";

        DataHomeDynamicMorning data0 = new DataHomeDynamicMorning(MorningTitle, MorningLecture1, MorningLecture2);
        dataList.add(data0);


        // set data from DB to ViewHolder2 (BoardsPreview) ::


        // Retrofit을 사용하는 BoardApiService의 메소드를 RxJava의 Single로 변경

        BoardApiService apiService = new BoardApiService();
        List<BoardKind> customOrder = Arrays.asList(BoardKind.ISSUE, BoardKind.FRESH, BoardKind.INFO, BoardKind.CAREER, BoardKind.FOSSIL);
        List<Single<List<Board>>> requests = new ArrayList<>();

        for (BoardKind boardKind: customOrder){
            requests.add(apiService.getBoardsByBoardKindAmountRx(boardKind, 2));
        }

        // Single.zip 연산자는 Single목록의 결과를 (각 Single은 비동기적으로 처리되지만)목록의 순서대로 배열에 담기 때문에 별도의 순서 정렬 필요 없음
        Single.zip(requests, responses -> {
                    // responses는 여기서 Object의 배열입니다. 각 Single에서 반환된 결과 리스트가 담겨 있습니다.
                    // 이것을 적절히 처리하여 dataHomeBoardList에 추가
                    List<DataHomeBoard> dataHomeBoardList = new ArrayList<>();


                    for (int i=0; i<responses.length; i++){
                        List<String> postIds = new ArrayList<>();
                        List<String> titles = new ArrayList<>();
                        List<String> contents = new ArrayList<>();
                        // 각 Board를 DataHomeBoard로 변환하고 dataHomeBoardList에 추가하는 로직
                        List<Board> boardList = (List<Board>) responses[i];  // response 하나는 요청한 board 수만큼의 board가 들어있는 boardList임
                        DataHomeBoard dataHomeBoard = new DataHomeBoard();

                        for (Board b: boardList){  // boardList(내용 자체는 postList. 백엔드쪽에서 Post모델 이름을 Board라고 만들어놨기 때문)에서 내용 추출

                            postIds.add(String.valueOf(b.getId()));
                            titles.add(b.getTitle());
                            contents.add(b.getContent());
                        }

                        dataHomeBoard.setBoardKind(customOrder.get(i));
                        dataHomeBoard.setBoard_name(BoardKindUtils.getBoardTitleByEnum(customOrder.get(i)));
                        dataHomeBoard.setPost_ids(postIds);
                        dataHomeBoard.setPost_titles(titles);
                        dataHomeBoard.setPost_contents(contents);
                        // 완성된 모델을 여기 넣고 정렬 후 Data Setting
                        dataHomeBoardList.add(dataHomeBoard);
                    }

                    dataList.addAll(dataHomeBoardList);
                    Log.d("FragHome", "dataHomeBoardList::"+dataHomeBoardList.toString());
                    Log.d("FragHome", "dataList::"+dataList.toString());
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d("FragHome", "데이터를 가져왔습니다.");
                    adapter.notifyDataSetChanged();

                    // 모든 작업이 완료된 후 실행될 코드
                    // 예: Progress dialog를 닫는다 or 새로고침 종료
                }, throwable -> {
                    // 에러 처리
                    Log.e("Error", throwable.getMessage());
                });




//        BoardApiService apiService = new BoardApiService();
//        List<Observable<List<Board>>> observables = new ArrayList<>();
//
//
//        observables.add(apiService.getBoardsByBoardKindAmount(BoardKind.FREE, 2)
//                .observeOn(AndroidSchedulers.mainThread()));
//        observables.add(apiService.getBoardsByBoardKindAmount(BoardKind.FRESH, 2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()));
//        observables.add(apiService.getBoardsByBoardKindAmount(BoardKind.INFO, 2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()));
//        observables.add(apiService.getBoardsByBoardKindAmount(BoardKind.CAREER, 2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()));
//        observables.add(apiService.getBoardsByBoardKindAmount(BoardKind.FOSSIL, 2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()));
//
//        disposables.add(Observable.merge(observables)
//                .toList()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(dataHomeBoardLists -> {
//                    // Merge and sort dataHomeBoardLists based on customOrder
//                    // ... (rest of the sorting logic)
//
//                    // Add the sorted data to the dataList and update the adapter
//                    dataList.addAll(sortedDataHomeBoardList);
//                    adapter.notifyDataSetChanged();
//                }));




//        BoardApiService apiService = new BoardApiService();
//        Call<List<Board>> call1 = apiService.getBoardsByBoardKindAmount(BoardKind.FREE, 2);
//        Call<List<Board>> call2 = apiService.getBoardsByBoardKindAmount(BoardKind.FRESH, 2);
//        Call<List<Board>> call3 = apiService.getBoardsByBoardKindAmount(BoardKind.INFO, 2);
//        Call<List<Board>> call4 = apiService.getBoardsByBoardKindAmount(BoardKind.CAREER, 2);
//        Call<List<Board>> call5 = apiService.getBoardsByBoardKindAmount(BoardKind.FOSSIL, 2);
//
//        List<DataHomeBoard> dataHomeBoardList = new ArrayList<>();
//        // 아래 callback 메소드가 완료되면 dataHomeBoardList에 비동기적으로 저장됨
//        call1.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter, BoardKind.FREE, dataHomeBoardList));
//        call2.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter, BoardKind.FRESH, dataHomeBoardList));
//        call3.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter, BoardKind.INFO, dataHomeBoardList));
//        call4.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter, BoardKind.CAREER, dataHomeBoardList));
//        call5.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter, BoardKind.FOSSIL, dataHomeBoardList));
//        // 무작위 순서 데이터들을 정렬하기
//        List<BoardKind> customOrder = new ArrayList<>();
//        customOrder.add(BoardKind.FREE);
//        customOrder.add(BoardKind.FRESH);
//        customOrder.add(BoardKind.INFO);
//        customOrder.add(BoardKind.CAREER);
//        customOrder.add(BoardKind.FOSSIL);
//        List<DataHomeBoard> sortedDataHomeBoardList = new ArrayList<>();
//        for (int i=0; i<customOrder.size(); i++){
//            for (DataHomeBoard d: dataHomeBoardList){
//                if (d.getBoardKind().equals(customOrder.get(i))){
//                    dataList.add(d);
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
        return view;
    }  // onCreateView ended

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_home, menu);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                // "notification" 아이템이 클릭되었을 때 수행할 코드 작성
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                // "search" 아이템이 클릭되었을 때 수행할 코드 작성
                Intent intent1 = new Intent(getContext(), SearchActivity.class);
                startActivity(intent1);
                return true;
            case R.id.myPage:
                // "myPage" 아이템이 클릭되었을 때 수행할 코드 작성
                Intent intent2 = new Intent(getContext(), MyPageActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();  // 구독 해제
    }

}
