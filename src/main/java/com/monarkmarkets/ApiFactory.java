package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.DocumentApi;
import com.monarkmarkets.primary.client.api.FinancialAdvisorApi;
import com.monarkmarkets.primary.client.api.FinancialInstitutionApi;
import com.monarkmarkets.primary.client.api.IndicationOfInterestApi;
import com.monarkmarkets.primary.client.api.IndicationOfInterestControllerV2Api;
import com.monarkmarkets.primary.client.api.InvestorApi;
import com.monarkmarkets.primary.client.api.InvestorSubscriptionActionApi;
import com.monarkmarkets.primary.client.api.InvestorSubscriptionApi;
import com.monarkmarkets.primary.client.api.PreIpoCompanyApi;
import com.monarkmarkets.primary.client.api.PreIpoCompanySpvApi;
import com.monarkmarkets.primary.client.api.QuestionnaireAnswerApi;
import com.monarkmarkets.primary.client.api.QuestionnaireApi;
import com.monarkmarkets.primary.client.api.RegisteredFundApi;
import com.monarkmarkets.primary.client.api.TransactionActionApi;
import com.monarkmarkets.primary.client.api.TransactionApi;
import com.monarkmarkets.primary.client.api.VersionApi;
import com.monarkmarkets.primary.client.invoker.ApiClient;
import com.monarkmarkets.primary.client.invoker.Configuration;
import com.monarkmarkets.primary.client.invoker.auth.ApiKeyAuth;
import lombok.Synchronized;

public class ApiFactory {

	private static final ApiClient apiClient = getConfiguredApiClient();

	// Lazy-loaded singletons
	private static InvestorApi investorApi;
	private static PreIpoCompanyApi preIpoCompanyApi;
	private static IndicationOfInterestApi indicationOfInterestApi;
	private static IndicationOfInterestControllerV2Api indicationOfInterestControllerV2Api;
	private static QuestionnaireApi questionnaireApi;
	private static QuestionnaireAnswerApi questionnaireAnswerApi;
	private static PreIpoCompanySpvApi preIpoCompanySpvApi;
	private static InvestorSubscriptionApi investorSubscriptionApi;
	private static InvestorSubscriptionActionApi investorSubscriptionActionApi;
	private static DocumentApi documentApi;
	private static FinancialAdvisorApi financialAdvisorApi;
	private static RegisteredFundApi registeredFundApi;
	private static TransactionApi transactionApi;
	private static TransactionActionApi transactionActionApi;
	private static FinancialInstitutionApi financialInstitutionApi;
	private static VersionApi versionApi;

	public static ApiClient getConfiguredApiClient() {
		Config config = Config.getInstance();
		ApiClient apiClient = Configuration.getDefaultApiClient();
		apiClient.setBasePath(config.getBaseUrl());

		ApiKeyAuth bearer = (ApiKeyAuth) apiClient.getAuthentication("Bearer");
		bearer.setApiKey(config.getApiKey());

		return apiClient;
	}

	@Synchronized
	public static InvestorApi getInvestorApi() {
		if (investorApi == null) {
			investorApi = new InvestorApi(apiClient);
		}
		return investorApi;
	}

	@Synchronized
	public static PreIpoCompanyApi getPreIpoCompanyApi() {
		if (preIpoCompanyApi == null) {
			preIpoCompanyApi = new PreIpoCompanyApi(apiClient);
		}
		return preIpoCompanyApi;
	}

	@Synchronized
	public static IndicationOfInterestApi getIndicationOfInterestApi() {
		if (indicationOfInterestApi == null) {
			indicationOfInterestApi = new IndicationOfInterestApi(apiClient);
		}
		return indicationOfInterestApi;
	}

	@Synchronized
	public static IndicationOfInterestControllerV2Api getIndicationOfInterestControllerV2Api() {
		if (indicationOfInterestControllerV2Api == null) {
			indicationOfInterestControllerV2Api = new IndicationOfInterestControllerV2Api(apiClient);
		}
		return indicationOfInterestControllerV2Api;
	}

	@Synchronized
	public static QuestionnaireApi getQuestionnaireApi() {
		if (questionnaireApi == null) {
			questionnaireApi = new QuestionnaireApi(apiClient);
		}
		return questionnaireApi;
	}

	@Synchronized
	public static QuestionnaireAnswerApi getQuestionnaireAnswerApi() {
		if (questionnaireAnswerApi == null) {
			questionnaireAnswerApi = new QuestionnaireAnswerApi(apiClient);
		}
		return questionnaireAnswerApi;
	}

	@Synchronized
	public static PreIpoCompanySpvApi getPreIpoCompanySpvApi() {
		if (preIpoCompanySpvApi == null) {
			preIpoCompanySpvApi = new PreIpoCompanySpvApi(apiClient);
		}
		return preIpoCompanySpvApi;
	}

	@Synchronized
	public static InvestorSubscriptionApi getInvestorSubscriptionApi() {
		if (investorSubscriptionApi == null) {
			investorSubscriptionApi = new InvestorSubscriptionApi(apiClient);
		}
		return investorSubscriptionApi;
	}

	@Synchronized
	public static InvestorSubscriptionActionApi getInvestorSubscriptionActionApi() {
		if (investorSubscriptionActionApi == null) {
			investorSubscriptionActionApi = new InvestorSubscriptionActionApi(apiClient);
		}
		return investorSubscriptionActionApi;
	}

	@Synchronized
	public static DocumentApi getDocumentApi() {
		if (documentApi == null) {
			documentApi = new DocumentApi(apiClient);
		}
		return documentApi;
	}

	@Synchronized
	public static FinancialAdvisorApi getFinancialAdvisorApi() {
		if (financialAdvisorApi == null) {
			financialAdvisorApi = new FinancialAdvisorApi(apiClient);
		}
		return financialAdvisorApi;
	}

	@Synchronized
	public static RegisteredFundApi getRegisteredFundApi() {
		if (registeredFundApi == null) {
			registeredFundApi = new RegisteredFundApi(apiClient);
		}
		return registeredFundApi;
	}

	@Synchronized
	public static TransactionApi getTransactionApi() {
		if (transactionApi == null) {
			transactionApi = new TransactionApi(apiClient);
		}
		return transactionApi;
	}

	@Synchronized
	public static TransactionActionApi getTransactionActionApi() {
		if (transactionActionApi == null) {
			transactionActionApi = new TransactionActionApi(apiClient);
		}
		return transactionActionApi;
	}

	@Synchronized
	public static FinancialInstitutionApi getFinancialInstitutionApi() {
		if (financialInstitutionApi == null) {
			financialInstitutionApi = new FinancialInstitutionApi(apiClient);
		}
		return financialInstitutionApi;
	}

	@Synchronized
	public static VersionApi getVersionApi() {
		if (versionApi == null) {
			versionApi = new VersionApi(apiClient);
		}
		return versionApi;
	}
}
