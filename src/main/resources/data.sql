INSERT INTO Member (FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, BIRTH_DATE, DISCOUNT_PRICE, MEMBER_ID, NAME, NICK_NAME,
                    PASSWORD, PHONE_NUMBER, SOCIAL, ROLE, SEX)
VALUES ('수원시', '영통구', '원천동', '1990-01-01', 1000, 'member01', '홍길동', 'hong',
        '$2a$10$6F16tR6bSb2psdG.yM3H9OBANJgAUpmiXSQgmy73Gu7Pr/xo7TsRK', '01012345678', false, 'ROLE_USER', 'MAN');

INSERT INTO Member (FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, BIRTH_DATE, DISCOUNT_PRICE, MEMBER_ID, NAME, NICK_NAME,
                    PASSWORD, PHONE_NUMBER, SOCIAL, ROLE, SEX)
VALUES ('수원시', '영통구', '원천동', '1985-05-10', 500, 'member02', '김철수', 'kim',
        '$2a$10$pw0oAflM1QmcbFywxxWJ4eZSmi.6CzOcc/pJQDPjWvQPtAGUekHvy', '01098765432', false, 'ROLE_USER', 'MAN');

INSERT INTO market (ID, FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, EMAIL, END_TIME, NAME, OWNER_NAME, PASSWORD, PHONE_NUMBER,
                    PHOTO_URL, START_TIME)
VALUES (1, 'Ulsan', 'Namgu', 'asfsaf', 'dltkdgns830@ajou.ac.kr', '21:00', '상훈이네', '이상훈', 'abc',
        '010-9973-2652', 'https://timeseller-bucket.s3.ap-northeast-2.amazonaws.com/shop1.jpg', '09:00');

INSERT INTO market (ID, FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, EMAIL, END_TIME, NAME, OWNER_NAME, PASSWORD, PHONE_NUMBER,
                    PHOTO_URL, START_TIME)
VALUES (2, 'Seoul', 'Bukgu', 'sfsf', 'abcde@ajou.ac.kr', '21:00', '재연이네', '양재연', 'asfsa',
        '010-2132-2552', 'https://timeseller-bucket.s3.ap-northeast-2.amazonaws.com/shop2.jpg', '09:00');

INSERT INTO market (ID, FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, EMAIL, END_TIME, NAME, OWNER_NAME, PASSWORD, PHONE_NUMBER,
                    PHOTO_URL, START_TIME)
VALUES (3, 'Suwon', 'paldal', 'asdad', 'sfsfe3@ajou.ac.kr', '21:00', '동우네', '강동우', 'abcdef',
        '010-9912-2522', 'https://timeseller-bucket.s3.ap-northeast-2.amazonaws.com/shop3.jpg', '09:00');

INSERT INTO market (ID, FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, EMAIL, END_TIME, NAME, OWNER_NAME, PASSWORD, PHONE_NUMBER,
                    PHOTO_URL, START_TIME)
VALUES (4, 'Suwon', 'YeongTong', 'DDONG', 'testd@timeseller', '21:00', '아주대학교', '아콩이', '1234',
        '010-1412-4212', 'abacd.png', '09:00');

INSERT INTO item (DISCOUNT_PRICE, EXPIRATION_DATE, ITEM_DETAIL, ITEM_QUANTITY, NAME, ORIGINAL_PRICE, PHOTO_URL,
                  REGISTER_DATE, WISHED_ITEM_ADDED_COUNT, MARKET_ID, RESERVED_QUANTITY)
VALUES (2000, '2023-04-20 00:00:00.000000', '맛있는 사과', 3, '사과', 4000, 'https://timeseller-bucket.s3.ap-northeast-2.amazonaws.com/apple.jpg', '2023-06-09 20:01:20.000000', 0, 1, 0);

INSERT INTO item (DISCOUNT_PRICE, EXPIRATION_DATE, ITEM_DETAIL, ITEM_QUANTITY, NAME, ORIGINAL_PRICE, PHOTO_URL,
                  REGISTER_DATE, WISHED_ITEM_ADDED_COUNT, MARKET_ID, RESERVED_QUANTITY)
VALUES (4500, '2023-04-21 00:00:00.000000', '파인애플', 4, '파인애플', 6000, 'https://timeseller-bucket.s3.ap-northeast-2.amazonaws.com/pineapple.jpg', '2023-06-09 21:01:20.000000', 0,
        1, 0);

INSERT INTO item (DISCOUNT_PRICE, EXPIRATION_DATE, ITEM_DETAIL, ITEM_QUANTITY, NAME, ORIGINAL_PRICE, PHOTO_URL,
                  REGISTER_DATE, WISHED_ITEM_ADDED_COUNT, MARKET_ID, RESERVED_QUANTITY)
VALUES (7000, '2023-04-25 00:00:00.000000', '수박', 2, '수박', 8500, 'https://timeseller-bucket.s3.ap-northeast-2.amazonaws.com/watermelon.jpg', '2023-06-09 00:00:00.000000', 0, 2, 0);

INSERT INTO notice (ID, CONTENT, PHOTO_URL, TITLE, VIEW_NUM, WRITE_DATE)
VALUES (1, 'ㅁㄴㅇㄹㅁㄴㄹㅇ', null, 'ㅁㄴ아럼니ㅏ얼', 0, '2023-04-06 22:32:24.000000');

INSERT INTO notice (ID, CONTENT, PHOTO_URL, TITLE, VIEW_NUM, WRITE_DATE)
VALUES (2, 'ㅁㄴㅇㄹㄴㅁㅇㄹㄴㅇ', 'ㄴㅓ래ㅓ야ㅐㄴ러', 'ㄴㅓ엚ㄴㅇㄹ', 0, '2023-04-06 22:32:37.000000');