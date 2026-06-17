package com.paicore.ussd_query_api_service.controller;

import com.paicore.ussd_query_api_service.dto.UssdQueryRequest;
import com.paicore.ussd_query_api_service.entity.CallDetailRecord;
import com.paicore.ussd_query_api_service.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ussd")
public class UssdQueryController {

	private final QueryService queryService;

	public UssdQueryController(QueryService queryService) {
		this.queryService = queryService;
	}

	@PostMapping("/query")
	public ResponseEntity<List<CallDetailRecord>> query(@Valid @RequestBody UssdQueryRequest request) {
		return ResponseEntity.ok(queryService.query(request));
	}
}
