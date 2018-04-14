package com.uniovi.main.cucumber;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.uniovi.main.InciManagerI2bApplication;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/features")
public class CucumberTest {

}
