package com.uniovi.main.selenium;

import static org.junit.Assert.assertEquals;
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
		driver.get(baseUrl + "/incident/create?method=chat");

		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'support service')]"));
		assertTrue(elements.size() == 0);

		elements = driver.findElements(By.xpath("//*[contains(text(),'Agent data')]"));
		assertTrue(elements.size() > 0);
	}

	// Access to the chat interface after logging in.
	@Test
	public void createIncidentValidTest() {
		logInAs(new AgentInfo("pacoo", "123456", "Person"));
		driver.get(baseUrl + "/incident/create?method=chat");
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
	
	// Access the create incident form without logging in first
	@Test
	public void retrieveIncidentForm() {
		driver.get(baseUrl + "/incident/create?method=form");

		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'Incident Information')]"));
		assertTrue(elements.size() == 0);

		elements = driver.findElements(By.xpath("//*[contains(text(),'Agent data')]"));
		assertTrue(elements.size() > 0);
	}

	// Log in, get to the form. Try to create an incident until it is accepteed and
	// shown
	@Test
	public void createInvalidIncidentForm() {
		logInAs(new AgentInfo("pacoo", "123456", "Person"));
		// We've been redirected to the incident list page
		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'incidents')]"));
		assertTrue(elements.size() > 0);
		// Check available create incident option
		elements = driver.findElements(By.xpath("//*[contains(text(),'Create incident (form)')]"));
		assertTrue(elements.size() > 0);
		// Click to access the element
		elements.get(0).click();
		// We're in the form
		elements = driver.findElements(By.xpath("//*[contains(text(),'Incident Information')]"));
		assertTrue(elements.size() > 0);
		// Try to submit the form
		elements = driver.findElements(By.xpath("//*[contains(text(),'Create incident')]"));
		elements.get(0).click();
		// Error message is shown
		elements = driver.findElements(By.xpath("//*[contains(text(),'Enter a name for the incident')]"));
		assertTrue(elements.size() > 0);
		// Fill the form
		fillIncidentForm("Selenium incident", "4.59", "5.56", "Selenium tag", "Test made by selenium");
		elements = driver.findElements(By.xpath("//*[contains(text(),'Create incident')]"));
		elements.get(0).click();
		// Redirected to the list, where the new incident is listed
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		elements = driver.findElements(By.xpath("//*[contains(text(),'selenium')]"));
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
	private void fillIncidentForm(String name, String latitude, String longitude, String tag, String moreInfo) {
		driver.findElement(By.name("inciName")).clear();
		driver.findElement(By.name("inciName")).sendKeys(name);

		driver.findElement(By.name("latitude")).clear();
		driver.findElement(By.name("latitude")).sendKeys(latitude);

		driver.findElement(By.name("longitude")).clear();
		driver.findElement(By.name("longitude")).sendKeys(longitude);

		driver.findElement(By.name("tags")).clear();
		driver.findElement(By.name("tags")).sendKeys(tag);

		driver.findElement(By.name("moreInfo")).clear();
		driver.findElement(By.name("moreInfo")).sendKeys(moreInfo);
	}
	
	@Test
	public void checkLandingPage() {
		driver.navigate().to(baseUrl+"/");
		assertEquals(driver.getCurrentUrl(), baseUrl+"/agentForm");
		
		logInAs(new AgentInfo("pacoo", "123456", "Person"));
		driver.navigate().to(baseUrl+"/");
		assertEquals(driver.getCurrentUrl(), baseUrl+"/incidents");
	}
}
