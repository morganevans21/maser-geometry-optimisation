package main.java.simulation;

import com.comsol.model.Model;
import com.comsol.model.util.ModelUtil;

import main.java.exported.ComsolModel;

public class ModelInitialiser {

    public static Model initialiseModel() {

        // Run COMSOL in standalone mode (so COMSOL GUI won't open)
        ModelUtil.initStandalone(false);

        // Run one COMSOL model
        Model model = ComsolModel.run();
        ComsolModel.run2(model);

        return model;
    }
}
