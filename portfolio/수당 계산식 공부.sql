CREATE TABLE EMPLOYEE(
  EMPLOYEE_CD INT PRIMARY KEY
);

CREATE TABLE SALARY(
  EMPLOYEE_CD INT PRIMARY KEY,
  SALARY_HD INT,
  SALARY_OP INT,
  FOREIGN KEY (EMPLOYEE_CD) REFERENCES EMPLOYEE(EMPLOYEE_CD)
);

CREATE TABLE COMMUTE(
  EMPLOYEE_CD INT,
  WORK_TM TIME,
  HO_WORK_TM TIME,
  WORK_OVER_TM TIME,
  FOREIGN KEY (EMPLOYEE_CD) REFERENCES EMPLOYEE(EMPLOYEE_CD)
);


INSERT INTO EMPLOYEE(EMPLOYEE_CD)
VALUES (102);

INSERT INTO COMMUTE(EMPLOYEE_CD,WORK_TM, HO_WORK_TM, WORK_OVER_TM)
VALUES (102, '05:58:48', '05:58:48', '05:58:48');

SELECT *
FROM EMPLOYEE;

SELECT *
FROM COMMUTE;

SELECT TRUNCATE((10030 * 1.5 *  HOUR(SEC_TO_TIME(ROUND(TIME_TO_SEC(HO_WORK_TM) / 1800) * 1800))),0) AS "야근수당"
FROM COMMUTE;
