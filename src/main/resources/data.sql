
INSERT INTO account_user (id, username)
VALUES (1, 'test_user');

INSERT INTO account_user (id, username)
VALUES (2, 'second_user');

INSERT INTO habit (id, label, description, created_date, type, is_active, user_id)
VALUES (1, '朝の散歩', '健康のために毎朝散歩する', '2025-04-21', 'CONTINUE', true, 1);

INSERT INTO habit (id, label, description, created_date, type, is_active, user_id)
VALUES (2, 'SNS視聴', 'タスクが終わるまでSNSを見ない', '2025-04-21', 'QUIT', true, 1);
