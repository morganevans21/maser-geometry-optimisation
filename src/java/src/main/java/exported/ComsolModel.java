package main.java.exported;

/*
 * ComsolModel.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on May 23 2025, 18:56 by COMSOL 6.0.0.405. */
public class ComsolModel {

    public static Model run() {
        Model model = ModelUtil.create("Model");

        model.modelPath("quantum-device-optimisation/java/src/exported");

        model.label("ComsolModel.mph");

        model.param().set("h1", "2.6[mm]");
        model.param().set("h2", "8.7[mm]");
        model.param().set("h3", "2.6[mm]");
        model.param().set("r1", "2.05[mm]");
        model.param().set("r2", "6.1[mm]");
        model.param().set("r3", "11[mm]");
        model.param().set("h2_sub", "h2/10");
        model.param().set("r2_sub", "(r2-r1)/10");

        model.component().create("mod1", false);

        model.component("mod1").geom().create("geom", 2);

        model.result().table().create("tbl1", "Table");

        model.component("mod1").geom("geom").axisymmetric(true);

        model.component("mod1").mesh().create("mesh1");

        model.component("mod1").geom("geom").create("blk11", "Rectangle");
        model.component("mod1").geom("geom").feature("blk11").set("pos", new int[] { 0, 0 });
        model.component("mod1").geom("geom").feature("blk11").set("size", new String[] { "r1", "h3" });
        model.component("mod1").geom("geom").create("blk12", "Rectangle");
        model.component("mod1").geom("geom").feature("blk12").set("pos", new String[] { "r1", "0" });
        model.component("mod1").geom("geom").feature("blk12").set("size", new String[] { "r2-r1", "h3" });
        model.component("mod1").geom("geom").create("blk13", "Rectangle");
        model.component("mod1").geom("geom").feature("blk13").set("pos", new String[] { "r2", "0" });
        model.component("mod1").geom("geom").feature("blk13").set("size", new String[] { "r3-r2", "h3" });
        model.component("mod1").geom("geom").create("blk21", "Rectangle");
        model.component("mod1").geom("geom").feature("blk21").set("pos", new String[] { "0", "h3" });
        model.component("mod1").geom("geom").feature("blk21").set("size", new String[] { "r1", "h2" });
        model.component("mod1").geom("geom").create("blk23", "Rectangle");
        model.component("mod1").geom("geom").feature("blk23").set("pos", new String[] { "r2", "h3" });
        model.component("mod1").geom("geom").feature("blk23").set("size", new String[] { "r3-r2", "h2" });
        model.component("mod1").geom("geom").create("blk31", "Rectangle");
        model.component("mod1").geom("geom").feature("blk31").set("pos", new String[] { "0", "h3+h2" });
        model.component("mod1").geom("geom").feature("blk31").set("size", new String[] { "r1", "h1" });
        model.component("mod1").geom("geom").create("blk32", "Rectangle");
        model.component("mod1").geom("geom").feature("blk32").set("pos", new String[] { "r1", "h3+h2" });
        model.component("mod1").geom("geom").feature("blk32").set("size", new String[] { "r2-r1", "h1" });
        model.component("mod1").geom("geom").create("blk33", "Rectangle");
        model.component("mod1").geom("geom").feature("blk33").set("pos", new String[] { "r2", "h3+h2" });
        model.component("mod1").geom("geom").feature("blk33").set("size", new String[] { "r3-r2", "h1" });
        model.component("mod1").geom("geom").create("sub0000", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0000")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0000").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0001", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0001")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0001").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0002", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0002")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0002").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0003", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0003")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0003").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0004", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0004")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0004").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0005", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0005")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0005").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0006", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0006")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0006").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0007", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0007")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0007").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0008", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0008")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0008").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0009", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0009")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 0*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0009").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0100", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0100")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0100").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0101", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0101")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0101").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0102", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0102")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0102").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0103", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0103")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0103").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0104", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0104")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0104").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0105", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0105")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0105").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0106", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0106")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0106").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0107", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0107")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0107").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0108", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0108")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0108").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0109", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0109")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 1*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0109").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0200", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0200")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0200").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0201", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0201")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0201").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0202", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0202")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0202").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0203", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0203")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0203").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0204", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0204")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0204").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0205", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0205")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0205").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0206", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0206")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0206").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0207", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0207")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0207").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0208", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0208")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0208").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0209", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0209")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 2*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0209").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0300", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0300")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0300").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0301", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0301")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0301").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0302", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0302")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0302").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0303", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0303")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0303").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0304", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0304")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0304").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0305", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0305")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0305").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0306", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0306")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0306").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0307", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0307")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0307").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0308", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0308")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0308").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0309", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0309")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 3*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0309").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0400", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0400")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0400").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0401", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0401")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0401").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0402", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0402")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0402").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0403", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0403")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0403").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0404", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0404")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0404").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0405", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0405")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0405").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0406", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0406")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0406").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0407", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0407")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0407").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0408", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0408")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0408").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0409", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0409")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 4*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0409").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0500", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0500")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0500").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0501", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0501")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0501").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0502", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0502")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0502").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0503", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0503")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0503").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0504", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0504")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0504").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0505", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0505")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0505").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0506", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0506")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0506").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0507", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0507")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0507").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0508", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0508")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0508").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0509", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0509")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 5*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0509").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0600", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0600")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0600").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0601", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0601")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0601").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0602", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0602")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0602").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0603", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0603")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0603").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0604", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0604")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0604").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0605", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0605")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0605").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0606", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0606")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0606").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0607", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0607")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0607").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0608", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0608")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0608").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0609", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0609")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 6*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0609").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0700", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0700")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0700").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0701", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0701")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0701").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0702", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0702")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0702").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0703", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0703")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0703").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0704", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0704")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0704").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0705", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0705")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0705").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0706", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0706")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0706").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0707", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0707")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0707").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0708", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0708")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0708").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0709", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0709")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 7*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0709").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0800", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0800")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0800").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0801", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0801")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0801").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0802", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0802")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0802").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0803", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0803")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0803").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0804", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0804")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0804").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0805", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0805")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0805").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0806", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0806")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0806").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0807", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0807")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0807").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0808", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0808")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0808").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0809", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0809")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 8*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0809").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0900", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0900")
                .set("pos", new String[] { "r1 + 0*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0900").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0901", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0901")
                .set("pos", new String[] { "r1 + 1*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0901").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0902", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0902")
                .set("pos", new String[] { "r1 + 2*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0902").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0903", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0903")
                .set("pos", new String[] { "r1 + 3*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0903").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0904", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0904")
                .set("pos", new String[] { "r1 + 4*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0904").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0905", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0905")
                .set("pos", new String[] { "r1 + 5*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0905").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0906", "Rectangle");

        return model;
    }

    public static Model run2(Model model) {
        model.component("mod1").geom("geom").feature("sub0906")
                .set("pos", new String[] { "r1 + 6*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0906").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0907", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0907")
                .set("pos", new String[] { "r1 + 7*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0907").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0908", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0908")
                .set("pos", new String[] { "r1 + 8*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0908").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").create("sub0909", "Rectangle");
        model.component("mod1").geom("geom").feature("sub0909")
                .set("pos", new String[] { "r1 + 9*r2_sub", "h3 + 9*h2_sub" });
        model.component("mod1").geom("geom").feature("sub0909").set("size", new String[] { "r2_sub", "h2_sub" });
        model.component("mod1").geom("geom").run();
        model.component("mod1").geom("geom").run("fin");

        model.component("mod1").material().create("matDielec", "Common");
        model.component("mod1").material().create("matAir", "Common");
        model.component("mod1").material().create("matCopper", "Common");
        model.component("mod1").material("matAir").selection().set(1, 2, 3, 4, 15, 106, 107, 108);
        model.component("mod1").material("matCopper").selection().geom("geom", 1);
        model.component("mod1").material("matCopper").selection().set(2, 7, 9, 32, 223, 237, 238, 239, 240);

        model.component("mod1").physics().create("emw", "ElectromagneticWaves", "geom");
        model.component("mod1").physics("emw").create("imp1", "Impedance", 1);
        model.component("mod1").physics("emw").feature("imp1").selection().all();

        model.component("mod1").mesh("mesh1").autoMeshSize(9);

        model.result().table("tbl1").label("Purcell");
        model.result().table("tbl1").comments("Eigenfrequencies (emw)");

        model.component("mod1").view("view1").axis().set("xmin", -0.006544854026287794);
        model.component("mod1").view("view1").axis().set("xmax", 0.017544854432344437);
        model.component("mod1").view("view1").axis().set("ymin", -0.0036436663940548897);
        model.component("mod1").view("view1").axis().set("ymax", 0.017543666064739227);

        model.component("mod1").material("matDielec").label("Strontium Titanate");
        model.component("mod1").material("matDielec").propertyGroup("def")
                .set("relpermittivity", new String[] { "316.3-j*0.0333378019259766", "0", "0", "0",
                        "316.3-j*0.0333378019259766", "0", "0", "0", "316.3-j*0.0333378019259766" });
        model.component("mod1").material("matDielec").propertyGroup("def")
                .set("electricconductivity",
                        new String[] { "0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]" });
        model.component("mod1").material("matDielec").propertyGroup("def")
                .set("relpermeability", new String[] { "1", "0", "0", "0", "1", "0", "0", "0", "1" });
        model.component("mod1").material("matAir").label("Air");
        model.component("mod1").material("matAir").propertyGroup("def")
                .set("relpermittivity", new String[] { "1", "0", "0", "0", "1", "0", "0", "0", "1" });
        model.component("mod1").material("matAir").propertyGroup("def")
                .set("electricconductivity",
                        new String[] { "0[S/m]", "0", "0", "0", "0[S/m]", "0", "0", "0", "0[S/m]" });
        model.component("mod1").material("matAir").propertyGroup("def")
                .set("relpermeability", new String[] { "1", "0", "0", "0", "1", "0", "0", "0", "1" });
        model.component("mod1").material("matCopper").label("Copper");
        model.component("mod1").material("matCopper").propertyGroup("def")
                .set("relpermittivity", new String[] { "1", "0", "0", "0", "1", "0", "0", "0", "1" });
        model.component("mod1").material("matCopper").propertyGroup("def")
                .set("electricconductivity",
                        new String[] { "5.998e7[S/m]", "0", "0", "0", "5.998e7[S/m]", "0", "0", "0", "5.998e7[S/m]" });
        model.component("mod1").material("matCopper").propertyGroup("def")
                .set("relpermeability", new String[] { "1", "0", "0", "0", "1", "0", "0", "0", "1" });

        model.study().create("std1");
        model.study("std1").create("eig", "Eigenfrequency");

        model.sol().create("sol1");
        model.sol("sol1").study("std1");
        model.sol("sol1").attach("std1");
        model.sol("sol1").create("st1", "StudyStep");
        model.sol("sol1").create("v1", "Variables");
        model.sol("sol1").create("e1", "Eigenvalue");
        model.sol("sol1").feature("e1").create("d1", "Direct");

        model.result().dataset().create("rev1", "Revolve2D");
        model.result().numerical().create("gev1", "EvalGlobal");
        model.result().numerical().create("int1", "IntVolume");
        model.result().numerical().create("max1", "MaxVolume");
        model.result().numerical("gev1").set("probetag", "none");
        model.result().numerical("int1").set("probetag", "none");
        model.result().numerical("max1").set("probetag", "none");
        model.result().create("pg1", "PlotGroup2D");
        model.result("pg1").create("surf1", "Surface");

        model.study("std1").feature("eig").set("neigs", 1);
        model.study("std1").feature("eig").set("neigsactive", true);
        model.study("std1").feature("eig").set("shift", "1.45[GHz]");
        model.study("std1").feature("eig").set("ngen", 1);
        model.study("std1").feature("eig").set("ngenactive", false);

        model.sol("sol1").attach("std1");
        model.sol("sol1").feature("st1").label("Compile Equations: Eigenfrequency");
        model.sol("sol1").feature("v1").label("Dependent Variables 1.1");
        model.sol("sol1").feature("e1").label("Eigenvalue Solver 1.1");
        model.sol("sol1").feature("e1").set("transform", "eigenfrequency");
        model.sol("sol1").feature("e1").set("neigs", 1);
        model.sol("sol1").feature("e1").set("shift", "1.45[GHz]");
        model.sol("sol1").feature("e1").set("eigref", "1.45[GHz]");
        model.sol("sol1").feature("e1").feature("dDef").label("Direct 2");
        model.sol("sol1").feature("e1").feature("aDef").label("Advanced 1");
        model.sol("sol1").feature("e1").feature("aDef").set("complexfun", true);
        model.sol("sol1").feature("e1").feature("d1").label("Suggested Direct Solver (emw)");
        model.sol("sol1").feature("e1").feature("d1").set("linsolver", "pardiso");
        model.sol("sol1").runAll();

        model.result().dataset("rev1").label("Revolution 2D");
        model.result().dataset("rev1").set("startangle", -90);
        model.result().dataset("rev1").set("revangle", 225);
        model.result().numerical("gev1").label("Eigenfrequencies (emw)");
        model.result().numerical("gev1").set("looplevelinput", new String[] { "first" });
        model.result().numerical("gev1").set("table", "tbl1");
        model.result().numerical("gev1").set("expr", new String[] { "emw.freq", "emw.Qfactor" });
        model.result().numerical("gev1").set("unit", new String[] { "GHz", "1" });
        model.result().numerical("gev1").set("descr", new String[] { "Frequency", "Quality factor" });
        model.result().numerical("int1").label("Vm Num");
        model.result().numerical("int1").set("looplevelinput", new String[] { "first" });
        model.result().numerical("int1").set("table", "tbl1");
        model.result().numerical("int1").set("expr", new String[] { "emw.normH*emw.normH" });
        model.result().numerical("int1").set("unit", new String[] { "m*A^2" });
        model.result().numerical("int1").set("descr", new String[] { "" });
        model.result().numerical("max1").label("Vm Den");
        model.result().numerical("max1").set("looplevelinput", new String[] { "first" });
        model.result().numerical("max1").set("table", "tbl1");
        model.result().numerical("max1").set("expr", new String[] { "emw.normH*emw.normH" });
        model.result().numerical("max1").set("unit", new String[] { "A^2/m^2" });
        model.result().numerical("max1").set("descr", new String[] { "" });
        model.result().numerical("gev1").setResult();
        model.result().numerical("int1").appendResult();
        model.result().numerical("max1").appendResult();
        model.result("pg1").label("Electric Field (emw)");
        model.result("pg1").set("frametype", "spatial");
        model.result("pg1").set("showlegendsmaxmin", true);
        model.result("pg1").feature("surf1").label("Surface");
        model.result("pg1").feature("surf1").set("colortable", "RainbowLight");
        model.result("pg1").feature("surf1").set("smooth", "internal");
        model.result("pg1").feature("surf1").set("resolution", "normal");

        return model;
    }

    public static void main(String[] args) {
        Model model = run();
        run2(model);
    }

}
