Feature: user login
	Scenario: Person logs in
		Given a list of agents:
			| username	| password | kind 	|
			| sonny 		| pass123  | Person  |
			| paco  		| pacoo42  | Sensor	|
			| tony  		| suuu142  | Entity  |
		When the user accesses "/agentform"
		And the user logs in with username "sonny" password "pass123" and kind "Person"
		Then the user receives status code of 302
		And the user receives the page "/incidents"
		
	Scenario: Wrong password
	  Given a list of agents:
			| username	| password | kind 	|
			| sonny 		| pass123  | Person  |
			| paco  		| pacoo42  | Sensor	|
			| tony  		| suuu142  | Entity  |
		When the user accesses "/agentform"
		And the user logs in with username "sonny" password "wrongpassword" and kind "Person"
		Then the user receives status code of 302
		And the user receives the page "/agentform?error=true"
		
		
	Scenario: Incorrect data login
	  Given a list of agents:
			| username	| password | kind 	|
			| sonny 		| pass123  | Person  |
			| paco  		| pacoo42  | Sensor	|
			| tony  		| suuu142  | Entity  |
		When the user accesses "/agentform"
		And the user logs in with username "juana" password "wrongpassword" and kind "Person"
		Then the user receives status code of 302
		And the user receives the page "/agentform?error=true"
		