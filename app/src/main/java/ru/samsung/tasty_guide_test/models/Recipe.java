package ru.samsung.tasty_guide_test.models;

public class Recipe {
    private int id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private int userId;
    private String userName;

    public Recipe(int id, String title, String description, String ingredients, String instructions, int userId, String userName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.userId = userId;
        this.userName = userName;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
}