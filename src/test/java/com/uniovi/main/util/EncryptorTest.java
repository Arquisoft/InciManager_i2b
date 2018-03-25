package com.uniovi.main.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.uniovi.util.JasyptEncryptor;

public class EncryptorTest {
	
	private JasyptEncryptor encryptor;
	
	@Before
	public void setUp() {
		encryptor = new JasyptEncryptor();
	}
	
	@Test
	public void testEncryptor() {
		String myPass = "passwd1234";
		String encrypted = encryptor.encryptPassword(myPass);
		assertTrue(encryptor.checkPassword(myPass, encrypted));
		assertFalse(encryptor.checkPassword("passwd12345", encrypted));
		String otherEncrypted = encryptor.encryptPassword("passwd123");
		assertNotEquals(encrypted, otherEncrypted);
	}

}
