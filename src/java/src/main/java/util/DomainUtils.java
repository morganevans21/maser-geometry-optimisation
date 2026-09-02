package main.java.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.jenetics.BitChromosome;
import main.java.model.DomainAssignment;

public class DomainUtils {

    public static List<Integer> createSubgridDomains() {

        // Define the N×N subgrid domains
        // Exclude all the initial "Air" domains
        // Here, N=10. The domain IDs will need to be changed if using a different value
        // for N
        Set<Integer> excludedDomains = new HashSet<>(Arrays.asList(1, 2, 3, 4, 15, 106, 107, 108));
        // Include all dielectric (non-air) domains
        List<Integer> subgridDomains = new ArrayList<>();
        // From the smallest domain ID to the largest (will need to change if N!=10)
        for (int i = 1; i <= 108; i++) {
            if (!excludedDomains.contains(i)) {
                subgridDomains.add(i);
            }
        }

        return subgridDomains;
    }

    public static DomainAssignment extractDomainAssignments(
            BitChromosome chromosome,
            List<Integer> subgridDomains) {

        // Lists to store domain assignments
        List<Integer> dielectricDomains = new ArrayList<>();
        List<Integer> airDomains = new ArrayList<>();

        for (int i = 0; i < chromosome.length(); i++) {
            int domainID = subgridDomains.get(i); // Get correct domain ID

            if (chromosome.get(i).booleanValue()) { // If bit is 1, assign to Air
                airDomains.add(domainID);
            } else { // Otherwise, assign to Dielectric
                dielectricDomains.add(domainID);
            }
        }

        return new DomainAssignment(
                dielectricDomains,
                airDomains);
    }
}
