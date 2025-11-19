package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.DocumentApi;
import com.monarkmarkets.primary.client.api.RegisteredFundApi;
import com.monarkmarkets.primary.client.api.TransactionActionApi;
import com.monarkmarkets.primary.client.api.TransactionApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateTransaction;
import com.monarkmarkets.primary.client.model.Document;
import com.monarkmarkets.primary.client.model.Pagination;
import com.monarkmarkets.primary.client.model.RegisteredFund;
import com.monarkmarkets.primary.client.model.RegisteredFundApiResponse;
import com.monarkmarkets.primary.client.model.SignDocument;
import com.monarkmarkets.primary.client.model.Transaction;
import com.monarkmarkets.primary.client.model.TransactionAction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class RegisteredFundTransactionRecipes {

	private static final RegisteredFundApi registeredFundApi = ApiFactory.getRegisteredFundApi();
	private static final TransactionApi transactionApi = ApiFactory.getTransactionApi();
	private static final TransactionActionApi transactionActionApi = ApiFactory.getTransactionActionApi();
	private static final DocumentApi documentApi = ApiFactory.getDocumentApi();

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

		// Step 4: PDF Preview Generation (commented out - not yet confirmed where this moved)
		// UUID transactionId = transaction.getId();
		// File subscriptionPreviewPdf = createSubscriptionPreviewPdf(transactionId);
		// log.info("Successfully generated subscription preview. PDF location: {}", subscriptionPreviewPdf.getAbsolutePath());

		// Step 5: Get all transaction actions
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

		// Step 8: Complete Transaction Actions - Sign Documents
		List<TransactionAction> requireSigningActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.DOCUMENT_SIGN)
				.toList();
		requireSigningActions.forEach(action -> {
			completeTransactionAction(action.getId());
		});

		// Step 9: Fetch final transaction to verify completion
		// Note: Completing all TransactionActions automatically triggers investor/advisor signing
		Transaction finalTransaction = getTransactionById(transaction.getId());
		log.info("Final Transaction status: {}", finalTransaction.getStatus());

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
			return transactionApi.getTransactionById(transactionId);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}
}
