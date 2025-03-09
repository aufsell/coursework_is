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

CREATE OR REPLACE FUNCTION find_similar_beers(user_id INT, limit_results INT)
    RETURNS TABLE (
                      beer_id INT,
                      name VARCHAR,
                      similarity_score DECIMAL
                  ) AS $$
BEGIN
    RETURN QUERY
        SELECT
            b.id AS beer_id,
            b.name AS name,
            ABS(tp.ibu_pref - b.ibu) +
            ABS(tp.srm_pref - b.srm) +
            ABS(tp.abv_pref - b.abv) +
            ABS(tp.og_pref - b.og) +
            ABS(tp.price - b.price) +
            CASE
                WHEN tp.fermentation_type = b.fermentation_type THEN 0
                ELSE 1
                END AS similarity_score
        FROM
            tasteprofiles tp
                CROSS JOIN beers b
        WHERE
            tp.user_id = $1
        ORDER BY
            similarity_score ASC
        LIMIT limit_results;
END;
$$ LANGUAGE plpgsql;


-- SELECT * FROM find_similar_beers(1, 10);



-- -------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION update_recommendations_after_tasteprofile_update()
    RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM recommendation
    WHERE user_id = NEW.user_id;

    INSERT INTO recommendation (user_id, beer_id)
    SELECT
        NEW.user_id,
        beer_id
    FROM
        find_similar_beers(NEW.user_id, 10) AS recommendations;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_update_recommendations
    AFTER UPDATE ON tasteprofiles
    FOR EACH ROW
EXECUTE FUNCTION update_recommendations_after_tasteprofile_update();

CREATE OR REPLACE FUNCTION update_last_updated()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_updated = CURRENT_TIMESTAMP AT TIME ZONE 'Europe/Moscow';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_insert_last_updated
    BEFORE INSERT ON beers
    FOR EACH ROW
EXECUTE FUNCTION update_last_updated();


CREATE TRIGGER trigger_update_last_updated
    BEFORE UPDATE ON beers
    FOR EACH ROW
EXECUTE FUNCTION update_last_updated();


CREATE OR REPLACE FUNCTION get_top_beers_for_limit(limit_ans INT)
    RETURNS TABLE(beer_reviewed_id INT, review_count INT) AS
$$
BEGIN
    RETURN QUERY
        SELECT
            beer_reviewed_id,
            COUNT(*) AS review_count
        FROM
            reviews
        GROUP BY
            beer_reviewed_id
        ORDER BY
            review_count DESC
        LIMIT limit_ans;
END;
$$ LANGUAGE plpgsql;

SELECT reviews.beer_reviewed_id, COUNT(*) AS review_count
FROM reviews
GROUP BY beer_reviewed_id
ORDER BY review_count DESC
LIMIT 4;


CREATE OR REPLACE FUNCTION get_top_beers()
    RETURNS TABLE(
                     beer_id INTEGER,
                     beer_name VARCHAR,
                     image_path VARCHAR,
                     price NUMERIC(10, 2),
                     average_rating NUMERIC(3, 2),
                     country VARCHAR,
                     review_count INTEGER
                 )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT
            b.id::integer AS beer_id,
            b.name AS beer_name,
            b.image_path,
            b.price::numeric(10,2),
            COALESCE(AVG(r.rating), 0)::numeric(3,2) AS average_rating,  -- Рассчитываем средний рейтинг
            b.country,
            COUNT(r.id)::integer AS review_count
        FROM
            reviews r
                JOIN
            beers b ON r.beer_reviewed_id = b.id  -- Выполняем join с таблицей beers
        GROUP BY
            b.id
        ORDER BY
            review_count DESC
        LIMIT 4;
END;
$$;

CREATE OR REPLACE FUNCTION calculate_average_rating(beer_id BIGINT)
    RETURNS VOID AS
$$
DECLARE
    avg_rating DOUBLE PRECISION;
BEGIN
    SELECT AVG(rating)
    INTO avg_rating
    FROM reviews
    WHERE beer_reviewed_id = beer_id;

    UPDATE beers
    SET average_rating = avg_rating
    WHERE id = beer_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trigger_update_average_rating()
    RETURNS TRIGGER AS
$$
BEGIN
    PERFORM calculate_average_rating(NEW.beer_reviewed_id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_average_rating_trigger
    AFTER INSERT ON reviews
    FOR EACH ROW
EXECUTE FUNCTION trigger_update_average_rating();