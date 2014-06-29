package be.klak.junit.jasmine.classes;

import com.vilt.minium.script.jasmine.JasmineSuite;

@JasmineSuite(specs = { "spec1.js", "spec2.js" }, sources = { "source1.js", "source2.js" })
public class JasmineSuiteGeneratorClassWithoutRunner {

}
