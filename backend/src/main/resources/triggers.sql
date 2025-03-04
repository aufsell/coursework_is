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
