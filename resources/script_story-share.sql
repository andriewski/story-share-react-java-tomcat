############################################## CREATING DATA BASE ######################################################
#DROP DATABASE story_share_db;
#CREATE DATABASE story_share_db;

#DELETE
#FROM USERS
#WHERE NAME LIKE 'Test%';
USE story_share_db;

CREATE TABLE USERS (
  USER_ID  INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  NAME     VARCHAR(60)     NOT NULL,
  EMAIL    VARCHAR(255)    NOT NULL,
  AVATAR   VARCHAR(255),
  PASSWORD VARCHAR(50)     NOT NULL,
  DELETED  CHAR(1)                  DEFAULT '-',
  ROLE     CHAR(7)                  DEFAULT 'USER'
  #state CHAR(1)     DEFAULT 'active blocked deleted'
);

CREATE TABLE MESSAGES (
  MESSAGE_ID          INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  TEXT                TEXT,
  DATE                DATETIME(3),
  DELETED_BY_SENDER   CHAR(1)                  DEFAULT '-',
  DELETED_BY_RECEIVER CHAR(1)                  DEFAULT '-',
  SENDER_ID           INT             NOT NULL,
  CONSTRAINT FK_SENDER_ID FOREIGN KEY (SENDER_ID)
  REFERENCES USERS (USER_ID),
  RECEIVER_ID         INT             NOT NULL,
  CONSTRAINT FK_RECEIVER_ID FOREIGN KEY (RECEIVER_ID)
  REFERENCES USERS (USER_ID)
);

CREATE TABLE POSTS (
  POST_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  TEXT    TEXT,
  DATE    DATETIME(3),
  USER_ID INT             NOT NULL,
  CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID)
  REFERENCES USERS (USER_ID),
  PICTURE VARCHAR(255)    NOT NULL
);

CREATE TABLE COMMENTS (
  COMMENT_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  USER_ID    INT             NOT NULL,
  CONSTRAINT FK_USER_COMMENT_ID FOREIGN KEY (USER_ID)
  REFERENCES USERS (USER_ID),
  POST_ID    INT             NOT NULL,
  CONSTRAINT FK_POST_COMMENT_ID FOREIGN KEY (POST_ID)
  REFERENCES POSTS (POST_ID)
    ON DELETE CASCADE,
  TEXT       TEXT,
  DATE       DATETIME(3)
);

CREATE TABLE LIKES (
  LIKES_ID     INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  POST_ID      INT             NOT NULL,
  CONSTRAINT FK_POST_ID FOREIGN KEY (POST_ID)
  REFERENCES POSTS (POST_ID)
    ON DELETE CASCADE,
  USER_LIKE_ID INT             NOT NULL,
  CONSTRAINT FK_USER_LIKE_ID FOREIGN KEY (USER_LIKE_ID)
  REFERENCES USERS (USER_ID)
);

########################################## DATA BASE HAS BEEN CREATED ##################################################
########################################## INITIALIZING ALL TABLES #####################################################
INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD, ROLE)
VALUES ('Mark',
        'mark@tut.by',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Vladimir_Putin_%282017-07-08%29.jpg/250px-Vladimir_Putin_%282017-07-08%29.jpg',
        '123456',
        'boss');
INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD, ROLE)
VALUES ('Denis', 'denis@tut.by', 'https://www.interfax.ru/ftproot/textphotos/2015/01/26/trump700.jpg', '123456', 'admin');
INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD, ROLE)
VALUES ('Grisha', 'grisha@tut.by', 'https://www.5.ua/media/pictures/original/29015.jpg', '123456', 'admin');
INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD)
VALUES ('Vladimir',
        'vladimir@tut.by',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Architect_Schuko_Vladimir_Alekseyevich.jpg/220px-Architect_Schuko_Vladimir_Alekseyevich.jpg',
        '123');
INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD)
VALUES ('Nikolai',
        'nikolai@tut.by',
        'https://cdna.artstation.com/p/assets/images/images/003/488/934/20160919030802/smaller_square/nikolai-lockertsen-image.jpg',
        '123');
INSERT INTO USERS (NAME, EMAIL, AVATAR, PASSWORD)
VALUES ('Василиса',
        'v@tut.by',
        'https://24smi.org/public/media/235x307/person/2018/01/08/lyvgm9v9cod4-vasilisa-premudraia.jpg',
        '123');


INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Ну привет', "1991-02-20 19:20:42.001", 1, 2);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Ky-ky', "1991-02-20 00:00:00.002", 2, 1);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Как дела?', "1991-02-21 01:10:01.003", 1, 2);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Дороу, Марк! Это Гриша', "1991-02-21 01:11:01.003", 3, 1);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Все пучком! :)', "1991-02-21 02:10:01.001", 2, 1);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Удачи!', "1991-02-22 12:10:01.005", 1, 2);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('И тебе удачи!', "1992-02-22 12:10:01.100", 2, 1);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Спасибо!', "1993-02-22 12:10:01.004", 1, 2);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Привет, Володя) Это Марк', "1989-02-22 12:10:01.001", 1, 4);
INSERT INTO MESSAGES (TEXT, DATE, SENDER_ID, RECEIVER_ID)
VALUES ('Привет, Коля! Это Марк', "1989-02-22 12:10:01.001", 1, 5);

INSERT INTO POSTS (TEXT, DATE, USER_ID, PICTURE)
VALUES ('Привет всем!', "1993-02-22 12:10:01.001", 1, 'https://i.ytimg.com/vi/uIDM3TK-_0I/maxresdefault.jpg');
INSERT INTO POSTS (TEXT, DATE, USER_ID, PICTURE)
VALUES ('И тебе привет!', "1994-02-22 12:10:01.002", 2, 'http://memesmix.net/media/created/r483oc.jpg');
INSERT INTO POSTS (TEXT, DATE, USER_ID, PICTURE)
VALUES ('Вот мой кот',
        "1995-02-22 12:10:01.003",
        3,
        'http://mignews.com.ua/modules/news/images/articles/changing/19594650-kot-so-slomannym-pozvonochnikom-porazil.jpg');
INSERT INTO POSTS (TEXT, DATE, USER_ID, PICTURE)
VALUES ('Ку, ребзи!', "1996-02-22 12:10:01.004", 1, 'https://i.ytimg.com/vi/LMByI5RHhfk/maxresdefault.jpg');
INSERT INTO POSTS (TEXT, DATE, USER_ID, PICTURE)
VALUES ('Дороу, это Гриша', "1997-02-22 12:10:01.005", 3, 'http://img.1001mem.ru/posts_temp/17-10-12/3919394.jpg');

INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (1, 1);
INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (1, 2);
INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (1, 3);
INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (2, 1);
INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (2, 3);
INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (4, 1);
INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (5, 1);

INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (1, 1, 'Мой первый коммент', "1998-02-22 12:10:01.006");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (2, 1, 'Второй!', "1999-02-22 12:10:01.007");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (2, 3, 'Я первый', "1998-02-22 12:10:01.008");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (1, 4, 'Я первый выкусите', "1999-02-22 12:10:01.009");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (1, 5, 'аххахах', "2000-02-22 12:10:01.010");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (3, 5, 'УХАХАХАХАХ', "2001-02-22 12:10:01.011");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (1, 5, 'Ты чего ржешь?', "2001-02-22 12:15:01.011");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (3, 5, 'А ты чего, морда, ржешь?', "2001-02-22 12:17:01.011");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (1, 5, 'Будешь грубить - забаню', "2001-02-22 12:19:01.011");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (3,
        5,
        'Я тогда нажалуюсь сам знаешь кому - тебе прилетит! И, да, это угроза. Так что можешь пойти уже свои вонючие портки сушить',
        "2001-02-22 12:21:01.011");
