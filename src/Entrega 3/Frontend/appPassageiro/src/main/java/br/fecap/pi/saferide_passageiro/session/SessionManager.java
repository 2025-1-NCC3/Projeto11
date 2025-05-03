package br.fecap.pi.saferide_passageiro.session;

import android.content.Context;
import android.content.SharedPreferences;

import br.fecap.pi.saferide_passageiro.models.UsuarioModel;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "UserSession";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String USER_ID = "UserId";
    private static final String USER_CPF = "UserCpf";
    private static final String USER_DATA_NASCIMENTO = "UserDataNascimento";
    private static final String USER_NOME = "UserNome";
    private static final String USER_TELEFONE = "UserTelefone";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_SENHA = "UserSenha";
    private static final String USER_TOKEN = "UserToken";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Criar sessão completa (login ou recriar após update)
    public void createSession(UsuarioModel usuario, String token) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putInt(USER_ID, usuario.getIdUsuario());
        editor.putString(USER_CPF, usuario.getCpf());
        editor.putString(USER_DATA_NASCIMENTO, usuario.getDataNascimento().toString());
        editor.putString(USER_NOME, usuario.getNome());
        editor.putString(USER_TELEFONE, usuario.getTelefone());
        editor.putString(USER_EMAIL, usuario.getEmail());
        // Se quiser guardar senha, adicione abaixo:
        // editor.putString(USER_SENHA, usuario.getSenha());
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    // Atualizar apenas os dados do usuário, mantendo o token
    public void updateUsuario(UsuarioModel usuario) {
        editor.putInt(USER_ID, usuario.getIdUsuario());
        editor.putString(USER_CPF, usuario.getCpf());
        editor.putString(USER_DATA_NASCIMENTO,
                usuario.getDataNascimento() != null ? usuario.getDataNascimento().toString() : getUserDataNascimento());
        editor.putString(USER_NOME, usuario.getNome());
        editor.putString(USER_TELEFONE, usuario.getTelefone());
        editor.putString(USER_EMAIL, usuario.getEmail());
        editor.putString(USER_SENHA, usuario.getSenha());
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return sharedPreferences.getInt(USER_ID, 0);
    }

    public String getUserCpf() {
        return sharedPreferences.getString(USER_CPF, null);
    }

    public String getUserDataNascimento() {
        return sharedPreferences.getString(USER_DATA_NASCIMENTO, null);
    }

    public String getUserNome() {
        return sharedPreferences.getString(USER_NOME, null);
    }

    public String getUserTelefone() {
        return sharedPreferences.getString(USER_TELEFONE, null);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, null);
    }

    public String getUserSenha() {
        return sharedPreferences.getString(USER_SENHA, null); // Corrigido aqui
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
