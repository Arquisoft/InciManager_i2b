Feature: person checks incidents status
	Scenario: Person checks the status of his incidents
		Given a person with name "paco" and password "paqui123"
		And 3 unassigned incidents belonging to the person
		When the person enters the system
		And the person accesses  "/incidents"
		Then the person receives a status code of 200
		And the person can see the incident status
		And the incident number 2 is opened
		And the person can refresh "/incidents"
		And the person can see the new status of his incident