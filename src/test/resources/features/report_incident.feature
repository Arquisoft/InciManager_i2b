Feature: report incident
	Scenario: Sensor reports an incident
		Given a list of users:
			| username	| password | kind   |
			| sonny 		| pass123  | Person |
			| paco  		| pacoo42  | Sensor |
			| tony  		| suuu142  | Entity |
		When the agent with username "paco" and password "pacoo42" posts an incident
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
		When the agent with username "sonny" and password "pass123" posts an incident
		Then the agent receives status code of 200
		And the agent receives the string "Incident correctly sent!"
		And the incident is stored
		And an operator is assigned to the incident
		
	Scenario: Sensor reports an incident
		Given a list of users:
			| username	| password | kind   |
			| sonny 		| pass123  | Person |
			| paco  		| pacoo42  | Sensor |
			| tony  		| suuu142  | Entity |
		When the agent with username "tony" and password "suuu142" posts an incident
		Then the agent receives status code of 200
		And the agent receives the string "Incident correctly sent!"
		And the incident is stored
		And an operator is assigned to the incident