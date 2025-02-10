package com.example.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadLocalRandom;

public class MemberMethodAdvice {

    private MemberMethodAdvice() {
    }

    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static boolean onEnter(@Advice.Origin Class<?> type,
                                  @Advice.This(typing = Assigner.Typing.DYNAMIC) Object target,
                                  @Advice.Origin Method method,
                                  @Advice.Origin("#t\\##m#s") String methodDesc,
                                  @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] arguments,
                                  @Advice.Local(value = "_ADVICE_KEY_$JOYLIVE_LOCAL") String adviceKey
    ) throws Throwable {
        adviceKey = "adviceKey_" + ThreadLocalRandom.current().nextInt();
        System.out.println("onEnter: " + adviceKey);
        return false;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void onExit(@Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object result,
                              @Advice.Thrown(readOnly = false) Throwable throwable,
                              @Advice.Local(value = "_ADVICE_KEY_$JOYLIVE_LOCAL") String adviceKey
    ) throws Throwable {
        System.out.println("onExit: " + adviceKey);
        result = "advice-" + adviceKey;
    }
}
