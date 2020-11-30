package com.app.biketour;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.biketour.ui.home.AboutFragment;
import com.app.biketour.ui.home.HomeFragment;
import com.app.biketour.ui.home.TourBlogFragment;
import com.app.biketour.ui.home.TourNewsFragment;
import com.app.biketour.ui.tour.TourFragment;
import com.app.biketour.ui.user.UserContactFragment;
import com.app.biketour.ui.user.UserProfileFragment;
import com.app.biketour.ui.user.UserSavedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    /**
     * Description:
     *      Main page
     * Function:
     *      onCreate()
     *      popupMenu()
     *      replaceFragment()
     *      onMenuItemClick()
     */

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        replaceFragment(new HomeFragment());
    }

    //Replace Fragment
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_tour:
                            selectedFragment = new TourFragment();
                            break;
                        case R.id.navigation_saved:
                            selectedFragment = new UserSavedFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new UserProfileFragment();
                            break;
                    }

                    replaceFragment(selectedFragment);

                    return true;
                }
            };

    public void popupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                replaceFragment(AboutFragment.newInstance());
                return true;
            case R.id.menuTour:
                bottomNavigationView.setSelectedItemId(R.id.navigation_tour);
                return true;
            case R.id.menuNews:
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                replaceFragment(TourNewsFragment.newInstance());
                return true;
            case R.id.menuBlog:
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                replaceFragment(TourBlogFragment.newInstance());
                return true;
            case R.id.menuContact:
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                replaceFragment(UserContactFragment.newInstance());
                return true;
            default:
                return false;
        }
    }
}
