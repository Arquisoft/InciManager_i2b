package com.uniovi.main.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.Operator;
import com.uniovi.main.InciManagerI2bApplication;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class OperatorTest {
	
	private Operator op1 = new Operator(11L,"eleven@dashboard.com", "eleven", 0);
	
	@Test
	public void testInstantiation() {
		assertEquals("eleven@dashboard.com", op1.getEmail());
		assertEquals("eleven", op1.getOperatorname());
		assertEquals(0, op1.getIsAdmin());
	}

	@Test
	public void testEquals() {
		Operator op2 = new Operator(11L, "eleven@dashboard.com", "twelve", 1);
		Operator op3 = new Operator(12L, "twelve@dashboard.com", "eleven", 0);
		Operator op4 = new Operator(13L, "thirteen@dashboard.com", "thirteen", 0);
		Operator op5 = new Operator();
		
		assertEquals(op1, op2);
		assertNotEquals(op1, op3);
		assertNotEquals(op1, op4);
		assertNotEquals(op1, op5);
		
		assertEquals(op1.hashCode(), op2.hashCode());
		assertNotEquals(op1.hashCode(), op3.hashCode());
		assertNotEquals(op1.hashCode(), op4.hashCode());
		assertNotEquals(op1.hashCode(), op5.hashCode());
	}
	
	@Test
	public void testToString() {
		String toStringOp = "Operator [email=eleven@dashboard.com, operatorname=eleven, isAdmin=0]";
		assertEquals(toStringOp, op1.toString());
	}
}
