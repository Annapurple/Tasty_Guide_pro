package ru.samsung.tasty_guide_test.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ru.samsung.tasty_guide_test.R;
import ru.samsung.tasty_guide_test.utils.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {
    private EditText editEmail, editName, editPassword;
    private Button btnSignUp;
    private TextView tvSignIn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper(this);

        editEmail = findViewById(R.id.editTextText);
        editName = findViewById(R.id.editTextText2);
        editPassword = findViewById(R.id.editTextText3);
        btnSignUp = findViewById(R.id.button);
        tvSignIn = findViewById(R.id.textView3);

        btnSignUp.setOnClickListener(v -> registerUser());
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
    }

    private void registerUser() {
        String email = editEmail.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.addUser(email, name, password)) {
            Toast.makeText(this, "Регистрация успешна! Теперь войдите", Toast.LENGTH_LONG).show();
            finish(); // Закрываем окно регистрации
        } else {
            Toast.makeText(this, "Email уже существует", Toast.LENGTH_SHORT).show();
        }
    }
}