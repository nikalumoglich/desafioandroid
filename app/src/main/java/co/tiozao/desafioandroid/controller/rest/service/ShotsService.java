package co.tiozao.desafioandroid.controller.rest.service;

import java.util.List;

import co.tiozao.desafioandroid.controller.Config;
import co.tiozao.desafioandroid.model.ShotModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ShotsService {

    @Headers(Config.API_OAUTH_HEADER)
    @GET("/v1/shots")
    Call<List<ShotModel>> getShots(@Query("per_page") int perPage);

    @Headers(Config.API_OAUTH_HEADER)
    @GET
    Call<List<ShotModel>> getShots(@Url String url);
}
