package ru.samsung.tasty_guide_test.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.samsung.tasty_guide_test.R;
import ru.samsung.tasty_guide_test.adapters.RecipeAdapter;
import ru.samsung.tasty_guide_test.models.Recipe;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView titleText;
    private ImageButton btnBack;
    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.recyclerView);
        titleText = findViewById(R.id.titleText);
        btnBack = findViewById(R.id.btnBack);

        // Кнопка назад на главную
        btnBack.setOnClickListener(v -> {
            finish(); // Возврат на главную
        });

        // Получаем результаты
        recipeList = (List<Recipe>) getIntent().getSerializableExtra("results");

        if (recipeList == null) {
            recipeList = new ArrayList<>();
        }

        titleText.setText("Найдено рецептов: " + recipeList.size());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecipeAdapter adapter = new RecipeAdapter(recipeList, recipe -> {
            Intent intent = new Intent(SearchResultsActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_title", recipe.getTitle());
            intent.putExtra("recipe_description", recipe.getDescription());
            intent.putExtra("recipe_ingredients", recipe.getIngredients());
            intent.putExtra("recipe_instructions", recipe.getInstructions());
            intent.putExtra("recipe_author", recipe.getUserName());
            intent.putExtra("recipe_image", recipe.getImage());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}