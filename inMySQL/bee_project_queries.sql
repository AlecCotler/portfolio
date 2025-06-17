use bee_project;

-- question 2
select Geo_Level, bee_colonies.State_ANSI, State AS 'State Abbreviation', bee_colonies.County_ANSI, Area_Name, Colonies_2002, Colonies_2007, Colonies_2012, Colonies_2017, Colonies_2022
from geo_codes JOIN bee_colonies 
ON geo_codes.State_ANSI = bee_colonies.State_ANSI AND geo_codes.County_ANSI = bee_colonies.County_ANSI
WHERE geo_codes.State = 'NJ'
ORDER BY Colonies_2022 DESC;
-- question 3
SELECT COUNT(*)  AS num_counties FROM Geo_Codes
WHERE Geo_level = 'County' AND State_ANSI = (
	SELECT DISTINCT State_ANSI 
	FROM Geo_Codes
    WHERE Geo_Level = 'State' AND Area_Name = 'New York'
    );
-- question 4
Select Area_Name as County_Name, format(population_2020,0) AS population_2020, colonies_2022
FROM Geo_Codes LEFT JOIN Population
ON geo_codes.State_ANSI = population.State_ANSI AND geo_codes.County_ANSI = population.County_ANSI
LEFT JOIN bee_colonies 
ON geo_codes.State_ANSI = bee_colonies.State_ANSI AND geo_codes.County_ANSI = bee_colonies.County_ANSI
WHERE Geo_level = 'County' AND geo_codes.State_ANSI = (
	SELECT DISTINCT State_ANSI 
	FROM Geo_Codes
    WHERE Geo_Level = 'State' AND Area_Name = 'New York'
    )
ORDER BY population_2020 DESC;
-- question 5
SELECT Geo_level, geo_codes.State_ANSI, "Alaska" AS State_Name, geo_codes.County_ANSI, Area_Name, 
Colonies_2002, Colonies_2007, Colonies_2012, Colonies_2017, Colonies_2022
from geo_codes JOIN bee_colonies 
ON geo_codes.State_ANSI = bee_colonies.State_ANSI AND geo_codes.County_ANSI = bee_colonies.County_ANSI 
WHERE Colonies_2022 = 
	( SELECT MAX(Colonies_2022) 
    from geo_codes JOIN bee_colonies 
	ON geo_codes.State_ANSI = bee_colonies.State_ANSI AND geo_codes.County_ANSI = bee_colonies.County_ANSI 
    WHERE geo_codes.Geo_Level = "County" AND
    geo_codes.State_ANSI =
    (SELECT DISTINCT State_ANSI FROM Geo_Codes WHERE Area_Name = "Alaska" AND Geo_Level = 'State') 
    )
    AND geo_codes.State_ANSI =
    (SELECT DISTINCT State_ANSI FROM Geo_Codes WHERE Area_Name = "Alaska" AND Geo_Level = 'State');
-- question 6
SELECT Geo_Codes.state_ansi, Area_Name AS state, COUNT(DISTINCT(Ag_District_Code)) AS num_ag_districts
FROM Geo_codes JOIN Ag_Codes
ON Geo_codes.State_ANSI = Ag_Codes.State_ANSI
WHERE geo_codes.County_ANSI = '000'
GROUP BY Geo_codes.State_ANSI
ORDER BY state ASC;
-- question 7
SELECT 
  ag.State_ANSI, state_geo.Area_Name AS state, ag.Ag_District_Code, ag.Ag_District,
  SUM(bc.Colonies_2002) AS Colonies_2002,
  SUM(bc.Colonies_2007) AS Colonies_2007,
  SUM(bc.Colonies_2012) AS Colonies_2012,
  SUM(bc.Colonies_2017) AS Colonies_2017,
  SUM(bc.Colonies_2022) AS Colonies_2022
FROM bee_colonies AS bc
JOIN ag_codes AS ag
  ON bc.State_ANSI = ag.State_ANSI AND bc.Ag_District_Code = ag.Ag_District_Code
JOIN geo_codes AS state_geo
  ON ag.State_ANSI = state_geo.State_ANSI AND state_geo.Geo_Level = 'State'
