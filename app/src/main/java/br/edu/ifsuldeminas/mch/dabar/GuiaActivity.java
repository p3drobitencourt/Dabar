package br.edu.ifsuldeminas.mch.dabar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Importação necessária
import androidx.annotation.NonNull; // Importação necessária
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Importação necessária
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuiaActivity extends BaseActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);

        setupToolbar(true);
        setupBottomNavigationWithoutSelection();
    }

}