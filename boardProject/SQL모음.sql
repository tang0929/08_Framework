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




---------------------------------------------------------------------------------------------------------------------------


CREATE TABLE "MEMBER" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"MEMBER_EMAIL"	NVARCHAR2(50)		NOT NULL,
	"MEMBER_PW"	NVARCHAR2(100)		NOT NULL,
	"MEMBER_NICKNAME"	NVARCHAR2(10)		NOT NULL,
	"MEMBER_TEL"	CHAR(11)		NOT NULL,
	"MEMBER_ADDRESS"	NVARCHAR2(300)		NULL,
	"PROFILE_IMG"	VARCHAR2(300)		NULL,
	"ENROLL_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MEMBER_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"AUTHORITY"	NUMBER	DEFAULT 1	NOT NULL
);

COMMENT ON COLUMN "MEMBER"."MEMBER_NO" IS '회원 번호';

COMMENT ON COLUMN "MEMBER"."MEMBER_EMAIL" IS '회원 이메일(ID)';

COMMENT ON COLUMN "MEMBER"."MEMBER_PW" IS '회원 비밀번호(암호화)';

COMMENT ON COLUMN "MEMBER"."MEMBER_NICKNAME" IS '회원 닉네임';

COMMENT ON COLUMN "MEMBER"."MEMBER_TEL" IS '회원 전화번호';

COMMENT ON COLUMN "MEMBER"."MEMBER_ADDRESS" IS '회원 주소';

COMMENT ON COLUMN "MEMBER"."PROFILE_IMG" IS '프로필 이미지';

COMMENT ON COLUMN "MEMBER"."ENROLL_DATE" IS '회원 가입일';

COMMENT ON COLUMN "MEMBER"."MEMBER_DEL_FL" IS '탈퇴 여부(Y,N)';

COMMENT ON COLUMN "MEMBER"."AUTHORITY" IS '권한(1:일반 2:관리자)';

