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
    val menClothing = listOf(
        "Shirt", "T-Shirt", "Jeans", "Trousers", "Kurta",
        "Suit", "Sherwani", "Jacket", "Shorts", "Traditional Wear", "Party Wear"
    )

    val womenClothing = listOf(
        "Saree", "Kurti", "Salwar Suit", "Top", "Jeans",
        "Dress", "Skirt", "Lehenga", "Blazer", "Jacket", "Traditional Wear", "Party Wear"
    )

    val girlsClothing = listOf(
        "Top", "Jeans", "Dress", "Skirt", "Kurti",
        "Salwar Suit", "Lehenga", "Jacket", "Party Wear", "Traditional Wear"
    )

    val boysClothing = listOf(
        "Shirt", "T-Shirt", "Jeans", "Trousers", "Shorts",
        "Kurta", "Jacket", "Party Wear", "Traditional Wear"
    )

    val childrenClothing = listOf(
        "Shirt", "T-Shirt", "Jeans", "Trousers", "Shorts",
        "Dress", "Traditional Wear", "Party Wear"
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
