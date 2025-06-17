Use crime;

DROP TABLE IF EXISTS Misdemeanor;
DROP TABLE IF EXISTS Felony;
DROP TABLE IF EXISTS suspected_of;
DROP TABLE IF EXISTS CrimeNotes;
DROP TABLE IF EXISTS Suspect;
DROP TABLE IF EXISTS Crime;
DROP TABLE IF EXISTS Organization;

CREATE TABLE Organization (
  OrganizationCode     VARCHAR(20)    NOT NULL,
  OrganizationName     VARCHAR(200)   NOT NULL,
  PRIMARY KEY (OrganizationCode)
);


CREATE TABLE Crime (
  OrganizationCode     VARCHAR(20)    NOT NULL,
  CrimeID              VARCHAR(50)    NOT NULL,
  CrimeDate            DATE           NOT NULL,
  PRIMARY KEY (OrganizationCode, CrimeID),
  FOREIGN KEY (OrganizationCode)
    REFERENCES Organization (OrganizationCode)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE CrimeNotes (
  notesID              INT AUTO_INCREMENT PRIMARY KEY,
  OrganizationCode     VARCHAR(20)    NOT NULL,
  CrimeID              VARCHAR(50)    NOT NULL,
  CrimeNotes           VARCHAR(500)   NOT NULL,
  FOREIGN KEY (OrganizationCode, CrimeID)
    REFERENCES Crime (OrganizationCode, CrimeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE Suspect (
  OrganizationCode     VARCHAR(20)    NOT NULL,
  SuspectID            VARCHAR(50)    NOT NULL,
  SuspectName          VARCHAR(200)   NOT NULL,
  PRIMARY KEY (OrganizationCode, SuspectID),
  FOREIGN KEY (OrganizationCode)
    REFERENCES Organization (OrganizationCode)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE suspected_of (
  SuspectOrganizationCode  VARCHAR(20)  NOT NULL,
  SuspectID                VARCHAR(50)  NOT NULL,
  CrimeOrganizationCode    VARCHAR(20)  NOT NULL,
  CrimeID                  VARCHAR(50)  NOT NULL,
  PRIMARY KEY (
    SuspectOrganizationCode,
    SuspectID,
    CrimeOrganizationCode,
    CrimeID
  ),
  FOREIGN KEY (SuspectOrganizationCode, SuspectID)
    REFERENCES Suspect (OrganizationCode, SuspectID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (CrimeOrganizationCode, CrimeID)
    REFERENCES Crime (OrganizationCode, CrimeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE Felony (
  OrganizationCode     VARCHAR(20)    NOT NULL,
  CrimeID              VARCHAR(50)    NOT NULL,
  FelonyFatalityCount  INT            NOT NULL,
  PRIMARY KEY (OrganizationCode, CrimeID),
  FOREIGN KEY (OrganizationCode, CrimeID)
    REFERENCES Crime (OrganizationCode, CrimeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE Misdemeanor (
  OrganizationCode       VARCHAR(20)  NOT NULL,
  CrimeID                VARCHAR(50)  NOT NULL,
  MisdemeanorDamageCost  DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (OrganizationCode, CrimeID),
  FOREIGN KEY (OrganizationCode, CrimeID)
    REFERENCES Crime (OrganizationCode, CrimeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
