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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.monarkmarkets.primary.client.model.ModifyIndividualInvestor.QualifiedStatusEnum.QUALIFIED_CLIENT;
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

		// Select the default financial institution (fail fast if none configured)
		FinancialInstitution randomFinancialInstitution = financialInstitutions.stream()
				.filter(fi -> Boolean.TRUE.equals(fi.getIsDefault()))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No default financial institution found"));
		log.info("Selected Financial Institution: {}", randomFinancialInstitution);
		UUID financialInstitutionId = randomFinancialInstitution.getId();

		// Step 2: Create an investor - Pending status
		String investorReferenceId = generateInvestorReferenceId();

		// Generate random investor data
		String randomSSN = generateRandomSSN();
		String randomEmail = generateRandomEmail();
		String randomPhone = generateRandomPhone();
		LocalDate randomDOB = generateRandomDateOfBirth();

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

		// Step 4: Answer all questions in questionnaires
		if (questionnaires != null && !questionnaires.isEmpty()) {
			for (Questionnaire questionnaire : questionnaires) {
				// Only process questionnaires with partnerName != null
				if (questionnaire.getPartnerName() == null) {
					log.info("Skipping questionnaire {} - partnerName is null", questionnaire.getId());
					continue;
				}

				log.info("Questionnaire: {}", questionnaire);

				if (questionnaire.getQuestions() == null || questionnaire.getQuestions().isEmpty()) {
					log.info("No questions found for questionnaire: {}", questionnaire.getId());
					continue;
				}

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
								.isUSBased(true)
								.dateOfBirth(randomDOB)
								.street1("123 Market Street")
								.street2("Apt 456")
								.city("San Francisco")
								.state("CA")
								.zipCode("94103")
								.countryCode("US")
								.phoneCountryCode("1")
								.phoneNumber(randomPhone)
								.mailingStreet1("123 Market Street")
								.mailingStreet2("Apt 456")
								.mailingAddressCity("San Francisco")
								.mailingAddressState("CA")
								.mailingAddressZipCode("94103")
								.mailingAddressCountry("US")
								.exemptPayeeCode("1")
								.isSubscriptionAdvisorOrERA(true)
								.email(randomEmail)
								.taxId(randomSSN)
								.citizenship("US")
								.qualifiedStatus(QUALIFIED_CLIENT)
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
			return investorApi.createInvestor(createInvestor);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Questionnaire> getAllQuestionnaires() {
		try {
			log.info("Fetch all questionnaires");
			QuestionnaireApiResponse questionnaireApiResponse =
					questionnaireApi.getAllQuestionnaires(null, null, null);
			return questionnaireApiResponse.getItems();
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

	private static List<FinancialInstitution> getAllFinancialInstitutions() {
		try {
			FinancialInstitutionApiResponse financialInstitutionResponse = financialInstitutionApi
					.getAllFinancialInstitutions(null, null, null, null);
			return financialInstitutionResponse.getItems();
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static FinancialAdvisor createFinancialAdvisor(CreateFinancialAdvisor createFinancialAdvisor) {
		try {
			log.info("Create financial advisor: {}", createFinancialAdvisor);
			return financialAdvisorApi.createFinancialAdvisor(createFinancialAdvisor);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static Investor updateInvestor(UpdateInvestor updateInvestor) {
		try {
			log.info("Update investor details: {}", updateInvestor);
			return investorApi.updateInvestor(updateInvestor);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static void updateInvestorAccreditation(UpdateInvestorAccreditation updateInvestorAccreditation) {
		try {
			log.info("Update accreditation status: {}", updateInvestorAccreditation);
			investorApi.updateInvestorAccreditation(updateInvestorAccreditation);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static Investor getInvestorById(UUID investorId) {
		try {
			log.info("Get investor by id: {}", investorId);
			return investorApi.getInvestorById(investorId);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generate a random SSN in XXX-XX-XXXX format (IRS standard).
	 * Avoids invalid patterns: area 000/666/900-999, group 00, serial 0000
	 */
	private static String generateRandomSSN() {
		// Area: 001-665 or 667-899 (avoiding 000, 666, 900-999)
		int area = random.nextInt(898) + 1;
		if (area >= 666) area += 1; // Skip 666
		String areaStr = String.format("%03d", area);

		// Group: 01-99 (avoiding 00)
		String group = String.format("%02d", random.nextInt(99) + 1);

		// Serial: 0001-9999 (avoiding 0000)
		String serial = String.format("%04d", random.nextInt(9999) + 1);

		return areaStr + "-" + group + "-" + serial;
	}

	/**
	 * Generate a random email address for the investor
	 */
	private static String generateRandomEmail() {
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
				.withinRange('a', 'z')
				.filteredBy(LETTERS)
				.build();
		return "investor." + generator.generate(8) + "@example.com";
	}

	/**
	 * Generate a random US phone number in the format 415XXXXXXX
	 */
	private static String generateRandomPhone() {
		return "415" + randomNumeric.generate(7);
	}

	/**
	 * Generate a random date of birth (between 25-65 years old)
	 */
	private static LocalDate generateRandomDateOfBirth() {
		LocalDate today = LocalDate.now();
		int yearsOld = random.nextInt(40) + 25; // 25-65 years old
		int daysOffset = random.nextInt(365); // Random day within that year
		return today.minusYears(yearsOld).minusDays(daysOffset);
	}
}