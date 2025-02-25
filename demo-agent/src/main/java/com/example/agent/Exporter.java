package com.example.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.nullability.MaybeNull;
import net.bytebuddy.utility.nullability.NeverNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Exporter extends AgentBuilder.Listener.Adapter {

    private final File output;

    private final char innerClassSeparator;

    public Exporter(File output) {
        this(output, (char) 0);
    }

    public Exporter(File output, char innerClassSeparator) {
        this.output = output;
        this.innerClassSeparator = innerClassSeparator;
    }

    @Override
    public void onTransformation(@NeverNull TypeDescription typeDescription,
                                 @MaybeNull ClassLoader classLoader,
                                 @MaybeNull JavaModule javaModule,
                                 boolean b,
                                 @NeverNull DynamicType dynamicType) {
        try {
            if (dynamicType instanceof DynamicType.Default) {
                save((DynamicType.Default) dynamicType);
            } else {
                System.out.println(dynamicType);
                //dynamicType.saveIn(output);
            }
        } catch (IOException e) {
            System.out.println("failed to save class byte code. " + typeDescription.getTypeName());
        }
    }

    protected void save(DynamicType.Default type) throws IOException {
        for (Map.Entry<TypeDescription, byte[]> entry : type.getAllTypes().entrySet()) {
            save(entry.getKey(), entry.getValue());
        }
    }

    protected void save(TypeDescription typeDescription, byte[] bytes) throws IOException {
        File target = new File(output, getName(typeDescription));
        File directory = target.getParentFile();
        if (directory != null && !directory.isDirectory() && !directory.mkdirs()) {
            throw new IOException("Could not create directory: " + directory);
        }
        try (OutputStream outputStream = Files.newOutputStream(target.toPath())) {
            outputStream.write(bytes);
            outputStream.flush();
        }
    }

    protected String getName(TypeDescription typeDescription) {
        String result = typeDescription.getName().replace('.', File.separatorChar) + ".class";
        if (innerClassSeparator > 0) {
            result = result.replace('$', innerClassSeparator);
        }
        return result;
    }

    public static AgentBuilder configure(AgentBuilder agentBuilder) {
        File output = null;
        try {
            Path path = Files.createTempDirectory(Paths.get("."), "class-output-");
            output = path.toFile();
        } catch (Exception e) {
            System.out.println("failed to create temp directory for class output." + e);
        }
        if (output != null) {
            agentBuilder = agentBuilder.with(new Exporter(output));
        }
        return agentBuilder;
    }

}
