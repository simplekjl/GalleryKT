package dreamers.dev.gallerykt.Network;

import java.util.List;

import dreamers.dev.gallerykt.Model.Photo;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by JoseLuis on 19/04/2016.
 */
public interface ApiServiceDescriptor {

    //Get method for the images the endpoint gives 500 hundred images http://jsonplaceholder.typicode.com
    @GET("/albums/1/photos")
    Call<List<Photo>> getPhotos();
}
