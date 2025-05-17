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

import java.util.ArrayList;
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
        LinearLayout avaliacoesContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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

        // Configurar o nome da rua
        holder.textRua.setText(rota.getDescricao());

        // Simular uma avaliação de nota 5 (forçando para ver se tudo funciona)
        List<AvaliacoesRotaResponseDTO> avaliacoesFalsas = new ArrayList<>();
        AvaliacoesRotaResponseDTO avaliacaoFake = new AvaliacoesRotaResponseDTO();
        avaliacaoFake.setNota(5);
        avaliacaoFake.setComentario("Excelente motorista! Viagem segura.");
        avaliacoesFalsas.add(avaliacaoFake);
        rota.setAvaliacoes(avaliacoesFalsas); // substitui as avaliações reais

    // Mostra a média com base nas avaliações falsas
        holder.starBar.setRating(AvaliacaoUtils.calcularMediaAvaliacao(rota.getAvaliacoes()));

    //        // Configurar a média de avaliações na barra de estrelas
//            if (rota.getAvaliacoes() != null) {
//                holder.starBar.setRating(AvaliacaoUtils.calcularMediaAvaliacao(rota.getAvaliacoes()));
//            }


        // Limpar o container de avaliações antes de adicionar novas
        holder.avaliacoesContainer.removeAllViews();

        // Adicionar cada avaliação ao container
        if (rota.getAvaliacoes() != null && !rota.getAvaliacoes().isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(context);

            for (AvaliacoesRotaResponseDTO avaliacao : rota.getAvaliacoes()) {
                View avaliacaoView = inflater.inflate(R.layout.item_avaliacao_layout, holder.avaliacoesContainer, false);

                // Configurar o texto da avaliação (feedbacks ou comentário)
                TextView textoAvaliacao = avaliacaoView.findViewById(R.id.textoAvaliacao);

                if (avaliacao.getFeedbacks() != null && !avaliacao.getFeedbacks().isEmpty()) {
                    StringBuilder feedbacksTexto = new StringBuilder();
                    for (AvaliacoesRotaResponseDTO.FeedbackDTO feedback : avaliacao.getFeedbacks()) {
                        feedbacksTexto.append("- ").append(feedback.getDescricao()).append("\n");
                    }
                    textoAvaliacao.setText(feedbacksTexto.toString().trim());
                } else {
                    textoAvaliacao.setText(avaliacao.getComentario());
                }

                // Configurar a cor do indicador baseado na nota
                View indicadorAvaliacao = avaliacaoView.findViewById(R.id.indicadorAvaliacao);

                int nota = avaliacao.getNota();
                int backgroundResId;

                if (nota >= 4) {
                    backgroundResId = R.drawable.circle_background_positive;
                } else if (nota == 3) {
                    backgroundResId = R.drawable.circle_background_neutral;
                } else if (nota > 0) {
                    backgroundResId = R.drawable.circle_background_negative;
                } else {
                    backgroundResId = R.drawable.circle_background_gray;
                }

                indicadorAvaliacao.setBackgroundResource(backgroundResId);

                // Adicionar a view de avaliação ao container
                holder.avaliacoesContainer.addView(avaliacaoView);
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
