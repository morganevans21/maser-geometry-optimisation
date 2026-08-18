package main.java.model;

import java.util.List;

public final class DomainAssignment {

    public final List<Integer> dielectricDomains;
    public final List<Integer> airDomains;

    public DomainAssignment(
            List<Integer> dielectricDomains,
            List<Integer> airDomains) {

        this.dielectricDomains = dielectricDomains;
        this.airDomains = airDomains;
    }
}
