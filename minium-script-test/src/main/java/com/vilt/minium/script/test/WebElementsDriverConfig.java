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
package com.vilt.minium.script.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vilt.minium.DefaultWebElementsDriver;

@Configuration
public class WebElementsDriverConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebElementsDriverConfig.class);

    @Bean(destroyMethod = "quit")
    public DefaultWebElementsDriver wd() throws MalformedURLException {
        String remoteWebDriverUrl = System.getProperty("remote.web.driver.url");

        WebDriver webDriver;
        if (remoteWebDriverUrl  != null) {
            webDriver = new RemoteWebDriver(new URL(remoteWebDriverUrl), DesiredCapabilities.chrome());
            webDriver = new Augmenter().augment(webDriver);
        } else {
            try {
                webDriver = new ChromeDriver();
            } catch (Exception e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Chrome driver not found or could not be launched, using Firefox driver instead (set log level to debug to check the exception)", e);
                } else {
                    LOGGER.warn("Chrome driver not found or could not be launched, using Firefox driver instead (set log level to debug to check the exception)");
                }
                webDriver = new FirefoxDriver();
            }
        }
        return new DefaultWebElementsDriver(webDriver);
    }
}
