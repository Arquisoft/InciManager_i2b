Feature: sensor incident
	Scenario: Sensor reports an incident
		Given a list of sensors:
			| username	| password | kind   |
			| sonny 		| pass123  | Sensor |
			| paco  		| pacoo42  | Sensor |
			| tony  		| suuu142  | Entity |
		When the sensor with username "sonny" and password "pass123" posts an incident
		Then the sensor receives status code of 200
		And the sensor receives the string "Incident correctly sent!"
		And the incident is stored
		And an operator is assigned to the incident