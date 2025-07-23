package br.edu.ifsuldeminas.mch.dabar;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

// Garanta que a classe herde da sua BaseActivity ou de AppCompatActivity
public class MainActivity extends BaseActivity {

    // Constante para o pedido de permissão de notificação
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;

    // Seus componentes de UI e do Firebase
    private Button btnGravarResumo, btnBibliotecaResumos, btnGuiaDabar, btnDicaEstudo, btnMetasEstudo;
    private TextView welcomeMessage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização do Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Verificação de segurança para garantir que há um usuário logado
        if (currentUser == null) {
            goToLogin();
            return; // Impede a execução do resto do código para evitar erros
        }

        // Configuração da sua Toolbar e Bottom Navigation (se estiverem na BaseActivity)
        setupToolbar(false);
        setupBottomNavigation(R.id.navigation_home);

        // Mapeamento de todos os componentes visuais
        welcomeMessage = findViewById(R.id.welcome_message);
        btnGravarResumo = findViewById(R.id.btn_gravar_resumo);
        btnBibliotecaResumos = findViewById(R.id.btn_biblioteca_resumos);
        btnGuiaDabar = findViewById(R.id.btn_guia_dabar);
        btnDicaEstudo = findViewById(R.id.btn_dica_estudo);
        btnMetasEstudo = findViewById(R.id.btn_metas_estudo);

        // Personaliza a mensagem de boas-vindas
        String userName = currentUser.getDisplayName();
        if (userName != null && !userName.isEmpty()) {
            welcomeMessage.setText(String.format("Bem-vindo, %s!", userName));
        } else {
            welcomeMessage.setText("Bem-vindo!");
        }

        // === INÍCIO DAS CORREÇÕES E ADIÇÕES PARA NOTIFICAÇÃO ===

        // 1. Pede a permissão de notificação ao usuário (essencial para Android 13+)
        pedirPermissaoDeNotificacao();

        // 2. Cria o canal de notificação com o ID correto
        createNotificationChannel();

        // 3. Agenda a notificação diária
        scheduleDailyNotification();

        // === FIM DAS CORREÇÕES E ADIÇÕES PARA NOTIFICAÇÃO ===

        // Configura os listeners dos botões
        setupButtonListeners();
    }

    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginInicioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupButtonListeners() {
        btnGravarResumo.setOnClickListener(v -> startActivity(new Intent(this, NovoResumoActivity.class)));
        btnBibliotecaResumos.setOnClickListener(v -> startActivity(new Intent(this, ListResumosActivity.class))); // Corrigido para a lista de resumos
        btnGuiaDabar.setOnClickListener(v -> startActivity(new Intent(this, GuiaActivity.class)));
        btnDicaEstudo.setOnClickListener(v -> startActivity(new Intent(this, CitacaoDoDiaActivity.class)));
        btnMetasEstudo.setOnClickListener(v -> startActivity(new Intent(this, MetasActivity.class)));
    }

    // --- LÓGICA DAS NOTIFICAÇÕES (COMPLETA E CORRIGIDA) ---

    /**
     * Pede a permissão POST_NOTIFICATIONS se o app estiver rodando no Android 13 ou superior.
     */
    private void pedirPermissaoDeNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }
    }

    /**
     * Cria o canal de notificação necessário para Android 8 (Oreo) e superior.
     * O ID do canal DEVE ser o mesmo usado no NotificationReceiver.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Lembretes Dabar";
            String description = "Canal para lembretes diários de estudo do Dabar";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // AQUI ESTÁ A CORREÇÃO: Usamos o CHANNEL_ID da sua classe NotificationReceiver
            NotificationChannel channel = new NotificationChannel(NotificationReceiver.CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Agenda um alarme repetitivo para disparar o NotificationReceiver todos os dias.
     */
    private void scheduleDailyNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20); // Notificação às 20h
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Se a hora já passou hoje, agenda para o dia seguinte
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