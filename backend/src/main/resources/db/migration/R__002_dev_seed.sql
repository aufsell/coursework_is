INSERT INTO roles (name) VALUES ('USER'),('ADMIN'),('MODERATOR')
ON CONFLICT (name) DO NOTHING;

INSERT INTO fermentation_types(name) VALUES ('Lager'), ('Ale'), ('Stout'), ('Porter')
ON CONFLICT (name) DO NOTHING;

INSERT INTO activity_types (name) VALUES ('Review')
ON CONFLICT (name) DO NOTHING;


DO $$
    DECLARE
        i BIGINT;
        random_username VARCHAR(255);
        random_password VARCHAR(255);
        random_first_name VARCHAR(255);
        random_last_name VARCHAR(255);
        random_country VARCHAR(255);
        random_language VARCHAR(255);
        random_role_id BIGINT;
    BEGIN
        FOR i IN 1..20 LOOP
                random_username := 'user_' || i;
                random_password := 'password_' || i;
                random_first_name := 'FirstName_' || i;
                random_last_name := 'LastName_' || i;
                random_country := 'Country_' || (i % 100);
                random_language := CASE (i % 3)
                                       WHEN 0 THEN 'en'
                                       WHEN 1 THEN 'ru'
                                       ELSE 'de'
                    END;

                random_role_id := (i % 3) + 1;

                INSERT INTO users (username, password, first_name, last_name, country, preferred_language, roles_id)
                VALUES (random_username, random_password, random_first_name, random_last_name, random_country, random_language, random_role_id)
                ON CONFLICT (username) DO NOTHING;
            END LOOP;
    END $$;


DO $$
    DECLARE
        i BIGINT;
        random_name VARCHAR(255);

        random_price DOUBLE PRECISION;
        random_volume DOUBLE PRECISION;
        random_average_rating DOUBLE PRECISION;

        random_srm BIGINT;
        random_ibu BIGINT;
        random_abv BIGINT;
        random_og  BIGINT;

        random_country VARCHAR(255);
        random_image_path VARCHAR(1024);
        random_fermentation_type BIGINT;
    BEGIN
        FOR i IN 1..100 LOOP
                random_price := ROUND((RANDOM() * 500 + 40)::numeric, 2)::double precision;
                random_volume := ROUND((RANDOM() * 2 + 0.33)::numeric, 2)::double precision;

                random_srm := (FLOOR(RANDOM() * 40) + 1)::bigint;
                random_ibu := (FLOOR(RANDOM() * 100))::bigint;
                random_abv := (FLOOR(RANDOM() * 15) + 1)::bigint;
                random_og  := (FLOOR(RANDOM() * 120) + 1)::bigint;

                random_name := CASE (i % 8)
                                   WHEN 0 THEN 'Corona'
                                   WHEN 1 THEN 'Corona Extra'
                                   WHEN 2 THEN 'Corona Premium'
                                   WHEN 3 THEN 'Paulaner'
                                   WHEN 4 THEN 'Paulaner Dark'
                                   WHEN 5 THEN 'Paulaner Premium'
                                   WHEN 6 THEN 'Baltika'
                                   WHEN 7 THEN 'Baltika Dark'
                                   ELSE 'Oxota 9'
                    END;

                random_country := CASE (i % 4)
                                      WHEN 0 THEN 'Germany'
                                      WHEN 1 THEN 'Italy'
                                      WHEN 2 THEN 'Russia'
                                      ELSE 'USA'
                    END;

                random_image_path := CASE
                                         WHEN RANDOM() < 0.33 THEN 'http://localhost:9000/pipivo/beer_images/beer_1.png'
                                         WHEN RANDOM() < 0.66 THEN 'http://localhost:9000/pipivo/beer_images/beer_2.png'
                                         ELSE 'http://localhost:9000/pipivo/beer_images/beer_3.png'
                    END;

                random_fermentation_type := (i % 4) + 1;
                random_average_rating := ROUND((RANDOM() * 5)::numeric, 2)::double precision;

                IF NOT EXISTS (
                    SELECT 1 FROM beers
                    WHERE name = random_name
                      AND country = random_country
                      AND fermentation_type = random_fermentation_type
                ) THEN
                    INSERT INTO beers (
                        name, price, volume, fermentation_type, srm, ibu, abv, og, country, image_path, average_rating
                    )
                    VALUES (
                               random_name, random_price, random_volume, random_fermentation_type,
                               random_srm, random_ibu, random_abv, random_og,
                               random_country, random_image_path, random_average_rating
                           );
                END IF;
            END LOOP;
    END $$;


