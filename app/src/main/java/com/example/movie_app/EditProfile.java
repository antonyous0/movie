package com.example.movie_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_app.Activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText userNameEditText;
    private EditText phoneNumberEditText;
    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private Button updateProfileButton;
    private Button updatePasswordButton;
    private Button returnToMainButton;
    private TextView usernameDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userNameEditText = findViewById(R.id.user_name);
        phoneNumberEditText = findViewById(R.id.phone_number);
        currentPasswordEditText = findViewById(R.id.current_password);
        newPasswordEditText = findViewById(R.id.new_password);
        updateProfileButton = findViewById(R.id.btn_update_profile);
        updatePasswordButton = findViewById(R.id.btn_update_password);
        returnToMainButton = findViewById(R.id.btn_return_to_main);
        usernameDisplay = findViewById(R.id.username_display);

        // Load current user information
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String phoneNumber = documentSnapshot.getString("phone_number");
                            if (name != null) {
                                usernameDisplay.setText(name);
                            }
                            if (phoneNumber != null) {
                                phoneNumberEditText.setText(phoneNumber);
                            }
                        }
                    });
        }

        // Update profile button click listener
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        // Update password button click listener
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        // Return to main activity button click listener
        returnToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });
    }

    private void updateProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        String name = userNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("phone_number", phoneNumber);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    usernameDisplay.setText(name);
                    Toast.makeText(EditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditProfile.this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updatePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String password = currentPasswordEditText.getText().toString().trim();

        if (newPassword.isEmpty() || password.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfile.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfile.this, "Failed to update password. Please check your current password.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void returnToMain() {
        Intent intent = new Intent(EditProfile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
