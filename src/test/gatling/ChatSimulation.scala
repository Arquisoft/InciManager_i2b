package cloud

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ChatSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://178.62.52.250:8081")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map("Accept" -> "text/css,*/*;q=0.1")

	val scn = scenario("ChatSimulation")
		.exec(http("Home")
			.get("/")
			.headers(headers_0)
			.resources(http("Resources /agentform")
			.get("/agentform")))
		.pause(7)
		.exec(http("Post /agentform")
			.post("/agentform")
			.headers(headers_0)
			.formParam("username", "sonny")
			.formParam("password", "pass123")
			.formParam("kind", "Person")
			.resources(http("Resource /agentform")
			.get("/incidents")
			.headers(headers_0)))
		.pause(4)
		.exec(http("Get incident chat")
			.get("/incident/create?method=chat")
			.headers(headers_0))
		.pause(40)
		.exec(http("Post /incident/create")
			.post("/incident/create")
			.header("Content-Type", "application/json")
			.body(RawFileBody("chat_request.json")).asJSON)

	setUp(scn.inject(rampUsers(1000) over(60 seconds))).protocols(httpProtocol)
}