package com.uniovi.main.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.Operator;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.OperatorsService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class OperatorsServiceTest {

	@Autowired
	private OperatorsService operatorsService;
	
	@Test
	public void randomOperatorTest() {
		List<Operator> operators = operatorsService.getOperators();
		
		for (int i = 0; i < 50; i++) {
			Operator operator = operatorsService.randomOperator();
			assertNotNull(operator);
			assertTrue(operators.contains(operator));
		}
	}
	
	@Test
	public void findOperatorByEmailTest() {
		Operator operator = operatorsService.findOperatorByEmail("operator11@dashboard.com");
		assertEquals("op1", operator.getOperatorname());
		assertEquals("operator11@dashboard.com", operator.getEmail());
		
		Operator notAnOperator = operatorsService.findOperatorByEmail("notAnEmail");
		assertNull(notAnOperator);
	}
}
