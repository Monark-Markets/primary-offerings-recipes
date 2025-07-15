package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.FinancialAdvisorApi;
import com.monarkmarkets.primary.client.api.FinancialInstitutionApi;
import com.monarkmarkets.primary.client.api.InvestorApi;
import com.monarkmarkets.primary.client.api.QuestionnaireAnswerApi;
import com.monarkmarkets.primary.client.api.QuestionnaireApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateFinancialAdvisor;
import com.monarkmarkets.primary.client.model.CreateInvestor;
import com.monarkmarkets.primary.client.model.CreateQuestionnaireAnswer;
import com.monarkmarkets.primary.client.model.CreateQuestionnaireQuestionAnswer;
import com.monarkmarkets.primary.client.model.FinancialAdvisor;
import com.monarkmarkets.primary.client.model.FinancialInstitution;
import com.monarkmarkets.primary.client.model.FinancialInstitutionApiResponse;
import com.monarkmarkets.primary.client.model.Investor;
import com.monarkmarkets.primary.client.model.ModifyIndividualInvestor;
import com.monarkmarkets.primary.client.model.Questionnaire;
import com.monarkmarkets.primary.client.model.QuestionnaireAnswer;
import com.monarkmarkets.primary.client.model.QuestionnaireApiResponse;
import com.monarkmarkets.primary.client.model.UpdateInvestor;
import com.monarkmarkets.primary.client.model.UpdateInvestorAccreditation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Slf4j
public class InvestorRecipes {

	private static final Random random = new Random();

	private static final RandomStringGenerator randomNumeric =
			new RandomStringGenerator.Builder()
					.withinRange('0', '9')
					.filteredBy(DIGITS)
					.build();

	private static final QuestionnaireAnswerApi questionnaireAnswerApi = ApiFactory.getQuestionnaireAnswerApi();
	private static final FinancialAdvisorApi financialAdvisorApi = ApiFactory.getFinancialAdvisorApi();
	private static final InvestorApi investorApi = ApiFactory.getInvestorApi();
	private static final QuestionnaireApi questionnaireApi = ApiFactory.getQuestionnaireApi();
	private static FinancialInstitutionApi financialInstitutionApi = ApiFactory.getFinancialInstitutionApi();

	/**
	 * Investor Onboarding
	 * Create an investor by passing the investorReferenceId, update accreditation status, and provide responses to questionnaire questions.
	 *
	 * @return the new Investor
	 */
	public static Investor investorOnboarding() {

		// Step 1: Get all financial institutions
		List<FinancialInstitution> financialInstitutions = getAllFinancialInstitutions();

		// Select a financial institution at random
		FinancialInstitution randomFinancialInstitution = financialInstitutions.get(
				current().nextInt(financialInstitutions.size()));
		log.info("Selected Financial Institution: {}", randomFinancialInstitution);
		UUID financialInstitutionId = randomFinancialInstitution.getId();

		// Step 2: Create an investor - Pending status
		String investorReferenceId = generateInvestorReferenceId();
		Investor investor = createInvestor(
				CreateInvestor.builder()
						.investorReferenceId(investorReferenceId)
						.financialInstitutionId(financialInstitutionId)
						.type(CreateInvestor.TypeEnum.INDIVIDUAL_INVESTOR)
						.build());
		log.info("Investor created: {}", investor);

		// Step 3: Get all questionnaires
		List<Questionnaire> questionnaires = getAllQuestionnaires();
		log.info("Questionnaires: {}", questionnaires);

		// Step 4: Answer all questions in questionnaires,
		// here we pick one at random for illustration purposes
		if (questionnaires != null && !questionnaires.isEmpty()) {
			for (Questionnaire questionnaire : questionnaires) {
				log.info("Questionnaire: {}", questionnaire);

				if (questionnaire.getQuestions() == null || questionnaire.getQuestions().isEmpty()) {
					log.info("No questions found for questionnaire: {}", questionnaire.getId());
					continue;
				}

				List<CreateQuestionnaireQuestionAnswer> createQuestionAnswers = questionnaire.getQuestions()
						.stream()
						.map(question -> {
							if (question.getOptions().isEmpty()) {
								return switch (question.getFormat()) {
									case INTEGER -> CreateQuestionnaireQuestionAnswer.builder()
											.questionnaireQuestionId(question.getId())
											.value(String.valueOf(random.nextInt(20)))
											.build();
									case BOOLEAN -> CreateQuestionnaireQuestionAnswer.builder()
											.questionnaireQuestionId(question.getId())
											.value(String.valueOf(random.nextBoolean()))
											.build();
									default -> CreateQuestionnaireQuestionAnswer.builder()
											.questionnaireQuestionId(question.getId())
											.value(String.valueOf(random.nextInt(2)))
											.build();
								};
							} else {
								return CreateQuestionnaireQuestionAnswer.builder()
										.questionnaireQuestionId(question.getId())
										.value(question.getOptions().get(random.nextInt(question.getOptions().size())))
										.build();
							}
						})
						.toList();
				CreateQuestionnaireAnswer createQuestionnaireAnswer = CreateQuestionnaireAnswer.builder()
						.questionnaireId(questionnaire.getId())
						.investorId(investor.getId())
						.createQuestionAnswers(createQuestionAnswers)
						.build();

				QuestionnaireAnswer questionnaireAnswer = createQuestionnaireAnswer(createQuestionnaireAnswer);
				log.info("QuestionnaireAnswer: {}", questionnaireAnswer);
			}
		}

		// Step 5: Update accreditation status
		// here we pick one at random for illustration purposes
		updateInvestorAccreditation(UpdateInvestorAccreditation.builder()
				.investorId(investor.getId())
				.accreditationStatus(UpdateInvestorAccreditation.AccreditationStatusEnum.KNOWLEDGEABLE_EMPLOYEE)
				.build());

		// Step 6: Create a CreateFinancialAdvisor object with required fields
		FinancialAdvisor financialAdvisor = createFinancialAdvisor(CreateFinancialAdvisor.builder()
				.financialInstitutionId(financialInstitutionId)
				.crdNumber(randomNumeric.generate(8)) // Replace with actual CRD number if available
				.iardNumber(randomNumeric.generate(8)) // Replace with actual IARD number if available
				.firstName("Jane")
				.lastName("Advisor")
				.mailingAddress("123 Main St")
				.mailingCity("New York")
				.mailingState("NY")
				.mailingZipCode("10001")
				.mailingCountryCode("US")
				.branchNumber("001")
				.phoneCountryCode("1")
				.phoneNumber("1234567890")
				.phoneExtension("123")
				.emailAddress("johnadvisor@example.com")
				.fax("123-456-7890")
				.prefix("Mrs")
				.broker("ExampleBroker")
				.build());
		log.info("Financial Advisor created successfully: {}", financialAdvisor);

		// Step 7: Update investor details
		Investor updatedInvestor = updateInvestor(UpdateInvestor.builder()
				.id(investor.getId())
				.investorReferenceId(investor.getInvestorReferenceId())
				.financialAdvisorId(financialAdvisor.getId())
				.individualInvestor(
						ModifyIndividualInvestor.builder()
								.firstName("John")
								.lastName("Doe")
								.email("john.doe@example.com")
								.exemptPayeeCode("1")
								.street1("123 Main St")
								.city("Metropolis")
								.state("NY")
								.zipCode("10001")
								.countryCode("US")
								.phoneCountryCode("1")
								.phoneNumber("5551234567")
								.mailingStreet1("123 Main St")
								.mailingAddressCity("Metropolis")
								.mailingAddressState("NY")
								.mailingAddressZipCode("10001")
								.mailingAddressCountry("US")
								.citizenship("US")
								.dateOfBirth(LocalDate.parse("1980-01-01"))
								.taxId("792-61-0047")
								.passportNumber("X1234567")
								.isSubscriptionAdvisorOrERA(true)
								.isUSBased(true)
								.build()
				)
				.build());
		log.info("Investor updated: {}", updatedInvestor);

		// Step 8: Get investor by id
		Investor investorById = getInvestorById(investor.getId());
		log.info("Investor by id: {}", investorById);

		return investorById;
	}

