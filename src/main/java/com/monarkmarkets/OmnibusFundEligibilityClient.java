package com.monarkmarkets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

/**
 * Reads the platform-admin FI access relationship used by Omnibus order validation.
 *
 * <p>The partner-facing SDK does not expose FI/fund access, so this small internal
 * client uses the admin API credentials already supplied to the recipes runner.</p>
 */
final class OmnibusFundEligibilityClient {

	private static final int PAGE_SIZE = 1000;
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(30))
			.build();
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	boolean hasTransactionAccess(UUID fundId, UUID financialInstitutionId) {
		if (fundId == null || financialInstitutionId == null) {
			return false;
		}

		String baseUrl = Config.getEnv("ADMIN_BASE_URL", Config.getInstance().getBaseUrl());
		String endpoint = trimTrailingSlash(baseUrl)
				+ "/primary-internal/v2/financial-institution-access/target/"
				+ fundId
				+ "/financial-institutions?targetType=OmnibusFund&page=1&pageSize="
				+ PAGE_SIZE;

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endpoint))
				.timeout(Duration.ofSeconds(30))
				.header("Authorization", Config.getRequiredEnv("ADMIN_API_KEY"))
				.header("Accept", "application/json")
				.GET()
				.build();

		try {
			HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				throw new IllegalStateException(
						"FI access lookup failed with HTTP " + response.statusCode() + " for Omnibus fund " + fundId);
			}

			JsonNode items = OBJECT_MAPPER.readTree(response.body()).path("items");
			if (!items.isArray()) {
				throw new IllegalStateException(
						"FI access lookup response did not contain an items array for Omnibus fund " + fundId);
			}

			for (JsonNode item : items) {
				if (financialInstitutionId.toString().equals(item.path("id").asText())
						&& item.path("transactionsEnabled").asBoolean(false)) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			throw new RuntimeException("Failed to query FI access for Omnibus fund " + fundId, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted while querying FI access for Omnibus fund " + fundId, e);
		}
	}

	private static String trimTrailingSlash(String value) {
		return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
	}
}
