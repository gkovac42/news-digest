package com.example.goran.mvvm_demo.util;

public abstract class Category {

    public static final String EVERYTHING = "Everything";
    public static final String BUSINESS = "Business";
    public static final String ENTERTAINMENT = "Entertainment";
    public static final String GENERAL = "General";
    public static final String HEALTH = "Health";
    public static final String SCIENCE = "Science";
    public static final String SPORTS = "Sports";
    public static final String TECHNOLOGY = "Technology";

    public static String[] getCategories() {
        return new String[]{EVERYTHING, BUSINESS, ENTERTAINMENT, GENERAL,
                HEALTH, SCIENCE, SPORTS, TECHNOLOGY};
    }
}
