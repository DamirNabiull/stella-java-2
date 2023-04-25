package org.stella.helpers;

import org.stella.utils.ExceptionsUtils;

import java.util.List;

public class SupportedExtensions {
    private static final List<String> SupportedExtensionsList = List.of(
            "#arithmetic-operators",
            "#comparison-operations",
            "#unit-type",
            "#pairs",
            "#tuples",
            "#sum-types",
            "#variants",
            "#records",
            "#fixpoint-combinator",
            "#general-recursion",
            "#type-aliases",
            "#structural-patterns",
            "#multiparameter-functions",
            "#nested-function-declarations",
            "#sequencing",
            "#references",
            "#panic",
            "#structural-subtyping",
            "#top-type",
            "#bottom-type"
    );

    public static void Support(String extensionName){
        if (!SupportedExtensionsList.contains(extensionName))
            ExceptionsUtils.throwExtensionException(extensionName);
    }
}
