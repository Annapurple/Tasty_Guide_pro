package ru.samsung.tasty_guide_test.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Recipe> recipeList;
    private SharedPreferences prefs;
    private Button btnAddRecipe, btnLogout, btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("RecipeApp", MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnLogout = findViewById(R.id.btnLogout);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRecipes();

        // Проверяем авторизован ли пользователь
        updateAuthButtons();

        // Кнопка добавления рецепта (только для авторизованных)
        btnAddRecipe.setOnClickListener(v -> {
            if (isLoggedIn()) {
                startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
            } else {
                Toast.makeText(this, "Сначала войдите в аккаунт", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка выхода
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            updateAuthButtons();
            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
        });

        // Кнопка входа
        btnSignIn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        });

        // Кнопка регистрации
        btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        });
    }

    private boolean isLoggedIn() {
        return prefs.getInt("userId", -1) != -1;
    }

    private void updateAuthButtons() {
        if (isLoggedIn()) {
            // Пользователь авторизован
            btnSignIn.setVisibility(android.view.View.GONE);
            btnSignUp.setVisibility(android.view.View.GONE);
            btnLogout.setVisibility(android.view.View.VISIBLE);
            btnAddRecipe.setVisibility(android.view.View.VISIBLE);
        } else {
            // Пользователь не авторизован
            btnSignIn.setVisibility(android.view.View.VISIBLE);
            btnSignUp.setVisibility(android.view.View.VISIBLE);
            btnLogout.setVisibility(android.view.View.GONE);
            btnAddRecipe.setVisibility(android.view.View.GONE);
        }
    }

    private void loadRecipes() {
        recipeList = dbHelper.getAllRecipes();
        if (recipeList == null || recipeList.isEmpty()) {
            recipeList = new ArrayList<>();
        }
        adapter = new RecipeAdapter(recipeList, recipe -> {
            Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_title", recipe.getTitle());
            intent.putExtra("recipe_description", recipe.getDescription());
            intent.putExtra("recipe_ingredients", recipe.getIngredients());
            intent.putExtra("recipe_instructions", recipe.getInstructions());
            intent.putExtra("recipe_author", recipe.getUserName());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAuthButtons();
        loadRecipes();
    }
}