CREATE TABLE "UPLOAD_FILE" (
	"FILE_NO"	NUMBER		NOT NULL,
	"FILE_PATH"	VARCHAR2(500)		NOT NULL,
	"FILE_ORIGINAL_NAME"	VARCHAR2(300)		NOT NULL,
	"FILE_RENAME"	VARCHAR2(100)		NOT NULL,
	"FILE_UPLOAD_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_NO" IS '파일 번호(PK)';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_PATH" IS '파일요청경로';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_ORIGINAL_NAME" IS '파일 원본명';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_RENAME" IS '파일 변경명';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_UPLOAD_DATE" IS '파일 업로드 날짜';

COMMENT ON COLUMN "UPLOAD_FILE"."MEMBER_NO" IS '업로드한 회원 번호';

CREATE TABLE "BOARD" (
	"BOARD_NO"	NUMBER		NOT NULL,
	"BOARD_TITLE"	NVARCHAR2(100)		NOT NULL,
	"BOARD_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"BOARD_WRITE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"BOARD_UPDATE_DATE"	DATE		NULL,
	"READ_COUNT"	NUMBER	DEFAULT 0	NOT NULL,
	"BOARD_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"BOARD_CODE"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD"."BOARD_NO" IS '게시글 번호(PK)';

COMMENT ON COLUMN "BOARD"."BOARD_TITLE" IS '게시글 제목';

COMMENT ON COLUMN "BOARD"."BOARD_CONTENT" IS '게시글 내용';

COMMENT ON COLUMN "BOARD"."BOARD_WRITE_DATE" IS '게시글 작성일';

COMMENT ON COLUMN "BOARD"."BOARD_UPDATE_DATE" IS '게시글 마지막 수정일';

COMMENT ON COLUMN "BOARD"."READ_COUNT" IS '게시글 조회수';

COMMENT ON COLUMN "BOARD"."BOARD_DEL_FL" IS '게시글 삭제 여부(Y/N)';

COMMENT ON COLUMN "BOARD"."BOARD_CODE" IS '게시판 종류에 따른 코드 번호';

COMMENT ON COLUMN "BOARD"."MEMBER_NO" IS '작성한 회원번호(FK)';

CREATE TABLE "BOARD_TYPE" (
	"BOARD_CODE"	NUMBER		NOT NULL,
	"BOARD_NAME"	NVARCHAR2(20)		NOT NULL
);

COMMENT ON COLUMN "BOARD_TYPE"."BOARD_CODE" IS '게시판 종류 코드 번호';

COMMENT ON COLUMN "BOARD_TYPE"."BOARD_NAME" IS '게시판명';

CREATE TABLE "BOARD_LIKE" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);



COMMENT ON COLUMN "BOARD_LIKE"."BOARD_NO" IS '게시글 번호(PK)';
COMMENT ON COLUMN "BOARD_LIKE"."MEMBER_NO" IS '회원 번호(PK)';


CREATE TABLE "BOARD_IMG" (
	"IMG_NO"	NUMBER		NOT NULL,
	"IMG_PATH"	VARCHAR2(200)		NOT NULL,
	"IMG_ORIGINAL_NAME"	NVARCHAR2(50)		NOT NULL,
	"IMG_RENAME"	NVARCHAR2(50)		NOT NULL,
	"IMG_ORDER"	NUMBER		NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD_IMG"."IMG_NO" IS '이미지 번호(PK)';

COMMENT ON COLUMN "BOARD_IMG"."IMG_PATH" IS '이미지 요청 경로';

COMMENT ON COLUMN "BOARD_IMG"."IMG_ORIGINAL_NAME" IS '이미지 원본명';

COMMENT ON COLUMN "BOARD_IMG"."IMG_RENAME" IS '이미지 변경명';

COMMENT ON COLUMN "BOARD_IMG"."IMG_ORDER" IS '이미지 순서';

COMMENT ON COLUMN "BOARD_IMG"."BOARD_NO" IS '게시글 번호(PK)';

CREATE TABLE "COMMENT" (
	"COMMENT_NO"	NUMBER		NOT NULL,
	"COMMENT_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"COMMENT_WRITE_DATE"	DATE		NOT NULL,
	"COMMENT_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"PARENT_COMMENT_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "COMMENT"."COMMENT_NO" IS '댓글 번호(PK)';

COMMENT ON COLUMN "COMMENT"."COMMENT_CONTENT" IS '댓글 내용';

COMMENT ON COLUMN "COMMENT"."COMMENT_WRITE_DATE" IS '댓글 작성일';

COMMENT ON COLUMN "COMMENT"."COMMENT_DEL_FL" IS '댓글 삭제 여부(Y/N)';

COMMENT ON COLUMN "COMMENT"."BOARD_NO" IS '게시글 번호(PK)';

COMMENT ON COLUMN "COMMENT"."MEMBER_NO" IS '회원 번호';

COMMENT ON COLUMN "COMMENT"."PARENT_COMMENT_NO" IS '부모 댓글 번호';

ALTER TABLE "MEMBER" ADD CONSTRAINT "PK_MEMBER" PRIMARY KEY (
	"MEMBER_NO"
);

ALTER TABLE "UPLOAD_FILE" ADD CONSTRAINT "PK_UPLOAD_FILE" PRIMARY KEY (
	"FILE_NO"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "PK_BOARD" PRIMARY KEY (
	"BOARD_NO"
);

ALTER TABLE "BOARD_TYPE" ADD CONSTRAINT "PK_UNTITLED4" PRIMARY KEY (
	"BOARD_CODE"
);

ALTER TABLE BOARD_LIKE DROP CONSTRAINT PK_BOARD_LIKE;

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "PK_BOARD_LIKE" PRIMARY KEY (
	"MEMBER_NO",
	"BOARD_NO"
);

ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "PK_BOARD_IMG" PRIMARY KEY (
	"IMG_NO"
);
  
ALTER TABLE "COMMENT" ADD CONSTRAINT "PK_COMMENT" PRIMARY KEY (
	"COMMENT_NO"
);






COMMIT;




ALTER TABLE "UPLOAD_FILE" ADD CONSTRAINT "FK_MEMBER_TO_UPLOAD_FILE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "FK_BOARD_TYPE_TO_BOARD_1" FOREIGN KEY (
	"BOARD_CODE"
)
REFERENCES "BOARD_TYPE" (
	"BOARD_CODE"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "FK_MEMBER_TO_BOARD_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "FK_BOARD_TO_BOARD_LIKE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "FK_BOARD_TO_BOARD_IMG_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_BOARD_TO_COMMENT_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_MEMBER_TO_COMMENT_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_COMMENT_TO_COMMENT_1" FOREIGN KEY (
	"PARENT_COMMENT_NO"
)
REFERENCES "COMMENT" (
	"COMMENT_NO"
);






-------------------------------------------------------------------------------------
----------------CHECK제약조건 추가---------------------------------------------------------

--게시글 삭제 여부
ALTER TABLE "BOARD" ADD
CONSTRAINT "BOARD_DEL_CHECK"
CHECK("BOARD_DEL_FL" IN ('Y','N'));


--댓글 삭제 여부
ALTER TABLE "COMMENT" ADD
CONSTRAINT "COMMENT_DEL_CHECK"
CHECK("COMMENT_DEL_FL" IN ('Y','N'));




SELECT * FROM BOARD WHERE BOARD_DEL_FL ='Y';




--------------------------------------------------------------------------------------------------------------





/* 책 관리 프로젝트 연습용 */

CREATE TABLE "BOOK" (
	"BOOK_NO"	NUMBER		NOT NULL,
	"BOOK_TITLE"	NVARCHAR2(50)		NOT NULL,
	"BOOK_WRITER"	NVARCHAR2(20)		NOT NULL,
	"BOOK_PRICE"	NUMBER		NOT NULL,
	"REG_DATE"	DATE	DEFAULT SYSDATE	NULL
);

COMMENT ON COLUMN "BOOK"."BOOK_NO" IS '책 번호';

COMMENT ON COLUMN "BOOK"."BOOK_TITLE" IS '책 제목';

COMMENT ON COLUMN "BOOK"."BOOK_WRITER" IS '글쓴이';

COMMENT ON COLUMN "BOOK"."BOOK_PRICE" IS '가격';

COMMENT ON COLUMN "BOOK"."REG_DATE" IS '등록일';

ALTER TABLE "BOOK" ADD CONSTRAINT "PK_BOOK" PRIMARY KEY (
	"BOOK_NO"
);


SELECT * FROM "BOOK";

CREATE SEQUENCE SEQ_BOOK_NO NOCACHE;
INSERT INTO "BOOK" VALUES(SEQ_BOOK_NO.NEXTVAL,'테스트북','김길동','12345',SYSDATE);


SELECT BOOK_NO, BOOK_TITLE, BOOK_WRITER, BOOK_PRICE, TO_CHAR(REG_DATE,'YYYY-MM-DD HH24:MI:SS') REG_DATE FROM BOOK;
FROM BOOK;




--------------------------------------------------------------------------------






CREATE TABLE TB_USER(

USER_NO NUMBER PRIMARY KEY,

USER_ID VARCHAR2(50) UNIQUE NOT NULL,

USER_NAME VARCHAR2(50) NOT NULL,

USER_AGE NUMBER NOT NULL

);

CREATE SEQUENCE SEQ_UNO;




INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'gd_hong', '홍길동', 20);

INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'sh_han', '한소희', 28);

INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'jm_park', '지민', 27);

INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'jm123', '지민', 25);



