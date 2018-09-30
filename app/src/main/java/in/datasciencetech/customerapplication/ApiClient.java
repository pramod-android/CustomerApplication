package in.datasciencetech.customerapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {
    String BASE_URL = "http://ivychain.com/ismtest/test/";

    @GET("getLatLongForTest")
    Call<Location> getHeroes();

}
