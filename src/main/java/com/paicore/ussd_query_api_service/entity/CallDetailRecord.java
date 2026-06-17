package com.paicore.ussd_query_api_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "call_detail_records")
@Getter
@Setter
@NoArgsConstructor
public class CallDetailRecord {

	@Id
	@Column(name = "ID", nullable = false, length = 150)
	@JsonIgnore
	private String id;

	@Column(name = "RECORD_DATE", nullable = false)
	@JsonProperty("RECORD_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime recordDate;

	@Column(name = "L_SPC")
	@JsonIgnore
	private Integer lSpc;

	@Column(name = "L_SSN")
	@JsonIgnore
	private Integer lSsn;

	@Column(name = "L_RI")
	@JsonIgnore
	private Integer lRi;

	@Column(name = "L_GT_I")
	@JsonIgnore
	private Integer lGtI;

	@Column(name = "L_GT_DIGITS", length = 18)
	@JsonIgnore
	private String lGtDigits;

	@Column(name = "R_SPC")
	@JsonIgnore
	private Integer rSpc;

	@Column(name = "R_SSN")
	@JsonIgnore
	private Integer rSsn;

	@Column(name = "R_RI")
	@JsonIgnore
	private Integer rRi;

	@Column(name = "R_GT_I")
	@JsonIgnore
	private Integer rGtI;

	@Column(name = "R_GT_DIGITS", length = 18)
	@JsonIgnore
	private String rGtDigits;

	@Column(name = "SERVICE_CODE", length = 50)
	@JsonIgnore
	private String serviceCode;

	@Column(name = "OR_NATURE")
	@JsonIgnore
	private Integer orNature;

	@Column(name = "OR_PLAN")
	@JsonIgnore
	private Integer orPlan;

	@Column(name = "OR_DIGITS", length = 18)
	@JsonIgnore
	private String orDigits;

	@Column(name = "DE_NATURE")
	@JsonIgnore
	private Integer deNature;

	@Column(name = "DE_PLAN")
	@JsonIgnore
	private Integer dePlan;

	@Column(name = "DE_DIGITS", length = 18)
	@JsonIgnore
	private String deDigits;

	@Column(name = "ISDN_NATURE")
	@JsonIgnore
	private Integer isdnNature;

	@Column(name = "ISDN_PLAN")
	@JsonIgnore
	private Integer isdnPlan;

	@Column(name = "MSISDN", length = 18)
	@JsonProperty("MSISDN")
	private String msisdn;

	@Column(name = "VLR_NATURE")
	@JsonIgnore
	private Integer vlrNature;

	@Column(name = "VLR_PLAN")
	@JsonIgnore
	private Integer vlrPlan;

	@Column(name = "VLR_DIGITS", length = 18)
	@JsonIgnore
	private String vlrDigits;

	@Column(name = "IMSI", length = 100)
	@JsonProperty("IMSI")
	private String imsi;

	@Column(name = "STATUS", nullable = false, length = 30)
	@JsonIgnore
	private String status;

	@Column(name = "TYPE", nullable = false, length = 30)
	@JsonIgnore
	private String type;

	@Column(name = "TSTAMP", nullable = false)
	@JsonIgnore
	private LocalDateTime tstamp;

	@Column(name = "LOCAL_DIALOG_ID")
	@JsonIgnore
	private Long localDialogId;

	@Column(name = "REMOTE_DIALOG_ID")
	@JsonIgnore
	private Long remoteDialogId;

	@Column(name = "DIALOG_DURATION")
	@JsonIgnore
	private Long dialogDuration;

	@Column(name = "USSD_STRING", length = 255)
	@JsonIgnore
	private String ussdString;
}
