package be.klak.env;

import org.junit.Before;
import org.junit.runner.RunWith;

import com.vilt.minium.script.jasmine.JasmineSuite;

import be.klak.junit.jasmine.JasmineTestRunner;
import be.klak.rhino.RhinoContext;

@RunWith(JasmineTestRunner.class)
@JasmineSuite(sources = "jquery-1.6.1.js", sourcesRootDir = "")
public class EnvUtilsTest {

    @Before
    public void loadJasmineJQueryMatchers(RhinoContext context) {
        context.loadFromClasspath("js/lib/jasmine-1.3.1/jasmine-jquery-rhino.js");
    }

}
