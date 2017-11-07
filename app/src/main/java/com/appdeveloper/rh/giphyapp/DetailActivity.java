package com.appdeveloper.rh.giphyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView gifImageView;
    TextView titleTextView;
    ImageButton shareBtn;
    ImageButton favBtn;

    DBFavGif db;
    GifObject obj;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();
        userName = prefs.getString("user", "");

        db = new DBFavGif(this);
        obj = getIntent().getParcelableExtra("theObject");

        gifImageView = (ImageView) findViewById(R.id.gifImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        shareBtn = (ImageButton) findViewById(R.id.shareBtn);
        favBtn = (ImageButton) findViewById(R.id.favBtn);

        Glide.with(this).load(obj.imageUrl).into(gifImageView);
        titleTextView.setText(obj.title);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                Cursor c = db.getItem(obj.imageUrl, userName);
                if(c.moveToFirst()){
                    db.deleteItem(obj.imageUrl, userName);
                    Toast.makeText(getBaseContext(), "Item removed from favourites", Toast.LENGTH_SHORT).show();
                }else {
                    db.insertItem(obj.title, obj.imageUrl, userName);
                    Toast.makeText(getBaseContext(), "Item added to favourites", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareInfo = "Here is a funny GIF " + obj.imageUrl;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Funny Gif");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareInfo);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }
}
