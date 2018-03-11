package com.uniovi.repositories;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface MasterFileParser {
	
	public String getKindNameOf(int kind) throws FileNotFoundException, IOException;
	public int getKindCodeOf(String kindName) throws FileNotFoundException, IOException;
	
}