INSERT INTO COMMENTS (USER_ID, POST_ID, TEXT, DATE)
VALUES (1, 5, 'Я тебя придупреждал. Сам виноват', "2001-02-22 12:25:01.011");

########################################## ALL TABLES HAVE BEEN INITIALIZED ############################################
########################################## TESTING TABLES METHODS ######################################################
####### TESTING USERS METHODS
#Get
SELECT *
FROM USERS
WHERE USER_ID = 1;

#Update
UPDATE USERS
SET NAME     = 'Maxxx',
    EMAIL    = 'ppp@tut.by',
    AVATAR   = 'shit',
    PASSWORD = 'psadad'
WHERE EMAIL = 'wwww@tut.by'; ###### - в методе update производится по айдишнику

#Deleted - аналог через update
UPDATE USERS
SET DELETED = '+'
WHERE USER_ID = 5;

#Get Users Avatars - WASTED
# SELECT DISTINCT USER_ID, AVATAR
# FROM USERS
#        INNER JOIN MESSAGES ON (USER_ID = SENDER_ID OR USER_ID = RECEIVER_ID)
# WHERE (SENDER_ID = 1 AND DELETED_BY_SENDER = '-'
#    OR RECEIVER_ID = 1 AND DELETED_BY_RECEIVER = '-') AND USER_ID != 1;

#Get User Avatar
SELECT AVATAR
FROM USERS
WHERE USER_ID = 1;
################ TESTING MESSAGES METHODS ##############################################################################
#Get
SELECT *
FROM MESSAGES
WHERE MESSAGE_ID = 1;

#Update
UPDATE MESSAGES
SET TEXT = 'qweqwewqweqwqeewq'
WHERE MESSAGE_ID = 1;

#Delete
DELETE
FROM MESSAGES
WHERE MESSAGE_ID = 1;

#GetMessagesWithOffset
SELECT TEXT, DATE, SENDER_ID, RECEIVER_ID, senders.NAME AS SENDER_NAME, receivers.NAME AS RECEIVER_NAME
FROM MESSAGES
       INNER JOIN USERS AS senders ON MESSAGES.SENDER_ID = senders.USER_ID
       INNER JOIN USERS AS receivers ON MESSAGES.RECEIVER_ID = receivers.USER_ID
WHERE (SENDER_ID = 1 AND DELETED_BY_SENDER = '-' AND RECEIVER_ID = 2)
   OR (SENDER_ID = 2 AND RECEIVER_ID = 1 AND DELETED_BY_RECEIVER = '-')
ORDER BY DATE DESC
LIMIT 10 OFFSET 0;

#GetLastMessages
SELECT TEXT, DATE, SENDER_ID, RECEIVER_ID, senders.NAME AS SENDER_NAME, receivers.NAME AS RECEIVER_NAME
FROM MESSAGES
       INNER JOIN USERS AS senders ON MESSAGES.SENDER_ID = senders.USER_ID
       INNER JOIN USERS AS receivers ON MESSAGES.RECEIVER_ID = receivers.USER_ID
       INNER JOIN (SELECT MAX(MESSAGE_ID) AS most_recent_message_id
                   FROM MESSAGES
                   GROUP BY CASE
                              WHEN (SENDER_ID > RECEIVER_ID AND ((SENDER_ID = 2 AND DELETED_BY_SENDER = '-') OR
                                                                 (RECEIVER_ID = 2 AND DELETED_BY_RECEIVER = '-')))
                                      THEN RECEIVER_ID
                              ELSE SENDER_ID
                       END -- low_id
                       , CASE
                           WHEN (SENDER_ID < RECEIVER_ID AND ((SENDER_ID = 2 AND DELETED_BY_SENDER = '-') OR
                                                              (RECEIVER_ID = 2 AND DELETED_BY_RECEIVER = '-')))
                                   THEN RECEIVER_ID
                           ELSE SENDER_ID
                       END -- high_id
                  ) T ON T.most_recent_message_id = MESSAGES.MESSAGE_ID
