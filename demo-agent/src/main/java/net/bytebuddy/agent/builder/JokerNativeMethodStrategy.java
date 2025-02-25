package net.bytebuddy.agent.builder;

import com.example.agent.JokerMethodNameTransformer;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class JokerNativeMethodStrategy implements AgentBuilder.Default.NativeMethodStrategy {

    private String nameTrait;

    public JokerNativeMethodStrategy(String nameTrait) {
        this.nameTrait = nameTrait;
    }

    @Override
    public MethodNameTransformer resolve() {
        return new JokerMethodNameTransformer(nameTrait);
    }

    @Override
    public void apply(Instrumentation instrumentation, ClassFileTransformer classFileTransformer) {
    }
}