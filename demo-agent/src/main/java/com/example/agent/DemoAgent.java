package com.example.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DemoAgent {
    private static final String ARRAY_DELIMITER_PATTERN = "[;,]";

    private static final String TARGET_CLASS = "com.example.demo.Target";

    public static void premain(String arguments, Instrumentation instrumentation) {
        Map<String, String> args = createArgs(arguments);
        System.out.println("Arguments: " + args);

        AgentBuilder builder = new AgentBuilder.Default();
        builder = EventLogger.configure(builder);
        builder = Exporter.configure(builder);

        if (Objects.equals("RETRANSFORMATION", args.get("RedefinitionStrategy"))) {
            System.out.println("Using RedefinitionStrategy.RETRANSFORMATION");
            builder = builder.with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION);
        } else if (Objects.equals("DISABLED", args.get("RedefinitionStrategy"))) {
            System.out.println("Using RedefinitionStrategy.DISABLED");
            builder = builder.with(AgentBuilder.RedefinitionStrategy.DISABLED);
        }

        AgentBuilder.Transformer transformer = null;
        if (Objects.equals("advice", args.get("transformer"))) {
            System.out.println("Using AdviceTransformer");
            transformer = new AdviceTransformer();
        } else if (Objects.equals("delegation", args.get("transformer"))) {
            System.out.println("Using DelegationTransformer");
            transformer = new DelegationTransformer();
        }

        if (transformer != null) {
            builder = builder.type(ElementMatchers.named(TARGET_CLASS)).transform(transformer);
        }


        builder.installOn(instrumentation);
    }

    private static Map<String, String> createArgs(String args) {
        Map<String, String> result = new HashMap<>();
        if (args != null) {
            // Split the input string into parts using the semicolon as a delimiter.
            String[] parts = args.trim().split(ARRAY_DELIMITER_PATTERN);
            for (String arg : parts) {
                // Find the index of the equal sign to separate key and value.
                int index = arg.indexOf('=');
                if (index > 0) { // Ensure that there is a key before the equal sign.
                    // Extract the key and value, trimming any whitespace.
                    String key = arg.substring(0, index).trim();
                    String value = arg.substring(index + 1).trim();
                    // If the value is not empty, put the key-value pair into the map.
                    if (!value.isEmpty()) {
                        result.put(key, value);
                    }
                }
            }
        }
        // Return the map with the parsed arguments.
        return result;
    }


}
