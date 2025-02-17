package com.monarkmarkets.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.ApiClient;
import com.monarkmarkets.dtos.partner.Partner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Get partner details
 *
 */
public class PartnerRecipes {

	private static final Logger logger = LoggerFactory.getLogger(PartnerRecipes.class);

	public static void main(String[] args) {
		getAllPartners().forEach(partner -> {
			logger.info(partner.toString());
		});
	}

	private static List<Partner> getAllPartners() {
		try {
			logger.info("GetAllPartners *****");
			return ApiClient.sendRequest("/primary-internal/v1/partner", "GET", null,
					new TypeReference<>() {
					});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}