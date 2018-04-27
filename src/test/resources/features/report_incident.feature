Feature: report incident
	Scenario: Sensor reports an incident
		Given a list of users:
			| username	| password | kind   |
			| sonny 		| pass123  | Person |
			| paco  		| pacoo42  | Sensor |
			| tony  		| suuu142  | Entity |
		When the sensor with username "paco" and password "pacoo42" posts an incident
		Then the agent receives status code of 200
		And the agent receives the string "Incident correctly sent!"
		And the incident is stored
		And an operator is assigned to the incident
		
	Scenario: Person reports an incident
		Given a list of users:
			| username	| password | kind   |
			| sonny 		| pass123  | Person |
			| paco  		| pacoo42  | Sensor |
			| tony  		| suuu142  | Entity |
		And an agent with username "sonny" password "pass123" and kind "Person"
		When the agent logs in
		And the agent accesses "incident/create?method=form"
		And the agent posts an incident
		Then the agent receives the string "Incident correctly sent!"
		And the incident is stored
		And an operator is assigned to the incident
		
	Scenario: Entity reports an incident
		Given a list of users:
			| username	| password | kind   |
			| sonny 		| pass123  | Person |
			| paco  		| pacoo42  | Sensor |
			| tony  		| suuu142  | Entity |
		And an agent with username "tony" password "suuu142" and kind "Entity"
		When the agent logs in
		And the agent accesses "incident/create?method=form"
		And the agent posts an incident
		Then the agent receives the string "Incident correctly sent!"
		And the incident is stored
		And an operator is assigned to the incident