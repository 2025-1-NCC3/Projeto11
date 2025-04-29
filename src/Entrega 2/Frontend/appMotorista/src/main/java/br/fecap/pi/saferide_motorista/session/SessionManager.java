package br.fecap.pi.saferide_motorista.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Locale;

import br.fecap.pi.saferide_motorista.models.MotoristaModel;
import br.fecap.pi.saferide_motorista.models.UsuarioModel;

public class SessionManager {

    // SharedPreferences para armazenar os dados
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Nome do arquivo de SharedPreferences
    private static final String PREF_NAME = "UserSession";
    private static final String IS_LOGGED_IN = "IsLoggedIn";

    // Dados do usuário
    private static final String USER_ID = "UserId";
    private static final String USER_CPF = "UserCpf";
    private static final String USER_DATA_NASCIMENTO = "UserDataNascimento";
    private static final String USER_NOME = "UserNome";
    private static final String USER_TELEFONE = "UserTelefone";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_SENHA = "UserSenha";
    private static final String USER_TOKEN = "UserToken";

    // Dados do motorista
    private static final String MOTORISTA_ID = "MotoristaId";
    private static final String MOTORISTA_CNH = "MotoristaCnh";
    private static final String MOTORISTA_VALIDADE_CNH = "MotoristaValidadeCnh";
    private static final String MOTORISTA_MODELO = "MotoristaModelo";
    private static final String MOTORISTA_COR = "MotoristaCor";
    private static final String MOTORISTA_PLACA = "MotoristaPlaca";
    private static final String MOTORISTA_AVALIACAO = "MotoristaAvaliacao";

    // Construtor
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Criar sessão do usuário
    public void createSession(UsuarioModel usuario, String token) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putInt(USER_ID, usuario.getIdUsuario());
        editor.putString(USER_CPF, usuario.getCpf());
        editor.putString(USER_DATA_NASCIMENTO, usuario.getDataNascimento().toLocaleString());
        editor.putString(USER_NOME, usuario.getNome());
        editor.putString(USER_TELEFONE, usuario.getTelefone());
        editor.putString(USER_EMAIL, usuario.getEmail());
        editor.putString(USER_SENHA, usuario.getDataNascimento().toString());
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    // Salvar dados do motorista
    public void saveMotorista(MotoristaModel motorista) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String validadeFormatada = sdf.format(motorista.getValidadeCarteira());

        editor.putInt(MOTORISTA_ID, Integer.parseInt(motorista.getIdMotorista()));  // Salvar como int
        editor.putString(MOTORISTA_CNH, motorista.getCnh());
        editor.putString(MOTORISTA_VALIDADE_CNH, validadeFormatada);
        editor.putString(MOTORISTA_MODELO, motorista.getModelo());
        editor.putString(MOTORISTA_COR, motorista.getCor());
        editor.putString(MOTORISTA_PLACA, motorista.getPlaca());
        editor.putFloat(MOTORISTA_AVALIACAO, motorista.getAvaliacao());
        editor.apply();
    }


    public MotoristaModel getMotorista() {
        String json = sharedPreferences.getString("motorista", null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, MotoristaModel.class);
        }
        return null;
    }


    // Métodos para recuperar dados do usuário
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
        return sharedPreferences.getString(USER_SENHA, null);
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public int getMotoristaId() {
        return sharedPreferences.getInt(MOTORISTA_ID, 0);
    }

    public String getMotoristaCnh() {
        return sharedPreferences.getString(MOTORISTA_CNH, null);
    }

    public String getMotoristaValidadeCnh() {
        return sharedPreferences.getString(MOTORISTA_VALIDADE_CNH, null);
    }

    public String getMotoristaModelo() {
        return sharedPreferences.getString(MOTORISTA_MODELO, null);
    }

    public String getMotoristaCor() {
        return sharedPreferences.getString(MOTORISTA_COR, null);
    }

    public String getMotoristaPlaca() {
        return sharedPreferences.getString(MOTORISTA_PLACA, null);
    }

    public float getMotoristaAvaliacao() {
        return sharedPreferences.getFloat(MOTORISTA_AVALIACAO, 0f);
    }

    // Logout
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
