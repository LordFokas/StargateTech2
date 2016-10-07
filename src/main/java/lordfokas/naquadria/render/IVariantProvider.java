package lordfokas.naquadria.render;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IVariantProvider {

    void addVariants(List<Pair<Integer, String>> variants);
}
