INSERT INTO notification.versionhistory VALUES ('1.2.0.0', CURRENT_TIMESTAMP);

ALTER TABLE notification.employeefiledetail
  ADD COLUMN preferredName          VARCHAR(50) NULL DEFAULT NULL AFTER lastName,
  ADD COLUMN reportName             VARCHAR(50) NULL DEFAULT NULL AFTER preferredName;

ALTER TABLE notification.employee
  ADD COLUMN preferredName          VARCHAR(50) NULL DEFAULT NULL AFTER lastName,
  ADD COLUMN reportName             VARCHAR(50) NULL DEFAULT NULL AFTER preferredName;


