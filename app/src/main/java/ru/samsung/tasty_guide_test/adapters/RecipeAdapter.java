package ru.samsung.tasty_guide_test.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ru.samsung.tasty_guide_test.R;
import ru.samsung.tasty_guide_test.models.Recipe;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.titleText.setText(recipe.getTitle());
        holder.descriptionText.setText(recipe.getDescription());
        holder.authorText.setText("Автор: " + recipe.getUserName());

        // Отображение фото - если есть фото, показываем, иначе скрываем
        if (recipe.getImage() != null && recipe.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length);
            holder.recipeImage.setImageBitmap(bitmap);
            holder.recipeImage.setVisibility(View.VISIBLE);
            // Если есть фото, добавляем отступ для текста
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.textContainer.getLayoutParams();
            params.setMarginStart(12);
            holder.textContainer.setLayoutParams(params);
        } else {
            holder.recipeImage.setVisibility(View.GONE);
            // Если нет фото, убираем отступ
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.textContainer.getLayoutParams();
            params.setMarginStart(0);
            holder.textContainer.setLayoutParams(params);
        }

        holder.cardView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descriptionText, authorText;
        CardView cardView;
        ImageView recipeImage;
        LinearLayout textContainer;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            titleText = itemView.findViewById(R.id.recipeTitle);
            descriptionText = itemView.findViewById(R.id.recipeDescription);
            authorText = itemView.findViewById(R.id.recipeAuthor);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            textContainer = itemView.findViewById(R.id.textContainer);
        }
    }
}