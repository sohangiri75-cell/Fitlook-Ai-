package com.example.data.model

enum class PersonCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val sampleImageResName: String
) {
    MAN("man", "Man", "Men's Fashion & Suits", "👨", "img_demo_person_man"),
    WOMAN("woman", "Woman", "Women's Ethnic & Western", "👩", "img_demo_person_woman"),
    BOY("boy", "Boy", "Boys' Trendy Outfits", "👦", "img_demo_person_man"),
    GIRL("girl", "Girl", "Girls' Festive & Casual", "👧", "img_demo_person_woman"),
    CHILD("child", "Child", "Kids' Cute Styles", "🧒", "img_demo_person_woman")
}

object CategoryCatalog {
    val standardClothingCategories = listOf(
        "Pant + Shirt",
        "T-Shirt + Jeans",
        "Formal Suit",
        "Jacket",
        "Traditional Clothes",
        "Kurta Pajama",
        "Dress",
        "Saree",
        "Custom Outfit"
    )

    val menClothing = listOf(
        "Pant + Shirt", "T-Shirt + Jeans", "Formal Suit", "Jacket", "Traditional Clothes", "Kurta Pajama", "Shirt", "T-Shirt", "Jeans", "Trousers", "Custom Outfit"
    )

    val womenClothing = listOf(
        "Dress", "Saree", "Traditional Clothes", "Formal Suit", "Jacket", "T-Shirt + Jeans", "Pant + Shirt", "Kurti", "Lehenga", "Custom Outfit"
    )

    val girlsClothing = listOf(
        "Dress", "Traditional Clothes", "T-Shirt + Jeans", "Jacket", "Pant + Shirt", "Kurti", "Lehenga", "Custom Outfit"
    )

    val boysClothing = listOf(
        "Pant + Shirt", "T-Shirt + Jeans", "Formal Suit", "Jacket", "Traditional Clothes", "Kurta Pajama", "Custom Outfit"
    )

    val childrenClothing = listOf(
        "T-Shirt + Jeans", "Pant + Shirt", "Dress", "Jacket", "Traditional Clothes", "Custom Outfit"
    )

    fun getClothingCategoriesFor(personCategory: PersonCategory): List<String> {
        return when (personCategory) {
            PersonCategory.MAN -> menClothing
            PersonCategory.WOMAN -> womenClothing
            PersonCategory.GIRL -> girlsClothing
            PersonCategory.BOY -> boysClothing
            PersonCategory.CHILD -> childrenClothing
        }
    }
}

