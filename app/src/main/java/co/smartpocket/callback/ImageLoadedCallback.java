package co.smartpocket.callback;

import android.widget.ProgressBar;
import com.squareup.picasso.Callback;

/**
 * Created by hugo on 9/7/14.
 */
public class ImageLoadedCallback implements Callback {
    ProgressBar progressBar;

    public  ImageLoadedCallback(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}
