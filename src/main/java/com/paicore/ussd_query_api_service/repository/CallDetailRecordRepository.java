package com.paicore.ussd_query_api_service.repository;

import com.paicore.ussd_query_api_service.entity.CallDetailRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CallDetailRecordRepository
		extends JpaRepository<CallDetailRecord, String>, JpaSpecificationExecutor<CallDetailRecord> {
}