COMMIT;



SELECT * FROM TB_USER;





CREATE TABLE CUSTOMER(

CUSTOMER_NO NUMBER PRIMARY KEY,

CUSTOMER_NAME VARCHAR2(60) NOT NULL,

CUSTOMER_TEL VARCHAR2(30) NOT NULL,

CUSTOMER_ADDRESS VARCHAR2(200) NOT NULL

);



CREATE SEQUENCE SEQ_CUSTOMER_NO NOCACHE;


SELECT * FROM CUSTOMER;





-----------------------------------------------------------------------------------------------




/* 게시판 종류 추가 */
CREATE SEQUENCE SEQ_BOARD_CODE NOCACHE;

INSERT INTO "BOARD_TYPE" VALUES(SEQ_BOARD_CODE.NEXTVAL, '공지 게시판');
INSERT INTO "BOARD_TYPE" VALUES(SEQ_BOARD_CODE.NEXTVAL, '정보 게시판');
INSERT INTO "BOARD_TYPE" VALUES(SEQ_BOARD_CODE.NEXTVAL, '자유 게시판');
COMMIT;

SELECT * FROM BOARD_TYPE
ORDER BY BOARD_CODE;



----------------------------------------------------------------------------------------------
/* 게시글 번호 시퀀스 생성 */
CREATE SEQUENCE SEQ_BOARD_NO NOCACHE;

