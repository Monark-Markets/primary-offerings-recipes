package com.monarkmarkets;

import com.monarkmarkets.SubscriptionCalculator.SubscriptionAmount;
import com.monarkmarkets.primary.client.api.DocumentApi;
import com.monarkmarkets.primary.client.api.PreIpoCompanySpvApi;
import com.monarkmarkets.primary.client.api.TransactionActionApi;
import com.monarkmarkets.primary.client.api.TransactionApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateTransaction;
import com.monarkmarkets.primary.client.model.Document;
import com.monarkmarkets.primary.client.model.Pagination;
import com.monarkmarkets.primary.client.model.PreIPOCompanySPV;
import com.monarkmarkets.primary.client.model.PreIPOCompanySPVApiResponse;
import com.monarkmarkets.primary.client.model.SignDocument;
import com.monarkmarkets.primary.client.model.Transaction;
import com.monarkmarkets.primary.client.model.TransactionAction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.monarkmarkets.primary.client.model.PreIPOCompanySPV.MonarkStageEnum.PRIMARY_FUNDRAISE;
import static java.lang.Boolean.TRUE;
import static java.util.concurrent.ThreadLocalRandom.current;

@Slf4j
public class InvestorSubscriptionTransactionRecipes {

	private static final PreIpoCompanySpvApi preIpoCompanySpvApi = ApiFactory.getPreIpoCompanySpvApi();
	private static final TransactionApi transactionApi = ApiFactory.getTransactionApi();
	private static final TransactionActionApi transactionActionApi = ApiFactory.getTransactionActionApi();
	private static final DocumentApi documentApi = ApiFactory.getDocumentApi();

	/**
	 * Subscription Creation using Transaction API
	 * This flow describes the creation and execution of a Subscription on the primary market using the new unified Transaction API.
	 * This flow will start once an Investor inputs the amount of shares and dollars they want to invest and clicks submit,
	 * and it will be standardized regardless of where the Investor started the subscription process.
	 *
	 * The signing process has changed: completing all TransactionActions triggers automatic investor/advisor signing.
	 */
	public static Transaction submitInvestorSubscription(UUID investorId) {
		// Step 1: Get a PreIPOCompanySPV
		// SPVs can only accept Subscriptions from Investors when the MonarkStage field is set to PRIMARY_FUNDRAISE
		List<PreIPOCompanySPV> preIPOCompanySPVs = getAllPreIPOCompanySPVs(investorId);
		PreIPOCompanySPV preIPOCompanySPV = choosePreIPOCompanySPV(preIPOCompanySPVs);
		log.info("PreIPOCompanySPV: {}", preIPOCompanySPV);

		// Step 1.1: Calculate the Subscription Amount based on the subscription rules
		SubscriptionAmount amount = SubscriptionCalculator.calculateSubscriptionAmount(preIPOCompanySPV);

		// Step 2: Create a Transaction for the investor subscription to the PreIPOCompanySPV
		CreateTransaction createTransaction = CreateTransaction.builder()
				.targetAssetType(CreateTransaction.TargetAssetTypeEnum.PRE_IPO_COMPANY_SPV)
				.targetId(preIPOCompanySPV.getId())
				.side(CreateTransaction.SideEnum.SUBSCRIPTION)
				.investorId(investorId)
				.amountReservedDollars(amount.amountReservedDollars)
				.build();
		Transaction transaction = createTransaction(createTransaction);
		log.info("Transaction: {}", transaction);

		// Step 3: Get all TransactionActions by Transaction
		List<TransactionAction> transactionActions = getAllTransactionActions(transaction.getId());
		log.info("TransactionActions: {}", transactionActions);

		// Step 4: Complete Transaction Actions - Document Acknowledge
		// All TransactionAction that have type=DocumentAcknowledge will require document acknowledging
		List<TransactionAction> documentAcknowledgeActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.DOCUMENT_ACKNOWLEDGE)
				.toList();
		documentAcknowledgeActions.forEach(action -> {
			// Acknowledge the document
			completeTransactionAction(action.getId());
		});

