Feature: person query incidents
	Scenario: Person queries his incident
		Given a person with username "sonny" and password "pass123"
		And 5 incidents belonging to the person
		When the person logs in the system
		And the person accesses "/incidents"
		Then the person receives status code of 200
		And the person can see his 5 incidents