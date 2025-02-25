package com.example.agent;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.utility.RandomString;

/**
 * Generate predicated auxiliary type name for delegate method.
 */
public class JokerAuxiliaryTypeNamingStrategy implements AuxiliaryType.NamingStrategy {
    private static final String DEFAULT_SUFFIX = "auxiliary$";
    private String suffix;

    public JokerAuxiliaryTypeNamingStrategy(String nameTrait) {
        this.suffix = nameTrait + DEFAULT_SUFFIX;
    }

    @Override
    public String name(TypeDescription instrumentedType, AuxiliaryType auxiliaryType) {
        // Auxiliary type name pattern: <origin_class_name>$<name_trait>$auxiliary$<auxiliary_type_instance_hash>
        return instrumentedType.getName() + "$" + suffix + RandomString.hashOf(auxiliaryType.hashCode());
    }

}
