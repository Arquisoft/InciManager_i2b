package cloud

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class FormSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://192.168.99.100:8081")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_8 = Map("Accept" -> "*/*")

	val headers_9 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json; charset=utf-8",
		"X-Requested-With" -> "XMLHttpRequest")

	val scn = scenario("FormSimulation")
		.exec(http("Home")
			.get("/")
			.headers(headers_0)
			.resources(http("Resources /agentform")
			.get("/agentform")))
		.pause(8)
		.exec(http("Post agentform")
			.post("/agentform")
			.formParam("username", "sonny")
			.formParam("password", "pass123")
			.formParam("kind", "Person")
			.resources(http("Resources /incidents ")
			.get("/incidents")))
		.pause(8)
		.exec(http("Get incident form")
			.get("/incident/create?method=form")
			.headers(headers_0)
			.resources(http("Resources form.js")
			.get("/script/form/form.js")
			.headers(headers_8)))
		.pause(45)
		.exec(http("Post /incident/create")
			.post("/incident/create")
			.headers(headers_9)
			.body(RawFileBody("FormSimulation_0009_request.txt"))
			.resources(http("Resources /incident")
			.get("/incidents")))

	setUp(scn.inject(rampUsers(1000) over(60 seconds))).protocols(httpProtocol)
}