CREATE OR REPLACE FUNCTION trigger_create_notification()
    RETURNS TRIGGER AS $$
DECLARE
    beer_name VARCHAR(255);
    user_name VARCHAR(255);
BEGIN
    IF NEW.type_id = 1 THEN
        SELECT username INTO user_name FROM users WHERE id = NEW.user_id;

        SELECT b.name INTO beer_name
        FROM reviews r
                 JOIN beers b ON r.beer_reviewed_id = b.id
        WHERE r.id = NEW.review_id;

        INSERT INTO notifications (user_id, activity_id, message, status)
        VALUES (
                   NEW.user_id,
                   NEW.id,
                   'Пользователь ' || user_name || ' оставил отзыв на пиво ' || beer_name,
                   'unread'
               );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_activity_insert
    AFTER INSERT ON activities
    FOR EACH ROW
EXECUTE FUNCTION trigger_create_notification();

-- -------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION trigger_create_activity_on_review_user()
    RETURNS TRIGGER AS $$
BEGIN

    INSERT INTO activities (user_id, type_id, review_id, created_at)
    VALUES (
               NEW.user_id,
               1,
               NEW.review_id,
               NOW()
           );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_review_user_insert
    AFTER INSERT ON review_user
    FOR EACH ROW
EXECUTE FUNCTION trigger_create_activity_on_review_user();


CREATE OR REPLACE FUNCTION trigger_add_notification_users()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO notification_user (notification_id, user_id)
    SELECT NEW.id, followed_user
    FROM subscribed_users
    WHERE user_id = NEW.user_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_notification_insert
    AFTER INSERT ON notifications
    FOR EACH ROW
EXECUTE FUNCTION trigger_add_notification_users();

-------

CREATE OR REPLACE FUNCTION find_similar_beers(user_id BIGINT, limit_results INT)
    RETURNS TABLE (
                      beer_id BIGINT,
                      beer_name VARCHAR,
                      similarity_score DECIMAL
                  ) AS $$
DECLARE
    recommended_beers CURSOR FOR
        SELECT b.id AS rec_beer_id, b.name AS rec_beer_name,
               CAST(ABS(CAST(tp.ibu_pref AS integer) - b.ibu) +
                    ABS(CAST(tp.srm_pref AS integer) - b.srm) +
                    ABS(CAST(tp.abv_pref AS integer) - b.abv) +
                    ABS(CAST(tp.og_pref AS integer) - b.og) +
                    ABS(tp.price - b.price) +
                    CASE
                        WHEN tp.fermentation_type = b.fermentation_type THEN 0
                        ELSE 1
                        END AS DECIMAL) AS rec_similarity_score
        FROM tasteprofiles tp
                 CROSS JOIN beers b
        WHERE tp.user_id = find_similar_beers.user_id
        ORDER BY rec_similarity_score ASC
        LIMIT limit_results;

    similar_beers CURSOR (excluded_beer_ids BIGINT[]) FOR
        SELECT s.beer_id AS sim_beer_id, s.name AS sim_beer_name, s.similarity_score AS sim_similarity_score
        FROM find_similar_beer_from_other_users(find_similar_beers.user_id, excluded_beer_ids) AS s;

    beer_count INT := 0;
    used_beer_ids BIGINT[] := '{}';
    rec_beer_id BIGINT;
    rec_beer_name VARCHAR;
    rec_similarity_score DECIMAL;
    sim_beer_id BIGINT;
    sim_beer_name VARCHAR;
    sim_similarity_score DECIMAL;
