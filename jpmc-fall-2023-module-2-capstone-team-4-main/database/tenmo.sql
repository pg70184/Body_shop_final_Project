BEGIN TRANSACTION;
DROP TABLE IF EXISTS transfer;
DROP TABLE IF EXISTS transfer_status;
DROP TABLE IF EXISTS transfer_type;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS tenmo_user;
DROP SEQUENCE IF EXISTS seq_user_id;
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;
CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20),
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);
CREATE TABLE account (
    account_id serial NOT NULL, --Primary KEY
    user_id int NOT NULL,   --Forigen KEY
    balance decimal(13,2) NOT NULL,
    --PK CONSTRAINT
    CONSTRAINT pk_account_id PRIMARY KEY (account_id),
    --CONSTRAINTS
    CONSTRAINT fk_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user(user_id),
    );
CREATE  TABLE transfer_type (
    transfer_type_id serial NOT NULL,
    transfer_type_desc varchar(10) NOT NULL,
    --PK CONSTRAINT
    CONSTRAINT pk_transfer_type_id PRIMARY KEY (transfer_type_id)
    );
 CREATE TABLE transfer_status (
    transfer_status_id serial NOT NULL,
    transfer_status_desc varchar(10) NOT NULL,
    -- PK CONSTRAINT
    CONSTRAINT pk_transfer_status_id PRIMARY KEY (transfer_status_id)
    );
 CREATE TABLE transfer (
    transfer_id serial NOT NULL,
    transfer_type_id INT NOT NULL,
	transfer_status_id int NOT NULL,
    account_from int NOT NULL,
    account_to int NOT NULL,
    amount decimal(13,2) NOT NULL,
    --PK CONSTRAINT
    CONSTRAINT pk_transfer_id PRIMARY KEY (transfer_id),
    --CONSTRAINTS
    CONSTRAINT fk_transfer_type_id FOREIGN KEY (transfer_type_id) REFERENCES transfer_type(transfer_type_id),
	CONSTRAINT fk_transfer_status_id FOREIGN KEY (transfer_status_id) REFERENCES transfer_status(transfer_status_id),
	CONSTRAINT fk_account_from FOREIGN KEY (account_from) REFERENCES account(account_id),
	CONSTRAINT fk_account_to FOREIGN KEY (account_to) REFERENCES account(account_id)
	 );

COMMIT;