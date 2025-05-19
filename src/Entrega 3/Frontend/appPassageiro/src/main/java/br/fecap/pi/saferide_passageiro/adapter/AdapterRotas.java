package br.fecap.pi.saferide_passageiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.dto.AvaliacoesRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.models.FeedbackModel;
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
        TextView textNumeracaoRota;
        TextView textRua;
        MaterialRatingBar starBar;
        LinearLayout avaliacoesContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNumeracaoRota = itemView.findViewById(R.id.textNumeracaoRota);
            textRua = itemView.findViewById(R.id.textRua);
            starBar = itemView.findViewById(R.id.starBarAdapter);
            avaliacoesContainer = itemView.findViewById(R.id.avaliacoesContainer);
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
        Context context = holder.itemView.getContext();
        MaterialRatingBar starBar = holder.itemView.findViewById(R.id.starBarAdapter);

        int notaAvaliacaoRota = rota.getAvaliacoes() != null ? AvaliacaoUtils.calcularMediaAvaliacao(rota.getAvaliacoes()) : 0;
        starBar.setRating(notaAvaliacaoRota);

        // Configurar o nome da rua
        holder.textNumeracaoRota.setText("Rota " + (position + 1));
        holder.textRua.setText(rota.getDescricao());

        // Limpar o container de avaliações antes de adicionar novas
        holder.avaliacoesContainer.removeAllViews();

        // Adicionar cada avaliação ao container
        if (rota.getAvaliacoes() != null && !rota.getAvaliacoes().isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(context);

            List<FeedbackModel> listaFeedbacks = rota.getFeedbacks();
            listaFeedbacks.sort(Comparator.comparing(FeedbackModel::getFrequencia).reversed());

            int limitCounter = 0;

            for (FeedbackModel feedback : listaFeedbacks) {
                if (limitCounter == 4) break;

                View avaliacaoView = inflater.inflate(R.layout.item_avaliacao_layout, holder.avaliacoesContainer, false);

                // Configurar o texto da avaliação (feedbacks ou comentário)
                TextView textoAvaliacao = avaliacaoView.findViewById(R.id.textoAvaliacao);

                textoAvaliacao.setText(feedback.getDescricao() + " - " + feedback.getFrequencia());

                // Configurar a cor do indicador baseado na nota
                View indicadorAvaliacao = avaliacaoView.findViewById(R.id.indicadorAvaliacao);

                int backgroundResId;

                switch(feedback.getCategoria()) {
                    case "Positivo":
                        backgroundResId = R.drawable.circle_background_positive;
                        break;
                    case "Negativo":
                        backgroundResId = R.drawable.circle_background_negative;
                        break;
                    case "Neutro":
                        backgroundResId = R.drawable.circle_background_neutral;
                        break;
                    default:
                        backgroundResId = R.drawable.circle_background_gray;
                        break;
                }

//                if (notaAvaliacaoRota >= 4) {
//                    backgroundResId = R.drawable.circle_background_positive;
//                } else if (notaAvaliacaoRota == 3) {
//                    backgroundResId = R.drawable.circle_background_neutral;
//                } else if (notaAvaliacaoRota > 0) {
//                    backgroundResId = R.drawable.circle_background_negative;
//                } else {
//                    backgroundResId = R.drawable.circle_background_gray;
//                }

                indicadorAvaliacao.setBackgroundResource(backgroundResId);

                // Adicionar a view de avaliação ao container
                holder.avaliacoesContainer.addView(avaliacaoView);
                limitCounter++;
            }
        } else {
            // Se não houver avaliações, exibir uma mensagem
            TextView semAvaliacoes = new TextView(context);
            semAvaliacoes.setText("Nenhuma avaliação disponível");
            semAvaliacoes.setPadding(8, 8, 8, 8);
            holder.avaliacoesContainer.addView(semAvaliacoes);
        }

        // Configurar o clique no item
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
