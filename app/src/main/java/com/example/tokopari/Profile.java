package com.example.tokopari;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentValues;
import android.app.AlertDialog;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Uri imageUri;
    private ImageButton photoProfile;

    private EditText etName, etUsername, etEmail;
    private Button buttonLogout, buttonSave;
    private SharedPreferences sharedPreferences;
    private TextInputLayout nameInputLayout, usernameInputLayout, emailInputLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi komponen
        etName = view.findViewById(R.id.etName);
        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        buttonLogout = view.findViewById(R.id.button);
        buttonSave = view.findViewById(R.id.buttonSave);
        nameInputLayout = view.findViewById(R.id.nameInputLayout);
        usernameInputLayout = view.findViewById(R.id.usernameInputLayout);
        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        photoProfile = view.findViewById(R.id.photoProfile);

        loadSavedData();
        setInitialHints();

        // Menambahkan listener untuk gambar profil
        photoProfile.setOnClickListener(v -> showImagePickerDialog());

        // Focus listeners untuk mengganti hint
        etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                nameInputLayout.setHint("Name");
            } else {
                nameInputLayout.setHint("Enter Name");
            }
        });

        etUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                usernameInputLayout.setHint("Username");
            } else {
                usernameInputLayout.setHint("Enter Username");
            }
        });

        // Handle "Done" key press untuk menyembunyikan keyboard
        etName.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                clearFocusFromFields();  // Remove focus from the fields
                hideKeyboard(v);  // Hide the keyboard
                return true;
            }
            return false;
        });

        etUsername.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                clearFocusFromFields();  // Remove focus from the fields
                hideKeyboard(v);  // Hide the keyboard
                return true;
            }
            return false;
        });

        // Set action untuk buttonSave dan buttonLogout
        buttonSave.setOnClickListener(v -> saveData());
        buttonLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void showImagePickerDialog() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a Photo");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openCamera();
            } else if (which == 1) {
                openGallery();
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Profile picture taken from camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                imageUri = data.getData();
            } else if (requestCode == CAMERA_REQUEST_CODE && imageUri != null) {
                // Handle camera image
            }

            if (imageUri != null) {
                // Update ImageView with selected photo
                photoProfile.setImageURI(imageUri);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("profileImageUri", imageUri.toString());
                editor.apply();
            }
        }
    }

    private void saveData() {
        String name = etName.getText().toString();
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString(); // Ambil email yang dimasukkan oleh pengguna

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());
        sharedPrefManager.setUserName(name);       // Simpan nama
        sharedPrefManager.setUserUsername(username); // Simpan username
        sharedPrefManager.setUserEmail(email);    // Simpan email yang baru
    }

    private void loadSavedData() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());

        // Ambil data yang disimpan dari SharedPreferences
        String savedName = sharedPrefManager.getUserName();
        String savedUsername = sharedPrefManager.getUserUsername();
        String savedEmail = sharedPrefManager.getUserEmail(); // Ambil email yang disimpan

        // Set data yang diambil ke EditText
        etName.setText(savedName);
        etUsername.setText(savedUsername);
        etEmail.setText(savedEmail);  // Menampilkan email di EditText

        String savedImageUri = sharedPreferences.getString("profileImageUri", null);
        if (savedImageUri != null) {
            imageUri = Uri.parse(savedImageUri);
            photoProfile.setImageURI(imageUri);
        }
    }

    private void setInitialHints() {
        if (!etName.getText().toString().isEmpty()) {
            nameInputLayout.setHint("Name");
        }
        if (!etUsername.getText().toString().isEmpty()) {
            usernameInputLayout.setHint("Username");
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        // Clear user login status and other data from SharedPreferences
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());
        sharedPrefManager.logout();  // Clears the login status and user info

        // Ensure the login status is properly reset
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // This clears all data from SharedPreferences
        editor.apply();

        // Redirect to LoginAndRegisterActivity and clear the back stack
        Intent intent = new Intent(getActivity(), LoginAndRegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear the activity stack
        startActivity(intent);

        // Close the current activity (Profile fragment's parent activity) and prevent going back
        getActivity().finishAffinity();  // Close all activities in the current task
    }

    private void clearFocusFromFields() {
        // Clear focus from both fields
        etName.clearFocus();
        etUsername.clearFocus();

        // Set focusable to false temporarily to remove focus indicators
        etName.setFocusable(false);
        etUsername.setFocusable(false);

        // Request focus on a non-editable view to ensure focus is cleared
        View nonEditableView = getView().findViewById(R.id.buttonSave);  // Use any view that is not focused
        nonEditableView.requestFocus();  // Request focus to remove focus from EditTexts

        // Set focusable back to true after the interaction is done
        etName.setFocusable(true);
        etUsername.setFocusable(true);
    }

    // Hide the keyboard when called
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
