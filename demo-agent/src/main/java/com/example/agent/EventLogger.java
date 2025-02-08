package com.example.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.nullability.MaybeNull;
import net.bytebuddy.utility.nullability.NeverNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class EventLogger implements AgentBuilder.Listener {
    public void onDiscovery(@NeverNull String typeName,
                            @MaybeNull ClassLoader classLoader,
                            @MaybeNull JavaModule module,
                            boolean loaded) {
//        if (logger.isDebugEnabled()) {
//            String message = String.format("ByteKit discovery %s [%s, %s, %s, loaded=%b]",
//                    typeName, getName(classLoader), module, Thread.currentThread(), loaded);
//            logger.debug(message);
//        }
    }

    @Override
    public void onTransformation(@NeverNull TypeDescription typeDescription,
                                 @MaybeNull ClassLoader classLoader,
                                 @MaybeNull JavaModule module,
                                 boolean loaded,
                                 @NeverNull DynamicType dynamicType) {
        String message = String.format("ByteKit transform %s [%s, %s, %s, loaded=%b]",
                    typeDescription.getName(), getName(classLoader), module, Thread.currentThread(), loaded);
        System.out.println(message);
    }

    @Override
    public void onIgnored(@NeverNull TypeDescription typeDescription,
                          @MaybeNull ClassLoader classLoader,
                          @MaybeNull JavaModule module,
                          boolean loaded) {
//        if (logger.isDebugEnabled()) {
//            String message = String.format("ByteKit ignore %s [%s, %s, %s, loaded=%b]",
//                    typeDescription.getName(), getName(classLoader), module, Thread.currentThread(), loaded);
//            logger.debug(message);
//        }
    }

    @Override
    public void onError(@NeverNull String typeName,
                        @MaybeNull ClassLoader classLoader,
                        @MaybeNull JavaModule module,
                        boolean loaded,
                        @NeverNull Throwable throwable) {
        OutputStream bos = new ByteArrayOutputStream(1024);
        PrintStream printStream = new PrintStream(bos);
        printStream.printf("ByteKit error %s [%s, %s, %s, loaded=%b]",
                typeName, getName(classLoader), module, Thread.currentThread(), loaded);
        throwable.printStackTrace(printStream);
        System.out.println(bos.toString());
    }

    @Override
    public void onComplete(@NeverNull String typeName,
                           @MaybeNull ClassLoader classLoader,
                           @MaybeNull JavaModule module,
                           boolean loaded) {
//        if (logger.isDebugEnabled()) {
//            String message = String.format("ByteKit complete %s [%s, %s, %s, loaded=%b]",
//                    typeName, getName(classLoader), module, Thread.currentThread(), loaded);
//            logger.debug(message);
//        }
    }

    protected String getName(ClassLoader classLoader) {
        if (classLoader == null) {
            return "";
        }
        return classLoader.getClass().getSimpleName();
    }

    public static AgentBuilder configure(AgentBuilder agentBuilder) {
        return agentBuilder.with(new EventLogger());
    }

}
