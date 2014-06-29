package be.klak.rhino;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.tools.shell.Global;

public class RhinoContextEnvjsLoadingTest {

    @Test
    public void loadEnvJSShouldSetWindowSpaceAndBeES5Complaint() {
        RhinoContext context = new RhinoContext();

        context.loadEnv("");
        assertThat(context.evalJS("window")).isInstanceOf(Global.class);

        assertThat(context.evalJS("Object.create({ test: 'test' });")).isInstanceOf(NativeObject.class);
    }

    @Test(expected = EcmaError.class)
    public void failWithoutLoadingEnvAndManipulatingDOMStuff() {
        RhinoContext context = new RhinoContext();
        context.evalJS("document.getElementById");
    }

}
