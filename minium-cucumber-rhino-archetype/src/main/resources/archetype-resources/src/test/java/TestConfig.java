#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.vilt.minium.DefaultWebElementsDriver;

@Configuration
public class TestConfig {

    @Bean(destroyMethod = "quit")
    public DefaultWebElementsDriver wd() throws MalformedURLException {
        String remoteWebDriverUrl = System.getProperty("remote.web.driver.url");

        WebDriver webDriver;
        if (remoteWebDriverUrl  != null) {
            webDriver = new RemoteWebDriver(new URL(remoteWebDriverUrl), DesiredCapabilities.chrome());
            webDriver = new Augmenter().augment(webDriver);
        }
        else {
            webDriver = new ChromeDriver();
        }
        return new DefaultWebElementsDriver(webDriver);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
