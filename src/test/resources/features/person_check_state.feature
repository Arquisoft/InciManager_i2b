Feature: person checks incidents status
	Scenario: Person checks the status of his incidents
		Given a person with username "paco" and password "paqui"
		And 1 unassigned incident belonging to the person
		When the person logs in the system
		And the person accesses "/incidents"
		Then the person receives status code of 200
		And the person can see the incident status
		And the incident is opened
		And the person can refresh "/incidents"
		And the person can see the new status of his incident