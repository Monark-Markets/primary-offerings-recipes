package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.primary.client.api.DocumentApi;
import com.monarkmarkets.primary.client.api.QuestionnaireAnswerApi;
import com.monarkmarkets.primary.client.api.QuestionnaireApi;
import com.monarkmarkets.primary.client.api.RegisteredFundApi;
import com.monarkmarkets.primary.client.api.TransactionActionApi;
import com.monarkmarkets.primary.client.api.TransactionApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateQuestionnaireAnswer;
import com.monarkmarkets.primary.client.model.CreateQuestionnaireQuestionAnswer;
import com.monarkmarkets.primary.client.model.CreateTransaction;
import com.monarkmarkets.primary.client.model.Document;
import com.monarkmarkets.primary.client.model.DocumentApiResponse;
import com.monarkmarkets.primary.client.model.Pagination;
import com.monarkmarkets.primary.client.model.Questionnaire;
import com.monarkmarkets.primary.client.model.QuestionnaireAnswer;
import com.monarkmarkets.primary.client.model.RegisteredFund;
import com.monarkmarkets.primary.client.model.RegisteredFundApiResponse;
import com.monarkmarkets.primary.client.model.SignDocument;
import com.monarkmarkets.primary.client.model.Transaction;
import com.monarkmarkets.primary.client.model.TransactionAction;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class RegisteredFundTransactionRecipes {

	private static final Random random = new Random();

	private static final RegisteredFundApi registeredFundApi = ApiFactory.getRegisteredFundApi();
	private static final TransactionApi transactionApi = ApiFactory.getTransactionApi();
	private static final TransactionActionApi transactionActionApi = ApiFactory.getTransactionActionApi();
	private static final DocumentApi documentApi = ApiFactory.getDocumentApi();
	private static final QuestionnaireApi questionnaireApi = ApiFactory.getQuestionnaireApi();
	private static final QuestionnaireAnswerApi questionnaireAnswerApi = ApiFactory.getQuestionnaireAnswerApi();

	/**
	 * Registered Fund Subscription Creation using Transaction API
	 * This flow creates and executes a Registered Fund subscription using the new unified Transaction API.
	 * The signing process has changed: completing all TransactionActions triggers automatic investor/advisor signing.
	 */
	public static Transaction submitRegisteredFundSubscription(UUID investorId) {
		// Step 1: Get all registered funds and select one
		List<RegisteredFund> registeredFunds = getAllRegisteredFunds();
		RegisteredFund registeredFund = registeredFunds.stream()
				.filter(rf -> "BCRED".equals(rf.getSymbol()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No Registered Fund found with symbol BCRED"));
		log.info("Selected Registered Fund: {}", registeredFund);

		// Step 2: Define the subscription amount
		final double subscriptionAmount = 10000.00;

		// Step 3: Create the transaction for registered fund subscription
		CreateTransaction createTransaction = CreateTransaction.builder()
				.targetAssetType(CreateTransaction.TargetAssetTypeEnum.REGISTERED_FUND)
				.targetId(registeredFund.getId())
				.side(CreateTransaction.SideEnum.SUBSCRIPTION)
				.investorId(investorId)
				.amountReservedDollars(subscriptionAmount)
				.build();

		Transaction transaction = createTransaction(createTransaction);
		log.info("Successfully created Transaction: {}", transaction);

		// Step 4: Get all transaction actions
		List<TransactionAction> transactionActions = getAllTransactionActions(transaction.getId());
		log.info("TransactionActions: {}", transactionActions);

		// Step 6: Complete Transaction Actions - Document Acknowledge
		List<TransactionAction> documentAcknowledgeActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.DOCUMENT_ACKNOWLEDGE)
				.toList();
		documentAcknowledgeActions.forEach(action -> {
			completeTransactionAction(action.getId());
		});

		// Step 7: Complete Transaction Actions - Text Acknowledge
		List<TransactionAction> textAcknowledgeActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.TEXT_ACKNOWLEDGE)
				.toList();
		textAcknowledgeActions.forEach(action -> {
			completeTransactionAction(action.getId());
		});

		// Step 8: Complete Transaction Actions - Questionnaire
		List<TransactionAction> questionnaireActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.QUESTIONNAIRE)
				.toList();
		for (TransactionAction questionnaireAction : questionnaireActions) {
			if (questionnaireAction.getDataId() == null) {
				log.warn("Questionnaire action {} has no dataId, skipping", questionnaireAction.getId());
				continue;
			}

			// Fetch questionnaire details using dataId
			UUID questionnaireId = UUID.fromString(questionnaireAction.getDataId());
			Questionnaire questionnaire = getQuestionnaireById(questionnaireId, investorId);
			log.info("Processing questionnaire: {} (ID: {})", questionnaire.getName(), questionnaire.getId());

			if (questionnaire.getQuestions() == null || questionnaire.getQuestions().isEmpty()) {
				log.info("No questions found for questionnaire: {}", questionnaire.getId());
				continue;
			}

			// Generate answers for all questions
			List<CreateQuestionnaireQuestionAnswer> createQuestionAnswers = questionnaire.getQuestions()
					.stream()
					.map(question -> {
						String value;
						switch (question.getFormat()) {
							case INTEGER -> value = String.valueOf(random.nextInt(100));
							case FLOAT -> value = String.format("%.2f", random.nextDouble() * 100);
							case PERCENTAGE -> value = String.format("%.2f", random.nextDouble() * 100);
							case MULTIPLE_CHOICE_SINGLE -> {
								if (question.getOptions() != null && !question.getOptions().isEmpty()) {
									value = question.getOptions().get(random.nextInt(question.getOptions().size()));
								} else {
									value = "No option available";
								}
							}
							case MULTIPLE_CHOICE_MULTIPLE -> {
								if (question.getOptions() != null && !question.getOptions().isEmpty()) {
									// Select 1-3 random options
									int numSelections = Math.min(random.nextInt(3) + 1, question.getOptions().size());
									List<String> shuffled = new ArrayList<>(question.getOptions());
									java.util.Collections.shuffle(shuffled, random);
									value = String.join(",", shuffled.subList(0, numSelections));
								} else {
									value = "No options available";
								}
							}
							case BOOLEAN -> value = "true";
							case DATE -> {
								// Random date within the last year
								LocalDate randomDate = LocalDate.now().minusDays(random.nextInt(365));
								value = randomDate.toString();
							}
							case TEXT -> value = "Sample text answer";
							case EMAIL -> value = "test@example.com";
							case SCALE -> {
								// Assuming default scale 0-10 if not specified
								int scaleValue = random.nextInt(11);
								value = String.valueOf(scaleValue);
							}
							default -> value = "Default answer";
						}
						return CreateQuestionnaireQuestionAnswer.builder()
								.questionnaireQuestionId(question.getId())
								.value(value)
								.build();
					})
					.toList();

			// Submit questionnaire answers
			CreateQuestionnaireAnswer createQuestionnaireAnswer = CreateQuestionnaireAnswer.builder()
					.questionnaireId(questionnaire.getId())
					.investorId(investorId)
					.createQuestionAnswers(createQuestionAnswers)
					.build();

			QuestionnaireAnswer questionnaireAnswer = createQuestionnaireAnswer(createQuestionnaireAnswer);
			log.info("Submitted questionnaire answers: {}", questionnaireAnswer.getId());

			// Complete the questionnaire action
			completeTransactionAction(questionnaireAction.getId());
		}

		// Step 9: Complete Transaction Actions - Sign Documents
		List<TransactionAction> requireSigningActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.DOCUMENT_SIGN)
				.toList();
		requireSigningActions.forEach(action -> {
			completeTransactionAction(action.getId());
		});

		// Step 10: Fetch final transaction to verify completion
		// Note: Completing all TransactionActions automatically triggers investor/advisor signing
		Transaction finalTransaction = getTransactionById(transaction.getId());
		log.info("Final Transaction status: {}", finalTransaction.getStatus());

		// Step 11: Fetch signed documents using V2 Document API
		List<Document> signedDocuments = getSignedDocumentsByTransactionId(transaction.getId());
		log.info("Retrieved {} signed document(s)", signedDocuments.size());
		signedDocuments.forEach(doc -> {
			log.info("  Document: {} (Type: {}, ID: {})", doc.getName(), doc.getType(), doc.getId());
		});

		// Step 12: Download all signed documents
		signedDocuments.forEach(doc -> {
			try {
				File downloadedFile = downloadDocument(doc);
				log.info("Successfully downloaded: {}", downloadedFile.getAbsolutePath());
			} catch (Exception e) {
				log.error("Failed to download document {}: {}", doc.getName(), e.getMessage(), e);
			}
		});

		return finalTransaction;
	}

	private static List<RegisteredFund> getAllRegisteredFunds() {
		try {
			log.info("Get all registered funds");

			// Initialize parameters for pagination
			int pageSize = 100;
			int currentPage = 1;
			int totalPages = Integer.MAX_VALUE; // Placeholder until the total pages are known
			List<RegisteredFund> allRegisteredFunds = new ArrayList<>();

			// Loop through pages
			while (currentPage <= totalPages) {
				log.info("Fetching page {} with pageSize {}", currentPage, pageSize);
				RegisteredFundApiResponse response = registeredFundApi.getAllRegisteredFunds(
						currentPage,
						pageSize,
						null,
						null,
						null,
						null,
						null
				);

				// Extract items and append to the result list
				if (response.getItems() != null) {
					allRegisteredFunds.addAll(response.getItems());
				}

				// Get pagination info
				Pagination pagination = response.getPagination();
				if (pagination != null) {
					totalPages = pagination.getTotalPages();
					log.info("Total pages: {}", totalPages);
				} else {
					log.warn("Pagination information is missing, stopping iteration.");
					break;
				}

				currentPage++;
			}

			log.info("Successfully fetched {} registered funds.", allRegisteredFunds.size());
			return allRegisteredFunds;

		} catch (ApiException e) {
			log.error("Error occurred while fetching registered funds: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch registered funds", e);
		}
	}

	private static Transaction createTransaction(CreateTransaction createTransaction) {
		try {
			log.info("Create Transaction: {}", createTransaction);
			return transactionApi.createTransaction(createTransaction);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<TransactionAction> getAllTransactionActions(UUID transactionId) {
		try {
			log.info("GetAllTransactionActions for transactionId: {}", transactionId);
			return transactionActionApi.getAllTransactionActions(transactionId);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static void completeTransactionAction(UUID transactionActionId) {
		try {
			log.info("Complete transaction action: {}", transactionActionId);
			transactionActionApi.completeTransactionAction(transactionActionId, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Document getDocument(UUID documentId) {
		try {
			log.info("Get document: {}", documentId);
			return documentApi.getDocumentById(documentId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Document> getSignedDocumentsByTransactionId(UUID transactionId) {
		try {
			log.info("Get signed documents for transaction: {}", transactionId);
			// Note: Using V2 Document API endpoint GET /primary/v2/document?transactionId={id}
			// This endpoint is available but may require manual API call if not yet in generated client

			// Attempt to use the custom invokeAPI method to call the V2 endpoint
			TypeReference<DocumentApiResponse> returnType = new TypeReference<>() {};

			String url = ApiFactory.getConfiguredApiClient().getBaseURL() + "/primary/v2/document?transactionId=" + transactionId;
			DocumentApiResponse response = documentApi.invokeAPI(url, "GET", null, returnType, new HashMap<>());

			return response.getItems() != null ? response.getItems() : new ArrayList<>();
		} catch (Exception e) {
			log.error("Error fetching signed documents: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private static void signDocument(UUID documentId, SignDocument signDocument) {
		try {
			log.info("Sign document: {}, signDocument: {}", documentId, signDocument);
			documentApi.signDocument(documentId, signDocument);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Transaction getTransactionById(UUID transactionId) {
		try {
			log.info("Get transaction by id: {}", transactionId);
			return transactionApi.getTransactionById(transactionId, true);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Downloads a document by following the 302 redirect to S3.
	 * The document URL returns a 302 redirect with the S3 presigned URL in the Location header.
	 *
	 * @param document The document to download
	 * @return The downloaded file
	 */
	private static File downloadDocument(Document document) {
		try {
			log.info("Downloading document: {} (ID: {})", document.getName(), document.getId());

			// Get API configuration
			Config config = Config.getInstance();
			String apiKey = config.getApiKey();
			String baseUrl = config.getBaseUrl();

			// Build the full download URL
			if (document.getUrl() == null) {
				throw new RuntimeException("Document URL is null");
			}
			String downloadUrl = document.getUrl().toString();
			if (!downloadUrl.startsWith("http")) {
				// If it's a relative URL, prepend the base URL
				downloadUrl = baseUrl + downloadUrl;
			}

			// Create HTTP client that doesn't follow redirects automatically
			HttpClient client = HttpClient.newBuilder()
					.followRedirects(HttpClient.Redirect.NEVER)
					.build();

			// First request to get the 302 redirect location
			HttpRequest initialRequest = HttpRequest.newBuilder()
					.uri(URI.create(downloadUrl))
					.header("Authorization", "Bearer " + apiKey)
					.GET()
					.build();

			log.info("Fetching redirect URL from: {}", downloadUrl);
			HttpResponse<Void> initialResponse = client.send(initialRequest, HttpResponse.BodyHandlers.discarding());

			// Check for 302 redirect
			if (initialResponse.statusCode() != 302) {
				throw new RuntimeException("Expected 302 redirect but got: " + initialResponse.statusCode());
			}

			// Get the S3 presigned URL from Location header
			String s3Url = initialResponse.headers().firstValue("location")
					.orElseThrow(() -> new RuntimeException("No Location header in 302 response"));

			log.info("Following redirect to S3: {}", s3Url);

			// Extract filename from S3 URL
			String filename = extractFilenameFromS3Url(s3Url);
			if (filename == null || filename.isEmpty()) {
				// Fallback to document name if we can't extract from URL
				filename = document.getName();
				if (filename != null && !filename.contains(".")) {
					filename += ".pdf"; // Default to PDF if no extension
				} else if (filename == null) {
					filename = "document_" + document.getId() + ".pdf";
				}
			}

			// Download the file from S3
			HttpRequest downloadRequest = HttpRequest.newBuilder()
					.uri(URI.create(s3Url))
					.GET()
					.build();

			File outputFile = new File(filename);
			HttpResponse<InputStream> downloadResponse = client.send(downloadRequest, HttpResponse.BodyHandlers.ofInputStream());

			if (downloadResponse.statusCode() != 200) {
				throw new RuntimeException("Failed to download from S3. Status: " + downloadResponse.statusCode());
			}

			// Save to file
			try (InputStream inputStream = downloadResponse.body();
				 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			}

			log.info("Document saved as: {}", outputFile.getAbsolutePath());
			return outputFile;

		} catch (Exception e) {
			log.error("Error downloading document: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to download document", e);
		}
	}

	/**
	 * Extracts the filename from an S3 presigned URL.
	 * The filename is typically in the path or in the response-content-disposition query parameter.
	 *
	 * @param s3Url The S3 presigned URL
	 * @return The extracted filename, or null if not found
	 */
	private static String extractFilenameFromS3Url(String s3Url) {
		try {
			URI uri = URI.create(s3Url);
			String query = uri.getQuery();

			// First, try to extract from response-content-disposition parameter
			if (query != null && query.contains("response-content-disposition")) {
				String[] params = query.split("&");
				for (String param : params) {
					if (param.startsWith("response-content-disposition=")) {
						String disposition = URLDecoder.decode(param.substring("response-content-disposition=".length()), StandardCharsets.UTF_8);
						// Look for filename in disposition: attachment; filename="document.pdf"
						if (disposition.contains("filename=")) {
							String filename = disposition.substring(disposition.indexOf("filename=") + 9);
							filename = filename.replaceAll("^\"|\"$", ""); // Remove surrounding quotes
							return filename;
						}
					}
				}
			}

			// Fallback: extract from path
			String path = uri.getPath();
			if (path != null && !path.isEmpty()) {
				int lastSlash = path.lastIndexOf('/');
				if (lastSlash >= 0 && lastSlash < path.length() - 1) {
					String filename = path.substring(lastSlash + 1);
					// Remove query parameters if they ended up in the filename
					int questionMark = filename.indexOf('?');
					if (questionMark > 0) {
						filename = filename.substring(0, questionMark);
					}
					return URLDecoder.decode(filename, StandardCharsets.UTF_8);
				}
			}

			return null;
		} catch (Exception e) {
			log.warn("Failed to extract filename from S3 URL: {}", e.getMessage());
			return null;
		}
	}

	private static Questionnaire getQuestionnaireById(UUID questionnaireId, UUID investorId) {
		try {
			log.info("Get questionnaire by id: {} for investor: {}", questionnaireId, investorId);
			return questionnaireApi.getQuestionnaireById(questionnaireId, investorId);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static QuestionnaireAnswer createQuestionnaireAnswer(CreateQuestionnaireAnswer createQuestionnaireAnswer) {
		try {
			log.info("Create questionnaire answer: {}", createQuestionnaireAnswer);
			return questionnaireAnswerApi.createQuestionnaireAnswer(createQuestionnaireAnswer);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}
}
