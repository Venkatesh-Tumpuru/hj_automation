package com.remco.testRunner;

import org.testng.annotations.Test;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//@RunWith(Cucumber.class)
@CucumberOptions(
		features	=	"src/test/java/com/remco/features/",
		glue		=	{"com/remco/stepDefinitions"},
		plugin		=	{"pretty",
						 "json:target/cucumber-reports/Cucumber.json",
						 "html:target/cucumber-reports"},
		tags		=	"",
		monochrome	= 	true
		)
@Test
public class RemcoHJTestRunner  extends AbstractTestNGCucumberTests{

}