		// Step 5: Complete Transaction Actions - Text Acknowledge
		// All TransactionAction that have type=TextAcknowledge will require text acknowledging
		List<TransactionAction> textAcknowledgeActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.TEXT_ACKNOWLEDGE)
				.toList();
		textAcknowledgeActions.forEach(action -> {
			// Acknowledge the text
			completeTransactionAction(action.getId());
		});

		// Step 6: Complete Transaction Actions - Sign Documents
		// All TransactionAction that have type=DocumentSign will require document signing
		List<TransactionAction> requireSigningActions = transactionActions.stream()
				.filter(action -> action.getType() == TransactionAction.TypeEnum.DOCUMENT_SIGN)
				.toList();
		requireSigningActions.forEach(action -> {
			// Get the document by id
			Document document = getDocument(UUID.fromString(action.getDataId()));
			log.info("Document: {}", document);

			// Sign the document
			signDocument(UUID.fromString(action.getDataId()), SignDocument.builder()
					.investorId(investorId)
					.metadata("SPV subscription signature " + UUID.randomUUID())
					.build());
		});

		// Step 7: Fetch final transaction to verify completion
		// Note: Completing all TransactionActions automatically triggers investor/advisor signing
		Transaction finalTransaction = getTransactionById(transaction.getId());
		log.info("Final Transaction status: {}", finalTransaction.getStatus());

		return finalTransaction;
	}

	private static PreIPOCompanySPV choosePreIPOCompanySPV(List<PreIPOCompanySPV> preIPOCompanySPVS) {
		List<PreIPOCompanySPV> eligibleSPVs = preIPOCompanySPVS.stream()
				.filter(spv -> spv.getRemainingShareAllocation() > 0 &&
						spv.getRemainingDollarAllocation() > 0 &&
						spv.getNumberOfSeatsRemaining() > 0 &&
						spv.getMonarkStage() == PRIMARY_FUNDRAISE &&
						TRUE.equals(spv.getIsApproved()))
				.toList();

		if (eligibleSPVs.isEmpty()) {
			throw new RuntimeException("No PreIPOCompanySPV found with remaining shares or dollar allocation");
		}

		return eligibleSPVs.get(current().nextInt(eligibleSPVs.size()));
	}

	private static List<PreIPOCompanySPV> getAllPreIPOCompanySPVs(UUID investorId) {
		try {
			log.info("Fetching all PreIPO company SPVs...");

			// Initialize parameters for pagination
			int pageSize = 100;
			int currentPage = 1;
			int totalPages = Integer.MAX_VALUE; // Placeholder until the total pages are known
			List<PreIPOCompanySPV> allPreIPOCompanySPVs = new ArrayList<>();

			// Loop through pages
			while (currentPage <= totalPages) {
				log.info("Fetching page {} with pageSize {}", currentPage, pageSize);
				PreIPOCompanySPVApiResponse response = preIpoCompanySpvApi.getAllPreIPOCompanySPVs(
						investorId,
						null,
						null,
						null,
						null,
						null,
						null,
						currentPage, // current page
						pageSize, // page size
						"Descending", // sortOrder
						"UpdatedAt" // sortBy
				);

				// Extract items and append to the result list
				if (response.getItems() != null) {
					allPreIPOCompanySPVs.addAll(response.getItems());
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

			log.info("Successfully fetched {} PreIPO company SPVs.", allPreIPOCompanySPVs.size());
			return allPreIPOCompanySPVs;

		} catch (ApiException e) {
			log.error("Error occurred while fetching PreIPO company SPVs: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch PreIPO company SPVs", e);
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

	private static void completeTransactionAction(UUID transactionActionId) {
		try {
			log.info("Complete transaction action: {}", transactionActionId);
			transactionActionApi.completeTransactionAction(transactionActionId, null);
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
}
