package be.klak.env;

import org.junit.Before;
import org.junit.runner.RunWith;

import be.klak.junit.jasmine.JasmineTestRunner;
import be.klak.rhino.RhinoContext;

import com.vilt.minium.script.jasmine.JasmineSuite;

@RunWith(JasmineTestRunner.class)
@JasmineSuite(sources = "jquery-1.6.1.js", sourcesRootDir = "", envJs = true)
public class EnvUtilsTest {

    @Before
    public void loadJasmineJQueryMatchers(RhinoContext context) {
        context.loadFromClasspath("js/lib/jasmine-1.3.1/jasmine-jquery-rhino.js");
    }

}
