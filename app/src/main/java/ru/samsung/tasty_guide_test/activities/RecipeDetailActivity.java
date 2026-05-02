package ru.samsung.tasty_guide_test.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ru.samsung.tasty_guide_test.R;

public class RecipeDetailActivity extends AppCompatActivity {
    private TextView titleText, authorText, descriptionText, ingredientsText, instructionsText;
    private ImageView recipeImage;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        titleText = findViewById(R.id.recipeTitle);
        authorText = findViewById(R.id.recipeAuthor);
        descriptionText = findViewById(R.id.recipeDescription);
        ingredientsText = findViewById(R.id.recipeIngredients);
        instructionsText = findViewById(R.id.recipeInstructions);
        recipeImage = findViewById(R.id.recipeImage);
        btnBack = findViewById(R.id.btnBack);

        String title = getIntent().getStringExtra("recipe_title");
        String author = getIntent().getStringExtra("recipe_author");
        String description = getIntent().getStringExtra("recipe_description");
        String ingredients = getIntent().getStringExtra("recipe_ingredients");
        String instructions = getIntent().getStringExtra("recipe_instructions");
        byte[] imageBytes = getIntent().getByteArrayExtra("recipe_image");

        titleText.setText(title);
        authorText.setText("Автор: " + author);
        descriptionText.setText(description);
        ingredientsText.setText(ingredients.replace(",", "\n• "));
        instructionsText.setText(instructions);

        // Отображение фото - если есть, показываем, иначе скрываем
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            recipeImage.setImageBitmap(bitmap);
            recipeImage.setVisibility(android.view.View.VISIBLE);
        } else {
            recipeImage.setVisibility(android.view.View.GONE);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}