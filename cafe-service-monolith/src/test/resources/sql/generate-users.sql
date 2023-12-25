insert into cafe_user(login, password, first_name, last_name, phone_number, address, activated, activation_key, reset_key)
    values
    ('test1@mail.ru', '{bcrypt}$2y$10$madIgmL5Uv.rRgMKpxgynuk/jOkS3hdz44zIrx/oE0X0ZPh7KQuuK', 'Пётр', 'Иванов', '+79073691243', 'г.Самара ул.Улица д.14', false, '431251',null),
    ('businessmail1710@mail.ru', '{bcrypt}$2y$10$gAbcJefjbgNyxHNXCgYE9uFbksYKe4Jg2ZLJT9l8C8xzF8Tx0OCXO', 'Peter', 'Ivanov', '+79031254212', 'My address', true, null, null),
    ('test3@mail.ru', '{bcrypt}$2y$10$o/QoH7Ifn4JQEaJ1NgJHtu/RvIqc4BpVp27gbWg0iaJe7.UZg.XMi', 'Данил', 'Орлов', '+79073691248', 'г.Самара ул.Улица3 д.19', true, null, '541243'),
    ('test4@mail.ru', '{bcrypt}$2y$10$bvmfN4XlZ9nEMAnAZNGFq.zSmWP6gNMouzaC6IeQQ/ocal66OsPLO', 'Константин', 'Кузнецов', '+79073691249', 'г.Самара ул.Улица д.143', false, null, null);

insert into cafe_role(role_name, user_id)
    values
    ('ROLE_CUSTOMER', 1),
    ('ROLE_ADMIN', 2),
    ('ROLE_CUSTOMER', 3),
    ('ROLE_CUSTOMER', 4);
