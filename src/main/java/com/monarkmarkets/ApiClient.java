package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monarkmarkets.dtos.PageResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiClient {

	private static final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(20))
			.build();
	private static final ObjectMapper objectMapper = ObjectMappers.createWithDefaults();
	private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
	private static final Config config = Config.getInstance();

	private static Map<String, List<String>> getDefaultHeaders() {
		return Map.of(
				"Content-Type", List.of("application/json"),
				"Authorization", List.of(config.getApiKey())
		);
	}

	private static Map<String, List<String>> getAdminHeaders() {
		return Map.of(
				"Content-Type", List.of("application/json"),
				"Authorization", List.of(config.getAdminApiKey()),
				"CF-Access-Client-Id", List.of(config.getCfAccessClientId()),
				"CF-Access-Client-Secret", List.of(config.getCfAccessClientSecret())
		);
	}

	/**
	 * Sends an API request and deserializes the response into a single object.
	 *
	 * @param endpoint     The API endpoint.
	 * @param method       The HTTP method (GET, POST, etc.).
	 * @param requestBody  The request body (can be null).
	 * @param responseType The expected response type (e.g., Investor.class).
	 * @param <T>          The response object type.
	 * @return The response deserialized into an object of type T.
	 */
	public static <T> T sendRequest(
			String endpoint,
			String method,
			Object requestBody,
			Class<T> responseType
	) {
		URI uri = URI.create(config.getBaseUrl() + endpoint);
		return sendRequestInternal(uri, method, requestBody, getDefaultHeaders(), responseType, null);
	}

	/**
	 * Sends an API request and deserializes the response into a list or other generic type.
	 *
	 * @param endpoint      The API endpoint.
	 * @param method        The HTTP method (GET, POST, etc.).
	 * @param requestBody   The request body (can be null).
	 * @param typeReference The expected response type (e.g., new TypeReference<List<Investor>>() {}).
	 * @param <T>           The response object type.
	 * @return The response deserialized into an object of type T.
	 */
	public static <T> T sendRequest(
			String endpoint,
			String method,
			Object requestBody,
			TypeReference<T> typeReference
	) {
		URI uri = URI.create(config.getBaseUrl() + endpoint);
		return sendRequestInternal(uri, method, requestBody, getDefaultHeaders(), null, typeReference);
	}

	/**
	 * Overloaded method for API requests that don't return a response body.
	 *
	 * @param endpoint    The API endpoint.
	 * @param method      The HTTP method (PUT, DELETE, etc.).
	 * @param requestBody The request body as an object (can be null).
	 */
	public static void sendRequest(
			String endpoint,
			String method,
			Object requestBody
	) {
		URI uri = URI.create(config.getBaseUrl() + endpoint);
		sendRequestInternal(uri, method, requestBody, getDefaultHeaders(), Void.class, null);
	}

	/**
	 * Internal method to send API requests and deserialize the response based on the provided type.
	 *
	 * @param uri           The API uri.
	 * @param method        The HTTP method (GET, POST, etc.).
	 * @param requestBody   The request body (can be null).
	 * @param headers       Request headers.
	 * @param responseType  The expected response type if it's a single object.
	 * @param typeReference The expected response type if it's a list or generic type.
	 * @param <T>           The response object type.
	 * @return The response deserialized into an object of type T.
	 */
	private static <T> T sendRequestInternal(
			URI uri,
			String method,
			Object requestBody,
			Map<String, List<String>> headers,
			Class<T> responseType,
			TypeReference<T> typeReference
	) {
		try {
			String body = requestBody != null ? objectMapper.writeValueAsString(requestBody) : null;
			logger.info("Request Body: {}", body);

			HttpRequest request = buildHttpRequest(uri, method, body, headers);
			logger.info("Request: {}", request);

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			logger.info("HTTP Status Code: {}", response.statusCode());

			if (response.statusCode() >= 200 && response.statusCode() < 300) {
				if (response.body() != null && !response.body().isBlank()) {
					logJsonResponse(response.body());

					// Deserialize based on provided type
					if (responseType == null || responseType.equals(Void.class)) {
						if (typeReference != null) {
							return objectMapper.readValue(response.body(), typeReference);
						} else if (responseType == null) {
							throw new IllegalArgumentException("Either responseType or typeReference must be provided.");
						}
					} else {
						return objectMapper.readValue(response.body(), responseType);
					}
				}
				return null;
			} else {
				throw new RuntimeException("API call failed. Status: " + response.statusCode() + " Response: " + response.body());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while making API call: " + e.getMessage(), e);
		}
	}

	/**
	 * Gets all items from a paged endpoint.
	 *
	 * @param endpoint The endpoint URL without query parameters.
	 * @param pageSize The number of items per page.
	 * @param clazz    The class of the items.
	 * @param <T>      The type of the items.
	 * @return A consolidated List of items of type T from all pages.
	 * @throws IOException          if an I/O error occurs when sending or receiving.
	 * @throws InterruptedException if the operation is interrupted.
	 */
	public static <T> List<T> getAllPaged(
			String endpoint,
			int pageSize,
			Class<T> clazz
	) throws IOException, InterruptedException {

		List<T> allItems = new ArrayList<>();
		int currentPage = 1;
		int totalPages = Integer.MAX_VALUE; // initial dummy value

		while (currentPage <= totalPages) {
			// Construct the paginated URL safely
			String paginatedUrl = constructPaginatedUrl(endpoint, currentPage, pageSize);
			URI uri = URI.create(paginatedUrl);

			HttpRequest request = buildHttpRequest(uri, "GET", null, getDefaultHeaders());
			logger.info("Request: " + request);

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			logger.info("HTTP Status Code: " + response.statusCode());

			if (response.statusCode() >= 200 && response.statusCode() < 300) {
				logJsonResponse(response.body());
			} else {
				throw new RuntimeException("Error: Received HTTP status code " + response.statusCode());
			}

			// Deserialize into PageResponse<T>
			JavaType pageResponseType =
					objectMapper.getTypeFactory().constructParametricType(PageResponse.class, clazz);
			PageResponse<T> pageResponse = objectMapper.readValue(response.body(), pageResponseType);
			if (pageResponse.getItems() != null) {
				allItems.addAll(pageResponse.getItems());
			}

			// Update totalPages from the pagination info.
			totalPages = pageResponse.getPagination().getTotalPages();
			currentPage++;
		}

		return allItems;
	}

	/**
	 * Constructs a paginated URL, correctly handling existing query parameters.
	 */
	private static String constructPaginatedUrl(String endpoint, int currentPage, int pageSize) {
		String baseUrl = config.getBaseUrl();

		if (endpoint.contains("?")) {
			// If endpoint already has query params, append using "&"
			return String.format("%s%s&page=%d&pageSize=%d", baseUrl, endpoint, currentPage, pageSize);
		} else {
			// Otherwise, append using "?"
			return String.format("%s%s?page=%d&pageSize=%d", baseUrl, endpoint, currentPage, pageSize);
		}
	}

	/**
	 * Builds an HttpRequest using the uri, method, headers and body provided.
	 *
	 * @param uri     The uri
	 * @param method  The HTTP method (GET, POST, PUT, etc.)
	 * @param body    The request body (can be null)
	 * @param headers The headers to add to the request
	 * @return The constructed HttpRequest
	 */
	public static HttpRequest buildHttpRequest(
			URI uri,
			String method,
			String body,
			Map<String, List<String>> headers
	) {
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);
		headers.forEach((key, values) ->
				values.forEach(value -> builder.header(key, value)));

		if ("POST".equalsIgnoreCase(method)) {
			builder.POST(HttpRequest.BodyPublishers.ofString(body));
		} else if ("PUT".equalsIgnoreCase(method)) {
			builder.PUT(HttpRequest.BodyPublishers.ofString(body));
		} else if ("GET".equalsIgnoreCase(method)) {
			builder.GET();
		}

		return builder.build();
	}

	public static void logJsonResponse(String responseBody) {
		if (responseBody == null || responseBody.isBlank()) {
			logger.info("Response Body: (empty)");
			return;
		}

		Object json = new JSONTokener(responseBody).nextValue();
		if (json instanceof JSONObject) {
			logger.info("Response Body: \n{}", ((JSONObject) json).toString(4));
		} else if (json instanceof JSONArray) {
			logger.info("Response Body: \n{}", ((JSONArray) json).toString(4));
		} else {
			logger.warn("Response is not a valid JSON object or array: {}", responseBody);
		}
	}

	public static <T> T sendAdminRequest(
			String endpoint,
			String method,
			Object requestBody,
			Class<T> responseType
	) {
		URI uri = URI.create(config.getAdminBaseUrl() + endpoint);
		return sendRequestInternal(uri, method, requestBody, getAdminHeaders(), responseType, null);
	}

	public static <T> T sendAdminRequest(
			String endpoint,
			String method,
			Object requestBody,
			TypeReference<T> typeReference
	) {
		URI uri = URI.create(config.getAdminBaseUrl() + endpoint);
		return sendRequestInternal(uri, method, requestBody, getAdminHeaders(), null, typeReference);
	}
}