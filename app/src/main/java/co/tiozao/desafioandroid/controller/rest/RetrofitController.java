package co.tiozao.desafioandroid.controller.rest;

import co.tiozao.desafioandroid.controller.Config;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitController {

    public static RetrofitController instance;

    private Retrofit retrofit;

    public static void initialize()
    {
        instance = new RetrofitController();

        instance.setRetrofit(new Retrofit.Builder()
            .baseUrl(Config.API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build());
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }
}
