package br.com.fecapccp.uber_saferide.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import br.com.fecapccp.uber_saferide.R;
import br.com.fecapccp.uber_saferide.ViagemAceitaActivity;
import br.com.fecapccp.uber_saferide.model.IniciarViagemModel;

public class adapter_IniciarViagem extends RecyclerView.Adapter<adapter_IniciarViagem.CorridaViewHolder> {

    private List<IniciarViagemModel> listaCorridas;
    private Context context;

    public adapter_IniciarViagem(Context context, List<IniciarViagemModel> listaCorridas) {
        this.listaCorridas = listaCorridas;
        this.context = context;
    }

    @NonNull
    @Override
    public CorridaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_corrida, parent, false);
        return new CorridaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CorridaViewHolder holder, int position) {
        IniciarViagemModel IniciarViagemModel = listaCorridas.get(position);

        holder.txtMensagem.setText(IniciarViagemModel.getNomePassageiro() + " solicitou uma corrida");
        holder.txtTempo.setText(IniciarViagemModel.getTempoEstimado());

        holder.btnComecar.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViagemAceitaActivity.class);
            intent.putExtra("id_corrida", IniciarViagemModel.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaCorridas.size();
    }

    public static class CorridaViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensagem, txtTempo;
        Button btnComecar;

        public CorridaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensagem = itemView.findViewById(R.id.txtMensagem);
            txtTempo = itemView.findViewById(R.id.txtTempo);
            btnComecar = itemView.findViewById(R.id.btnComecar);
        }
    }
}

