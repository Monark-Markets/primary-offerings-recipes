package com.monarkmarkets.internal;

import com.monarkmarkets.Config;
import com.monarkmarkets.Recipes;
import com.monarkmarkets.internal.alert.AlertManager;
import com.monarkmarkets.internal.alert.AlertManagerConfig;
import com.monarkmarkets.internal.alert.SendAlertOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalRecipesRunner {

	private static final Logger logger = LoggerFactory.getLogger(InternalRecipesRunner.class);
	private static final AlertManager alertManager = new AlertManager(
			new AlertManagerConfig(
					Config.getInstance().getAlertManagerUrl(),
					Config.getInstance().getAlertManagerUser(),
					Config.getInstance().getAlertManagerPassword()
			));

	public static void main(String[] args) {
		try {
			Recipes.main(args);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			alertManager.sendAlert(new SendAlertOptions() {{
				setTitle("Recipes execution failed");
				setMessage("Failed to execute internal recipes due to an error: " + e.getMessage());
				setDetails(e.getMessage());
				setSourceComponent("InternalRecipesRunner");
				setEnvironment(Config.getInstance().getEnvironment());
				setSeverity("error");
			}});
			System.exit(-1);
		}
	}
}