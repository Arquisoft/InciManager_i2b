package com.uniovi.main.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.Incident;
import com.uniovi.entities.Operator;
import com.uniovi.main.InciManagerI2bApplication;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class OperatorTest {
	
	private Operator op1 = new Operator("eleven@dashboard.com", "eleven", false);
	
	@Test
	public void testInstantiation() {
		assertEquals("eleven@dashboard.com", op1.getEmail());
		assertEquals("eleven", op1.getOperatorname());
		assertEquals(false, op1.getIsAdmin());
	}

	@Test
	public void testEquals() {
		Operator op2 = new Operator("eleven@dashboard.com", "twelve", true);
		Operator op3 = new Operator(new ObjectId(), "twelve@dashboard.com", "eleven", false);
		Operator op4 = new Operator("thirteen@dashboard.com", "thirteen", false);
		Operator op5 = new Operator();
		
		assertEquals(op1, op2);
		assertNotEquals(op1, op3);
		assertNotEquals(op1, op4);
		assertNotEquals(op1, op5);
		
		assertEquals(op1.hashCode(), op2.hashCode());
		assertNotEquals(op1.hashCode(), op3.hashCode());
		assertNotEquals(op1.hashCode(), op4.hashCode());
		assertNotEquals(op1.hashCode(), op5.hashCode());
		
		// strange cases
		Operator op6 = op2;
		assertEquals(op6, op2);
		
		assertNotEquals(op5, new Incident());
		assertNotEquals(null, op5);
		
		op2.setEmail(null);
		assertNotEquals(op2, op4);
	}
	
	@Test
	public void testToString() {
		String toStringOp = "Operator [email=eleven@dashboard.com, operatorname=eleven, isAdmin=false]";
		assertEquals(toStringOp, op1.toString());
		
		Operator op2 = new Operator("cleopatra@gob.eg", "cleo", true);
		toStringOp = "Operator [email=cleopatra@gob.eg, operatorname=cleo, isAdmin=true]";
		assertEquals(toStringOp, op2.toString());
		
		op2.setEmail("cleopatra@dead.world");
		assertEquals("Operator [email=cleopatra@dead.world, operatorname=cleo, isAdmin=true]", op2.toString());
		
		op2.setIsAdmin(false);
		assertEquals("Operator [email=cleopatra@dead.world, operatorname=cleo, isAdmin=false]", op2.toString());
		
		op2.setOperatorname("CLEOPATRA");
		assertEquals("Operator [email=cleopatra@dead.world, operatorname=CLEOPATRA, isAdmin=false]", op2.toString());
	}
	
	@Test
	public void testNumNotifications() {
		Operator op2 = new Operator("eleven@dashboard.com", "twelve", true);
		op2.setNumNotifications(2);
		assertEquals(2, op2.getNumNotifications());
		op2.setNumNotifications(5);
		assertEquals(5, op2.getNumNotifications());
	}
}
