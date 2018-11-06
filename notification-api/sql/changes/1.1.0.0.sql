INSERT INTO notification.versionhistory VALUES ('1.1.0.0', CURRENT_TIMESTAMP);

ALTER TABLE notification.employeefiledetail
  ADD COLUMN terminationreason          VARCHAR(20) NULL DEFAULT NULL AFTER terminationdate,
  ADD COLUMN terminationdescription     VARCHAR(100) NULL DEFAULT NULL AFTER terminationreason,
  ADD COLUMN comment                    VARCHAR(200) NULL DEFAULT NULL AFTER terminationdescription;

ALTER TABLE notification.employee
  ADD COLUMN terminationreason          VARCHAR(20) NULL DEFAULT NULL AFTER terminationdate,
  ADD COLUMN terminationdescription     VARCHAR(100) NULL DEFAULT NULL AFTER terminationreason,
  ADD COLUMN comment                    VARCHAR(200) NULL DEFAULT NULL AFTER terminationdescription;


