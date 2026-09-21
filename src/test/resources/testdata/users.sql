DELETE FROM users;

INSERT INTO users (id, username, email, password_hash, display_name, country)
VALUES
    ('11111111-1111-1111-1111-111111111111',
     'hansi3',
     'peter12@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Peter Strele',
     'PL'),

    ('22222222-2222-2222-2222-222222222222',
     'hansi4',
     'peter@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Hans Strele',
     'PL'),

    ('33333333-3333-3333-3333-333333333333',
     'hansi5',
     'pet@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Hanswurst Strele',
     'PL'),

    ('44444444-4444-4444-4444-444444444444',
     'anna_keller',
     'anna.keller@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Anna Keller',
     'DE'),

    ('55555555-5555-5555-5555-555555555555',
     'lukas_mueller',
     'lukas.mueller@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Lukas Müller',
     'DE'),

    ('66666666-6666-6666-6666-666666666666',
     'sofia_rossi',
     'sofia.rossi@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Sofia Rossi',
     'IT'),

    ('77777777-7777-7777-7777-777777777777',
     'marco_rossi',
     'marco.rossi@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Marco Rossi',
     'IT'),

    ('88888888-8888-8888-8888-888888888888',
     'john_smith',
     'john.smith@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'John Smith',
     'US'),

    ('99999999-9999-9999-9999-999999999999',
     'jane_smith',
     'jane.smith@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Jane Smith',
     'US'),

    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     'pierre_dupont',
     'pierre.dupont@example.com',
     '$2a$12$REPLACE_WITH_TEST_HASH',
     'Pierre Dupont',
     'FR');