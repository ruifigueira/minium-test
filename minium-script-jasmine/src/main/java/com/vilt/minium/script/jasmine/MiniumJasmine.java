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
package com.vilt.minium.script.jasmine;

import static com.google.common.base.Throwables.propagate;

import java.io.IOException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import be.klak.junit.jasmine.JasmineTestRunner;
import be.klak.rhino.RhinoContext;

import com.vilt.minium.script.test.impl.MiniumRhinoTestsSupport;

public class MiniumJasmine extends JasmineTestRunner {

    public MiniumJasmine(Class<?> testClass) throws IOException {
        super(testClass);
    }

    @Override
    protected void setUpJasmine(RhinoContext rhinoContext) {
        try {
            super.setUpJasmine(rhinoContext);

            Context cx = rhinoContext.getJsContext();
            Scriptable scope = rhinoContext.getJsScope();

            MiniumRhinoTestsSupport helper = new MiniumRhinoTestsSupport(testClass.getClassLoader(), testClass, cx, scope);
            helper.initialize();

            // we need require
            rhinoContext.loadFromClasspath("js/lib/jasmine-override.js");
        } catch (IOException e) {
            propagate(e);
        }
    }

}
