package hu.unideb.inf.smartrecipe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hu.unideb.inf.smartrecipe.fragment.FavoritesFragment;
import hu.unideb.inf.smartrecipe.fragment.GroceryFragment;
import hu.unideb.inf.smartrecipe.fragment.IngredientSearchFragment;
import hu.unideb.inf.smartrecipe.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        if (savedInstanceState == null) {
            loadFragment(new SearchFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_search) {
                fragment = new SearchFragment();
            } else if (itemId == R.id.nav_ingredients) {
                fragment = new IngredientSearchFragment();
            } else if (itemId == R.id.nav_favorites) {
                fragment = new FavoritesFragment();
            } else if (itemId == R.id.nav_grocery) {
                fragment = new GroceryFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
