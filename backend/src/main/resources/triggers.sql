-- Создаем функцию, которая будет выполнять необходимую логику
CREATE OR REPLACE FUNCTION trigger_create_notification()
    RETURNS TRIGGER AS $$
DECLARE
    beer_name VARCHAR(255);
    user_name VARCHAR(255);
BEGIN
    -- Проверяем, что type_id новой записи равно 0
    IF NEW.type_id = 1 THEN
        -- Получаем имя пользователя
        SELECT username INTO user_name FROM users WHERE id = NEW.user_id;

        -- Получаем имя пива из связанных таблиц
        SELECT b.name INTO beer_name
        FROM reviews r
                 JOIN beers b ON r.beer_reviewed_id = b.id
        WHERE r.id = NEW.review_id;

        -- Создаем запись в таблице notifications
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

-- Создаем триггер, который вызывается при добавлении записей в таблицу activity
CREATE OR REPLACE TRIGGER after_activity_insert
    AFTER INSERT ON activity
    FOR EACH ROW
EXECUTE FUNCTION trigger_create_notification();

-- -------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION trigger_create_activity_on_review_user()
    RETURNS TRIGGER AS $$
BEGIN
    -- Создаем новую запись в таблице activities
    INSERT INTO activity (user_id, type_id, review_id, created_at)
    VALUES (
               NEW.user_id,  -- user_id из добавленной записи в review_user
               1,            -- type_id = 0
               NEW.review_id, -- review_id из добавленной записи в review_user
               NOW()         -- текущая дата и время
           );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Создаем триггер, который вызывается при добавлении записей в таблицу review_user
CREATE OR REPLACE TRIGGER after_review_user_insert
    AFTER INSERT ON review_user
    FOR EACH ROW
EXECUTE FUNCTION trigger_create_activity_on_review_user();

-- Напиши тригер для таблицы notifications, который будет при добавлении записи
-- найдёт всех подписчиков юзера(user_id из notification), который оставил отзыв(подписчики - folowed_user из таблицы subscribed_users), и создаст для каждого из них запись в таблице
-- notification_user, где user_id = followed_user, notification_id = id новой записи в notifications.

CREATE OR REPLACE FUNCTION trigger_add_notification_users()
    RETURNS TRIGGER AS $$
BEGIN
    -- Цикл по всем подписчикам пользователя (user_id из notifications)
    INSERT INTO notification_user (notification_id, user_id)
    SELECT NEW.id, followed_user
    FROM subscribed_users
    WHERE user_id = NEW.user_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_notification_insert
    AFTER INSERT ON notification
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
            b.id::integer AS beer_id,
            b.name AS name,
            -- Приводим результат вычислений к типу double precision
            SQRT(
                    POWER(tp.ibu_preference - b.ibu, 2) +
                    POWER(tp.srm_preference - b.srm, 2) +
                    POWER(tp.abv_preference - b.abv, 2) +
                    POWER(tp.og_preference - b.og, 2) +
                    POWER(tp.price_preference - b.price, 2) +
                    CASE
                        WHEN tp.fermentation_type_id = b.fermentation_type_id THEN 0
                        ELSE 1 -- Если тип ферментации не совпадает, добавляем штраф
                        END
            )::numeric AS similarity_score
        FROM
            taste_profile tp
                JOIN
            beer b ON TRUE
        WHERE
            tp.user_id = $1
        ORDER BY
            similarity_score ASC -- Чем меньше значение, тем лучше совпадение
        LIMIT
            limit_results;

END;
$$ LANGUAGE plpgsql;

SELECT * FROM find_similar_beers(1, 10);



-- -------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION update_recommendations_after_tasteprofile_update()
    RETURNS TRIGGER AS $$
BEGIN
    -- Удаляем старые рекомендации для этого пользователя перед вставкой новых
    DELETE FROM recommendation
    WHERE user_id = NEW.user_id;

    -- Вставляем новые рекомендации на основе обновленных предпочтений
    INSERT INTO recommendation (user_id, beer_id)
    SELECT
        NEW.user_id,
        beer_id
    FROM
        find_similar_beers(NEW.user_id, 10) AS recommendations; -- Здесь 10 - это лимит, вы можете изменить его

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_update_recommendations
    AFTER INSERT OR UPDATE ON taste_profile
    FOR EACH ROW
EXECUTE FUNCTION update_recommendations_after_tasteprofile_update();






-- TODO скрипт удалания всех тблиц и тригеров, создание корректьных данных, индексы, отчёт