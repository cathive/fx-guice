/*
 * Copyright (C) 2012 The Cat Hive Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cathive.fx.guice.thread;

import javafx.application.Platform;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.cathive.fx.guice.FxApplicationThread;

/**
 * This class can be used as an AOP interceptor in conjunction with the
 * {@link FxApplicationThread} annotation. It enforces method calls on the
 * JavaFX Application Thread.
 * <p>Basically, this class offers some wrapper functionality around
 * the {@link Platform#runLater(Runnable) runLater()-Method} provided by
 * the JavaFX Platform class.</p>
 * 
 * @see FxApplicationThread
 * @see Platform#runLater(Runnable)
 * 
 * @author Benjamin P. Jung
 */
class FxApplicationThreadMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        final FxApplicationThread annotation = invocation.getMethod().getAnnotation(FxApplicationThread.class);
        final FxTask fxTask = new FxTask(invocation);

        if (annotation == null) {
            throw new IllegalStateException("Method is not annotated with '@FxApplicationThread'!");
        }

        if (!invocation.getMethod().getReturnType().equals(Void.class)) {
            throw new RuntimeException(String.format("[%s#%s] Only methods with return type 'void' can be annotated with @FXApplicationThread!", invocation.getThis().getClass().getName(), invocation.getMethod().getName()));
        }

        if (invocation.getMethod().getExceptionTypes().length > 0) {
            throw new RuntimeException("Only methods that don't declare exception types can be annotated with @FxApplicationThread!");
        }

        final Object retval;
        if (Platform.isFxApplicationThread()) {
            retval = invocation.proceed();
        } else {
            Platform.runLater(fxTask);
            retval = null;
        }
        return retval;

    }


    /**
     * 
     * @author Benjamin P. Jung
     */
    private static class FxTask implements Runnable {

        private final MethodInvocation methodInvocation;
        private Object returnValue;

        private FxTask(MethodInvocation methodInvocation) {
            super();
            this.methodInvocation = methodInvocation;
        }
        @Override
        public void run() {
            try {
                this.returnValue = methodInvocation.proceed();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

    }

}