	private static String generateInvestorReferenceId() {
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
				.withinRange('0', 'z')
				.filteredBy(LETTERS, DIGITS)
				.get();
		return "Investor-" + generator.generate(20);
	}

	private static Investor createInvestor(CreateInvestor createInvestor) {
		try {
			log.info("Create investor {}: {}", createInvestor.getInvestorReferenceId(), createInvestor);
			return investorApi.primaryV1InvestorPost(createInvestor);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Questionnaire> getAllQuestionnaires() {
		try {
			log.info("Fetch all questionnaires");
			QuestionnaireApiResponse questionnaireApiResponse =
					questionnaireApi.primaryV1QuestionnaireGet(null, null, null);
			return questionnaireApiResponse.getItems();
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static QuestionnaireAnswer createQuestionnaireAnswer(CreateQuestionnaireAnswer createQuestionnaireAnswer) {
		try {
			log.info("Create questionnaire answer: {}", createQuestionnaireAnswer);
			return questionnaireAnswerApi.primaryV1QuestionnaireAnswerPost(createQuestionnaireAnswer);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<FinancialInstitution> getAllFinancialInstitutions() {
		try {
			FinancialInstitutionApiResponse financialInstitutionResponse = financialInstitutionApi
					.primaryV1FinancialInstitutionGet(null, null, null);
			return financialInstitutionResponse.getItems();
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static FinancialAdvisor createFinancialAdvisor(CreateFinancialAdvisor createFinancialAdvisor) {
		try {
			log.info("Create financial advisor: {}", createFinancialAdvisor);
			return financialAdvisorApi.primaryV1FinancialAdvisorPost(createFinancialAdvisor);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static Investor updateInvestor(UpdateInvestor updateInvestor) {
		try {
			log.info("Update investor details: {}", updateInvestor);
			return investorApi.primaryV1InvestorPut(updateInvestor);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static void updateInvestorAccreditation(UpdateInvestorAccreditation updateInvestorAccreditation) {
		try {
			log.info("Update accreditation status: {}", updateInvestorAccreditation);
			investorApi.primaryV1InvestorAccreditationPut(updateInvestorAccreditation);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static Investor getInvestorById(UUID investorId) {
		try {
			log.info("Get investor by id: {}", investorId);
			return investorApi.primaryV1InvestorIdGet(investorId);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}
}