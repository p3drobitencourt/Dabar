package br.edu.ifsuldeminas.mch.dabar;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Esta classe é um BroadcastReceiver. Ela "ouve" por um sinal do sistema
 * (neste caso, o alarme que vamos agendar) e executa uma ação quando o recebe.
 * A ação aqui é criar e exibir uma notificação.
 */
public class NotificationReceiver extends BroadcastReceiver {

    // Um ID único para o nosso canal de notificação.
    public static final String CHANNEL_ID = "DabarReminderChannel";
    // Um ID único para a notificação em si.
    private static final int NOTIFICATION_ID = 100;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Cria um Intent para abrir a MainActivity quando o usuário clicar na notificação.
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE);

        // Constrói a notificação usando o NotificationCompat.Builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_dabar_logo) // Ícone pequeno (obrigatório)
                .setContentTitle("Hora de Dabar!") // Título da notificação
                .setContentText("Que tal revisar um resumo para fixar o conteúdo de hoje?") // Texto principal
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridade da notificação
                .setContentIntent(pendingIntent) // Ação ao clicar na notificação
                .setAutoCancel(true); // Remove a notificação da barra quando clicada

        // Exibe a notificação.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Verifica se o app tem permissão para postar notificações (necessário para API 33+)
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Se não tiver permissão, não faz nada. A permissão deve ser pedida na Activity.
            // Para o escopo do trabalho, vamos assumir que a permissão será concedida.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}