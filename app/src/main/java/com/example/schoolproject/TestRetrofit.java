package com.example.schoolproject;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class TestRetrofit {
    Context context;
    public TestRetrofit(Context context){
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderService service = retrofit.create(JsonPlaceholderService.class);

        Call<List<Post>> postsCall = service.getPosts();
        postsCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    // taking error
                    return;
                }

                List<Post> posts = response.body();
                // UI updating with posts
                Intent intent = new Intent(context, TestRetrofitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // to call startActivity() from outside of an Activity
                intent.putParcelableArrayListExtra("posts", new ArrayList<>(posts));
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // taking network error, etc problems
            }
        });
    }

}

class Post implements Parcelable {
    private int userId;
    private int id;
    private String title;
    private String body;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // Parcelable
    protected Post(Parcel in){
        userId = in.readInt();
        id = in.readInt();
        title = in.readString();
        body = in.readString();
    }
    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(body);
    }
}
interface JsonPlaceholderService{
    @GET("posts")
    Call<List<Post>> getPosts();
}

