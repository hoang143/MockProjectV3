package com.example.mockprojectv3.request;

import com.example.mockprojectv3.utils.Credentials;
import com.example.mockprojectv3.utils.MovieApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static OkHttpClient.Builder okBuilder;
    private static Retrofit.Builder retrofitBuilder;
    private static Retrofit retrofit;
    private static MovieApi movieApi;

    public static MovieApi getMovieApi() {
        if (movieApi == null) {
            initMovieApi();
        }
        return movieApi;
    }

    private static void initMovieApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        okBuilder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    // Thêm API key vào header của yêu cầu
                    Request originalRequest = chain.request();
                    Request newRequest = originalRequest.newBuilder()
                            .header("api_key", Credentials.API_KEY)
                            .build();
                    return chain.proceed(newRequest);
                });

        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(okBuilder.build());

        retrofit = retrofitBuilder.build();
        movieApi = retrofit.create(MovieApi.class);
    }
}
