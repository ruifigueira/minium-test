package be.klak.junit.jasmine.classes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;

import com.vilt.minium.script.jasmine.JasmineSuite;

import be.klak.rhino.RhinoContext;


@JasmineSuite(specs = { "emptySpec.js" })
public class JasmineTestRunnerBeforeAndAfterClass extends JasmineTestRunnerBeforeAndAfterSuperClass {

    @Before
    public void runMij() {
        assertThat(runs).isEmpty();
        runs.add(RUN_MIJ_FLAG);
    }

    @After
    public void runMijAfterOok(RhinoContext context) {
        assertThat(runs).containsOnly(RUN_MIJ_FLAG, RUN_MIJ_OOK_FLAG);
    }

    public void runMijNiet() {
        fail("should not be run");
    }
}