WHERE (senders.USER_ID = 2 AND DELETED_BY_SENDER = '-')
   OR (receivers.USER_ID = 2 AND DELETED_BY_RECEIVER = '-')
ORDER BY MESSAGES.DATE DESC;

#Delete in Certain user
UPDATE MESSAGES
SET DELETED_BY_SENDER   = CASE
WHEN (SENDER_ID = 2)
                                  THEN '+'
                          ELSE (SELECT DELETED_BY_SENDER
                                FROM (SELECT * FROM MESSAGES) AS m
                                WHERE MESSAGE_ID = 7) END,
    DELETED_BY_RECEIVER = CASE
                            WHEN (RECEIVER_ID = 2)
                                    THEN '+'
                            ELSE (SELECT DELETED_BY_RECEIVER
                                  FROM (SELECT * FROM MESSAGES) AS m
                                  WHERE MESSAGE_ID = 7) END
WHERE MESSAGE_ID = 7;

########################TESTING POSTS METHODS ##########################################################################
#Get
SELECT *
FROM POSTS
WHERE POST_ID = 1;

#Update
UPDATE POSTS
SET TEXT    = 'ХАЙ',
    PICTURE = 'Каааааартинка1'
WHERE POST_ID = 1;

#Delete
DELETE
FROM POSTS
WHERE POST_ID = 1;

#GetPostsWithPagination
SELECT POST_ID,
       USERS.USER_ID,
       TEXT,
       DATE,
       USERS.NAME,
       USERS.AVATAR,
       PICTURE,
       (SELECT COUNT(*) FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID)     AS LIKES,
       (SELECT LIKES.USER_LIKE_ID FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID
                                               AND LIKES.USER_LIKE_ID = -1) AS THIS_POST_IS_LIKED_BY_YOU
FROM POSTS
       INNER JOIN USERS ON POSTS.USER_ID = USERS.USER_ID
ORDER BY DATE DESC
LIMIT 20 OFFSET 0;

#GetNumberOfAllPosts
SELECT COUNT(*)
FROM POSTS;

#####like AND unlikePostByUser
INSERT INTO LIKES (POST_ID, USER_LIKE_ID)
VALUES (5, 1);

DELETE
FROM LIKES
WHERE USER_LIKE_ID = 1
  AND POST_ID = 5;

###getSinglePostDTO
SELECT POST_ID,
       USERS.USER_ID,
       TEXT,
       DATE,
       USERS.NAME,
       USERS.AVATAR,
       PICTURE,
       (SELECT COUNT(*) FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID)    AS LIKES,
       (SELECT LIKES.USER_LIKE_ID FROM LIKES WHERE LIKES.POST_ID = POSTS.POST_ID
                                               AND LIKES.USER_LIKE_ID = 1) AS THIS_POST_IS_LIKED_BY_YOU
FROM POSTS
       INNER JOIN USERS ON POSTS.USER_ID = USERS.USER_ID
WHERE POST_ID = 1;

################### TESTING COMMENTS METHODS ###########################################################################
#Get
SELECT *
FROM COMMENTS
WHERE COMMENT_ID = 1;

#Update
UPDATE COMMENTS
SET TEXT = 'моооой пееееервйы коооммент'
WHERE COMMENT_ID = 1;

#Delete
DELETE
FROM COMMENTS
WHERE COMMENT_ID = 7;

#GetCommentsWithOffsetAndLimit
SELECT USERS.NAME, COMMENTS.TEXT, COMMENTS.DATE
FROM COMMENTS
       INNER JOIN USERS ON COMMENTS.USER_ID = USERS.USER_ID
WHERE POST_ID = 5
ORDER BY DATE DESC
LIMIT 10 OFFSET 0;

#GetNumberOfCommentsInThePosts
SELECT COUNT(*)
FROM COMMENTS
WHERE POST_ID = 5;