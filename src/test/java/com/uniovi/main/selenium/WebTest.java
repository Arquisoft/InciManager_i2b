package com.uniovi.main.selenium;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.uniovi.entities.AgentInfo;

/**
 * Selenium tests to check that the web interface works as expected. They must
 * be run with the incident manager server already started, so DONT INCLUDE THIS
 * IN MAVEN TESTS.
 */
public class WebTest {

	private WebDriver driver;
	private String baseUrl;

	@Before
	public void setUp() throws Exception {
		driver = new HtmlUnitDriver();
		baseUrl = "http://localhost:8081";
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	// Try to access the chat interface without logging in first.
	@Test
	public void createIncidentInvalidTest() throws Exception {
		driver.get(baseUrl + "/incident/create");

		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'support service')]"));
		assertTrue(elements.size() == 0);

		elements = driver.findElements(By.xpath("//*[contains(text(),'Agent data')]"));
		assertTrue(elements.size() > 0);
	}

	// Access to the chat interface after logging in.
	@Test
	public void createIncidentValidTest() {
		logInAs(new AgentInfo("pacoo", "123456", "Person"));
		driver.get(baseUrl + "/incident/create");
		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'support service')]"));
		assertTrue(elements.size() > 0);
	}

	// Check that an agent is redirected to the incident list after loggin in
	@Test
	public void listIncidents() {
		logInAs(new AgentInfo("pacoo", "123456", "Person"));
		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'incidents')]"));
		assertTrue(elements.size() > 0);
	}

	private void logInAs(AgentInfo agentInfo) {
		driver.get(baseUrl + "/agentform");

		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(agentInfo.getUsername());

		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(agentInfo.getPassword());

		driver.findElement(By.name("kind")).clear();
		driver.findElement(By.name("kind")).sendKeys(agentInfo.getKind());

		driver.findElement(By.tagName("button")).click();
	}
}
