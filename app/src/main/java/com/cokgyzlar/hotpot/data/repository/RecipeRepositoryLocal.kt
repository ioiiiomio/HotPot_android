package com.cokgyzlar.hotpot.data.repository

import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.data.model.MealType
import com.cokgyzlar.hotpot.models.Calories

//object RecipeRepositoryLocal {
////    val recipes = listOf(
////        Recipe(
////            id = 1,
////            name = "Pancakes",
////            description = "Fluffy and delicious pancakes",
////            calories = Calories(350, 8, 10, 60),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.BREAKFAST,
////            ingredients = listOf("2 eggs", "1 cup flour", "1/2 cup milk"),
////            instructions = listOf("Mix ingredients", "Cook on a pan", "Serve hot"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 2,
////            name = "Avocado Toast",
////            description = "Healthy avocado toast with a crispy base",
////            calories = Calories(280, 6, 18, 22),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.BREAKFAST,
////            ingredients = listOf("1 slice bread", "1/2 avocado", "Salt", "Pepper"),
////            instructions = listOf("Toast bread", "Mash avocado", "Spread on toast"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 3,
////            name = "Grilled Chicken Salad",
////            description = "A fresh and healthy chicken salad",
////            calories = Calories(320, 35, 12, 8),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.LUNCH,
////            ingredients = listOf("Chicken breast", "Lettuce", "Tomatoes", "Dressing"),
////            instructions = listOf("Grill chicken", "Chop veggies", "Mix with dressing"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 4,
////            name = "Omelette",
////            description = "A simple and protein-packed omelette",
////            calories = Calories(250, 20, 18, 2),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.BREAKFAST,
////            ingredients = listOf("3 eggs", "Cheese", "Spinach", "Salt", "Pepper"),
////            instructions = listOf("Beat eggs", "Cook in a pan", "Add cheese and spinach", "Serve hot"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 5,
////            name = "Caesar Salad",
////            description = "Crisp lettuce with a creamy Caesar dressing",
////            calories = Calories(300, 10, 24, 12),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.LUNCH,
////            ingredients = listOf("Romaine lettuce", "Caesar dressing", "Croutons", "Parmesan cheese"),
////            instructions = listOf("Toss lettuce with dressing", "Top with croutons and cheese"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 6,
////            name = "Grilled Cheese Sandwich",
////            description = "Classic comfort food with a gooey cheese filling",
////            calories = Calories(410, 14, 26, 32),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.LUNCH,
////            ingredients = listOf("2 slices bread", "2 slices cheese", "Butter"),
////            instructions = listOf("Butter bread", "Grill until golden and cheese is melted"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 7,
////            name = "Chicken Alfredo",
////            description = "Creamy pasta with grilled chicken",
////            calories = Calories(600, 35, 28, 50),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.DINNER,
////            ingredients = listOf("Chicken breast", "Fettuccine pasta", "Alfredo sauce", "Parmesan cheese"),
////            instructions = listOf("Cook pasta", "Grill chicken", "Mix together with sauce"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 8,
////            name = "Margarita Pizza",
////            description = "Simple pizza with mozzarella, basil, and tomato sauce",
////            calories = Calories(450, 16, 18, 52),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.DINNER,
////            ingredients = listOf("Pizza dough", "Tomato sauce", "Mozzarella", "Basil"),
////            instructions = listOf("Spread sauce on dough", "Top with cheese and basil", "Bake at 450°F until crispy"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 9,
////            name = "Spaghetti Carbonara",
////            description = "Classic Italian pasta dish with egg and pancetta",
////            calories = Calories(520, 25, 22, 48),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.DINNER,
////            ingredients = listOf("Spaghetti", "Eggs", "Pancetta", "Parmesan cheese", "Black pepper"),
////            instructions = listOf("Cook spaghetti", "Fry pancetta", "Mix with eggs and cheese"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 10,
////            name = "Beef Tacos",
////            description = "Ground beef tacos with fresh toppings",
////            calories = Calories(480, 28, 24, 30),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.LUNCH,
////            ingredients = listOf("Ground beef", "Taco shells", "Lettuce", "Tomato", "Cheese", "Sour cream"),
////            instructions = listOf("Cook beef with seasoning", "Fill taco shells with toppings"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 11,
////            name = "Fruit Salad",
////            description = "A refreshing mix of seasonal fruits",
////            calories = Calories(150, 2, 1, 35),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.SNACKS,
////            ingredients = listOf("Strawberries", "Blueberries", "Grapes", "Oranges"),
////            instructions = listOf("Cut fruits and toss together"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 12,
////            name = "Greek Yogurt Parfait",
////            description = "Layered yogurt with granola and fresh fruit",
////            calories = Calories(220, 12, 7, 22),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.SNACKS,
////            ingredients = listOf("Greek yogurt", "Granola", "Honey", "Berries"),
////            instructions = listOf("Layer yogurt, granola, and berries", "Drizzle with honey"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 13,
////            name = "Smoothie Bowl",
////            description = "A thick smoothie topped with fresh fruit and granola",
////            calories = Calories(280, 6, 9, 42),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.SNACKS,
////            ingredients = listOf("Frozen berries", "Banana", "Almond milk", "Granola"),
////            instructions = listOf("Blend frozen berries and banana", "Top with granola and fresh fruits"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 14,
////            name = "Vegetable Stir Fry",
////            description = "Quick stir fry with assorted vegetables",
////            calories = Calories(200, 6, 8, 28),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.DINNER,
////            ingredients = listOf("Bell peppers", "Carrots", "Broccoli", "Soy sauce"),
////            instructions = listOf("Stir-fry vegetables in oil", "Add soy sauce and cook until tender"),
////            isFavorite = false
////        ),
////        Recipe(
////            id = 15,
////            name = "Egg Fried Rice",
////            description = "A simple stir-fried rice with eggs and vegetables",
////            calories = Calories(350, 12, 14, 38),
////            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
////            mealType = MealType.DINNER,
////            ingredients = listOf("Rice", "Eggs", "Carrots", "Peas", "Soy sauce"),
////            instructions = listOf("Cook rice", "Fry eggs with veggies", "Stir in rice and soy sauce"),
////            isFavorite = false
////        )
//    )
//
//    fun getAllRecipes(): List<Recipe> = recipes
//
//    fun getRecipesByMealType(mealType: MealType): List<Recipe> {
//        return recipes.filter { it.mealType == mealType }
//    }
//}
