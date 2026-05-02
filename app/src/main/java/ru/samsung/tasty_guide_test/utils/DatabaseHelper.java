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
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_PASSWORD = "password";

    // Recipes table
    private static final String TABLE_RECIPES = "recipes";
    private static final String COL_RECIPE_ID = "id";
    private static final String COL_RECIPE_TITLE = "title";
    private static final String COL_RECIPE_DESCRIPTION = "description";
    private static final String COL_RECIPE_INGREDIENTS = "ingredients";
    private static final String COL_RECIPE_INSTRUCTIONS = "instructions";
    private static final String COL_RECIPE_USER_ID = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_TITLE + " TEXT, " +
                COL_RECIPE_DESCRIPTION + " TEXT, " +
                COL_RECIPE_INGREDIENTS + " TEXT, " +
                COL_RECIPE_INSTRUCTIONS + " TEXT, " +
                COL_RECIPE_USER_ID + " INTEGER)";
        db.execSQL(createRecipesTable);

        insertSampleRecipes(db);
    }

    private void insertSampleRecipes(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_TITLE, "Паста Карбонара");
        values.put(COL_RECIPE_DESCRIPTION, "Классическая итальянская паста с яйцом и беконом");
        values.put(COL_RECIPE_INGREDIENTS, "паста,бекон,яйца,пармезан,черный перец");
        values.put(COL_RECIPE_INSTRUCTIONS, "1. Сварите пасту\n2. Обжарьте бекон\n3. Смешайте яйца с сыром\n4. Соедините все ингредиенты");
        values.put(COL_RECIPE_USER_ID, 0);
        db.insert(TABLE_RECIPES, null, values);

        values = new ContentValues();
        values.put(COL_RECIPE_TITLE, "Цезарь с курицей");
        values.put(COL_RECIPE_DESCRIPTION, "Свежий салат с хрустящей курицей");
        values.put(COL_RECIPE_INGREDIENTS, "курица,салат романо,сухарики,пармезан,соус цезарь");
        values.put(COL_RECIPE_INSTRUCTIONS, "1. Обжарьте курицу\n2. Нарежьте салат\n3. Смешайте все ингредиенты с соусом");
        values.put(COL_RECIPE_USER_ID, 0);
        db.insert(TABLE_RECIPES, null, values);

        values = new ContentValues();
        values.put(COL_RECIPE_TITLE, "Греческий салат");
        values.put(COL_RECIPE_DESCRIPTION, "Легкий и полезный салат");
        values.put(COL_RECIPE_INGREDIENTS, "огурцы,помидоры,фета,оливки,лук,оливковое масло");
        values.put(COL_RECIPE_INSTRUCTIONS, "1. Нарежьте овощи\n2. Добавьте фету и оливки\n3. Заправьте маслом");
        values.put(COL_RECIPE_USER_ID, 0);
        db.insert(TABLE_RECIPES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    public boolean addUser(String email, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD))
            );
            cursor.close();
            return user;
        }
        return null;
    }

    public boolean addRecipe(String title, String description, String ingredients, String instructions, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_TITLE, title);
        values.put(COL_RECIPE_DESCRIPTION, description);
        values.put(COL_RECIPE_INGREDIENTS, ingredients);
        values.put(COL_RECIPE_INSTRUCTIONS, instructions);
        values.put(COL_RECIPE_USER_ID, userId);
        long result = db.insert(TABLE_RECIPES, null, values);
        return result != -1;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r." + COL_RECIPE_ID + ", r." + COL_RECIPE_TITLE + ", r." + COL_RECIPE_DESCRIPTION +
                ", r." + COL_RECIPE_INGREDIENTS + ", r." + COL_RECIPE_INSTRUCTIONS + ", r." + COL_RECIPE_USER_ID +
                ", u." + COL_USER_NAME + " FROM " + TABLE_RECIPES + " r " +
                "LEFT JOIN " + TABLE_USERS + " u ON r." + COL_RECIPE_USER_ID + " = u." + COL_USER_ID +
                " ORDER BY r." + COL_RECIPE_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6) != null ? cursor.getString(6) : "Админ"
                );
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    public List<Recipe> searchRecipesByIngredients(List<String> ingredients) {
        List<Recipe> allRecipes = getAllRecipes();
        List<Recipe> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            String recipeIngredients = recipe.getIngredients().toLowerCase();
            int matchCount = 0;
            for (String ingredient : ingredients) {
                if (recipeIngredients.contains(ingredient.toLowerCase())) {
                    matchCount++;
                }
            }
            if (matchCount > 0) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }
}