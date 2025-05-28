package com.monarkmarkets.internal;

import com.monarkmarkets.Config;
import lombok.Getter;

@Getter
public class ConfigInternal {

	private final String alertManagerUrl;
	private final String alertManagerUser;
	private final String alertManagerPassword;
	private final String environment;

	private static final ConfigInternal INSTANCE = new ConfigInternal();

	private ConfigInternal() {
		this.alertManagerUrl = Config.getRequiredEnv("ALERTMANAGER_URL");
		this.alertManagerUser = Config.getRequiredEnv("ALERTMANAGER_USER");
		this.alertManagerPassword = Config.getRequiredEnv("ALERTMANAGER_PASSWORD");
		this.environment = Config.getEnv("GITHUB_ENVIRONMENT", "local");
	}

	public static ConfigInternal getInstance() {
		return INSTANCE;
	}
}
