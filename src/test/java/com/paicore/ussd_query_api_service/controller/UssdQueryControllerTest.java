package com.paicore.ussd_query_api_service.controller;

import com.paicore.ussd_query_api_service.dto.UssdQueryRequest;
import com.paicore.ussd_query_api_service.entity.CallDetailRecord;
import com.paicore.ussd_query_api_service.exception.GlobalExceptionHandler;
import com.paicore.ussd_query_api_service.service.QueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UssdQueryController.class)
@Import({GlobalExceptionHandler.class})
class UssdQueryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private QueryService queryService;

	@Test
	void queryReturns200ForValidRequest() throws Exception {
		CallDetailRecord record = new CallDetailRecord();
		record.setId("rec-001");
		record.setRecordDate(LocalDateTime.of(2023, 8, 18, 10, 30, 0));
		record.setMsisdn("573228550000");
		record.setImsi("1234567890");
		record.setStatus("OK");
		record.setType("USSD");
		record.setTstamp(LocalDateTime.of(2023, 8, 18, 10, 30, 1));

		when(queryService.query(any(UssdQueryRequest.class))).thenReturn(List.of(record));

		mockMvc.perform(post("/api/v1/ussd/query")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "record_date_start": "2023-08-18 10:30:00",
								  "record_date_end": "2023-08-18 10:31:00",
								  "msisdn": "573228550000",
								  "imsi": "1234567890"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].RECORD_DATE").value("2023-08-18 10:30:00"))
				.andExpect(jsonPath("$[0].MSISDN").value("573228550000"))
				.andExpect(jsonPath("$[0].IMSI").value("1234567890"));
	}

	@Test
	void queryReturns400ForInvalidPayload() throws Exception {
		mockMvc.perform(post("/api/v1/ussd/query")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "record_date_end": "2023-08-18 10:31:00"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Validation failed"))
				.andExpect(jsonPath("$.fieldErrors[0].field").value("recordDateStart"));
	}
}
