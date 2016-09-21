package me.jiantao.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static String name;
	static {
		InputStream is = Config.class.getClassLoader().getResourceAsStream(
				"config.properties");
		Properties config = new Properties();
		try {
			config.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		name = config.getProperty("name");
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Config.name = name;
	}

}
