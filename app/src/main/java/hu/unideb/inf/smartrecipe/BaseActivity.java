package hu.unideb.inf.smartrecipe;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hu.unideb.inf.smartrecipe.activities.FavoritesActivity;
import hu.unideb.inf.smartrecipe.activities.IngredientSearchActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_search && id != selectedItemId) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_ingredients && id != selectedItemId) {
                startActivity(new Intent(this, IngredientSearchActivity.class));
                return true;
            } else if (id == R.id.nav_favorites && id != selectedItemId) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (id == R.id.nav_grocery) {
                // TODO
                return true;
            }
            return false;
        });
    }
}