BEGIN
    BEGIN
        RAISE NOTICE 'Starting find_similar_beers for user_id % with limit %', user_id, limit_results;


        OPEN recommended_beers;
        FETCH NEXT FROM recommended_beers INTO rec_beer_id, rec_beer_name, rec_similarity_score;

        WHILE FOUND AND beer_count < 7 LOOP
                IF rec_beer_id = ANY(used_beer_ids) THEN
                    FETCH NEXT FROM recommended_beers INTO rec_beer_id, rec_beer_name, rec_similarity_score;
                    CONTINUE;
                END IF;
                beer_id := rec_beer_id;
                beer_name := rec_beer_name;
                similarity_score := rec_similarity_score;
                RETURN NEXT;
                used_beer_ids := array_append(used_beer_ids, rec_beer_id);
                beer_count := beer_count + 1;
                RAISE NOTICE 'Added beer_id % from recommended_beers, count = %', rec_beer_id, beer_count;
                FETCH NEXT FROM recommended_beers INTO rec_beer_id, rec_beer_name, rec_similarity_score;
            END LOOP;

        CLOSE recommended_beers;


        IF beer_count < 8 THEN
            OPEN similar_beers(used_beer_ids);
            FETCH NEXT FROM similar_beers INTO sim_beer_id, sim_beer_name, sim_similarity_score;

            IF FOUND AND NOT (sim_beer_id = ANY(used_beer_ids)) THEN
                beer_id := sim_beer_id;
                beer_name := sim_beer_name;
                similarity_score := sim_similarity_score;
                RETURN NEXT;
                used_beer_ids := array_append(used_beer_ids, sim_beer_id);
                beer_count := beer_count + 1;
                RAISE NOTICE 'Added beer_id % from from other users, count = %', sim_beer_id, beer_count;
            ELSE
                RAISE NOTICE 'No unique beer added from similar_beers';
            END IF;

            CLOSE similar_beers;
        END IF;


        IF beer_count < 8 THEN
            OPEN recommended_beers;
            FETCH NEXT FROM recommended_beers INTO rec_beer_id, rec_beer_name, rec_similarity_score;

            WHILE FOUND AND beer_count < 8 LOOP
                    IF rec_beer_id = ANY(used_beer_ids) THEN
                        FETCH NEXT FROM recommended_beers INTO rec_beer_id, rec_beer_name, rec_similarity_score;
                        CONTINUE;
                    END IF;
                    beer_id := rec_beer_id;
                    beer_name := rec_beer_name;
                    similarity_score := rec_similarity_score;
                    RETURN NEXT;
                    used_beer_ids := array_append(used_beer_ids, rec_beer_id);
                    beer_count := beer_count + 1;
                    RAISE NOTICE 'Added beer_id % from recommended_beers (second pass), count = %', rec_beer_id, beer_count;
                    FETCH NEXT FROM recommended_beers INTO rec_beer_id, rec_beer_name, rec_similarity_score;
                END LOOP;

            CLOSE recommended_beers;
        END IF;

        RAISE NOTICE 'Finished with % beers', beer_count;
    EXCEPTION WHEN OTHERS THEN
        RAISE NOTICE 'Error in find_similar_beers for user_id %: %', user_id, SQLERRM;
        RETURN;
    END;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_similar_beer_from_other_users(input_user_id BIGINT, excluded_beer_ids BIGINT[])
    RETURNS TABLE (
                      beer_id BIGINT,
                      name VARCHAR,
                      similarity_score DECIMAL
                  ) AS $$
