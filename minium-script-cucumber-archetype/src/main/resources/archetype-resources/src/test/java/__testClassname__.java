#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.junit.runner.RunWith;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.vilt.minium.DefaultWebElementsDriver;
import com.vilt.minium.script.test.JsVariable;
import com.vilt.minium.script.cucumber.MiniumCucumber;

import cucumber.api.CucumberOptions;

@RunWith(MiniumCucumber.class)
@CucumberOptions(
        format = { "progress", "html:target/cucumber-html-report",  "json:target/cucumber-json-report.json" },
        glue   = { "classpath:cucumber/runtime/minium", "classpath:${packageInPathFormat}" }
)
@ContextConfiguration(classes = TestConfig.class)
public class ${testClassname} {

    @Autowired
    @JsVariable("wd")
    private DefaultWebElementsDriver wd;

    @JsVariable(value = "config", resource = "classpath:config/dev.json")
    private Scriptable config;
}