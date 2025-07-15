package com.monarkmarkets.internal;

import com.monarkmarkets.ApiFactory;
import com.monarkmarkets.Recipes;
import com.monarkmarkets.primary.client.api.VersionApi;
import com.monarkmarkets.primary.client.invoker.Configuration;
import com.monarkmarkets.primary.client.model.ApiVersion;
import com.monarkmarkets.internal.alert.AlertManager;
import com.monarkmarkets.internal.alert.AlertManagerConfig;
import com.monarkmarkets.internal.alert.SendAlertOptions;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalRecipesRunner {

	private static final Logger logger = LoggerFactory.getLogger(InternalRecipesRunner.class);
	private static final AlertManager alertManager = new AlertManager(
			new AlertManagerConfig(
					ConfigInternal.getInstance().getAlertManagerUrl(),
					ConfigInternal.getInstance().getAlertManagerUser(),
					ConfigInternal.getInstance().getAlertManagerPassword()
			));
	private static final VersionApi versionApi = ApiFactory.getVersionApi();

	@SneakyThrows
	public static void main(String[] args) {
		try {
			Recipes.runAll(args);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			ApiVersion apiVersion = versionApi.primaryV1VersionGet();
			alertManager.sendAlert(new SendAlertOptions() {{
				setTitle("Recipes execution failed. Public API version: " + apiVersion.getVersion() + ". Client API version: " + Configuration.VERSION);
				setMessage("Failed to execute internal recipes due to an error: " + e.getMessage());
				setDetails(e.getMessage());
				setSourceComponent("InternalRecipesRunner");
				setEnvironment("STAGING");
				setSeverity("error");
			}});
			System.exit(-1);
		}
	}
}