package com.example.agent;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class DelegationInterceptor {
    @RuntimeType
    public static Object intercept(
            @SuperCall Callable<?> zuper,
            @Origin final Method method,
            @AllArguments Object[] args) throws Exception {
        String delegationKey = "delegationKey_" + ThreadLocalRandom.current().nextInt();
        System.out.println("onEnter: " + delegationKey);
        try {
            zuper.call();
            return "intercept-" + delegationKey;
        } finally {
            System.out.println("onExit: " + delegationKey);
        }
    }
}
