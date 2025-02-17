package com.monarkmarkets;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import lombok.Getter;

@Getter
public class Config {
	private static final Dotenv dotenv;

	static {
		Dotenv tempDotenv = null;
		try {
			tempDotenv = Dotenv.load();
		} catch (DotenvException e) {
			System.err.println("Warning: .env file not found. Falling back to system environment variables.");
		}
		dotenv = tempDotenv;
	}

	private static final Config INSTANCE = new Config();

	private final String baseUrl;
	private final String adminBaseUrl;
	private final String apiKey;
	private final String adminApiKey;
	private final String cfAccessClientId;
	private final String cfAccessClientSecret;

	private Config() {
		this.baseUrl = getRequiredEnv("BASE_URL");
		this.apiKey = getRequiredEnv("API_KEY");

		this.adminBaseUrl = getRequiredEnv("ADMIN_BASE_URL");
		this.adminApiKey = getRequiredEnv("ADMIN_API_KEY");
		this.cfAccessClientId = getRequiredEnv("CF_ACCESS_CLIENT_ID");
		this.cfAccessClientSecret = getRequiredEnv("CF_ACCESS_CLIENT_SECRET");
	}

	private static String getEnv(String key, String defaultValue) {
		String value = (dotenv != null) ? dotenv.get(key) : null;
		if (value == null) {
			value = System.getenv(key);
		}
		return (value != null) ? value : defaultValue;
	}

	private static String getRequiredEnv(String key) {
		String value = (dotenv != null) ? dotenv.get(key) : null;
		if (value == null) {
			value = System.getenv(key);
		}
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Environment variable '" + key + "' is required but not set.");
		}
		return value;
	}

	public static Config getInstance() {
		return INSTANCE;
	}
}

