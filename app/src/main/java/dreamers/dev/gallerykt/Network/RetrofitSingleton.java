package dreamers.dev.gallerykt.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JoseLuis on 19/04/2016.
 */
public class RetrofitSingleton {

    public static final String BASE_URL = "http://jsonplaceholder.typicode.com";
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static final ApiServiceDescriptor apiService = retrofit.create(ApiServiceDescriptor.class);
}
