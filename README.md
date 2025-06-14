# SmartRecipe 🍽️

SmartRecipe is an Android application that helps users discover recipes based on ingredients they have. It allows users to:

- Add and manage grocery items
- Search for recipes using one or more ingredients
- View detailed recipe instructions
- Save favorite recipes for later
- Search for recipes based on your grocery items
- Save groceries

## 🚀 Features

- 📦 **Room Database** for storing favorite recipes and grocery items
- 🔍 **Ingredient-Based Search** using the [Spoonacular API](https://spoonacular.com/food-api)
- 🧾 **Recipe Detail View** with title, image, and full instructions
- 🧺 **Grocery List** management (add, delete, view)
- ❤️ **Favorites Tab** to save and revisit recipes
- 🧭 **Bottom Navigation** to navigate between Search, Grocery, Ingredients, and Favorites

## 🧪 Technologies Used

- Java
- Android SDK
- Room (SQLite)
- Retrofit (for REST API)
- Spoonacular API
- Material Design Components

## 📸 Screenshots
![Screenshot 2025-06-07 224402](https://github.com/user-attachments/assets/398f6cb8-c702-42fd-a169-ba3f9757829a)
![Screenshot 2025-06-07 224317](https://github.com/user-attachments/assets/4fe98fa0-7f22-499e-9da0-710dca51a6e0)
![Screenshot 2025-06-07 224447](https://github.com/user-attachments/assets/3a1cb57f-5863-426a-bf9c-1d01bf943d27)
![Screenshot 2025-06-07 224147](https://github.com/user-attachments/assets/0d6b16db-9567-4082-94a9-fa552451f258)

## 🛠️ Getting Started

### Prerequisites

- Android Studio
- Minimum SDK: 27
- Spoonacular API key

### Clone the repository

```bash
git clone https://github.com/abrarzihan1/smart-recipe-app.git
cd smartrecipe
````

### Run the app

1. Open the project in Android Studio.
    
2. Add your **Spoonacular API Key** in `local.properties`:
    

```properties
API_KEY=YOUR_API_KEY_HERE
```

3. Build and run on an emulator or physical device.
    

## 🧾 Room Database

The app uses Room to store:

- `GroceryItem`: items added by the user
    
- `FavoriteRecipe`: recipes the user has saved

Schema is auto-generated by Room.

## 📃 License

This project is open source and available under the [MIT License](https://chatgpt.com/c/LICENSE).