DO $$
    DECLARE
        total_records INT := 20;
        user_count BIGINT;
        i INT := 0;
        random_user_id BIGINT;
        random_followed_user BIGINT;
    BEGIN
        SELECT COUNT(*) INTO user_count FROM users;

        WHILE i < total_records LOOP
                random_user_id := (FLOOR(random() * user_count) + 1)::bigint;
                random_followed_user := (FLOOR(random() * user_count) + 1)::bigint;

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


DO $$
    DECLARE
        i INT;
        user_count BIGINT;
        beer_count BIGINT;
        random_user_id BIGINT;
        random_beer_id BIGINT;
    BEGIN
        SELECT COUNT(*) INTO user_count FROM users;
        SELECT COUNT(*) INTO beer_count FROM beers;

        FOR i IN 1..20 LOOP
                random_user_id := (FLOOR(RANDOM() * user_count) + 1)::bigint;
                random_beer_id := (FLOOR(RANDOM() * beer_count) + 1)::bigint;

                INSERT INTO favourite_beer (user_id, beer_id)
                VALUES (random_user_id, random_beer_id)
                ON CONFLICT (user_id, beer_id) DO NOTHING;
            END LOOP;
    END $$;


DO $$
    DECLARE
        i INT;
        beer_count BIGINT;
    BEGIN
        SELECT COUNT(*) INTO beer_count FROM beers;

        FOR i IN 1..20 LOOP
                INSERT INTO reviews (beer_reviewed_id, rating, comment)
                VALUES (
                           ((i % beer_count) + 1)::bigint,
                           ROUND((RANDOM() * 5)::numeric, 1)::real,
                           'Комментарий ' || i
                       );
            END LOOP;
    END $$;


DO $$
    DECLARE
        i INT;
        review_count BIGINT;
        user_count BIGINT;
    BEGIN
        SELECT COUNT(*) INTO review_count FROM reviews;
        SELECT COUNT(*) INTO user_count FROM users;

        FOR i IN 1..20 LOOP
                INSERT INTO review_user (user_id, review_id)
                VALUES (((i % user_count) + 1)::bigint, ((i % review_count) + 1)::bigint)
                ON CONFLICT DO NOTHING;
            END LOOP;
    END $$;


INSERT INTO tasteprofiles (user_id)
SELECT u.id
FROM users u
ON CONFLICT (user_id) DO NOTHING;


DO $$
    DECLARE
        v_user_id BIGINT;
        fermentation_count BIGINT;
    BEGIN
        SELECT COUNT(*) INTO fermentation_count FROM fermentation_types;

        FOR v_user_id IN (SELECT id FROM users) LOOP
                UPDATE tasteprofiles
                SET
                    ibu_pref = (FLOOR(RANDOM() * 100) + 1)::bigint,
                    srm_pref = (FLOOR(RANDOM() * 40) + 1)::bigint,
                    abv_pref = (FLOOR(RANDOM() * 15) + 1)::bigint,
                    og_pref  = (FLOOR(RANDOM() * 120) + 1)::bigint,
                    fermentation_type = CASE
                                            WHEN fermentation_count > 0 THEN ((v_user_id - 1) % fermentation_count) + 1
                                            ELSE NULL
                        END,
                    price = (FLOOR(RANDOM() * 50) + 1)::bigint
                WHERE tasteprofiles.user_id = v_user_id;
            END LOOP;
    END $$;
