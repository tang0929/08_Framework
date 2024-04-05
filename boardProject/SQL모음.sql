/* 계정 생성(관리자 계정으로 접속) */
ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;


CREATE USER SPRING_NTH IDENTIFIED BY SPRING1234;


GRANT CONNECT, RESOURCE TO SPRING_NTH;


/* SPRING_NTH가 테이블생성 권한을 20M만큼 얻게됨 */
ALTER USER SPRING_NTH DEFAULT TABLESPACE USERS QUOTA 20M ON USERS;


--> 계정 생성 후 접속 방법(새 DB_) 추가

--------------------------------------------------------------------------------------

/* Spring 계정 접속 후 작성하기*/

-- "" : 내부에 작성된 글(모양)을 그대로 인식하여 - 대소문자 구분. ""작성 권장

-- CHAR(10) : 고정 길이에 문자열 10바이트
-- VARCHAR2()20) : 가변길이 문자열 20바이트
-- CLOB : 가변



/* MEMBER테이블 생성*/
CREATE TABLE MEMBER(
"MEMBER_NO" NUMBER CONSTRAINT "MEMBER_PK" PRIMARY KEY,
"MEMBER_EMAIL" NVARCHAR2(50) NOT NULL,
"MEMBER_PW" NVARCHAR2(100) NOT NULL,
"MEMBER_NICKNAME" NVARCHAR2(10) NOT NULL,
"MEMBER_TEL" CHAR(11) NOT NULL,
"MEMBER_ADDRESS" NVARCHAR2(150),
"PROFILE_IMG" VARCHAR2(300),   -- 
"ENROLL_DATE" DATE DEFAULT SYSDATE NOT NULL,
"MEMBER_DEL_FL" CHAR(1) DEFAULT 'N' CHECK("MEMBER_DEL_FL" IN('Y','N')),
"AUTHORITY" NUMBER DEFAULT 1 CHECK("AUTHORITY" IN (1,2)) 

);

--DROP TABLE SPRING_NTH;

/* 회원 번호 시퀀스 만들기 */
CREATE SEQUENCE SEQ_MEMBER_NO NOCACHE;




-- 샘플 회원 데이터 삽입
INSERT INTO MEMBER VALUES(SEQ_MEMBER_NO.NEXTVAL,
'member01@naver.com',
'$2a$10$izuhz1H7TRVbtOFu5DEe3OD1u50XDsWTJfbVebo1vJNB.oBO/jB5q',
'샘플1',
'01012341234',
'서울시',
NULL,
DEFAULT,DEFAULT,DEFAULT);

COMMIT;

SELECT * FROM MEMBER;

UPDATE MEMBER SET MEMBER_PW = '$2a$10$SsDOD0iwBgVAeFv21SFnm.kBSEc5OnRcAiV2a5tefVn62fxmGlh2C';
DELETE FROM "MEMBER" WHERE MEMBER_EMAIL = 'rhoth0402@gmail.com';


-- 로그인

--> BCrypt 암호화 사용 중이므로 DB에서 비밀번호 비교가 불가능
--> 이메일이 일치하는 회원 + 탈퇴 안한 회원의 조건만 추가
SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_PW,
			MEMBER_TEL, MEMBER_ADDRESS, PROFILE_IMG, AUTHORITY,
			TO_CHAR(ENROLL_DATE, 
				'YYYY"년" MM"월" DD"일" HH24"시" MI"분" SS"초"') ENROLL_DATE
FROM MEMBER
WHERE MEMBER_EMAIL = ?
AND   MEMBER_DEL_FL = 'N';


COMMIT;



-- 이메일 중복 검사(탈퇴 안한 회원 중에서 검사)
-- 조회한 결과가 0이면 중복이 없다는 뜻
--SELECT COUNT(*)
--FROM "MEMBER"
--WHERE MEMBER_DEL_FL = 'N'
--AND MEMBER_EMAIL = 


-- 닉네임 중복 검사
SELECT COUNT(*) FROM "MEMBER" WHERE MEMBER_DEL_FL = 'N'
AND MEMBER_NICKNAME;



-- 이메일과 인증키를 저장하는 테이블 생성
CREATE TABLE TB_AUTH_KEY (
	"KEY_NO" NUMBER PRIMARY KEY,
	"EMAIL" NVARCHAR2(50) NOT NULL,
	"AUTH_KEY" CHAR(6) NOT NULL,
	"CREATE_TIME" DATE DEFAULT SYSDATE NOT NULL
);

COMMENT ON COLUMN TB_AUTH_KEY.KEY_NO IS '인증키 구분 번호(시퀀스)';
COMMENT ON COLUMN TB_AUTH_KEY.EMAIL IS '인증 이메일';
COMMENT ON COLUMN TB_AUTH_KEY.AUTH_KEY IS '인증 번호';
COMMENT ON COLUMN TB_AUTH_KEY.CREATE_TIME IS '인증 번호 생성 시간';

CREATE SEQUENCE SEQ_KEY_NO NOCACHE; -- 인증키 순번

SELECT * FROM TB_AUTH_KEY;


/*
SELECT COUNT(*) FROM TB_AUTH_KEY
WHERE EMAIL = ?
AND AUTH_KEY = ?;
*/

SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_DEL_FL FROM MEMBER;


UPDATE MEMBER SET MEMBER_ADDRESS = 'A^^^B^^^C' WHERE MEMBER_NO = 20;
COMMIT;


SELECT * FROM MEMBER;


-----------------------------------------
-- 파일 업로드 테스트용 테이블



CREATE TABLE "UPLOAD_FILE"(
	FILE_NO NUMBER PRIMARY KEY,
	FILE_PATH VARCHAR2(500) NOT NULL,
	FILE_ORIGINAL_NAME VARCHAR2(300) NOT NULL,
	FILE_RENAME VARCHAR2(100) NOT NULL,
	FILE_UPLOAD_DATE DATE DEFAULT SYSDATE,
	MEMBER_NO NUMBER REFERENCES "MEMBER"

);



-- UPLOAD_FILE 코멘트 달기
COMMENT ON COLUMN UPLOAD_FILE.FILE_NO IS '파일번호(PK)';
COMMENT ON COLUMN UPLOAD_FILE.FILE_PATH IS '클라이언토 요청 경로';
COMMENT ON COLUMN UPLOAD_FILE.FILE_ORIGINAL_NAME IS '파일 원본명';
COMMENT ON COLUMN UPLOAD_FILE.FILE_RENAME IS '변경된 파일명';
COMMENT ON COLUMN UPLOAD_FILE.FILE_UPLOAD_DATE IS '업로드 날짜';
COMMENT ON COLUMN UPLOAD_FILE.MEMBER_NO IS 'MEMBER 테이블의 PK 참조';

CREATE SEQUENCE SEQ_FILE_NO NOCACHE;

SELECT * FROM UPLOAD_FILE;

-- 파일 목록 조회
SELECT FILE_NO, FILE_PATH, FILE_ORIGINAL_NAME, FILE_RENAME, TO_CHAR(FILE_UPLOAD_DATE,'YYYY-MM-DD') FILE_UPLOAD_DATE, MEMBER_NICKNAME
FROM UPLOAD_FILE
JOIN MEMBER USING(MEMBER_NO) 
ORDER BY FILE_NO DESC;

