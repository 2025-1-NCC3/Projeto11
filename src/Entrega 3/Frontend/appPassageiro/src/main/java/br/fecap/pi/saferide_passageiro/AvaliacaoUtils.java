package br.fecap.pi.saferide_passageiro;


import java.util.List;

import br.fecap.pi.saferide_passageiro.dto.AvaliacoesRotaResponseDTO;

public class AvaliacaoUtils {
    
    /**
     * Calcula a média das avaliações de uma rota
     * @param avaliacoes Lista de avaliações da rota
     * @return Média das avaliações (0-5)
     */
    public static float calcularMediaAvaliacao(List<AvaliacoesRotaResponseDTO> avaliacoes) {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0;
        }
        
        float soma = 0;
        for (AvaliacoesRotaResponseDTO avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        
        return soma / avaliacoes.size();
    }
    
    /**
     * Determina o tipo de feedback baseado na nota
     * @param nota Nota da avaliação (0-5)
     * @return Tipo de feedback (positivo, neutro, negativo ou sem avaliação)
     */
    public static String getTipoFeedback(int nota) {
        if (nota >= 4) {
            return "positivo";
        } else if (nota == 3) {
            return "neutro";
        } else if (nota > 0) {
            return "negativo";
        } else {
            return "sem_avaliacao";
        }
    }
}
