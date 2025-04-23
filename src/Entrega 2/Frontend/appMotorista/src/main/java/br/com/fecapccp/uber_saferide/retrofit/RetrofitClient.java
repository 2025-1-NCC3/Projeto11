package br.com.fecapccp.uber_saferide.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://fkhx9k-5000.csb.app";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            // Configura o interceptor de logs
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Mostra tudo

            // Configura o cliente HTTP
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // Adiciona o logger
                    .connectTimeout(30, TimeUnit.SECONDS) // Timeout de conexão
                    .readTimeout(30, TimeUnit.SECONDS)    // Timeout de leitura
                    .build();

            // Configura o Gson com o formato de data correto
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();

            // Usa o Gson configurado na criação do Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Aqui está o segredo!
                    .build();

        }
        return retrofit.create(ApiService.class);
    }
}