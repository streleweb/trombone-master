DELETE FROM users;

INSERT INTO users (
    id,
    username,
    email,
    password_hash,
    display_name,
    country
)
VALUES
    (
        '11111111-1111-1111-1111-111111111111',
        'hansi3',
        'peter12@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Peter Strele',
        'PL'
    ),
    (
        '22222222-2222-2222-2222-222222222222',
        'hansi4',
        'peter@example.com',
        '$2a$10$oSUBgzGnpUnB8UgX3Otxxu7myS.gc.Rmiy7kqwGbH5r2URinbE6z6',
        'Hans Strele',
        'PL'
    ),
    (
        '33333333-3333-3333-3333-333333333333',
        'hansi5',
        'pet@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Hanswurst Strele',
        'PL'
    ),
    (
        '44444444-4444-4444-4444-444444444444',
        'anna_keller',
        'anna@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Anna Keller',
        'DE'
    ),
    (
        '55555555-5555-5555-5555-555555555555',
        'lukas_mueller',
        'lukas@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Lukas Müller',
        'DE'
    ),
    (
        '66666666-6666-6666-6666-666666666666',
        'sofia_rossi',
        'sofia@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Sofia Rossi',
        'IT'
    ),
    (
        '77777777-7777-7777-7777-777777777777',
        'marco_rossi',
        'marco.rossi@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Marco Rossi',
        'IT'
    ),
    (
        '88888888-8888-8888-8888-888888888888',
        'john_smith',
        'john@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'John Smith',
        'US'
    ),
    (
        '99999999-9999-9999-9999-999999999999',
        'jane_smith',
        'jane@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Jane Smith',
        'US'
    ),
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'pierre_dupont',
        'pierre@example.com',
        '$2a$10$dummyhashfortestfixture000000000000000000000000000',
        'Pierre Dupont',
        'FR'
    );