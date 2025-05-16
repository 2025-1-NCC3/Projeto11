package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import br.fecap.pi.saferide_passageiro.models.RotaModel;

// RouteDTO.java
public class CalcularRotaResponseDTO {
    @SerializedName("routes")
    private ArrayList<RotaModel> routes;

    public CalcularRotaResponseDTO(ArrayList<RotaModel> routes) {
        this.routes = routes;
    }

    public ArrayList<RotaModel> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<RotaModel> routes) {
        this.routes = routes;
    }
}
