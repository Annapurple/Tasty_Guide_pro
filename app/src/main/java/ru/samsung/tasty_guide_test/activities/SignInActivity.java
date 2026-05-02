package ru.samsung.tasty_guide_test.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ru.samsung.tasty_guide_test.R;
import ru.samsung.tasty_guide_test.models.User;
import ru.samsung.tasty_guide_test.utils.DatabaseHelper;

public class SignInActivity extends AppCompatActivity {
    private EditText editEmail, editPassword;
    private Button btnSignIn;
    private TextView tvSignUp;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("RecipeApp", MODE_PRIVATE);

        editEmail = findViewById(R.id.editTextText2);
        editPassword = findViewById(R.id.editTextText3);
        btnSignIn = findViewById(R.id.button);
        tvSignUp = findViewById(R.id.textView3);

        btnSignIn.setOnClickListener(v -> loginUser());
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = dbHelper.loginUser(email, password);
        if (user != null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("userId", user.getId());
            editor.putString("userName", user.getName());
            editor.apply();

            Toast.makeText(this, "Добро пожаловать, " + user.getName() + "!", Toast.LENGTH_SHORT).show();

            // Закрываем окно входа и возвращаемся на главную
            finish();
        } else {
            Toast.makeText(this, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
        }
    }
}