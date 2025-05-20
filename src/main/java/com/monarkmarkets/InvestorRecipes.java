package com.monarkmarkets;

import com.monarkmarkets.dtos.investor.AccreditationStatus;
import com.monarkmarkets.dtos.investor.CreateInvestor;
import com.monarkmarkets.dtos.investor.Investor;
import com.monarkmarkets.dtos.investor.InvestorType;
import com.monarkmarkets.dtos.investor.ModifyIndividualInvestor;
import com.monarkmarkets.dtos.investor.UpdateInvestor;
import com.monarkmarkets.dtos.investor.UpdateInvestorAccreditation;
import com.monarkmarkets.dtos.questionnaire.CreateQuestionnaireAnswer;
import com.monarkmarkets.dtos.questionnaire.CreateQuestionnaireQuestionAnswer;
import com.monarkmarkets.dtos.questionnaire.Questionnaire;
import com.monarkmarkets.dtos.questionnaire.QuestionnaireAnswer;
import com.monarkmarkets.dtos.questionnaire.QuestionnaireApiResponse;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

public class InvestorRecipes {

	private static final Random random = new Random();
	private static final Logger logger = LoggerFactory.getLogger(InvestorRecipes.class);

	/**
	 * Investor Onboarding
	 * Create an investor by passing the investorReferenceId, update accreditation status, and provide responses to questionnaire questions.
	 *
	 * @return the new Investor
	 */
	public static Investor investorOnboarding() {

		// Step 1: Create an investor - Pending status
		String investorReferenceId = generateInvestorReferenceId();
		Investor investor = createInvestor(CreateInvestor.builder()
				.investorReferenceId(investorReferenceId)
				.type(InvestorType.IndividualInvestor)
				.build());
		logger.info("Investor created: {}", investor);

		// Step 2: Get all questionnaires
		List<Questionnaire> questionnaires = getAllQuestionnaires();
		logger.info("Questionnaires: {}", questionnaires);

		// Step 3: Answer all questions in questionnaires,
		// here we pick one at random for illustration purposes
		if (questionnaires != null && !questionnaires.isEmpty()) {
			for (Questionnaire questionnaire : questionnaires) {
				logger.info("Questionnaire: {}", questionnaire);

				List<CreateQuestionnaireQuestionAnswer> createQuestionAnswers = questionnaire.getQuestions().stream()
						.map(question -> {
							if (question.getOptions().isEmpty()) {
								switch (question.getFormat()) {
									case Integer:
										return CreateQuestionnaireQuestionAnswer.builder()
												.questionnaireQuestionId(question.getId())
												.value(String.valueOf(random.nextInt(20)))
												.build();
									case Boolean:
										return CreateQuestionnaireQuestionAnswer.builder()
												.questionnaireQuestionId(question.getId())
												.value(String.valueOf(random.nextBoolean()))
												.build();
									default:
										return CreateQuestionnaireQuestionAnswer.builder()
												.questionnaireQuestionId(question.getId())
												.value(String.valueOf(random.nextInt(2)))
												.build();
								}
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

				logger.info("CreateQuestionnaireAnswer: {}", createQuestionnaireAnswer);
				QuestionnaireAnswer questionnaireAnswer = createQuestionnaireAnswer(createQuestionnaireAnswer);
				logger.info("QuestionnaireAnswer: {}", questionnaireAnswer);
			}
		}

		// Step 4: Update accreditation status
		// here we pick one at random for illustration purposes
		updateInvestorAccreditation(UpdateInvestorAccreditation.builder()
				.investorId(investor.getId())
				.accreditationStatus(AccreditationStatus.KNOWLEDGEABLE_EMPLOYEE)
				.build());

		// Step 5: Update investor details
		Investor updatedInvestor = updateInvestor(UpdateInvestor.builder()
				.id(investor.getId())
				.investorReferenceId(investor.getInvestorReferenceId())
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
								.dateOfBirth(LocalDate.parse("1980-01-01").toString())
								.taxId("792-61-0047")
								.passportNumber("X1234567")
								.isSubscriptionAdvisorOrERA(true)
								.isUSBased(true)
								.build()
				)
				.build());
		logger.info("Investor updated: {}", updatedInvestor);

		// Step 6: Get investor by id
		Investor investorById = getInvestorById(investor.getId());
		logger.info("Investor by id: {}", investorById);

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
			logger.info("CreateInvestor *****");
			return ApiClient.sendRequest("/primary/v1/investor", "POST", createInvestor, Investor.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Questionnaire> getAllQuestionnaires() {
		try {
			logger.info("GetAllQuestionnaires *****");
			QuestionnaireApiResponse response = ApiClient.sendRequest("/primary/v1/questionnaire", "GET", null,
					QuestionnaireApiResponse.class);
			return response.getItems();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static QuestionnaireAnswer createQuestionnaireAnswer(CreateQuestionnaireAnswer createQuestionnaireAnswer) {
		try {
			logger.info("CreateQuestionnaireAnswer *****");
			return ApiClient.sendRequest("/primary/v1/questionnaire-answer", "POST", createQuestionnaireAnswer,
					QuestionnaireAnswer.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Investor updateInvestor(UpdateInvestor updateInvestor) {
		try {
			logger.info("UpdateInvestorDetails *****");
			return ApiClient.sendRequest("/primary/v1/investor", "PUT", updateInvestor, Investor.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void updateInvestorAccreditation(UpdateInvestorAccreditation updateInvestorAccreditation) {
		try {
			logger.info("UpdateAccreditationStatus *****");
			ApiClient.sendRequest("/primary/v1/investor/accreditation", "PUT", updateInvestorAccreditation);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Investor getInvestorById(UUID investorId) {
		try {
			logger.info("GetInvestorById *****");
			return ApiClient.sendRequest("/primary/v1/investor/" + investorId, "GET", null,
					Investor.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}