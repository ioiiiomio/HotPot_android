package com.cokgyzlar.hotpot.data.repository

import com.cokgyzlar.hotpot.data.model.Calories
import com.cokgyzlar.hotpot.data.model.MealType
import com.cokgyzlar.hotpot.data.model.Recipe

object RecipeRepositoryLocal {
    val recipes = listOf(
        Recipe(
            id = 1,
            name = "Pancakes",
            description = "Fluffy and golden pancakes made from scratch, perfect for a hearty breakfast with syrup and berries.",
            calories = Calories(350, 8, 10, 60),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.BREAKFAST,
            ingredients = listOf("2 eggs", "1 cup flour", "1/2 cup milk", "1 tbsp sugar", "1 tsp baking powder", "Butter for frying"),
            instructions = listOf("Mix eggs, flour, sugar, baking powder, and milk into a batter.", "Heat butter on a skillet.", "Pour batter and cook until bubbles form, then flip.", "Serve hot with syrup or fruit."),
            isFavorite = false
        ),
        Recipe(
            id = 2,
            name = "Avocado Toast",
            description = "Crispy toasted bread topped with creamy mashed avocado, seasoned to perfection for a quick breakfast.",
            calories = Calories(280, 6, 18, 22),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.BREAKFAST,
            ingredients = listOf("1 slice whole grain bread", "1/2 ripe avocado", "Salt", "Black pepper", "Chili flakes (optional)"),
            instructions = listOf("Toast bread to desired crispiness.", "Mash avocado with salt and pepper.", "Spread on toast and top with chili flakes if desired."),
            isFavorite = false
        ),
        Recipe(
            id = 3,
            name = "Grilled Chicken Salad",
            description = "Light and nutritious grilled chicken served over crisp greens with a tangy vinaigrette.",
            calories = Calories(320, 35, 12, 8),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.LUNCH,
            ingredients = listOf("Chicken breast", "Lettuce", "Cherry tomatoes", "Cucumber", "Olive oil", "Lemon juice", "Salt", "Pepper"),
            instructions = listOf("Season and grill chicken breast.", "Chop veggies and mix with lettuce.", "Slice chicken and place on top.", "Drizzle with lemon vinaigrette."),
            isFavorite = false
        ),
        Recipe(
            id = 4,
            name = "Omelette",
            description = "Fluffy omelette with cheese and fresh spinach, rich in protein and flavor.",
            calories = Calories(250, 20, 18, 2),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.BREAKFAST,
            ingredients = listOf("3 eggs", "1/4 cup shredded cheese", "Handful of spinach", "Salt", "Pepper"),
            instructions = listOf("Beat eggs with salt and pepper.", "Heat a nonstick pan and pour eggs.", "Add cheese and spinach, cook until set, then fold and serve."),
            isFavorite = false
        ),
        Recipe(
            id = 5,
            name = "Caesar Salad",
            description = "Crispy romaine lettuce tossed with creamy Caesar dressing, crunchy croutons, and Parmesan cheese.",
            calories = Calories(300, 10, 24, 12),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.LUNCH,
            ingredients = listOf("Romaine lettuce", "Caesar dressing", "Croutons", "Parmesan cheese"),
            instructions = listOf("Chop lettuce and place in bowl.", "Add croutons and cheese.", "Drizzle dressing and toss to coat."),
            isFavorite = false
        ),
        Recipe(
            id = 6,
            name = "Grilled Cheese Sandwich",
            description = "Golden toasted sandwich with gooey melted cheese – the ultimate comfort lunch.",
            calories = Calories(410, 14, 26, 32),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.LUNCH,
            ingredients = listOf("2 slices of bread", "2 slices of cheddar cheese", "Butter"),
            instructions = listOf("Butter the bread slices.", "Place cheese between slices and grill until golden and crisp on both sides."),
            isFavorite = false
        ),
        Recipe(
            id = 7,
            name = "Chicken Alfredo",
            description = "Creamy Alfredo pasta tossed with grilled chicken and sprinkled with Parmesan cheese.",
            calories = Calories(600, 35, 28, 50),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.DINNER,
            ingredients = listOf("Chicken breast", "Fettuccine pasta", "Alfredo sauce", "Parmesan cheese"),
            instructions = listOf("Grill seasoned chicken breast.", "Boil pasta until al dente.", "Mix pasta with Alfredo sauce and top with sliced chicken and Parmesan."),
            isFavorite = false
        ),
        Recipe(
            id = 8,
            name = "Margarita Pizza",
            description = "A simple yet flavorful pizza topped with tomato sauce, fresh mozzarella, and basil leaves.",
            calories = Calories(450, 16, 18, 52),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.DINNER,
            ingredients = listOf("Pizza dough", "Tomato sauce", "Fresh mozzarella", "Basil leaves"),
            instructions = listOf("Preheat oven to 450°F.", "Spread sauce on dough, top with mozzarella and basil.", "Bake until crust is golden and cheese melted."),
            isFavorite = false
        ),
        Recipe(
            id = 9,
            name = "Spaghetti Carbonara",
            description = "Classic Roman pasta dish made with eggs, Parmesan, pancetta, and black pepper.",
            calories = Calories(520, 25, 22, 48),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.DINNER,
            ingredients = listOf("Spaghetti", "Eggs", "Pancetta", "Parmesan cheese", "Black pepper"),
            instructions = listOf("Boil pasta and reserve some water.", "Fry pancetta until crisp.", "Mix eggs and cheese, then toss with hot pasta and pancetta off heat."),
            isFavorite = false
        ),
        Recipe(
            id = 10,
            name = "Beef Tacos",
            description = "Savory beef tacos loaded with lettuce, tomatoes, cheese, and sour cream in a crunchy shell.",
            calories = Calories(480, 28, 24, 30),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.LUNCH,
            ingredients = listOf("Ground beef", "Taco shells", "Lettuce", "Tomato", "Cheese", "Sour cream"),
            instructions = listOf("Brown ground beef with seasoning.", "Fill taco shells with meat and toppings."),
            isFavorite = false
        ),
        Recipe(
            id = 11,
            name = "Fruit Salad",
            description = "A vibrant mix of fresh seasonal fruits, perfect as a light snack or dessert.",
            calories = Calories(150, 2, 1, 35),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.SNACKS,
            ingredients = listOf("Strawberries", "Blueberries", "Grapes", "Oranges", "Mint leaves"),
            instructions = listOf("Wash and chop all fruits into bite-sized pieces.", "Toss gently in a bowl and garnish with mint."),
            isFavorite = false
        ),
        Recipe(
            id = 12,
            name = "Greek Yogurt Parfait",
            description = "Layered parfait with protein-rich Greek yogurt, crunchy granola, and fresh berries.",
            calories = Calories(220, 12, 7, 22),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.SNACKS,
            ingredients = listOf("Greek yogurt", "Granola", "Honey", "Mixed berries"),
            instructions = listOf("Layer yogurt, granola, and berries in a glass.", "Drizzle with honey and serve."),
            isFavorite = false
        ),
        Recipe(
            id = 13,
            name = "Smoothie Bowl",
            description = "A thick smoothie served in a bowl and topped with fresh fruits, seeds, and granola.",
            calories = Calories(280, 6, 9, 42),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.SNACKS,
            ingredients = listOf("Frozen berries", "Banana", "Almond milk", "Granola", "Chia seeds"),
            instructions = listOf("Blend frozen berries, banana, and almond milk.", "Pour into bowl and top with granola, fruits, and seeds."),
            isFavorite = false
        ),
        Recipe(
            id = 14,
            name = "Vegetable Stir Fry",
            description = "A quick, colorful stir fry with a medley of vegetables and a savory soy sauce glaze.",
            calories = Calories(200, 6, 8, 28),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.DINNER,
            ingredients = listOf("Bell peppers", "Carrots", "Broccoli", "Soy sauce", "Garlic", "Sesame oil"),
            instructions = listOf("Chop vegetables.", "Stir-fry with garlic and sesame oil.", "Add soy sauce and cook until tender."),
            isFavorite = false
        ),
        Recipe(
            id = 15,
            name = "Egg Fried Rice",
            description = "A satisfying dish of rice stir-fried with scrambled eggs and mixed vegetables.",
            calories = Calories(350, 12, 14, 38),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.DINNER,
            ingredients = listOf("Cooked rice", "Eggs", "Carrots", "Peas", "Soy sauce", "Green onions"),
            instructions = listOf("Scramble eggs in a pan.", "Add veggies and stir-fry.", "Add rice and soy sauce, mix thoroughly."),
            isFavorite = false
        ),
        Recipe(
            id = 16,
            name = "Tomato Basil Soup",
            description = "Creamy homemade tomato soup with fresh basil, served warm and comforting.",
            calories = Calories(180, 5, 10, 20),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.LUNCH,
            ingredients = listOf("Tomatoes", "Garlic", "Onion", "Basil", "Cream", "Olive oil"),
            instructions = listOf("Roast tomatoes and garlic.", "Blend with onion and basil.", "Simmer and add cream before serving."),
            isFavorite = false
        ),
        Recipe(
            id = 17,
            name = "Turkey Sandwich",
            description = "Lean turkey breast on whole grain bread with lettuce, tomato, and mustard.",
            calories = Calories(320, 25, 8, 30),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.LUNCH,
            ingredients = listOf("2 slices whole grain bread", "Turkey breast slices", "Lettuce", "Tomato", "Mustard"),
            instructions = listOf("Toast bread if desired.", "Layer turkey, lettuce, and tomato.", "Add mustard and assemble sandwich."),
            isFavorite = false
        ),
        Recipe(
            id = 18,
            name = "Banana Oatmeal",
            description = "Warm and hearty oatmeal sweetened with ripe banana and a touch of cinnamon.",
            calories = Calories(290, 6, 5, 55),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.BREAKFAST,
            ingredients = listOf("Rolled oats", "Milk or water", "Banana", "Cinnamon", "Honey"),
            instructions = listOf("Cook oats in milk or water.", "Stir in mashed banana and cinnamon.", "Top with honey."),
            isFavorite = false
        ),
        Recipe(
            id = 19,
            name = "Chili Con Carne",
            description = "A hearty and spicy chili made with ground beef, beans, and tomatoes.",
            calories = Calories(500, 30, 22, 35),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.DINNER,
            ingredients = listOf("Ground beef", "Kidney beans", "Tomatoes", "Onion", "Garlic", "Chili powder"),
            instructions = listOf("Brown beef with onion and garlic.", "Add tomatoes, beans, and spices.", "Simmer for 30 minutes."),
            isFavorite = false
        ),
        Recipe(
            id = 20,
            name = "Protein Energy Balls",
            description = "No-bake energy bites with oats, peanut butter, and chocolate chips.",
            calories = Calories(180, 7, 9, 18),
            imageUrl = "android.resource://com.example.hotpot/drawable/dummy_recipe",
            mealType = MealType.SNACKS,
            ingredients = listOf("Rolled oats", "Peanut butter", "Honey", "Chia seeds", "Dark chocolate chips"),
            instructions = listOf("Mix all ingredients in a bowl.", "Form into small balls and chill in fridge."),
            isFavorite = false
        )

    )
    fun getRecipeById(recipeIds: List<Int>): List<Recipe> {
        return recipes.filter { it.id in recipeIds }
    }

    fun getAllRecipes(): List<Recipe> = recipes

    fun getRecipesByMealType(mealType: MealType): List<Recipe> {
        return recipes.filter { it.mealType == mealType }
    }

}
