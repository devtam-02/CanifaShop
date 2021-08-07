package com.example.canifa_shop.Category.Object;

public class Category{
    private int idCategory;
    private String nameCategory;
    private long amountCategory;
    private String describe;

    public Category(int idCategory, String nameCategory, long amountCategory, String describe) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.amountCategory = amountCategory;
        this.describe = describe;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public long getAmountCategory() {
        return amountCategory;
    }

    public void setAmountCategory(long amountCategory) {
        this.amountCategory = amountCategory;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return nameCategory;
    }
}
