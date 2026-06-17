-- Seed data for local API testing against the ussd database.
-- Run: psql -d ussd -f scripts/seed-test-data.sql

INSERT INTO call_detail_records (
    "ID", "RECORD_DATE", "MSISDN", "IMSI", "STATUS", "TYPE", "TSTAMP",
    "SERVICE_CODE", "USSD_STRING", "DIALOG_DURATION"
) VALUES
    (
        'test-002',
        '2023-08-18 10:30:45',
        '573228550000',
        '1234567890',
        'OK',
        'USSD',
        '2023-08-18 10:30:46',
        '*123#',
        'Balance inquiry',
        12
    ),
    (
        'test-003',
        '2023-08-18 10:30:50',
        '573228550001',
        '1234567891',
        'OK',
        'USSD',
        '2023-08-18 10:30:51',
        '*456#',
        'Data bundle',
        18
    ),
    (
        'test-004',
        '2023-08-18 10:30:55',
        '573228550002',
        '1234567892',
        'OK',
        'USSD',
        '2023-08-18 10:30:56',
        '*789#',
        'Airtime transfer',
        25
    ),
    (
        'test-005',
        '2023-08-18 10:31:05',
        '573228550000',
        '9876543210',
        'OK',
        'USSD',
        '2023-08-18 10:31:06',
        '*100#',
        'Same MSISDN, different IMSI',
        9
    ),
    (
        'test-006',
        '2023-08-18 11:00:00',
        '573228550000',
        '1234567890',
        'OK',
        'USSD',
        '2023-08-18 11:00:01',
        '*200#',
        'Outside 10:30-10:31 window',
        15
    ),
    (
        'test-007',
        '2023-08-17 10:30:30',
        '573999000111',
        '5555555555',
        'FAILED',
        'USSD',
        '2023-08-17 10:30:31',
        '*999#',
        'Previous day record',
        5
    ),
    (
        'test-008',
        '2023-08-19 09:15:00',
        '573228550003',
        '1234567893',
        'OK',
        'USSD',
        '2023-08-19 09:15:01',
        '*321#',
        'Next day record',
        30
    )
ON CONFLICT ("ID") DO NOTHING;

-- Quick verification
SELECT "ID", "RECORD_DATE", "MSISDN", "IMSI", "STATUS"
FROM call_detail_records
ORDER BY "RECORD_DATE";
