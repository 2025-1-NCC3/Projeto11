package br.fecap.pi.saferide_passageiro.utils;

import java.util.List;

import br.fecap.pi.saferide_passageiro.dto.AvaliacoesRotaResponseDTO;

public class AvaliacaoUtils {
    public static int calcularMediaAvaliacao(List<AvaliacoesRotaResponseDTO> avaliacoes) {
        int somaNotaAvaliacoes = 0;

        for (AvaliacoesRotaResponseDTO avaliacao : avaliacoes) {
            somaNotaAvaliacoes += avaliacao.getNota();
        }

        return somaNotaAvaliacoes / avaliacoes.size();
    }
}
