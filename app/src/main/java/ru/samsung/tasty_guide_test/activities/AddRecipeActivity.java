package ru.samsung.tasty_guide_test.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ru.samsung.tasty_guide_test.R;
import ru.samsung.tasty_guide_test.utils.DatabaseHelper;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddRecipeActivity extends AppCompatActivity {
    private EditText editTitle, editDescription, editIngredients, editInstructions;
    private CheckBox checkAnonymous;
    private Button btnSave, btnSelectImage;
    private ImageButton btnBack;
    private ImageView imagePreview;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;
    private static final int PICK_IMAGE = 1;
    private byte[] imageBytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("RecipeApp", MODE_PRIVATE);

        initViews();
        setupListeners();
    }

    private void initViews() {
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editIngredients = findViewById(R.id.editIngredients);
        editInstructions = findViewById(R.id.editInstructions);
        checkAnonymous = findViewById(R.id.checkAnonymous);
        btnSave = findViewById(R.id.btnSave);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnBack = findViewById(R.id.btnBack);
        imagePreview = findViewById(R.id.imagePreview);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        btnSave.setOnClickListener(v -> saveRecipe());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Сжимаем изображение
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                imagePreview.setImageBitmap(scaledBitmap);

                // Конвертируем в байты
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                imageBytes = stream.toByteArray();

            } catch (Exception e) {
                Toast.makeText(this, "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveRecipe() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String ingredients = editIngredients.getText().toString().trim();
        String instructions = editInstructions.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(instructions)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId;
        String authorName;

        if (checkAnonymous.isChecked()) {
            userId = 0;
            authorName = "Аноним";
        } else {
            userId = prefs.getInt("userId", -1);
            authorName = prefs.getString("userName", "Пользователь");
            if (userId == -1) {
                Toast.makeText(this, "Вы не авторизованы! Войдите в аккаунт или добавьте анонимно", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (dbHelper.addRecipe(title, description, ingredients, instructions, userId, authorName, imageBytes)) {
            Toast.makeText(this, "Рецепт добавлен!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка при добавлении рецепта", Toast.LENGTH_SHORT).show();
        }
    }
}