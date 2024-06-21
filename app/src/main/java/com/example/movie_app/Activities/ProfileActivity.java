package com.example.movie_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.movie_app.LoginActivity;
import com.example.movie_app.R;
import com.example.movie_app.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
/*
        button = findViewById(R.id.logout);
*/


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



/*
        setupBottomNavigation();
*/
    }

    /*    private void setupBottomNavigation() {
        LinearLayout profile = findViewById(R.id.profile);
        profile.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MainActivity.class)));
    }*/

}
