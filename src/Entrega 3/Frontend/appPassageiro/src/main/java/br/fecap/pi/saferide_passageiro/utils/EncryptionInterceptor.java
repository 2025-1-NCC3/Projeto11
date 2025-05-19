package br.fecap.pi.saferide_passageiro.utils;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import java.io.IOException;

public class EncryptionInterceptor implements Interceptor {
    private static final int SHIFT = 3;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String url = originalRequest.url().toString();

        // Se for a rota de histórico de corridas, ignora criptografia/descriptografia
        if (url.contains("/corridas/passageiro/")) {
            return chain.proceed(originalRequest); // Passa direto sem modificar
        }

        RequestBody originalBody = originalRequest.body();
        MediaType contentType = originalBody != null ? originalBody.contentType() : null;

        Request encryptedRequest = originalRequest;

        // Criptografar o corpo da requisição se for JSON (para outras rotas)
        if (originalBody != null && contentType != null && "json".equalsIgnoreCase(contentType.subtype())) {
            Buffer buffer = new Buffer();
            originalBody.writeTo(buffer);
            String originalJson = buffer.readUtf8();

            String encryptedJson = CaesarCipher.encrypt(originalJson, SHIFT);

            RequestBody encryptedBody = RequestBody.create(
                    encryptedJson,
                    MediaType.get("application/json; charset=utf-8")
            );

            encryptedRequest = originalRequest.newBuilder()
                    .method(originalRequest.method(), encryptedBody)
                    .build();
        }

        // Envia a requisição (criptografada ou não)
        Response response = chain.proceed(encryptedRequest);

        // Se for a rota de histórico, retorna a resposta original
        if (url.contains("/corridas/passageiro/")) {
            return response;
        }

        // Descriptografar a resposta se for JSON (para outras rotas)
        ResponseBody responseBody = response.body();
        MediaType responseContentType = responseBody != null ? responseBody.contentType() : null;

        if (responseBody != null && responseContentType != null &&
                "json".equalsIgnoreCase(responseContentType.subtype())) {

            String encryptedResponseBody = responseBody.string();
            String decryptedResponseBody = CaesarCipher.decrypt(encryptedResponseBody, SHIFT);

            ResponseBody decryptedBody = ResponseBody.create(decryptedResponseBody, responseContentType);

            return response.newBuilder()
                    .body(decryptedBody)
                    .build();
        }

        return response;
    }
}