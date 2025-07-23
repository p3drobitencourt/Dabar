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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends BaseActivity {

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

        // Verificação de segurança: se não há usuário, não deveria estar aqui.
        if (currentUser == null) {
            goToLogin();
            return; // Impede a execução do resto do código para evitar erros.
        }

        setupToolbar(false);
        setupBottomNavigation(R.id.navigation_home);

        // Mapeamento de todos os componentes visuais.
        welcomeMessage = findViewById(R.id.welcome_message);
        btnGravarResumo = findViewById(R.id.btn_gravar_resumo);
        btnBibliotecaResumos = findViewById(R.id.btn_biblioteca_resumos);
        btnGuiaDabar = findViewById(R.id.btn_guia_dabar);
        btnDicaEstudo = findViewById(R.id.btn_dica_estudo);
        btnMetasEstudo = findViewById(R.id.btn_metas_estudo); // Botão "Minhas Metas"
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Personaliza a mensagem de boas-vindas com o nome do usuário.
        String userName = currentUser.getDisplayName();
        if (userName != null && !userName.isEmpty()) {
            welcomeMessage.setText(String.format("Bem-vindo, %s!", userName));
        } else {
            welcomeMessage.setText("Bem-vindo!");
        }

        // Chamada dos métodos de configuração.
        createNotificationChannel();
        scheduleDailyNotification();
        setupButtonListeners();
    }

    // --- MÉTODOS AUXILIARES E DE NAVEGAÇÃO ---
    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginInicioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupButtonListeners() {
        btnGravarResumo.setOnClickListener(v -> startActivity(new Intent(this, NovoResumoActivity.class)));
        btnBibliotecaResumos.setOnClickListener(v -> startActivity(new Intent(this, BibliotecaActivity.class))); // Corrigido para BibliotecaActivity
        btnGuiaDabar.setOnClickListener(v -> startActivity(new Intent(this, GuiaActivity.class)));
        btnDicaEstudo.setOnClickListener(v -> startActivity(new Intent(this, CitacaoDoDiaActivity.class)));
        btnMetasEstudo.setOnClickListener(v -> startActivity(new Intent(this, MetasActivity.class)));
    }

    // --- LÓGICA DAS NOTIFICAÇÕES ---
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
        calendar.set(Calendar.HOUR_OF_DAY, 20); // Notificação às 20h
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
        }
    }
}