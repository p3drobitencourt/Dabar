package br.edu.ifsuldeminas.mch.dabar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btnGravarResumo, btnBibliotecaResumos, btnGuiaDabar, btnDicaEstudo, btnMetasEstudo;
    private BottomNavigationView bottomNavigation;
    private TextView welcomeMessage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            goToLogin();
            return;
        }

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        welcomeMessage = findViewById(R.id.welcome_message);
        btnGravarResumo = findViewById(R.id.btn_gravar_resumo);
        btnBibliotecaResumos = findViewById(R.id.btn_biblioteca_resumos);
        btnGuiaDabar = findViewById(R.id.btn_guia_dabar);
        btnDicaEstudo = findViewById(R.id.btn_dica_estudo);
        // Lembre-se de adicionar um botão com o id 'btn_metas_estudo' no seu activity_main.xml
        // btnMetasEstudo = findViewById(R.id.btn_metas_estudo);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        String userName = currentUser.getDisplayName();
        if (userName != null && !userName.isEmpty()) {
            welcomeMessage.setText(String.format("Bem-vindo, %s!", userName));
        } else {
            welcomeMessage.setText("Bem-vindo!");
        }

        createNotificationChannel();
        scheduleDailyNotification();
        setupButtonListeners();
        setupBottomNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_logout) {
            mAuth.signOut();
            goToLogin();
            return true;
        } else if (itemId == R.id.action_metas) {
            startActivity(new Intent(this, MetasActivity.class));
            return true;
        } else if (itemId == R.id.action_dica) {
            startActivity(new Intent(this, CitacaoDoDiaActivity.class));
            return true;
        } else if (itemId == R.id.action_guia) {
            startActivity(new Intent(this, GuiaActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginInicioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupButtonListeners() {
        btnGravarResumo.setOnClickListener(v -> startActivity(new Intent(this, NovoResumoActivity.class)));
        btnBibliotecaResumos.setOnClickListener(v -> startActivity(new Intent(this, ListResumosActivity.class)));
        btnGuiaDabar.setOnClickListener(v -> startActivity(new Intent(this, GuiaActivity.class)));
        btnDicaEstudo.setOnClickListener(v -> startActivity(new Intent(this, CitacaoDoDiaActivity.class)));
        // if(btnMetasEstudo != null) {
        //    btnMetasEstudo.setOnClickListener(v -> startActivity(new Intent(this, MetasActivity.class)));
        // }
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.navigation_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_library) {
                startActivity(new Intent(this, BibliotecaActivity.class));
                return true;
            } else if (itemId == R.id.navigation_new_category) {
                startActivity(new Intent(this, NovaCategoriaActivity.class));
                return true;
            }
            return false;
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Lembrete Dabar";
            String description = "Canal para lembretes diários de estudo do Dabar";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationReceiver.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleDailyNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Toast.makeText(this, "Lembrete diário de estudos ativado!", Toast.LENGTH_SHORT).show();
        }
    }
}