use bee_project;
DROP TABLE IF EXISTS bee_colonies_county_src;
DROP TABLE IF EXISTS population_src;
DROP TABLE IF EXISTS bee_colonies_state_src;

DROP TABLE IF EXISTS county;
DROP TABLE IF EXISTS populations;
DROP TABLE IF EXISTS state;


DROP TABLE IF EXISTS Bee_Colonies;
DROP TABLE IF EXISTS Ag_Codes;
DROP TABLE IF EXISTS Population;
DROP TABLE IF EXISTS Geo_Codes;
CREATE TABLE `bee_colonies_county_src` (
  `Year` text,
  `Geo_Level` text,
  `State` text,
  `State_ANSI` text,
  `Ag_District` text,
  `Ag_District_Code` text,
  `County` text,
  `County_ANSI` text,
  `Value` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE COUNTY(
  `Year` YEAR,
  `Geo_Level` CHAR(7) CHECK(Geo_Level IN ('County','State','Country')),
  `State` VARCHAR(14),
  `State_ANSI` CHAR(2) CHECK (`State_ANSI` REGEXP '^([0-4][0-9]|5[0-6])$'),
  `Ag_District` VARCHAR(70),
  `Ag_District_Code` CHAR (2) CHECK (`Ag_District_Code` REGEXP '^[0-9]{2}$'),
  `County` VARCHAR(70),
  `County_ANSI` CHAR (3) CHECK (`County_ANSI` REGEXP '^[0-9]{3}$'),
  `Value` INT 
);

CREATE TABLE `population_src` (
  `FIPStxt` text,
  `State` text,
  `Area_name` text,
  `Rural_urban_code_2013` text,
  `Population_1990` text,
  `Population_2000` text,
  `Population_2010` text,
  `Population_2020` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE populations (
  `FIPStxt` CHAR(5) CHECK (`FIPStxt`REGEXP '^[0-9]{5}$'),
  `State` VARCHAR(30),
  `Area_name` VARCHAR(80),
  `Rural_urban_code_2013` CHAR(1) CHECK (Rural_urban_code_2013 REGEXP '^[0-9]{1}$'),
  `Population_1990` INT,
  `Population_2000` INT,
  `Population_2010` INT,
  `Population_2020` INT
);

CREATE TABLE `bee_colonies_state_src` (
  `Year` text,
  `Geo_Level` text,
  `State` text,
  `State_ANSI` text,
  `Ag_District` text,
  `Ag_District_Code` text,
  `County` text,
  `County_ANSI` text,
  `Value` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE state (
  `Year` YEAR,
  `Geo_Level` CHAR(7) CHECK(Geo_Level IN ('County','State','Country')),
  `State` VARCHAR(14),
  `State_ANSI` CHAR(2) CHECK (`State_ANSI` REGEXP '^([0-4][0-9]|5[0-6])$'),
  `Ag_District` VARCHAR(70),
  `Ag_District_Code` CHAR (2) CHECK (`Ag_District_Code` REGEXP '^[0-9]{2}$'),
  `County` VARCHAR(70),
  `County_ANSI` CHAR (3) CHECK (`County_ANSI` REGEXP '^[0-9]{3}$'),
  `Value` INT 
);
--------------------------------------------------------------------------------------------------------------------------------------------
SET GLOBAL LOCAL_INFILE = 1;

LOAD DATA LOCAL INFILE
'C:\\Users\\acotl\\Downloads\\individual_project\\individual_project\\bee_colonies_county_src.csv'
INTO TABLE final_project.bee_colonies_county_src
CHARACTER SET 'latin1'
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE
'C:\\Users\\acotl\\Downloads\\individual_project\\individual_project\\population_src.csv'
INTO TABLE final_project.population_src
CHARACTER SET 'latin1'
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE
'C:\\Users\\acotl\\Downloads\\individual_project\\individual_project\\bee_colonies_state_src.csv'
INTO TABLE final_project.bee_colonies_state_src
CHARACTER SET 'latin1'
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;
----------------------------------------------------------------------------------------------------------------------------------
-- Clean and insert data into the `state` table
INSERT INTO state (
  Year, Geo_Level, State, State_ANSI, Ag_District, Ag_District_Code, County, County_ANSI, Value
)
SELECT
  NULLIF(TRIM(Year), ''),
  NULLIF(TRIM(Geo_Level), ''),
  NULLIF(TRIM(State), ''),
  LPAD(NULLIF(TRIM(State_ANSI), ''), 2, '0'),
  NULLIF(TRIM(Ag_District), ''),
  NULLIF(TRIM(Ag_District_Code), ''),
  NULLIF(TRIM(County), ''),
  NULLIF(TRIM(County_ANSI), ''),
  NULLIF(REPLACE(TRIM(Value), ',', ''), '') + 0
FROM bee_colonies_state_src;

-- Clean and insert data into the `population` table
-- Clean and insert data into the `population` table
INSERT INTO populations (
  FIPStxt, State, Area_name, Rural_urban_code_2013,
  Population_1990, Population_2000, Population_2010, Population_2020
)
SELECT
  -- Only pad with '000' if FIPStxt has fewer than 5 digits
  CASE 
    WHEN LENGTH(TRIM(FIPStxt)) = 5 THEN TRIM(FIPStxt) 
    ELSE LPAD(TRIM(FIPStxt), 5, '0') 
  END AS FIPStxt,
  NULLIF(TRIM(State), ''),
  NULLIF(TRIM(Area_name), ''),
  NULLIF(TRIM(Rural_urban_code_2013), ''),
  NULLIF(REPLACE(TRIM(Population_1990), ',', ''), '') + 0,
  NULLIF(REPLACE(TRIM(Population_2000), ',', ''), '') + 0,
  NULLIF(REPLACE(TRIM(Population_2010), ',', ''), '') + 0,
  NULLIF(REPLACE(TRIM(Population_2020), ',', ''), '') + 0
FROM population_src;

-- Clean and insert data into the `county` table
INSERT INTO county (
  Year, Geo_Level, State, State_ANSI, Ag_District, Ag_District_Code, County, County_ANSI, Value
)
SELECT
  NULLIF(TRIM(Year), ''),
  NULLIF(TRIM(Geo_Level), ''),
  NULLIF(TRIM(State), ''),
  LPAD(NULLIF(TRIM(State_ANSI), ''), 2, '0'),
  NULLIF(TRIM(Ag_District), ''),
  NULLIF(TRIM(Ag_District_Code), ''),
  NULLIF(TRIM(County), ''),
  NULLIF(TRIM(County_ANSI), ''),
  CASE
    WHEN TRIM(Value) = '(D)' THEN NULL
    ELSE NULLIF(REPLACE(TRIM(Value), ',', ''), '') + 0
  END
FROM bee_colonies_county_src;

UPDATE county
SET 
  County = 'Aleutians West Census Area'
WHERE 
  Geo_Level   = 'County'
  AND County = 'Aleutian Islands';
  
  UPDATE county AS c
JOIN populations AS p
  ON LEFT(p.FIPStxt, 2) = c.State_ANSI
 AND (
       TRIM(p.Area_name)   LIKE CONCAT('%', TRIM(c.County), '%')
    OR TRIM(c.County) LIKE CONCAT('%', TRIM(p.Area_name), '%')
    )
SET
  c.County      = p.Area_name,
  c.County_ANSI = RIGHT(p.FIPStxt, 3)
WHERE
  c.Geo_Level     = 'County'
  AND c.County_ANSI IS NULL;

CREATE TABLE Geo_Codes (
  Geo_Level    VARCHAR(7)    NOT NULL  CHECK (Geo_Level IN ('Country','State','County')),
  State_ANSI   CHAR(2)       NOT NULL,
  County_ANSI  CHAR(3)       DEFAULT '000',
  State        VARCHAR(100)  NOT NULL,
  Area_Name    VARCHAR(100)  NOT NULL,
  primary key (State_ANSI, County_ANSI)
);

CREATE TABLE Ag_Codes (
  State_ANSI        CHAR(2)      NOT NULL,
  Ag_District_Code  CHAR(2)      NOT NULL,
  Ag_District       VARCHAR(100) NOT NULL,
  PRIMARY KEY (State_ANSI, Ag_District_Code),
  FOREIGN KEY (State_ANSI) REFERENCES Geo_Codes(State_ANSI)
);

CREATE TABLE Population (
  State_ANSI            CHAR(2) NOT NULL,
  County_ANSI           CHAR(3) NOT NULL,
  Rural_Urban_Code_2013 CHAR(1),
  Population_1990       INT,
  Population_2000       INT,
  Population_2010       INT,
  Population_2020       INT,
  PRIMARY KEY (State_ANSI, County_ANSI),
  FOREIGN KEY (State_ANSI, County_ANSI) REFERENCES Geo_Codes(State_ANSI, County_ANSI)
);

CREATE TABLE Bee_Colonies (
  State_ANSI        CHAR(2) NOT NULL,
  County_ANSI       CHAR(3) NOT NULL,
  Ag_District_Code  CHAR(2),
  Colonies_2002     INT,
  Colonies_2007     INT,
  Colonies_2012     INT,
  Colonies_2017     INT,
  Colonies_2022     INT,
  PRIMARY KEY (State_ANSI, County_ANSI),
  FOREIGN KEY (State_ANSI, County_ANSI) REFERENCES Geo_Codes(State_ANSI, County_ANSI),
  FOREIGN KEY (State_ANSI, Ag_District_Code) REFERENCES Ag_Codes(State_ANSI, Ag_District_Code)
);

INSERT INTO Geo_Codes (Geo_Level, State_ANSI, County_ANSI, State, Area_Name)
SELECT
  CASE
    WHEN LEFT(p.FIPStxt, 2) = '00' THEN 'Country'
    WHEN RIGHT(p.FIPStxt, 3) = '000' THEN 'State'
    ELSE 'County'
  END AS Geo_Level,
  LEFT(p.FIPStxt, 2)   AS State_ANSI,
  RIGHT(p.FIPStxt, 3)  AS County_ANSI,
  p.State          AS State,
  p.Area_name          AS Area_Name
FROM populations AS p;

INSERT INTO Ag_Codes (State_ANSI, Ag_District_Code, Ag_District)
SELECT DISTINCT State_ANSI, Ag_District_Code, Ag_District
FROM state
WHERE Ag_District_Code IS NOT NULL

UNION

SELECT DISTINCT State_ANSI, Ag_District_Code, Ag_District
FROM county
WHERE Ag_District_Code IS NOT NULL;

INSERT INTO Population
  (State_ANSI, County_ANSI,
   Rural_Urban_Code_2013,
   Population_1990, Population_2000,
   Population_2010, Population_2020)
SELECT
  g.State_ANSI,
  g.County_ANSI,
  p.Rural_urban_code_2013,
  p.Population_1990,
  p.Population_2000,
  p.Population_2010,
  p.Population_2020
FROM populations AS p
JOIN Geo_Codes AS g
  ON p.FIPStxt = CONCAT(g.State_ANSI, g.County_ANSI);

INSERT INTO Bee_Colonies
  (State_ANSI, County_ANSI, Ag_District_Code,
   Colonies_2002, Colonies_2007,
   Colonies_2012, Colonies_2017,
   Colonies_2022)
SELECT
  State_ANSI,
  County_ANSI,
  Ag_District_Code,
  SUM(IF(Year = 2002, Value, NULL)) AS Colonies_2002,
  SUM(IF(Year = 2007, Value, NULL)) AS Colonies_2007,
  SUM(IF(Year = 2012, Value, NULL)) AS Colonies_2012,
  SUM(IF(Year = 2017, Value, NULL)) AS Colonies_2017,
  SUM(IF(Year = 2022, Value, NULL)) AS Colonies_2022
FROM county
WHERE Geo_Level = 'County'
GROUP BY
  State_ANSI,
  County_ANSI,
  Ag_District_Code;

INSERT INTO Bee_Colonies
  (State_ANSI, County_ANSI, Ag_District_Code,
   Colonies_2002, Colonies_2007,
   Colonies_2012, Colonies_2017,
   Colonies_2022)
SELECT
  State_ANSI,
  '000' AS County_ANSI,  
  NULL AS Ag_District_Code,
  SUM(IF(Year = 2002, Value, NULL)) AS Colonies_2002,
  SUM(IF(Year = 2007, Value, NULL)) AS Colonies_2007,
  SUM(IF(Year = 2012, Value, NULL)) AS Colonies_2012,
  SUM(IF(Year = 2017, Value, NULL)) AS Colonies_2017,
  SUM(IF(Year = 2022, Value, NULL)) AS Colonies_2022
FROM state
WHERE Geo_Level = 'State'
GROUP BY
  State_ANSI;

