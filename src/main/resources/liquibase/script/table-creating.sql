-- liquibase formatted sql

-- changeset fsalyakhov:1
CREATE TABLE dynamic_rules (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    product_text VARCHAR(1000) NOT NULL
);

CREATE TABLE rule (
    id SERIAL PRIMARY KEY,
    query VARCHAR(100) NOT NULL,
    arguments VARCHAR(20)[],
    negate BOOLEAN NOT NULL,
    dynamic_rules_id INT NOT NULL,
    CONSTRAINT pr_parent
    FOREIGN KEY (dynamic_rules_id) REFERENCES dynamic_rules (id) ON DELETE CASCADE
);

INSERT INTO dynamic_rules (product_name, product_id, product_text)
VALUES ('Invest 500', '147f6a0f-3b91-413b-ab99-87f081d60d5a',
'Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!');

INSERT INTO dynamic_rules (product_name, product_id, product_text)
VALUES ('Top Saving', '59efc529-2fff-41af-baff-90ccd7402925',
'Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем! \n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости. \n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг. \n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!');

INSERT INTO dynamic_rules (product_name, product_id, product_text)
VALUES ('Top Saving', '59efc529-2fff-41af-baff-90ccd7402925',
'Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем! \n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости. \n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг. \n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!');

INSERT INTO dynamic_rules (product_name, product_id, product_text)
VALUES ('Простой кредит', 'ab138afb-f3ba-4a93-b74f-0fcee86d447f',
'Откройте мир выгодных кредитов с нами!\n\nИщете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\nПочему выбирают нас:\n\nБыстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n\nУдобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\nШирокий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n\nНе упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!');

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('USER_OF', '{"DEBIT"}', FALSE, 1);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('USER_OF', '{"INVEST"}', TRUE, 1);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('TRANSACTION_SUM_COMPARE', '{"SAVING", "DEPOSIT", ">", "1000"}', FALSE, 1);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('USER_OF', '{"DEBIT"}', FALSE, 2);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('TRANSACTION_SUM_COMPARE', '{"DEBIT", "DEPOSIT", ">=", "50000"}', FALSE, 2);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW', '{"DEBIT", ">"}', FALSE, 2);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('USER_OF', '{"DEBIT"}', FALSE, 3);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('TRANSACTION_SUM_COMPARE', '{"SAVING", "DEPOSIT", ">=", "50000"}', FALSE, 3);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW', '{"DEBIT", ">"}', FALSE, 3);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('USER_OF', '{"CREDIT"}', TRUE, 4);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW', '{"DEBIT", ">"}', FALSE, 4);

INSERT INTO rule (query, arguments, negate, dynamic_rules_id)
VALUES ('TRANSACTION_SUM_COMPARE', '{"DEBIT", "WITHDRAW", ">", "100000"}', FALSE, 4);