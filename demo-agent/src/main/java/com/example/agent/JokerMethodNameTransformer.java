package com.example.agent;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;
import net.bytebuddy.utility.RandomString;

/**
 * Generate fixed origin method name with method description hash code
 */
public class JokerMethodNameTransformer implements MethodNameTransformer {

    private static final String DEFAULT_PREFIX = "original$";

    private String prefix;

    public JokerMethodNameTransformer(String nameTrait) {
        this.prefix = nameTrait + DEFAULT_PREFIX;
    }

    @Override
    public String transform(MethodDescription methodDescription) {
        // Origin method rename pattern: <name_trait>$original$<method_name>$<method_description_hash>
        return prefix + methodDescription.getInternalName() + "$" + RandomString.hashOf(methodDescription.toString().hashCode());
    }

}
