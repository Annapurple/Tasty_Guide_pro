package ru.samsung.tasty_guide_test.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private List<Recipe> originalRecipeList;
    private SharedPreferences prefs;
    private Button btnAddRecipe, btnLogout, btnSignIn, btnSignUp, btnSearchByIngredients;
    private SearchView searchView;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("RecipeApp", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);

        initViews();
        setupListeners();
        loadRecipes();
        updateAuthButtons();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnLogout = findViewById(R.id.btnLogout);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSearchByIngredients = findViewById(R.id.btnSearchByIngredients);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        originalRecipeList = new ArrayList<>();

        searchView.setQueryHint("Поиск рецептов...");
    }

    private void setupListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRecipesByTitle(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecipesByTitle(newText);
                return true;
            }
        });

        btnSearchByIngredients.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchByIngredientsActivity.class));
        });

        btnAddRecipe.setOnClickListener(v -> {
            if (isLoggedIn()) {
                startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
            } else {
                Toast.makeText(this, "Сначала войдите в аккаунт", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            currentUserId = -1;
            updateAuthButtons();
            loadRecipes();
            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
        });

        btnSignIn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        });

        btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        });
    }

    private boolean isLoggedIn() {
        return currentUserId != -1;
    }

    private void updateAuthButtons() {
        if (isLoggedIn()) {
            btnSignIn.setVisibility(android.view.View.GONE);
            btnSignUp.setVisibility(android.view.View.GONE);
            btnLogout.setVisibility(android.view.View.VISIBLE);
            btnAddRecipe.setVisibility(android.view.View.VISIBLE);
        } else {
            btnSignIn.setVisibility(android.view.View.VISIBLE);
            btnSignUp.setVisibility(android.view.View.VISIBLE);
            btnLogout.setVisibility(android.view.View.GONE);
            btnAddRecipe.setVisibility(android.view.View.GONE);
        }
    }

    private void loadRecipes() {
        originalRecipeList = dbHelper.getAllRecipes();
        recipeList.clear();
        recipeList.addAll(originalRecipeList);

        adapter = new RecipeAdapter(recipeList,
                recipe -> {
                    Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("recipe_title", recipe.getTitle());
                    intent.putExtra("recipe_description", recipe.getDescription());
                    intent.putExtra("recipe_ingredients", recipe.getIngredients());
                    intent.putExtra("recipe_instructions", recipe.getInstructions());
                    intent.putExtra("recipe_author", recipe.getUserName());
                    intent.putExtra("recipe_image", recipe.getImage());
                    startActivity(intent);
                },
                position -> showDeleteConfirmationDialog(position),
                currentUserId
        );
        recyclerView.setAdapter(adapter);
    }

    private void showDeleteConfirmationDialog(int position) {
        Recipe recipe = recipeList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Удаление рецепта")
                .setMessage("Вы уверены, что хотите удалить рецепт \"" + recipe.getTitle() + "\"?")
                .setPositiveButton("Удалить", (dialog, which) -> deleteRecipe(position))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void deleteRecipe(int position) {
        Recipe recipe = recipeList.get(position);

        if (dbHelper.deleteRecipe(recipe.getId(), currentUserId)) {
            recipeList.remove(position);
            originalRecipeList.remove(recipe);
            adapter.notifyItemRemoved(position);
            Toast.makeText(this, "Рецепт удален", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при удалении", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterRecipesByTitle(String query) {
        if (query == null || query.isEmpty()) {
            recipeList.clear();
            recipeList.addAll(originalRecipeList);
        } else {
            List<Recipe> filtered = new ArrayList<>();
            for (Recipe recipe : originalRecipeList) {
                if (recipe.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(recipe);
                }
            }
            recipeList.clear();
            recipeList.addAll(filtered);

            if (filtered.isEmpty()) {
                Toast.makeText(this, "Рецепты не найдены", Toast.LENGTH_SHORT).show();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUserId = prefs.getInt("userId", -1);
        updateAuthButtons();
        loadRecipes();
        searchView.setQuery("", false);
        searchView.clearFocus();
    }
}