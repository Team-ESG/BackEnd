INSERT INTO Member (FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, BIRTH_DATE, DISCOUNT_PRICE, MEMBER_ID, NAME, NICK_NAME,
                    PASSWORD, PHONE_NUMBER)
VALUES ('서울특별시', '강남구', '역삼동', '1990-01-01', 1000, 'member01', '홍길동', 'hong', 'password1234', '010-1234-5678');

INSERT INTO Member (FIRST_ADDR, SECOND_ADDR, THIRD_ADDR, BIRTH_DATE, DISCOUNT_PRICE, MEMBER_ID, NAME, NICK_NAME,
                    PASSWORD, PHONE_NUMBER)
VALUES ('부산광역시', '해운대구', '우동', '1985-05-10', 500, 'member02', '김철수', 'kim', 'pw9876', '010-9876-5432');

INSERT INTO market (email, password, phone_number, photo_url, first_addr, second_addr, third_addr, owner_name)
VALUES ('dltkdgns830@ajou.ac.kr', 'abc', '010-9973-2652', 'abcd.png', 'Ulsan', 'Namgu', 'asfsaf', '이상훈');

INSERT INTO market (email, password, phone_number, photo_url, first_addr, second_addr, third_addr, owner_name)
VALUES ('abcde@ajou.ac.kr', 'asfsa', '010-2132-2552', 'ad.png', 'Seoul', 'Bukgu', 'sfsf', '양재연');

INSERT INTO market (email, password, phone_number, photo_url, first_addr, second_addr, third_addr, owner_name)
VALUES ('sfsfe3@ajou.ac.kr', 'abcdef', '010-9912-2522', 'acd.png', 'Suwon', 'paldal', 'asdad', '강동우');

INSERT INTO item (DISCOUNT_PRICE, EXPIRATION_DATE, ITEM_DETAIL, ITEM_QUANTITY, NAME, ORIGINAL_PRICE, PHOTO_URL,
                  REGISTER_DATE, WISHED_ITEM_ADDED_COUNT, MARKET_ID)
VALUES (2000, '2023-04-20 00:00:00.000000', '맛있는 사과', 3, '사과', 4000, 'abcd.jpg', '2023-04-18 20:01:20.000000', 0,
        1);
INSERT INTO item (DISCOUNT_PRICE, EXPIRATION_DATE, ITEM_DETAIL, ITEM_QUANTITY, NAME, ORIGINAL_PRICE, PHOTO_URL,
                  REGISTER_DATE, WISHED_ITEM_ADDED_COUNT, MARKET_ID)
VALUES (4500, '2023-04-21 00:00:00.000000', '파인애플', 4, '파인애플', 6000, 'pineapple.jpg', '2023-04-19 21:01:20.000000',
        0, 1);
INSERT INTO item (DISCOUNT_PRICE, EXPIRATION_DATE, ITEM_DETAIL, ITEM_QUANTITY, NAME, ORIGINAL_PRICE, PHOTO_URL,
                  REGISTER_DATE, WISHED_ITEM_ADDED_COUNT, MARKET_ID)
VALUES (7000, '2023-04-25 00:00:00.000000', '모카빵', 2, '모카빵', 8500, 'bread.jpg', '2023-04-20 00:00:00.000000', 0, 2);