BEGIN
    BEGIN
        RETURN QUERY
            WITH similar_users AS (
                SELECT tp.user_id
                FROM tasteprofiles tp
                WHERE tp.user_id != input_user_id
                  AND ABS(CAST(tp.ibu_pref AS integer) - (SELECT ibu_pref FROM tasteprofiles WHERE user_id = input_user_id)) < 5
                  AND ABS(CAST(tp.srm_pref AS integer) - (SELECT srm_pref FROM tasteprofiles WHERE user_id = input_user_id)) < 5
                  AND ABS(CAST(tp.abv_pref AS integer) - (SELECT abv_pref FROM tasteprofiles WHERE user_id = input_user_id)) < 5
                LIMIT 5
            )
            SELECT b.id AS beer_id, b.name,
                   CAST(ABS(CAST(tp.ibu_pref AS integer) - b.ibu) +
                        ABS(CAST(tp.srm_pref AS integer) - b.srm) +
                        ABS(CAST(tp.abv_pref AS integer) - b.abv) +
                        ABS(CAST(tp.og_pref AS integer) - b.og) +
                        ABS(tp.price - b.price) +
                        CASE
                            WHEN tp.fermentation_type = b.fermentation_type THEN 0
                            ELSE 1
                            END AS DECIMAL) AS similarity_score
            FROM similar_users su
                     JOIN tasteprofiles tp ON tp.user_id = su.user_id
                     JOIN beers b ON b.id = ANY(excluded_beer_ids) IS FALSE
            ORDER BY similarity_score ASC
            LIMIT 1;

        IF NOT FOUND THEN
            RAISE NOTICE 'No similar beers found from other users for user_id %', input_user_id;
        END IF;
    EXCEPTION WHEN OTHERS THEN
        RAISE NOTICE 'Error in find_similar_beer_from_other_users for user_id %: %', input_user_id, SQLERRM;
        RETURN;
    END;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_recommendations_after_tasteprofile_update()
    RETURNS TRIGGER AS $$
BEGIN
    BEGIN
        DELETE FROM recommendation
        WHERE user_id = NEW.user_id;

        INSERT INTO recommendation (user_id, beer_id)
        SELECT
            NEW.user_id,
            beer_id
        FROM
            find_similar_beers(NEW.user_id::BIGINT, 8) AS recommendations(beer_id, beer_name, similarity_score);

        RETURN NEW;
    EXCEPTION WHEN OTHERS THEN
        RAISE NOTICE 'Error in update_recommendations_after_tasteProfile_update for user_id %: %', NEW.user_id, SQLERRM;
        RETURN NULL;
    END;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_update_recommendations
    AFTER UPDATE OR INSERT ON tasteprofiles
    FOR EACH ROW
EXECUTE FUNCTION update_recommendations_after_tasteprofile_update();


CREATE OR REPLACE FUNCTION update_last_updated()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_updated = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER trigger_update_last_updated
    BEFORE UPDATE OR INSERT ON beers
    FOR EACH ROW
EXECUTE FUNCTION update_last_updated();



CREATE OR REPLACE FUNCTION create_taste_profile_on_user_insert()
    RETURNS TRIGGER AS $$
DECLARE
    top_beer RECORD;
BEGIN
    SELECT *
    INTO top_beer
    FROM get_most_popular_beer();

    INSERT INTO tasteprofiles (abv_pref, ibu_pref, og_pref, price, srm_pref, fermentation_type, user_id)
    VALUES (
               top_beer.abv,
               top_beer.ibu,
               top_beer.og,
               top_beer.price,
               top_beer.srm,
               top_beer.fermentation_type,
               NEW.id
           );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_create_taste_profile_on_user_insert
    AFTER INSERT ON users
    FOR EACH ROW
EXECUTE FUNCTION create_taste_profile_on_user_insert();

CREATE OR REPLACE FUNCTION get_most_popular_beer()
    RETURNS TABLE (
                      beer_id INTEGER,
                      abv BIGINT,
                      ibu BIGINT,
                      og BIGINT,
                      price NUMERIC(10, 2),
                      srm BIGINT,
                      fermentation_type BIGINT,
                      review_count INTEGER
                  )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT
            b.id::integer AS beer_id,
            b.abv,
            b.ibu,
            b.og,
            b.price::NUMERIC(10, 2),
            b.srm,
            b.fermentation_type,
            COUNT(r.id)::integer AS review_count
        FROM
            reviews r
                JOIN
            beers b ON r.beer_reviewed_id = b.id
        GROUP BY
            b.id, b.abv, b.ibu, b.og, b.price, b.srm, b.fermentation_type
        ORDER BY
            review_count DESC
        LIMIT 1;
END;
$$;