SELECT * FROM MEMBER;

/* DBMS_RANDOM.VALUE(0,3) : 0.0이상 3.0미만의 난수 생성*/
/* CEIL : 올림*/
/* 게시판(BOARD)에 샘플 데이터 삽입(PL/SQL) */
BEGIN
	
	FOR I IN 1 .. 3 LOOP
		INSERT INTO BOARD VALUES(SEQ_BOARD_NO.NEXTVAL, SEQ_BOARD_NO.CURRVAL || '번째 게시글', SEQ_BOARD_NO.CURRVAL || '번째 게시글의 내용',
	DEFAULT,DEFAULT,DEFAULT,DEFAULT,
CEIL(DBMS_RANDOM.VALUE(0,3)),21 );
	END LOOP;
	
END;


SELECT * FROM BOARD;


-- 게시판 종류별 샘플 데이터 삽입 확인
SELECT BOARD_CODE, COUNT(*) FROM BOARD GROUP BY BOARD_CODE ORDER BY BOARD_CODE;



------------------------------------------------------------------
/* 부모 댓글 번호 NULL 허용하도록 변경*/
ALTER TABLE "COMMENT" MODIFY PARENT_COMMENT_NO NUMBER NULL;




CREATE SEQUENCE SEQ_COMMENT_NO NOCACHE;





/* 댓글 테이블에 샘플 데이터 추가*/

BEGIN
	
	FOR I IN 1..3 LOOP
		
		INSERT INTO "COMMENT" VALUES(SEQ_COMMENT_NO.NEXTVAL, SEQ_COMMENT_NO.CURRVAL || '번째 댓글', DEFAULT, DEFAULT,
	CEIL(DBMS_RANDOM.VALUE(0,2000)),21,NULL);
		
	END LOOP;
	
END;
COMMIT;



-- 게시글 번호 최소값, 최대값
SELECT MIN(BOARD_NO), MAX(BOARD_NO) FROM "BOARD";



SELECT COUNT(*) FROM "COMMENT";

/* 댓글 삽입 확인 */
SELECT BOARD_NO, COUNT(*) FROM "COMMENT" GROUP BY BOARD_NO ORDER BY BOARD_NO;

COMMIT;


------------------------------------------

/* 특정 게시판(BOARD_CODE)에 삭제되지 않은 게시글 목록을 조회. 단, 최신글이 제일 위에 존재
 *몇 초/분/시간 전 또는 YYYY-MM-DD형식으로 작성일을 조회
 *
 * + 댓글 개수
 * + 좋아요 개수
 */

-- 번호 / 제목[댓글개수] / 작성자 닉네임 / 조회수 / 좋아요 개수 / 작성일
-- 상관 서브 쿼리
/* 1. 메인 쿼리 1행 조회
 * 2. 1행 조회 결과를 이용해서 서브쿼리 수행
 * (메인쿼리 모두 조회할 때 까지 반복)*/
SELECT BOARD_NO, BOARD_TITLE, MEMBER_NICKNAME, READ_COUNT, 

(SELECT COUNT(*) FROM "COMMENT" C
WHERE C.BOARD_NO = B.BOARD_NO) COMMENT_COUNT, 

(SELECT COUNT(*) FROM "BOARD_LIKE" L
WHERE L.BOARD_NO = B.BOARD_NO) LIKE_COUNT,

