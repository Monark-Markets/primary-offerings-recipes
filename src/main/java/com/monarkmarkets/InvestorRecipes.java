package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.dtos.investor.AccreditationStatus;
import com.monarkmarkets.dtos.investor.CreateInvestor;
import com.monarkmarkets.dtos.investor.IndividualInvestor;
import com.monarkmarkets.dtos.investor.Investor;
import com.monarkmarkets.dtos.investor.QualificationStatus;
import com.monarkmarkets.dtos.investor.UpdateInvestor;
import com.monarkmarkets.dtos.investor.UpdateInvestorAccreditation;
import com.monarkmarkets.dtos.questionnaire.CreateQuestionnaireAnswer;
import com.monarkmarkets.dtos.questionnaire.Questionnaire;
import com.monarkmarkets.dtos.questionnaire.QuestionnaireAnswer;
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

				List<CreateQuestionnaireAnswer.CreateQuestionAnswer> createQuestionAnswers = questionnaire.getQuestions().stream()
						.map(question -> {
							CreateQuestionnaireAnswer.CreateQuestionAnswer createQuestionAnswer = new CreateQuestionnaireAnswer.CreateQuestionAnswer();
							createQuestionAnswer.setQuestionnaireQuestionId(question.getId());
							createQuestionAnswer.setValue(question.getOptions().get(random.nextInt(question.getOptions().size())));
							return createQuestionAnswer;
						})
						.toList();
				CreateQuestionnaireAnswer createQuestionnaireAnswer = new CreateQuestionnaireAnswer();
				createQuestionnaireAnswer.setQuestionnaireId(questionnaire.getId());
				createQuestionnaireAnswer.setInvestorId(investor.getId());
				createQuestionnaireAnswer.setCreateQuestionAnswers(createQuestionAnswers);

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
				.qualificationStatus(QualificationStatus.QUALIFIED_PURCHASER)
				.individualInvestor(
						IndividualInvestor.builder()
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
								.mailingStreet1("mailing1")
								.citizenship("US")
								.dateOfBirth(LocalDate.parse("1980-01-01"))
								.taxId("792-61-0047")
								.passportNumber("X1234567")
								.subscriptionAdvisorOrERA(true)
								.usBased(true)
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
			return ApiClient.sendRequest("/primary/v1/questionnaire", "GET", null,
					new TypeReference<>() { });
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