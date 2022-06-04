package android.sulc.nasaapod.api;

import android.sulc.nasaapod.models.APODModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APOD {
    @GET("apod")
    Call<APODModel> getAPOD(@Query("date") String Date,
                            @Query("hd") boolean hd,
                            @Query("api_key") String API_KEY);

}
