package be.klak.junit.jasmine.classes;

import com.vilt.minium.script.jasmine.JasmineSuite;

@JasmineSuite(specs = { "doesNotLoadEnvJSSpec.js" }, envJs = false)
public class JasmineTestRunnerDoesNotLoadEnvJS { }
