DROP PROCEDURE IF EXISTS notification.GenerateSequenceNumber;

DELIMITER //

CREATE PROCEDURE notification.GenerateSequenceNumber (IN keyName varchar(20), OUT sequenceNo bigint)
BEGIN
  DECLARE count int default -1;

  -- Retrieve and update in single statement
  UPDATE notification.SEQUENCEGENERATOR
    SET SEQUENCENUMBER = (@SEQUENCENUMBER := SEQUENCENUMBER) + 1, VERSION = (@VERSION := VERSION) + 1
    WHERE NAME = keyName;

  SET sequenceNo = @SEQUENCENUMBER + 1;

	SELECT ROW_COUNT() into count ;

	IF count = 0 THEN
       INSERT INTO notification.SEQUENCEGENERATOR (`NAME`,`SEQUENCENUMBER`,`VERSION`) values (keyName ,1 ,0);
       SET sequenceNo = 1;
	END IF;

END

//
DELIMITER ;