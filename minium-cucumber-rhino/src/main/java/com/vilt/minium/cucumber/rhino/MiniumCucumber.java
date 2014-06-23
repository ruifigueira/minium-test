/*
 * Copyright (C) 2013 The Minium Authors
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
package com.vilt.minium.cucumber.rhino;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Throwables.propagate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.json.JsonParser;
import org.mozilla.javascript.json.JsonParser.ParseException;
import org.mozilla.javascript.tools.shell.Global;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.vilt.minium.script.MiniumContextLoader;

import cucumber.api.CucumberOptions;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.snippets.SummaryPrinter;

/**
 * Classes annotated with {@code @RunWith(Cucumber.class)} will run a Cucumber Feature.
 * The class should be empty without any fields or methods.
 * <p/>
 * Cucumber will look for a {@code .feature} file on the classpath, using the same resource
 * path as the annotated class ({@code .class} substituted by {@code .feature}).
 * <p/>
 * Additional hints can be given to Cucumber by annotating the class with {@link Options}.
 *
 * @see Options
 */
public class MiniumCucumber extends ParentRunner<FeatureRunner> {

    private final JUnitReporter jUnitReporter;
    private final List<FeatureRunner> children = new ArrayList<FeatureRunner>();
    private final Runtime runtime;

    private MiniumBackend backend;
    private Global scope;
    private Context cx;

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws java.io.IOException if there is a problem
     * @throws org.junit.runners.model.InitializationError
     *                             if there is another problem
     * @throws URISyntaxException
     */
    public MiniumCucumber(Class<?> clazz) throws InitializationError, IOException, URISyntaxException {
        super(clazz);
        ClassLoader classLoader = clazz.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(clazz);

        @SuppressWarnings("unchecked")
        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz, new Class[] { CucumberOptions.class });
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);

        String expectjsFile = MiniumCucumber.class.getClassLoader().getResource("modules/expect.js").toExternalForm();
        String internalModulesUrl = expectjsFile.substring(0, expectjsFile.lastIndexOf("/"));

        cx = Context.enter();
        scope = new Global(cx);
        scope.installRequire(cx, Lists.newArrayList(internalModulesUrl, "src/test/resources/modules"), false);
        new MiniumContextLoader(classLoader).load(cx, scope);

        backend = new MiniumBackend(resourceLoader, cx, scope);

        TestContextManager contextManager = new TestContextManager(getTestClass().getJavaClass());
        contextManager.registerTestExecutionListeners(new DependencyInjectionTestExecutionListener());
        createTestClassInstance(contextManager);

        runtime = new Runtime(resourceLoader, classLoader, Collections.singleton(backend), runtimeOptions);

        jUnitReporter = new JUnitReporter(runtimeOptions.reporter(classLoader), runtimeOptions.formatter(classLoader), runtimeOptions.isStrict());
        addChildren(runtimeOptions.cucumberFeatures(resourceLoader));
    }

    @Override
    public List<FeatureRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(FeatureRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        child.run(notifier);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        jUnitReporter.done();
        jUnitReporter.close();
        new SummaryPrinter(System.out).print(runtime);
    }

    private void addChildren(List<CucumberFeature> cucumberFeatures) throws InitializationError {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            children.add(new FeatureRunner(cucumberFeature, runtime, jUnitReporter));
        }
    }

    protected Object createTestClassInstance(TestContextManager contextManager) {
        try {
            final Object testInstance = getInstance();
            contextManager.prepareTestInstance(testInstance);

            ReflectionUtils.doWithFields(getTestClass().getJavaClass(), new FieldCallback() {

                @Override
                public void doWith(Field f) throws IllegalArgumentException, IllegalAccessException {
                    f.setAccessible(true);
                    JsVariable jsVariable = f.getAnnotation(JsVariable.class);
                    if (jsVariable == null) return;

                    String varName = jsVariable.value();
                    checkNotNull(varName, "@JsVariable.value() should not be null");
                    Object fieldVal = f.get(testInstance);
                    Object val = getVal(jsVariable, f.getType(), fieldVal);
                    put(scope, varName, val);

                    if (fieldVal == null && val != null) {
                        f.set(testInstance, val);
                    }
                }
            });

            return testInstance;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getVal(JsVariable jsVariable, Class<?> clazz, Object object) {
        try {
            String resourcePath = jsVariable.resource();
            if (StringUtils.isNotEmpty(resourcePath)) {
                Resource resource = new DefaultResourceLoader(getTestClass().getJavaClass().getClassLoader()).getResource(resourcePath);
                checkState(resource.exists() && resource.isReadable());

                if (clazz == String.class) {
                    InputStream is = resource.getInputStream();
                    try {
                        return IOUtils.toString(is, Charsets.UTF_8.name());
                    } finally {
                        IOUtils.closeQuietly(is);
                    }
                } else {
                    Object val = parseJson(cx, scope, resource);
                    checkState(clazz.isAssignableFrom(val.getClass()));
                    return val;
                }
            } else {
                return object;
            }
        } catch (IOException e) {
            throw propagate(e);
        } catch (ParseException e) {
            throw propagate(e);
        }
    }

    protected void put(Scriptable scope, String name, Object value) {
        scope.put(name, scope, Context.javaToJS(value, scope));
    }

    protected void delete(Scriptable scope, String name) {
        scope.delete(name);
    }

    protected Object parseJson(Context cx, Scriptable scope, Resource resource) throws ParseException, IOException {
        String json = IOUtils.toString(resource.getInputStream());
        return new JsonParser(cx, scope).parseValue(json);
    }

    protected Object parseJson(Context cx, Scriptable scope, String json) throws ParseException {
        return new JsonParser(cx, scope).parseValue(json);
    }

    private Object getInstance() {
        try {
            return getTestClass().getJavaClass().newInstance();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
