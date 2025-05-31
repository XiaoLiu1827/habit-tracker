
INSERT INTO account_user (id, username)
VALUES (1, 'test_user');

INSERT INTO account_user (id, username)
VALUES (2, 'second_user');

INSERT INTO habit (id, label, description, created_date, type, is_active, user_id)
VALUES (1, '朝の散歩', '健康のために毎朝散歩する', '2025-04-21', 'CONTINUE', true, 1);

INSERT INTO habit (id, label, description, created_date, type, is_active, user_id)
VALUES (2, 'SNS視聴', 'タスクが終わるまでSNSを見ない', '2025-04-21', 'QUIT', true, 1);

INSERT INTO review_question_master (id, question_label, habit_type, review_type) VALUES
(1, 'なぜ継続できたと思いますか？', 'CONTINUE', 'SUCCESS'),
(2, '継続できなかった理由は何ですか？', 'CONTINUE', 'FAILURE'),
(3, 'なぜやめられたと思いますか？', 'QUIT', 'SUCCESS'),
(4, 'やめられなかった理由は何ですか？', 'QUIT', 'FAILURE');

-- CONTINUE / SUCCESS
INSERT INTO review_choice_master (id, question_id, choice_label) VALUES
(1, 1, '時間に余裕があった'),
(2, 1, '気分が良かった'),
(3, 1, '前日の準備ができていた'),
(4, 1, '習慣になってきた');

-- CONTINUE / FAILURE
INSERT INTO review_choice_master (id, question_id, choice_label) VALUES
(5, 2, '忘れていた'),
(6, 2, '気が進まなかった'),
(7, 2, '他の予定が入っていた'),
(8, 2, '体調が悪かった');

-- QUIT / SUCCESS
INSERT INTO review_choice_master (id, question_id, choice_label) VALUES
(9, 3, '興味が薄れた'),
(10, 3, '代わりの習慣を始めた'),
(11, 3, '忙しくて手がつかなかった'),
(12, 3, '気持ちが落ち着いていた');

-- QUIT / FAILURE
INSERT INTO review_choice_master (id, question_id, choice_label) VALUES
(13, 4, 'ストレスが溜まっていた'),
(14, 4, 'つい手が伸びた'),
(15, 4, '環境が変わっていない'),
(16, 4, '我慢の限界だった');

--Review_Record
INSERT INTO review_record (habit_id, user_id, date, success)
VALUES
-- 17日成功
(1, 1, '2025-05-01', true),
(1, 1, '2025-05-02', true),
(1, 1, '2025-05-03', true),
(1, 1, '2025-05-04', true),
(1, 1, '2025-05-05', true),
(1, 1, '2025-05-06', true),
(1, 1, '2025-05-07', true),
(1, 1, '2025-05-08', true),
(1, 1, '2025-05-09', true),
(1, 1, '2025-05-10', true),
(1, 1, '2025-05-11', true),
(1, 1, '2025-05-12', true),
(1, 1, '2025-05-13', true),
(1, 1, '2025-05-14', true),
(1, 1, '2025-05-15', true),
(1, 1, '2025-05-16', true),
(1, 1, '2025-05-17', true),
-- 3日失敗
(1, 1, '2025-05-18', false),
(1, 1, '2025-05-19', false),
(1, 1, '2025-05-20', false),
(1, 1, '2025-04-15', true),
(1, 1, '2025-04-16', true),
(1, 1, '2025-04-17', true);

INSERT INTO review_record (habit_id, user_id, date, success)
VALUES
(2, 1, '2025-05-01', true),
(2, 1, '2025-05-02', false),
(2, 1, '2025-05-03', true),
(2, 1, '2025-05-04', false);


