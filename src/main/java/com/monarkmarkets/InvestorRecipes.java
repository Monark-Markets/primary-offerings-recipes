package com.monarkmarkets;

import com.monarkmarkets.api.primary.webapi.api.FinancialAdvisorApi;
import com.monarkmarkets.api.primary.webapi.api.FinancialInstitutionApi;
import com.monarkmarkets.api.primary.webapi.api.InvestorApi;
import com.monarkmarkets.api.primary.webapi.api.QuestionnaireAnswerApi;
import com.monarkmarkets.api.primary.webapi.api.QuestionnaireApi;
import com.monarkmarkets.api.primary.webapi.invoker.ApiException;
import com.monarkmarkets.api.primary.webapi.model.CreateFinancialAdvisor;
import com.monarkmarkets.api.primary.webapi.model.CreateInvestor;
import com.monarkmarkets.api.primary.webapi.model.CreateQuestionnaireAnswer;
import com.monarkmarkets.api.primary.webapi.model.CreateQuestionnaireQuestionAnswer;
import com.monarkmarkets.api.primary.webapi.model.FinancialAdvisor;
import com.monarkmarkets.api.primary.webapi.model.FinancialInstitution;
import com.monarkmarkets.api.primary.webapi.model.FinancialInstitutionApiResponse;
import com.monarkmarkets.api.primary.webapi.model.Investor;
import com.monarkmarkets.api.primary.webapi.model.ModifyIndividualInvestor;
import com.monarkmarkets.api.primary.webapi.model.Questionnaire;
import com.monarkmarkets.api.primary.webapi.model.QuestionnaireAnswer;
import com.monarkmarkets.api.primary.webapi.model.QuestionnaireApiResponse;
import com.monarkmarkets.api.primary.webapi.model.UpdateInvestor;
import com.monarkmarkets.api.primary.webapi.model.UpdateInvestorAccreditation;
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
		// Step 1: Create an investor - Pending status
		String investorReferenceId = generateInvestorReferenceId();
		Investor investor = createInvestor(
				CreateInvestor.builder()
						.investorReferenceId(investorReferenceId)
						.type(CreateInvestor.TypeEnum.INDIVIDUAL_INVESTOR)
						.build());
		log.info("Investor created: {}", investor);

		// Step 2: Get all questionnaires
		List<Questionnaire> questionnaires = getAllQuestionnaires();
		log.info("Questionnaires: {}", questionnaires);

		// Step 3: Answer all questions in questionnaires,
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

		// Step 4: Update accreditation status
		// here we pick one at random for illustration purposes
		updateInvestorAccreditation(UpdateInvestorAccreditation.builder()
				.investorId(investor.getId())
				.accreditationStatus(UpdateInvestorAccreditation.AccreditationStatusEnum.KNOWLEDGEABLE_EMPLOYEE)
				.build());

		// Get all financial institutions
		List<FinancialInstitution> financialInstitutions = getAllFinancialInstitutions();

		// Select a financial institution at random
		FinancialInstitution randomFinancialInstitution = financialInstitutions.get(
				current().nextInt(financialInstitutions.size()));
		log.info("Selected Financial Institution: {}", randomFinancialInstitution);

		// Step 3: Create a CreateFinancialAdvisor object with required fields
		UUID financialInstitutionId = randomFinancialInstitution.getId();
		CreateFinancialAdvisor createFinancialAdvisor = CreateFinancialAdvisor.builder()
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
				.build();

		// Step 4: Call FinancialAdvisorApi to create a financial advisor
		FinancialAdvisor financialAdvisor = createFinancialAdvisor(createFinancialAdvisor);
		log.info("Financial Advisor created successfully: {}", financialAdvisor);

		// Step 5: Update investor details
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

		// Step 6: Get investor by id
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