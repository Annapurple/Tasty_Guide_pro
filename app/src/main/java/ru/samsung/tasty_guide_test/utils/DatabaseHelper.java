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
    private static final int DATABASE_VERSION = 2;

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

        values = new ContentValues();
        values.put("title", "Салат Оливье");
        values.put("description", "Классический новогодний салат");
        values.put("ingredients", "картофель,морковь,яйца,колбаса,огурцы,горошек,майонез,соль,перец,зелень");
        values.put("instructions", "1. Отварите картофель, морковь и яйца до готовности\n2. Нарежьте вареные овощи, яйца и колбасу мелкими кубиками\n3. Добавьте консервированный горошек\n4. Посолите, поперчите по вкусу\n5. Заправьте майонезом и тщательно перемешайте\n6. Украсьте свежей зеленью перед подачей");
        values.put("user_id", 0);
        values.put("author_name", "Шеф-повар");
        db.insert("recipes", null, values);

        values.put("title", "Паста Карбонара");
        values.put("description", "Классическая итальянская паста с беконом и яйцом в сливочном соусе");
        values.put("ingredients", "паста,бекон,яйца,сливки,сыр,чеснок,перец черный,соль");
        values.put("instructions", "1. Отварите пасту в подсоленной воде\n2. Нарежьте бекон, обжарьте его на сухой сковороде до хруста\n3. В миске смешайте яйца, сливки, тертый сыр, измельченный чеснок, соль и перец\n4. Слейте воду с пасты, оставив немного воды для соуса\n5. Смешайте горячую пасту с беконом, залейте яично-сливочной смесью и быстро перемешайте\n6. Подавайте сразу, посыпав сыром и черным перцем");
        values.put("user_id", 0);
        values.put("author_name", "Шеф-повар");
        db.insert("recipes", null, values);

        values.put("title", "Салат Цезарь с курицей");
        values.put("description", "Знаменитый салат с хрустящей курицей, сухариками и соусом Цезарь");
        values.put("ingredients", "курица,салат,хлеб,сыр,яйца,помидоры,чеснок,масло,лимон,соль,перец");
        values.put("instructions", "1. Куриное филе посолите, поперчите и обжарьте на сковороде до золотистой корочки, затем нарежьте полосками\n2. Для сухариков: нарежьте хлеб кубиками, обжарьте с измельченным чесноком на оливковом масле\n3. Для соуса: смешайте желтки, горчицу, сок лимона, измельченный чеснок, тертый сыр и оливковое масло\n4. Листья салата порвите руками на крупные куски\n5. Смешайте салат с курицей, сухариками и соусом\n6. Посыпьте тертым пармезаном и подавайте сразу");
        values.put("user_id", 0);
        values.put("author_name", "Шеф-повар");
        db.insert("recipes", null, values);

        values.put("title", "Фруктовый салат с медовой заправкой");
        values.put("description", "Легкий и яркий салат из свежих фруктов с ароматной медово-цитрусовой заправкой. Готовится за 15 минут!");
        values.put("ingredients", "клубника,банан,киви,йогурт,мед,лимон,орехи");
        values.put("instructions", "1. Клубнику вымойте и нарежьте четвертинками, банан и киви очистите и нарежьте кубиками\n" +
                "2. Все фрукты сложите в глубокую миску\n" +
                "3. Для заправки смешайте йогурт, мед и свежевыжатый сок лимона. Тщательно перемешайте до однородности\n" +
                "4. Залейте фрукты заправкой и аккуратно перемешайте\n" +
                "5. Грецкие орехи слегка обжарьте на сухой сковороде (2 минуты) и измельчите\n" +
                "6. Посыпьте салат орехами\n" +
                "7. Подавайте сразу после приготовления, пока фрукты не пустили сок");
        values.put("user_id", 0);
        values.put("author_name", "Шеф-повар");
        db.insert("recipes", null, values);


        values.put("title", "Йогуртово-клубничный десерт в шоколаде");
        values.put("description", "Нежный и полезный десерт из йогурта с клубникой в шоколадной глазури");
        values.put("ingredients", "йогурт,клубника,шоколад");
        values.put("instructions", "1. Клубнику вымойте и нарежьте мелкими кусочками\n2. Смешайте нарезанную клубнику с йогуртом в однородную массу\n3. Выложите смесь в формочки для льда или на противень, застеленный пергаментом, формируя небольшие кружочки\n4. Поставьте в морозилку на 3-4 часа до полного застывания\n5. Растопите шоколад на водяной бане или в микроволновой волне\n6. Достаньте замороженные десерты, обмакните каждый в растопленный шоколад\n7. Снова поставьте в морозилку на 30 минут до застывания шоколада\n8. Храните в морозилке, подавайте охлажденными");
        values.put("user_id", 0);
        values.put("author_name", "Шеф-повар");
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

    public List<Recipe> searchRecipesByIngredients(List<String> userIngredients) {
        List<Recipe> allRecipes = getAllRecipes();
        List<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            String[] recipeIngredientsArray = recipe.getIngredients().toLowerCase().split(",");
            List<String> recipeIngredientsList = new ArrayList<>();
            for (String ing : recipeIngredientsArray) {
                recipeIngredientsList.add(ing.trim());
            }

            int matchedCount = 0;
            for (String userIng : userIngredients) {
                if (recipeIngredientsList.contains(userIng.toLowerCase().trim())) {
                    matchedCount++;
                }
            }

            double requiredPercent = 0.5;
            boolean hasEnough = matchedCount >= recipeIngredientsList.size() * requiredPercent;

            if (hasEnough) {
                filtered.add(recipe);
            }
        }
        return filtered;
    }
    public boolean deleteRecipe(int recipeId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
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