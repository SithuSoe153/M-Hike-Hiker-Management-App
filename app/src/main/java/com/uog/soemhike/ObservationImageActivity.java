package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.uog.soemhike.R;
import com.uog.soemhike.database.Observation;

public class ObservationImageActivity extends AppCompatActivity {

    PhotoView iv_OimageFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_image);

        iv_OimageFull = findViewById(R.id.iv_OimageFull);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String avatarFilePath = bundle.getString(Observation.AVATAR_FILE_PATH);
            // Handle the image file path, for example, set it to an ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(avatarFilePath);
            if (bitmap != null) {
//                avatarImageView.setImageBitmap(bitmap);
                iv_OimageFull.setImageBitmap(bitmap);

            } else {
                // Handle the case where the image couldn't be loaded
                iv_OimageFull.setImageResource(R.drawable.default_avatar); // Set a default image
            }
        }


    }
}