CASE 
	WHEN SYSDATE - BOARD_WRITE_DATE < 1 / 24 / 60
	THEN FLOOR((SYSDATE - BOARD_WRITE_DATE) * 24 * 60 * 60) || '초 전'
	
	WHEN SYSDATE - BOARD_WRITE_DATE < 1 / 24 
	THEN FLOOR((SYSDATE - BOARD_WRITE_DATE) * 24 * 60)  || '분 전'
	
	WHEN SYSDATE - BOARD_WRITE_DATE < 1
	THEN FLOOR((SYSDATE - BOARD_WRITE_DATE) * 24 ) || '시간 전'
	
	ELSE TO_CHAR(BOARD_WRITE_DATE, 'YYYY-MM-DD')
	
	
END BOARD_WRITE_DATE 

FROM "BOARD" B
JOIN "MEMBER" USING(MEMBER_NO)
WHERE BOARD_DEL_FL = 'N'
AND BOARD_CODE = 1
ORDER BY BOARD_NO DESC;


-- 특정 게시글의 댓글 개수 조회
SELECT COUNT(*) FROM "COMMENT" WHERE BOARD_NO = 1;

SELECT (SYSDATE - TO_DATE('2024-04-10 12:14:30','YYYY-MM-DD HH24:MI:SS'))* 60 * 60 *24 FROM DUAL;



-- 지정된 게시판에서 삭제되지 않은 게시글을 조회

SELECT COUNT(*) FROM "BOARD"
WHERE BOARD_DEL_FL = 'N'
AND BOARD_CODE = 3;




--------------------------------------------------------------------------------------------------







/* BOARD_IMG 테이블용 시퀀스 생성 */
CREATE SEQUENCE SEQ_IMG_NO NOCACHE;


/* BOARD_IMG 테이블에 샘플 데이터 삽입*/
INSERT INTO "BOARD_IMG" VALUES(
SEQ_IMG_NO.NEXTVAL,
'/images/board/', '원본1.jpg', 'test1.jpg',0,2006
);
INSERT INTO "BOARD_IMG" VALUES(
SEQ_IMG_NO.NEXTVAL,
'/images/board/', '원본2.jpg', 'test2.jpg',1,2006
);
INSERT INTO "BOARD_IMG" VALUES(
SEQ_IMG_NO.NEXTVAL,
'/images/board/', '원본3.jpg', 'test3.jpg',2,2006
);
INSERT INTO "BOARD_IMG" VALUES(
SEQ_IMG_NO.NEXTVAL,
'/images/board/', '원본4.jpg', 'test4.jpg',1,2006
);
INSERT INTO "BOARD_IMG" VALUES(
SEQ_IMG_NO.NEXTVAL,
'/images/board/', '원본5.jpg', 'test5.jpg',1,2006
);

COMMIT;

SELECT * FROM BOARD_IMG;


/* 게시글 상세 조회 */
SELECT BOARD_NO, BOARD_TITLE, BOARD_CONTENT, BOARD_CODE, READ_COUNT, 
	MEMBER_NO, MEMBER_NICKNAME, PROFILE_IMG,
	
	TO_CHAR(BOARD_WRITE_DATE, 'YYYY"년" MM"월" DD"일" HH24:MI:SS') BOARD_WRITE_DATE,  
	TO_CHAR(BOARD_UPDATE_DATE, 'YYYY"년" MM"월" DD"일" HH24:MI:SS') BOARD_UPDATE_DATE,
	
	(SELECT COUNT(*) 
	 FROM "BOARD_LIKE"
	 WHERE BOARD_NO = 2006) LIKE_COUNT,
	 
	(SELECT IMG_PATH || IMG_RENAME 
	 FROM "BOARD_IMG"
	 WHERE BOARD_NO = 2006
	 AND   IMG_ORDER = 0) THUMBNAIL,
	 
	 (SELECT COUNT(*) 
	 FROM BOARD_LIKE 
	 WHERE MEMBER_NO = 21
	 AND BOARD_NO = 2006) LIKE_CHECK

FROM "BOARD"
JOIN "MEMBER" USING(MEMBER_NO)
WHERE BOARD_DEL_FL = 'N'
AND BOARD_CODE = 1
AND BOARD_NO = 2006
;


