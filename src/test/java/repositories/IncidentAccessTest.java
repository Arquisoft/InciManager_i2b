package repositories;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.Incident;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.IncidentsService;
import com.uniovi.util.JasyptEncryptor;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class IncidentAccessTest {
    
    @Autowired
	IncidentsService incidentsService;
    
    JasyptEncryptor encryptor;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        encryptor = new JasyptEncryptor();
    }

    
//    String username, String passw, String name, String location
    @Test
    public void _01testInsertIncident() throws Exception {
		Incident incident1 = new Incident ("user1", "passwd1", "inci1", "location1");
		Incident incident2 = new Incident ("user2", "passwd2", "inci2", "location2");
		Incident incident3 = new Incident ("user3", "passwd3", "inci3", "location3");
		Incident incident4 = new Incident ("user4", "passwd4", "inci4", "location4");
		Incident incident5 = new Incident ("user5", "passwd5", "inci5", "location5");

		incidentsService.addIncident(incident1);
		incidentsService.addIncident(incident2);
		incidentsService.addIncident(incident3);
		incidentsService.addIncident(incident4);
		incidentsService.addIncident(incident5);
		
		assertEquals(5, incidentsService.getIncidents().size());
    }
    
    @Test
    public void _02testReadIncident() throws Exception {
		
		List<Incident> incidentsUser1 = incidentsService.getIncidentsByUsername("user1");
		assertEquals(1, incidentsUser1.size());
		Incident inci1 = incidentsUser1.get(0);
		
		assertEquals("user1", inci1.getUsername());
		assertEquals(true, encryptor.checkPassword("passwd1", inci1.getPassword()));
		assertEquals("inci1", inci1.getInciName());
		assertEquals("location1", inci1.getLocation());
		assertEquals(0, inci1.getProperties().size());
		
		//Add a second incident to user1
		Incident incident6 = new Incident ("user1", "passwd1", "inci6", "location1");
		incidentsService.addIncident(incident6);
		
		
		incidentsUser1 = incidentsService.getIncidentsByUsername("user1");
		assertEquals(2, incidentsUser1.size());

    }
    
    @Test
    public void _03testDeleteIncident() throws Exception {
		
		List<Incident> incidents = incidentsService.getIncidents();
		assertEquals(6, incidents.size());

		incidentsService.deleteIncidentByName("inci2");
		incidents = incidentsService.getIncidents();
		assertEquals(5, incidents.size());
		
		//User 2 no incidents
		incidents = incidentsService.getIncidentsByUsername("user2");
		assertEquals(0, incidents.size());
		
		incidentsService.deleteIncidentByName("inci1");
		incidents = incidentsService.getIncidentsByUsername("user1");
		assertEquals(1, incidents.size());
		
		
		incidents = incidentsService.getIncidentsByUsername("user1");
		assertEquals("user1", incidents.get(0).getUsername());
		assertEquals("inci6", incidents.get(0).getInciName());
		
		
		incidentsService.deleteIncidentByName("inci1");
		incidentsService.deleteIncidentByName("inci2");
		incidentsService.deleteIncidentByName("inci3");
		incidentsService.deleteIncidentByName("inci4");
		incidentsService.deleteIncidentByName("inci5");
		incidentsService.deleteIncidentByName("inci6");

    }

  
}
