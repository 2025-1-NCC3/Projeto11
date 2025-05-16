package br.fecap.pi.saferide_passageiro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.dto.AvaliacoesRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.models.RotaModel;
import br.fecap.pi.saferide_passageiro.utils.AvaliacaoUtils;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AdapterRotas extends RecyclerView.Adapter<AdapterRotas.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(RotaModel rota);
    }

    private List<RotaModel> listaRotas;
    private OnItemClickListener listener;

    public AdapterRotas(List<RotaModel> listaRotas, OnItemClickListener listener) {
        this.listaRotas = listaRotas;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textRua;
        MaterialRatingBar starBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textRua = itemView.findViewById(R.id.textRua);
            starBar = itemView.findViewById(R.id.starBarAdapter);
        }
    }

    @NonNull
    @Override
    public AdapterRotas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_rotas_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRotas.ViewHolder holder, int position) {
        RotaModel rota = listaRotas.get(position);
        holder.textRua.setText(rota.getDescricao());

        if (rota.getAvaliacoes() != null) {
            holder.starBar.setRating(AvaliacaoUtils.calcularMediaAvaliacao(rota.getAvaliacoes()));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(rota);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRotas.size();
    }
}