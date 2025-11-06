package com.annchao.springboot_mall.constant;

public class MyTest {


    public static void main(String[] args) {
        ProductCategory category = ProductCategory.BOOK;
        String  categoryString = category.name();
        System.out.println(categoryString);

        String anotherString = "E_BOOKS";
        ProductCategory anotherCategory = ProductCategory.valueOf(anotherString);
        System.out.println(anotherCategory);
    }
}
