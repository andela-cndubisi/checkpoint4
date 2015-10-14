package checkpoint.andela.com.productivitytracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class StartFragment  extends Fragment{
    private ImageButton startButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_fragment, container);
        startButton = (ImageButton)v.findViewById(R.id.playButton);
        return v;
    }

    public ImageButton getStartButton() {
        return startButton;
    }
}
