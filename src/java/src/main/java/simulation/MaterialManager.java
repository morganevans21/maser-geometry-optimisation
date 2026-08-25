package main.java.simulation;

import java.util.List;

import com.comsol.model.Model;

import main.java.io.jenetics.BitChromosome;

public class MaterialManager {

    public static void assignMaterial(
            Model model,
            int domainID,
            boolean air) {

        if (air) {

            model.component("mod1")
                    .material("matDielec")
                    .selection()
                    .remove(domainID);

            model.component("mod1")
                    .material("matAir")
                    .selection()
                    .add(domainID);

        } else {

            model.component("mod1")
                    .material("matAir")
                    .selection()
                    .remove(domainID);

            model.component("mod1")
                    .material("matDielec")
                    .selection()
                    .add(domainID);
        }
    }

    public static void updateMaterialAssignments(
            Model model,
            BitChromosome chromosome,
            List<Integer> subgridDomains) {

        // Update material assignments for the subgrid
        for (int i = 0; i < chromosome.length(); i++) {
            int domainID = subgridDomains.get(i); // Get correct domain ID

            assignMaterial(
                    model,
                    domainID,
                    chromosome.get(i).bit());
        }
    }
}
