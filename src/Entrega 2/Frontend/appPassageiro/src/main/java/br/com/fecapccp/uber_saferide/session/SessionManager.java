package br.com.fecapccp.uber_saferide.session;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.fecapccp.uber_saferide.models.UsuarioModel;

public class SessionManager {

    // SharedPreferences para armazenar os dados
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Nome do arquivo de SharedPreferences
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

    // Construtor da classe
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Salvar dados do usuário na sessão
    public void createSession(UsuarioModel usuario, String token) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putInt(USER_ID, usuario.getIdUsuario());
        editor.putString(USER_CPF, usuario.getCpf());
        editor.putString(USER_DATA_NASCIMENTO, usuario.getDataNascimento().toString());
        editor.putString(USER_NOME, usuario.getNome());
        editor.putString(USER_TELEFONE, usuario.getTelefone());
        editor.putString(USER_EMAIL, usuario.getEmail());
        editor.putString(USER_SENHA, usuario.getDataNascimento().toString());
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    // Verificar se o usuário está logado
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
        return sharedPreferences.getString(USER_TELEFONE, null);
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    // Limpar os dados de sessão (logout)
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
