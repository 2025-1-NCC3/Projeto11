package br.com.fecapccp.uber_saferide.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.fecapccp.uber_saferide.R;
import br.com.fecapccp.uber_saferide.model.AtividadeModel;  // Corrigido o import para 'models'

public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {

    private final List<AtividadeModel> listaAtividades;

    public adapter(@NonNull List<AtividadeModel> lista) {
        this.listaAtividades = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Atualizado para usar o nome correto do arquivo de layout
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista_atividade, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AtividadeModel atividade = listaAtividades.get(position);
        holder.textRua.setText(atividade.getEndereco());
        holder.textData.setText(atividade.getData());
        holder.textHoras.setText(atividade.getHorario());
    }

    @Override
    public int getItemCount() {
        return listaAtividades.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textRua;
        private final TextView textData;
        private final TextView textHoras;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textRua = itemView.findViewById(R.id.textRua);
            textData = itemView.findViewById(R.id.textData);
            textHoras = itemView.findViewById(R.id.textHoras);
        }
    }
}
