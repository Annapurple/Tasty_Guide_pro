package ru.samsung.tasty_guide_test.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.samsung.tasty_guide_test.R;
import ru.samsung.tasty_guide_test.adapters.RecipeAdapter;
import ru.samsung.tasty_guide_test.models.Recipe;
import ru.samsung.tasty_guide_test.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class SearchByIngredientsActivity extends AppCompatActivity {
    private LinearLayout ingredientsContainer;
    private Button btnSearch;
    private DatabaseHelper dbHelper;
    private String[] allIngredients = {"курица", "паста", "бекон", "яйца", "сыр", "салат", "помидоры", "огурцы", "оливки", "хлеб"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingredients);

        dbHelper = new DatabaseHelper(this);
        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        btnSearch = findViewById(R.id.btnSearch);

        // Создаем чекбоксы для каждого продукта
        for (String ingredient : allIngredients) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(ingredient.substring(0, 1).toUpperCase() + ingredient.substring(1));
            checkBox.setPadding(16, 8, 16, 8);
            checkBox.setTextSize(16);
            ingredientsContainer.addView(checkBox);
        }

        btnSearch.setOnClickListener(v -> searchRecipes());
    }

    private void searchRecipes() {
        List<String> selectedIngredients = new ArrayList<>();

        for (int i = 0; i < ingredientsContainer.getChildCount(); i++) {
            if (ingredientsContainer.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) ingredientsContainer.getChildAt(i);
                if (checkBox.isChecked()) {
                    selectedIngredients.add(checkBox.getText().toString().toLowerCase());
                }
            }
        }

        if (selectedIngredients.isEmpty()) {
            Toast.makeText(this, "Выберите хотя бы один продукт", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Recipe> foundRecipes = dbHelper.searchRecipesByIngredients(selectedIngredients);

        if (foundRecipes.isEmpty()) {
            Toast.makeText(this, "Рецепты не найдены", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("results", new ArrayList<>(foundRecipes));
            startActivity(intent);
        }
    }
}