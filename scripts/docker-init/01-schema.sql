CREATE TABLE call_detail_records (
    "ID"                  VARCHAR(150)  NOT NULL,
    "RECORD_DATE"         TIMESTAMP     NOT NULL,
    "L_SPC"               INTEGER,
    "L_SSN"               INTEGER,
    "L_RI"                INTEGER,
    "L_GT_I"              INTEGER,
    "L_GT_DIGITS"         VARCHAR(18),
    "R_SPC"               INTEGER,
    "R_SSN"               INTEGER,
    "R_RI"                INTEGER,
    "R_GT_I"              INTEGER,
    "R_GT_DIGITS"         VARCHAR(18),
    "SERVICE_CODE"        VARCHAR(50),
    "OR_NATURE"           INTEGER,
    "OR_PLAN"             INTEGER,
    "OR_DIGITS"           VARCHAR(18),
    "DE_NATURE"           INTEGER,
    "DE_PLAN"             INTEGER,
    "DE_DIGITS"           VARCHAR(18),
    "ISDN_NATURE"         INTEGER,
    "ISDN_PLAN"           INTEGER,
    "MSISDN"              VARCHAR(18),
    "VLR_NATURE"          INTEGER,
    "VLR_PLAN"            INTEGER,
    "VLR_DIGITS"          VARCHAR(18),
    "IMSI"                VARCHAR(100),
    "STATUS"              VARCHAR(30)   NOT NULL,
    "TYPE"                VARCHAR(30)   NOT NULL,
    "TSTAMP"              TIMESTAMP     NOT NULL,
    "LOCAL_DIALOG_ID"     BIGINT,
    "REMOTE_DIALOG_ID"    BIGINT,
    "DIALOG_DURATION"     BIGINT,
    "USSD_STRING"         VARCHAR(255),
    CONSTRAINT pk_call_detail_records PRIMARY KEY ("ID")
);

CREATE INDEX idx_cdr_record_date_msisdn_imsi
    ON call_detail_records ("RECORD_DATE", "MSISDN", "IMSI");

CREATE INDEX idx_cdr_record_date
    ON call_detail_records ("RECORD_DATE");

CREATE INDEX idx_cdr_msisdn
    ON call_detail_records ("MSISDN");

CREATE INDEX idx_cdr_imsi
    ON call_detail_records ("IMSI");
