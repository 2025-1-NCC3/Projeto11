package br.fecap.pi.saferide_passageiro.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaceAutoSuggestAdapter extends ArrayAdapter<String> {
    private PlacesClient placesClient;
    private List<String> resultList = new ArrayList<>();
    private List<String> placeIdList = new ArrayList<>();

    public PlaceAutoSuggestAdapter(Context context, PlacesClient placesClient) {
        super(context, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        this.placesClient = placesClient;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        // Verificar se a posição é válida antes de acessar a lista
        if (position >= 0 && position < resultList.size()) {
            return resultList.get(position);
        }
        return ""; // Retornar string vazia em caso de posição inválida
    }

    public String getPlaceId(int position) {
        if (position >= 0 && position < placeIdList.size()) {
            return placeIdList.get(position);
        }
        return ""; // Retornar string vazia em caso de posição inválida
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // Verificar se a constraint não é nula e não está vazia
                if (constraint != null && constraint.length() > 0) {
                    // Obter as previsões de autocompletar
                    List<String> predictions = getAutocompletePredictions(constraint.toString());

                    if (predictions != null && !predictions.isEmpty()) {
                        results.values = predictions;
                        results.count = predictions.size();
                    } else {
                        // Garantir que temos pelo menos uma lista vazia, não null
                        results.values = new ArrayList<String>();
                        results.count = 0;
                    }
                } else {
                    // Constraint vazia, retornar lista vazia
                    results.values = new ArrayList<String>();
                    results.count = 0;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Limpar a lista de resultados atual
                resultList.clear();

                // Verificar se há resultados válidos
                if (results != null && results.count > 0 && results.values != null) {
                    @SuppressWarnings("unchecked")
                    List<String> filteredList = (List<String>) results.values;
                    resultList.addAll(filteredList);

                    // Notificar que os dados foram alterados
                    notifyDataSetChanged();
                } else {
                    // Se não houver resultados, notificar que o conjunto de dados é inválido
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private List<String> getAutocompletePredictions(String query) {
        // Limpar listas anteriores
        List<String> tempResultList = new ArrayList<>();
        List<String> tempPlaceIdList = new ArrayList<>();

        // Verificar se a query tem pelo menos 2 caracteres
        if (query == null || query.length() < 2) {
            return tempResultList;
        }

        try {
            // Criar token de sessão
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

            // Configurar a requisição
            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(token)
                    .setQuery(query)
                    .build();

            Task<FindAutocompletePredictionsResponse> task = placesClient.findAutocompletePredictions(request);

            // Use o Tasks.await com um tempo limite para evitar bloqueios indefinidos
            FindAutocompletePredictionsResponse response = Tasks.await(task, 5, TimeUnit.SECONDS);

            if (response != null) {
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    tempResultList.add(prediction.getFullText(null).toString());
                    tempPlaceIdList.add(prediction.getPlaceId());
                }
            }

            // Atualizar as listas principais após o processamento bem-sucedido
            synchronized (this) {
                placeIdList.clear();
                placeIdList.addAll(tempPlaceIdList);
            }

            return tempResultList;

        } catch (Exception e) {
            Log.e("PlaceAutoSuggestAdapter", "Erro ao obter previsões: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retornar lista vazia em caso de erro
        }
    }
}