package ru.samsung.tasty_guide_test.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ru.samsung.tasty_guide_test.models.Recipe;
import ru.samsung.tasty_guide_test.models.User;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipe_app.db";
    private static final int DATABASE_VERSION = 2; // Обновляем версию

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Таблица пользователей
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +
                "name TEXT, " +
                "password TEXT)";
        db.execSQL(createUsersTable);

        // Таблица рецептов с фото
        String createRecipesTable = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "ingredients TEXT, " +
                "instructions TEXT, " +
                "user_id INTEGER, " +
                "author_name TEXT, " +
                "image BLOB)";
        db.execSQL(createRecipesTable);

        // Добавляем примеры рецептов
        addSampleRecipes(db);
    }

    private void addSampleRecipes(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("title", "Паста Карбонара");
        values.put("description", "Классическая итальянская паста");
        values.put("ingredients", "паста, бекон, яйца, сыр пармезан, черный перец");
        values.put("instructions", "1. Сварите пасту\n2. Обжарьте бекон\n3. Смешайте яйца с сыром\n4. Соедините все");
        values.put("user_id", 0);
        values.put("author_name", "Аноним");
        db.insert("recipes", null, values);

        values = new ContentValues();
        values.put("title", "Цезарь с курицей");
        values.put("description", "Свежий салат с курицей");
        values.put("ingredients", "курица, салат, сухарики, сыр пармезан, соус цезарь");
        values.put("instructions", "1. Обжарьте курицу\n2. Нарежьте салат\n3. Смешайте все с соусом");
        values.put("user_id", 0);
        values.put("author_name", "Аноним");
        db.insert("recipes", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }

    public boolean addUser(String email, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("name", name);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, "email=? AND password=?",
                new String[]{email, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password"))
            );
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public boolean addRecipe(String title, String description, String ingredients, String instructions, int userId, String authorName, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("ingredients", ingredients);
        values.put("instructions", instructions);
        values.put("user_id", userId);
        values.put("author_name", authorName);
        if (image != null) {
            values.put("image", image);
        }
        long result = db.insert("recipes", null, values);
        db.close();
        return result != -1;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM recipes ORDER BY id DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String authorName = cursor.getString(cursor.getColumnIndexOrThrow("author_name"));
                if (authorName == null || authorName.isEmpty()) {
                    authorName = "Аноним";
                }

                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ingredients")),
                        cursor.getString(cursor.getColumnIndexOrThrow("instructions")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        authorName,
                        cursor.getBlob(cursor.getColumnIndexOrThrow("image"))
                );
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipes;
    }

    public List<Recipe> searchRecipesByIngredients(List<String> ingredients) {
        List<Recipe> allRecipes = getAllRecipes();
        List<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            String recipeIngredients = recipe.getIngredients().toLowerCase();
            for (String ingredient : ingredients) {
                if (recipeIngredients.contains(ingredient.toLowerCase())) {
                    filtered.add(recipe);
                    break; // Добавляем рецепт только один раз
                }
            }
        }

        return filtered;
    }
    public boolean deleteRecipe(int recipeId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Удаляем только если рецепт принадлежит пользователю
        int result = db.delete("recipes", "id=? AND user_id=?",
                new String[]{String.valueOf(recipeId), String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    public boolean isRecipeOwner(int recipeId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("recipes", null, "id=? AND user_id=?",
                new String[]{String.valueOf(recipeId), String.valueOf(userId)},
                null, null, null);
        boolean isOwner = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        db.close();
        return isOwner;
    }
}