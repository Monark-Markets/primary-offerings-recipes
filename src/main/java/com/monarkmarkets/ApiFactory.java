package com.monarkmarkets;

import com.monarkmarkets.api.primary.webapi.api.DocumentApi;
import com.monarkmarkets.api.primary.webapi.api.FinancialAdvisorApi;
import com.monarkmarkets.api.primary.webapi.api.FinancialInstitutionApi;
import com.monarkmarkets.api.primary.webapi.api.IndicationOfInterestApi;
import com.monarkmarkets.api.primary.webapi.api.InvestorApi;
import com.monarkmarkets.api.primary.webapi.api.InvestorSubscriptionActionApi;
import com.monarkmarkets.api.primary.webapi.api.InvestorSubscriptionApi;
import com.monarkmarkets.api.primary.webapi.api.PreIpoCompanyApi;
import com.monarkmarkets.api.primary.webapi.api.PreIpoCompanySpvApi;
import com.monarkmarkets.api.primary.webapi.api.QuestionnaireAnswerApi;
import com.monarkmarkets.api.primary.webapi.api.QuestionnaireApi;
import com.monarkmarkets.api.primary.webapi.api.RegisteredFundApi;
import com.monarkmarkets.api.primary.webapi.api.RegisteredFundSubscriptionApi;
import com.monarkmarkets.api.primary.webapi.api.VersionApi;
import com.monarkmarkets.api.primary.webapi.invoker.ApiClient;
import com.monarkmarkets.api.primary.webapi.invoker.Configuration;
import com.monarkmarkets.api.primary.webapi.invoker.auth.ApiKeyAuth;
import lombok.Synchronized;

public class ApiFactory {

	private static final ApiClient apiClient = getConfiguredApiClient();

	// Lazy-loaded singletons
	private static InvestorApi investorApi;
	private static PreIpoCompanyApi preIpoCompanyApi;
	private static IndicationOfInterestApi indicationOfInterestApi;
	private static QuestionnaireApi questionnaireApi;
	private static QuestionnaireAnswerApi questionnaireAnswerApi;
	private static PreIpoCompanySpvApi preIpoCompanySpvApi;
	private static InvestorSubscriptionApi investorSubscriptionApi;
	private static InvestorSubscriptionActionApi investorSubscriptionActionApi;
	private static DocumentApi documentApi;
	private static FinancialAdvisorApi financialAdvisorApi;
	private static RegisteredFundApi registeredFundApi;
	private static RegisteredFundSubscriptionApi registeredFundSubscriptionApi;
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
	public static RegisteredFundSubscriptionApi getRegisteredFundSubscriptionApi() {
		if (registeredFundSubscriptionApi == null) {
			registeredFundSubscriptionApi = new RegisteredFundSubscriptionApi(apiClient);
		}
		return registeredFundSubscriptionApi;
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
