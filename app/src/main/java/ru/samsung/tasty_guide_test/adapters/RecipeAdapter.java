package ru.samsung.tasty_guide_test.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    private OnRecipeDeleteListener deleteListener;
    private int currentUserId;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public interface OnRecipeDeleteListener {
        void onRecipeDelete(int position);
    }

    // КОНСТРУКТОР С 4 ПАРАМЕТРАМИ
    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener, OnRecipeDeleteListener deleteListener, int currentUserId) {
        this.recipes = recipes;
        this.listener = listener;
        this.deleteListener = deleteListener;
        this.currentUserId = currentUserId;
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

        // Отображение фото
        if (recipe.getImage() != null && recipe.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length);
            holder.recipeImage.setImageBitmap(bitmap);
            holder.recipeImage.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.textContainer.getLayoutParams();
            params.setMarginStart(12);
            holder.textContainer.setLayoutParams(params);
        } else {
            holder.recipeImage.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.textContainer.getLayoutParams();
            params.setMarginStart(0);
            holder.textContainer.setLayoutParams(params);
        }

        // ПОКАЗЫВАЕМ КНОПКУ УДАЛЕНИЯ ТОЛЬКО ДЛЯ СВОИХ РЕЦЕПТОВ
        if (currentUserId != -1 && recipe.getUserId() == currentUserId && recipe.getUserId() != 0) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(v -> listener.onRecipeClick(recipe));

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onRecipeDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateList(List<Recipe> newList) {
        this.recipes = newList;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descriptionText, authorText;
        CardView cardView;
        ImageView recipeImage;
        ImageButton btnDelete;
        LinearLayout textContainer;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            titleText = itemView.findViewById(R.id.recipeTitle);
            descriptionText = itemView.findViewById(R.id.recipeDescription);
            authorText = itemView.findViewById(R.id.recipeAuthor);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            textContainer = itemView.findViewById(R.id.textContainer);
        }
    }
}