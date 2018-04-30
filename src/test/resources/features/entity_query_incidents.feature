Feature: entity query incidents
	Scenario: Entity queries its incidents
		Given an entity with username "paco" and password "paqui123"
		And 5 incidents belonging to the entity
		When the entity logs in the system
		And the entity accesses "/incidents"
		Then the entity receives status code of 200
		And the entity can see its 5 incidents