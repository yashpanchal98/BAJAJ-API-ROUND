package com.bajaj_api_assignment.Bajaj.API.round.model;

public class SolutionRequest {
    private String finalQuery;

    public SolutionRequest() {}

    public SolutionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }

    @Override
    public String toString() {
        return "SolutionRequest{" +
                "finalQuery='" + finalQuery + '\'' +
                '}';
    }
}