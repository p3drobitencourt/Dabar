package br.edu.ifsuldeminas.mch.dabar.network;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.security.cert.CertificateException;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.quotable.io/";
    private static volatile Retrofit retrofitInstance = null;

    public static Retrofit getClient() {
        if (retrofitInstance == null) {
            synchronized (RetrofitClient.class) {
                if (retrofitInstance == null) {
                    try {
                        // Cria um TrustManager que não valida cadeias de certificados
                        final TrustManager[] trustAllCerts = new TrustManager[]{
                                new X509TrustManager() {
                                    @Override
                                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                                    @Override
                                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                                    @Override
                                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                        return new java.security.cert.X509Certificate[]{};
                                    }
                                }
                        };

                        // Instala o TrustManager "confia-em-todos"
                        final SSLContext sslContext = SSLContext.getInstance("SSL");
                        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                        builder.hostnameVerifier((hostname, session) -> true);

                        OkHttpClient okHttpClient = builder.build();

                        // Constrói a instância do Retrofit usando o cliente customizado
                        retrofitInstance = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(okHttpClient) // <-- AQUI ESTÁ A MUDANÇA IMPORTANTE
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        return retrofitInstance;
    }
}