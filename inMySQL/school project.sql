-- NOTE: USE WHICHEVER DATABASE YOU WANT INSTEAD OF alec --
use alec;

-- NOTE: USE TABLE IMPORT WISARD TO GET THE DATA, NAME THE TABLES school_salary_src and school_src respectively, but dont change any of the datatypes.
DROP TABLE IF EXISTS school_salary;
DROP TABLE IF EXISTS school;
-- NOTE, EXPRESS PERMISSION FROM PROF. ORDILLE was obtained to have school_salary reference school instead of the other way around
CREATE TABLE school (
    school_name VARCHAR(100) NOT NULL,
    conference VARCHAR(11),
    PRIMARY KEY (school_name),
    CHECK (conference IS NULL OR conference IN ('Patriot', 'Pac-12', 'SEC', 'Big 12', 'ACC', 'Big Ten', 'Independent'))
);

CREATE TABLE school_salary (
    school VARCHAR(100) NOT NULL,
    region VARCHAR(12),
    starting_median DECIMAL(10,2),
    mid_career_median DECIMAL(10,2),
    mid_career_90 DECIMAL(10,2),
    PRIMARY KEY (school),
    CHECK (region IS NULL OR region IN ('Northeastern', 'Southern', 'Western', 'Midwestern', 'California')),
    FOREIGN KEY (school) REFERENCES school(school_name)
    ON UPDATE Cascade
    ON DELETE Cascade
);
-----------------------------------------------
-- get all of the schools and conferences into school, and set blanks equal to null for the conference
INSERT INTO school (school_name, conference)
SELECT 
    school_name,
    CASE 
        WHEN conference = ' ' THEN NULL 
        ELSE conference 
    END
FROM school_src;
---------------------------------------

-- Clean the data such that the names of schools in school will match those in school_salary
UPDATE school s
JOIN (
    SELECT s.school_name, ss.school AS correct_school
    FROM school s
    JOIN school_salary_src ss 
        ON (s.school_name LIKE CONCAT('%', ss.school, '%') 
            OR ss.school LIKE CONCAT('%', s.school_name, '%'))
    WHERE s.school_name <> ss.school -- finds the names of schools that dont match but are contained within each other
    AND NOT EXISTS (
        SELECT 1 
        FROM school_salary_src ss_exact 
        WHERE ss_exact.school = s.school_name
    ) -- gets rid of those that are already matched (to go from 6 to 4 school names that need to be switched)
) AS subquery
ON s.school_name = subquery.school_name
SET s.school_name = subquery.correct_school; -- changes the school_name to be that in the school_salary table for those that need to be changed
---------------------------------------------
-- adds the schools that are missing in school (not the ones that needed to be changed) from school_salary_src so the foreign key constraint will work
INSERT INTO school (school_name)
SELECT DISTINCT ss.school
FROM school_salary_src ss
LEFT JOIN school s ON ss.school = s.school_name
WHERE s.school_name IS NULL;
---------------------------------------
-- adds and cleans the data to enter into school_salary the correct data types
INSERT INTO school_salary (school, region, starting_median, mid_career_median, mid_career_90)
SELECT 
    school,
    CASE 
        WHEN TRIM(region) = '' THEN NULL 
        ELSE region 
    END,
    CASE 
        WHEN TRIM(starting_median) = '' OR TRIM(starting_median) LIKE '%[^0-9,.]%' THEN NULL 
        ELSE CAST(REPLACE(REPLACE(TRIM(starting_median), '$', ''), ',', '') AS DECIMAL(10,2)) 
    END,
    CASE 
        WHEN TRIM(mid_career_median) = '' OR TRIM(mid_career_median) LIKE '%[^0-9,.]%' THEN NULL 
        ELSE CAST(REPLACE(REPLACE(TRIM(mid_career_median), '$', ''), ',', '') AS DECIMAL(10,2)) 
    END,
    CASE 
        WHEN TRIM(mid_career_90) = '' OR TRIM(mid_career_90) LIKE '%[^0-9,.]%' THEN NULL 
        ELSE CAST(REPLACE(REPLACE(TRIM(mid_career_90), '$', ''), ',', '') AS DECIMAL(10,2)) 
    END
FROM school_salary_src;
------------------------------------------------------------
-- updates the conferences to be current
UPDATE school
SET conference = 'ACC'
WHERE school_name IN('Stanford University','University of California, Berkeley');

UPDATE school
SET conference = 'Big 12'
WHERE school_name IN ('Arizona State University (ASU)','University of Arizona','University of Colorado - Boulder (UCB)','University of Utah');

UPDATE school
SET conference = 'Big Ten'
WHERE school_name IN ('University of California at Los Angeles (UCLA)','University of Oregon' ,'University of Washington (UW)');
--------------------------------------------------------------------------------------------------
-- Q2
SELECT school, region, CONCAT('$',FORMAT(starting_median,2))AS starting_median , CONCAT('$',FORMAT(mid_career_median,2))AS mid_career_median, CONCAT('$',FORMAT(mid_career_90,2)) AS mid_career_90
FROM school_salary
WHERE school = 'Rutgers University';
------------------------------------------------------------------------------------------------
-- Q3
SELECT conference, count(school_name) AS "Number of Schools"
FROM school
WHERE conference IS NOT NULL
GROUP BY conference
ORDER BY count(school_name) DESC, conference ASC;
-----------------------------------------------------------------------------------------------
-- Q4
SELECT * from school_salary 
WHERE school LIKE '%tech%'
ORDER BY starting_median DESC;
------------------------------------------------------------------------------------------
-- Q5
SELECT school, CONCAT('$',FORMAT(starting_median,2))AS starting_median , CONCAT('$',FORMAT(mid_career_median,2))AS mid_career_median, CONCAT('$',FORMAT(mid_career_90,2)) AS mid_career_90
FROM school_salary
WHERE school IN ('Fairleigh Dickinson University', 'Princeton University', 'Rider University', 'Rutgers University', 'Seton Hall University', 'Stevens Institute of Technology')
ORDER BY school ASC;
----------------------------------------------------------------------------
-- Q6
SELECT DISTINCT conference,region 
FROM school JOIN school_salary
ON school_name = school
WHERE conference IS NOT NULL AND school_name IS NOT NULL
ORDER BY conference, region;
------------------------------------------------------------------------
-- Q7
SELECT school, conference, region, mid_career_median
FROM school JOIN school_salary
ON school_name = school
WHERE region = 'Northeastern' AND conference IS NOT NULL
ORDER BY mid_career_median DESC;
-----------------------------------------------------------------------
-- Q8
SELECT *
FROM school JOIN school_salary
ON school_name = school
WHERE region = 'Northeastern' AND mid_career_median = (
    SELECT MAX(mid_career_median)
    FROM school_salary
    WHERE region = 'Northeastern'
);
------------------------------------------------------------------------
-- Q9
SELECT conference, COUNT(school_name) AS Member_Count
FROM school
WHERE conference IS NOT NULL AND conference != 'Big Ten'
GROUP BY conference
HAVING COUNT(school_name) = (
    SELECT MAX(count_schools)
    FROM (
        SELECT COUNT(school_name) AS count_schools
        FROM school
        WHERE conference IS NOT NULL AND conference != 'Big Ten'
        GROUP BY conference
    ) AS subquery
);