-------------------------------------------------------------------


/* 상세조회되는 게시글의 모든 이미지 조회*/
SELECT * FROM "BOARD_IMG"
WHERE BOARD_NO = 2006
ORDER BY IMG_ORDER;



/* 상세조회 되는 게시글의 모든 댓글 조회 */
SELECT LEVEL, C.* FROM
      (SELECT COMMENT_NO, COMMENT_CONTENT,
          TO_CHAR(COMMENT_WRITE_DATE, 'YYYY"년" MM"월" DD"일" HH24"시" MI"분" SS"초"') COMMENT_WRITE_DATE,
          BOARD_NO, MEMBER_NO, MEMBER_NICKNAME, PROFILE_IMG, PARENT_COMMENT_NO, COMMENT_DEL_FL
      FROM "COMMENT"
      JOIN MEMBER USING(MEMBER_NO)
      WHERE BOARD_NO = 2006) C
   WHERE COMMENT_DEL_FL = 'N'
   OR 0 != (SELECT COUNT(*) FROM "COMMENT" SUB
               WHERE SUB.PARENT_COMMENT_NO = C.COMMENT_NO
               AND COMMENT_DEL_FL = 'N')
   START WITH PARENT_COMMENT_NO IS NULL
   CONNECT BY PRIOR COMMENT_NO = PARENT_COMMENT_NO
   ORDER SIBLINGS BY COMMENT_NO;


  
------------------------------------------------------------------------
  
  
  /* 좋아요 샘플데이터 */
  
  INSERT INTO BOARD_LIKE VALUES(21,2006);  -- 21번 회원이 2006번 게시글에 좋아요함
  
  COMMIT;
 
 SELECT * FROM BOARD_LIKE;


/* 좋아요 여부 확인(COUNT 값이 1이면 누른상태 아니면 0) */
SELECT COUNT(*) FROM BOARD_LIKE WHERE MEMBER_NO = 21 AND BOARD_NO = 2006;




----------------------------------------------------------
CREATE TABLE TB_USER(

USER_NO NUMBER PRIMARY KEY,

USER_ID VARCHAR2(50) UNIQUE NOT NULL,

USER_NAME VARCHAR2(50) NOT NULL,

USER_AGE NUMBER NOT NULL

);

CREATE SEQUENCE SEQ_UNO;


INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'gd_hong', '홍길동', 20);

INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'sh_han', '한소희', 28);

INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'jm_park', '지민', 27);

INSERT INTO TB_USER VALUES(SEQ_UNO.NEXTVAL, 'jm123', '지민', 25);

COMMIT;

SELECT * FROM TB_USER;



-------------------------------------------------------------------------------------




/* 여러 행을 한번에 삽입하는 방법 -> INSERT + SUBQUERY */



INSERT INTO "BOARD_IMG"
(
SELECT NEXT_IMG_NO(), '경로1','원본1','변경1', 1, 1999 FROM DUAL
UNION
SELECT NEXT_IMG_NO(), '경로2','원본2','변경2', 2, 1999 FROM DUAL
UNION
SELECT NEXT_IMG_NO(), '경로3','원본3','변경3', 3, 1999 FROM DUAL
);

SELECT SEQ_IMG_NO.NEXTVAL FROM DUAL;

SELECT * FROM BOARD_IMG;

-- OPA-02287 : 시퀀스 번호는 이 위치에 사용할 수 없습니다.
-- 시퀀스로 번호 생성하는 부분을 별도 함수로 분리 후 호출하면 문제 없음


ROLLBACK;


-- SEQ_IMG_NO 시퀀스의 다음 값을 반환하는 함수를 생성
CREATE OR REPLACE FUNCTION NEXT_IMG_NO 
RETURN NUMBER 

IS IMG_NO NUMBER;
BEGIN 
	SELECT SEQ_IMG_NO.NEXTVAL INTO IMG_NO FROM DUAL;
RETURN IMG_NO;
END;
;


SELECT NEXT_IMG_NO() FROM DUAL;