package com.paicore.ussd_query_api_service.service;

import com.paicore.ussd_query_api_service.dto.UssdQueryRequest;
import com.paicore.ussd_query_api_service.entity.CallDetailRecord;
import com.paicore.ussd_query_api_service.repository.CallDetailRecordRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {

	private final CallDetailRecordRepository callDetailRecordRepository;

	public QueryService(CallDetailRecordRepository callDetailRecordRepository) {
		this.callDetailRecordRepository = callDetailRecordRepository;
	}

	public List<CallDetailRecord> query(UssdQueryRequest request) {
		validateDateRange(request);
		return callDetailRecordRepository.findAll(buildSpecification(request));
	}

	private void validateDateRange(UssdQueryRequest request) {
		if (request.getRecordDateEnd().isBefore(request.getRecordDateStart())) {
			throw new IllegalArgumentException("record_date_end must be on or after record_date_start");
		}
	}

	private Specification<CallDetailRecord> buildSpecification(UssdQueryRequest request) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(criteriaBuilder.between(
					root.get("recordDate"),
					request.getRecordDateStart(),
					request.getRecordDateEnd()));

			if (StringUtils.hasText(request.getMsisdn())) {
				predicates.add(criteriaBuilder.equal(root.get("msisdn"), request.getMsisdn()));
			}

			if (StringUtils.hasText(request.getImsi())) {
				predicates.add(criteriaBuilder.equal(root.get("imsi"), request.getImsi()));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
