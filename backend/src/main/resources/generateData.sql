INSERT INTO roles (name) VALUES  ('USER'),('ADMIN'), ('MODERATOR');

DO $$
DECLARE
i INT;
    random_username VARCHAR(255);
    random_email VARCHAR(255);
    random_password VARCHAR(255);
    random_first_name VARCHAR(255);
    random_last_name VARCHAR(255);
    random_country VARCHAR(255);
    random_language VARCHAR(255);
    random_role_id INT;
BEGIN
FOR i IN 1..100000 LOOP
        random_username := 'user_' || i;
        random_email := 'user' || i || '@example.com';
        random_password := 'password_' || i;
        random_first_name := 'FirstName_' || i;
        random_last_name := 'LastName_' || i;
        random_country := 'Country_' || (i % 100);
        random_language := CASE (i % 3)
                            WHEN 0 THEN 'en'
                            WHEN 1 THEN 'ru'
                            ELSE 'de'
END;
        random_role_id := i % 3 +1;

INSERT INTO users (username, email, password, first_name, last_name, country, preferred_language, roles_id, created_at)
VALUES (
           random_username,
           random_email,
           random_password,
           random_first_name,
           random_last_name,
           random_country,
           random_language,
           random_role_id,
           NOW()
       );
END LOOP;
END $$;

INSERT INTO fermentation_types(name) VALUES ('Lager'), ('Ale'), ('Stout'), ('Porter');

DO $$
    DECLARE
i INT;
        random_name VARCHAR(255);
        random_price DECIMAL(10, 2);
        random_volume DECIMAL(5, 2);
        random_srm DECIMAL(5, 2);
        random_ibu DECIMAL(5, 2);
        random_abv DECIMAL(5, 2);
        random_og DECIMAL(5, 2);
        random_country VARCHAR(255);
        random_image_path VARCHAR(255);
        random_fermentation_type INT;
BEGIN
FOR i IN 51001..300000 LOOP
                random_name := 'Beer_' || i;
                random_price := ROUND((RANDOM() * 500 + 40)::NUMERIC, 2);
                random_volume := ROUND((RANDOM() * 2 + 0.33)::NUMERIC, 2);
                random_srm := ROUND((RANDOM() * 40 + 1)::NUMERIC, 2);
                random_ibu := ROUND((RANDOM() * 100)::NUMERIC, 2);
                random_abv := ROUND((RANDOM() * 15 + 1)::NUMERIC, 2);
                random_og := ROUND((RANDOM() * 1.1 + 1)::NUMERIC, 3);
                random_country := 'Country_' || (i % 100);
                random_image_path := '/images/beer_' || i || '.jpg';
                random_fermentation_type := i % 4 +1;

INSERT INTO beers (
    name,
    price,
    volume,
    fermentation_type,
    srm,
    ibu,
    abv,
    og,
    country,
    image_path
)
VALUES (
           random_name,
           random_price,
           random_volume,
           random_fermentation_type,
           random_srm,
           random_ibu,
           random_abv,
           random_og,
           random_country,
           random_image_path
       );
END LOOP;
END $$;

DO $$
    DECLARE
total_records INT := 100000;
        user_count INT;
        i INT := 0;
        random_user_id INT;
        random_followed_user INT;
BEGIN
SELECT COUNT(*) INTO user_count FROM users;
WHILE i < total_records LOOP

                random_user_id := trunc(random() * user_count) + 1;
                random_followed_user := trunc(random() * user_count) + 1;

                IF random_user_id != random_followed_user THEN
BEGIN
INSERT INTO subscribed_users (user_id, followed_user)
VALUES (random_user_id, random_followed_user);
i := i + 1;
EXCEPTION WHEN unique_violation THEN
                        CONTINUE;
END;
END IF;
END LOOP;
END $$;

INSERT INTO activity_types (name) VALUES ('Review');

DO $$
    DECLARE
i INT;
        user_count INT;
        beer_count INT;
        random_user_id INT;
        random_beer_id INT;
BEGIN
SELECT COUNT(*) INTO user_count FROM users;
SELECT COUNT(*) INTO beer_count FROM beers;
FOR i IN 1..20000 LOOP
                random_user_id := (RANDOM() * (user_count-1)+ 1);
                random_beer_id := (RANDOM() * (beer_count-1) + 1);

INSERT INTO favourite_beer (
    user_id,
    beer_id
)
VALUES (
           random_user_id,
           random_beer_id
       )
    ON CONFLICT (user_id, beer_id) DO NOTHING;
END LOOP;
END $$;

DO $$
    DECLARE
i INT;
        beer_count INT;
        user_count INT;
BEGIN
SELECT COUNT(*) INTO beer_count FROM beers;
SELECT COUNT(*) INTO user_count FROM users;

FOR i IN 1..100000 LOOP
                INSERT INTO reviews (beer_reviewed_id, rating, comment)
                VALUES (
                           (i % beer_count) + 1,
                           ROUND((RANDOM() * 5)::numeric, 1),
                           'Комментарий ' || i
                       );
END LOOP;
END $$;

DO $$
    DECLARE
        i INT;
        review_count INT;
        user_count INT;
    BEGIN
        SELECT COUNT(*) INTO review_count FROM reviews;
        SELECT COUNT(*) INTO user_count FROM users;

        FOR i IN 1..100000 LOOP
                INSERT INTO review_user (user_id, review_id)
                VALUES (
                           (i % user_count) + 1,
                           (i % review_count) + 1
                       );
            END LOOP;
    END $$;



DO $$
    DECLARE
user_id INT;
        fermentation_count INT;
BEGIN
SELECT COUNT(*) INTO fermentation_count FROM fermentation_types;

FOR user_id IN (SELECT id FROM users) LOOP
                INSERT INTO tasteprofiles (
                    user_id,
                    ibu_pref,
                    srm_pref,
                    abv_pref,
                    og_pref,
                    fermentation_type,
                    price
                )
                VALUES (
                           user_id,
                           ROUND((RANDOM() * 100)::numeric, 1),
                           ROUND((RANDOM() * 40)::numeric, 1),
                           ROUND((RANDOM() * 15)::numeric, 1),
                           ROUND((RANDOM() * 1.1)::numeric, 2),
                           (user_id % fermentation_count) + 1,
                           ROUND((RANDOM() * 50 + 1)::numeric, 2)

                       );
END LOOP;
END $$;