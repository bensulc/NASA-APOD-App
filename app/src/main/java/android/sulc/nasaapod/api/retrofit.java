package android.sulc.nasaapod.api;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class retrofit {
    private static retrofit instance= null;
    private APOD apod;

    private retrofit() {

        Retrofit retrofitBuilder = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/planetary/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apod = retrofitBuilder.create(APOD.class);
}

    public static synchronized retrofit getInstance() {
        if(instance == null) {
            instance = new retrofit();
        }
        return instance;
    }

    public APOD getApod() {return apod;}
}
