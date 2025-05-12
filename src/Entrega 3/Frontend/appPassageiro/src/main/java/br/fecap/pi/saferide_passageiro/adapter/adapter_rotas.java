package br.fecap.pi.saferide_passageiro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.models.RotasModel;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class adapter_rotas extends RecyclerView.Adapter<adapter_rotas.ViewHolder> {

    private List<RotasModel> listaRotas;

    public adapter_rotas(List<RotasModel> listaRotas) {
        this.listaRotas = listaRotas;
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
    public adapter_rotas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_rotas_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_rotas.ViewHolder holder, int position) {
        RotasModel rota = listaRotas.get(position);
        holder.textRua.setText(rota.getRua());
        holder.starBar.setRating(rota.getNota()); // Como nota é int e rating espera float, converte automaticamente
    }

    @Override
    public int getItemCount() {
        return listaRotas.size();
    }
}
