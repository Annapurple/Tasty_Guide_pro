package ru.samsung.tasty_guide_test.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ru.samsung.tasty_guide_test.R;
import ru.samsung.tasty_guide_test.utils.DatabaseHelper;

public class AddRecipeActivity extends AppCompatActivity {
    private EditText editTitle, editDescription, editIngredients, editInstructions;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("RecipeApp", MODE_PRIVATE);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editIngredients = findViewById(R.id.editIngredients);
        editInstructions = findViewById(R.id.editInstructions);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveRecipe());
    }

    private void saveRecipe() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String ingredients = editIngredients.getText().toString().trim();
        String instructions = editInstructions.getText().toString().trim();
        int userId = prefs.getInt("userId", -1);

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(instructions)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.addRecipe(title, description, ingredients, instructions, userId)) {
            Toast.makeText(this, "Рецепт добавлен!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка при добавлении рецепта", Toast.LENGTH_SHORT).show();
        }
    }
}