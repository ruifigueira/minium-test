#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.junit.runner.RunWith;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.vilt.minium.DefaultWebElementsDriver;
import com.vilt.minium.script.jasmine.JasmineSuite;
import com.vilt.minium.script.jasmine.MiniumJasmine;
import com.vilt.minium.script.test.JsVariable;

@RunWith(MiniumJasmine.class)
@JasmineSuite(specs = "${packageInPathFormat}/${specName}_spec.js", envJs = false)
@ContextConfiguration(classes = TestConfig.class)
public class ${testClassname} {

    @Autowired
    @JsVariable("wd")
    private DefaultWebElementsDriver wd;

    @JsVariable(value = "config", resource = "classpath:config/dev.json")
    private Scriptable config;

}