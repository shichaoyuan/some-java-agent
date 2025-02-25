package net.bytebuddy.implementation;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.TypeInitializer;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.utility.RandomString;

/**
 * Support custom suffix name trait, using in cache value field name, field getter/setter delegation, accessor method and so on.
 */
public class JokerImplementationContextFactory implements Implementation.Context.Factory {

    private String suffixNameTrait;

    public JokerImplementationContextFactory(String suffixNameTrait) {
        this.suffixNameTrait = suffixNameTrait;
    }

    @Override
    public Implementation.Context.ExtractableView make(TypeDescription instrumentedType,
                                                       AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy,
                                                       TypeInitializer typeInitializer,
                                                       ClassFileVersion classFileVersion,
                                                       ClassFileVersion auxiliaryClassFileVersion) {
        return this.make(instrumentedType, auxiliaryTypeNamingStrategy, typeInitializer, classFileVersion,
                auxiliaryClassFileVersion, Implementation.Context.FrameGeneration.GENERATE);
    }

    @Override
    public Implementation.Context.ExtractableView make(TypeDescription instrumentedType,
                                                       AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy,
                                                       TypeInitializer typeInitializer,
                                                       ClassFileVersion classFileVersion,
                                                       ClassFileVersion auxiliaryClassFileVersion,
                                                       Implementation.Context.FrameGeneration frameGeneration) {
        // Method cache value field pattern: cachedValue$<name_trait>$<origin_class_name_hash>$<field_value_hash>
        // Accessor method name pattern: <renamed_origin_method>$accessor$<name_trait>$<origin_class_name_hash>
        return new Implementation.Context.Default(instrumentedType, classFileVersion, auxiliaryTypeNamingStrategy,
                typeInitializer, auxiliaryClassFileVersion, frameGeneration,
                suffixNameTrait + RandomString.hashOf(instrumentedType.getName().hashCode()));
    }
}