GROUP BY 
  ag.State_ANSI, state_geo.Area_Name, ag.Ag_District_Code, ag.Ag_District
HAVING SUM(bc.Colonies_2022) = (
  SELECT MAX(total_colonies)
  FROM (
    SELECT SUM(Colonies_2022) AS total_colonies
    FROM bee_colonies
    WHERE Ag_District_Code IS NOT NULL
    GROUP BY State_ANSI, Ag_District_Code
  ) AS district_totals
);
-- question 8
WITH StateColonySums AS (
  SELECT 
    g.State_ANSI,
    g.State,
    g.Area_Name AS State_Name,
    SUM(b.Colonies_2002) AS Colonies_2002,
    SUM(b.Colonies_2007) AS Colonies_2007,
    SUM(b.Colonies_2012) AS Colonies_2012,
    SUM(b.Colonies_2017) AS Colonies_2017,
    SUM(b.Colonies_2022) AS Colonies_2022
  FROM bee_colonies b
  JOIN geo_codes g ON b.State_ANSI = g.State_ANSI AND g.Geo_Level = 'State'
  GROUP BY g.State_ANSI, g.State, g.Area_Name
),
StateChanges AS (
  SELECT 
    *,
    ROUND((Colonies_2022 - Colonies_2002) * 100.0 / Colonies_2002) AS Percent_Change
  FROM StateColonySums
  WHERE Colonies_2002 is not NULL and Colonies_2022 is NOT NULL
),
MaxMinChange AS (
  SELECT Percent_Change FROM StateChanges
  WHERE Percent_Change = (SELECT MAX(Percent_Change) FROM StateChanges)
     OR Percent_Change = (SELECT MIN(Percent_Change) FROM StateChanges)
)
SELECT 
  s.state,
  s.State_Name,
  FORMAT(s.Colonies_2002, 0) AS Colonies_2002,
  FORMAT(s.Colonies_2007, 0) AS Colonies_2007,
  FORMAT(s.Colonies_2012, 0) AS Colonies_2012,
  FORMAT(s.Colonies_2017, 0) AS Colonies_2017,
  FORMAT(s.Colonies_2022, 0) AS Colonies_2022,
  CONCAT(s.Percent_Change, '%') AS Percent_Change
FROM StateChanges s
JOIN MaxMinChange m ON s.Percent_Change = m.Percent_Change;
-- question 9
WITH StateColonyChanges AS (
  SELECT 
    g.State_ANSI,
    g.State,
    g.Area_Name AS State_Name,
    SUM(b.Colonies_2002) AS Colonies_2002,
    SUM(b.Colonies_2022) AS Colonies_2022,
    ROUND((SUM(b.Colonies_2022) - SUM(b.Colonies_2002)) * 100.0 / SUM(b.Colonies_2002)) AS Percent_Change
  FROM bee_colonies b
  JOIN geo_codes g 
    ON b.State_ANSI = g.State_ANSI AND g.Geo_Level = 'State'
  GROUP BY g.State_ANSI, g.State, g.Area_Name
  HAVING Colonies_2002 is not NULL and colonies_2022 is not NULL
),
ExtremeChangeStates AS (
  SELECT State_ANSI FROM StateColonyChanges
  WHERE Percent_Change = (SELECT MAX(Percent_Change) FROM StateColonyChanges)
     OR Percent_Change = (SELECT MIN(Percent_Change) FROM StateColonyChanges)
)
SELECT 
  g.State,
  g.Area_Name AS State_Name,
  FORMAT(p.Population_1990, 0) AS Population_1990,
  FORMAT(p.Population_2000, 0) AS Population_2000,
  FORMAT(p.Population_2010, 0) AS Population_2010,
  FORMAT(p.Population_2020, 0) AS Population_2020,
  FORMAT(p.Population_2020 - p.Population_1990, 0) AS Amount_of_Change
FROM Population p
JOIN geo_codes g 
  ON p.State_ANSI = g.State_ANSI
JOIN ExtremeChangeStates ecs 
  ON p.State_ANSI = ecs.State_ANSI
  WHERE p.County_ANSI = '000' AND g.Geo_Level = 'State';
