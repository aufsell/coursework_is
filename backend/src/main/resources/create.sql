CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);

CREATE TABLE fermentation_types (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL
);

CREATE TABLE beers (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       price DECIMAL(10, 2),
                       volume DECIMAL(5, 2),
                       fermentation_type INT REFERENCES fermentation_types(id),
                       srm DECIMAL(5, 2),
                       ibu DECIMAL(5, 2),
                       abv DECIMAL(5, 2),
                       og DECIMAL(5, 2),
                       country VARCHAR(255),
                       image_path TEXT,
                       average_rating NUMERIC(3,2),
                       last_updated TIMESTAMP
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       country VARCHAR(255),
                       preferred_language VARCHAR(255),
                       roles_id INT REFERENCES roles(id),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       avatar_path TEXT
);

CREATE TABLE reviews (
                         id SERIAL PRIMARY KEY,
                         rating DECIMAL(5, 2) NOT NULL,
                         comment TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         beer_reviewed_id INT REFERENCES beers(id)
);

CREATE TABLE review_user (
                             user_id INT REFERENCES users(id),
                             review_id INT REFERENCES reviews(id),
                             PRIMARY KEY (user_id, review_id)
);

CREATE TABLE recommendation (
                                user_id INT REFERENCES users(id),
                                beer_id INT REFERENCES beers(id),
                                PRIMARY KEY (user_id, beer_id)
);

CREATE TABLE favourite_beer (
                                user_id INT REFERENCES users(id),
                                beer_id INT REFERENCES beers(id),
                                PRIMARY KEY (user_id, beer_id)
);

CREATE TABLE tasteprofiles (
                               id SERIAL PRIMARY KEY,
                               user_id INT REFERENCES users(id) UNIQUE,
                               ibu_pref DECIMAL(5, 2),
                               srm_pref DECIMAL(5, 2),
                               abv_pref DECIMAL(5, 2),
                               og_pref DECIMAL(5, 2),
                               fermentation_type INT REFERENCES fermentation_types(id),
                               price DECIMAL(10, 2)
);

CREATE TABLE subscribed_users (
                                  user_id INT REFERENCES users(id),
                                  followed_user INT REFERENCES users(id),
                                  PRIMARY KEY (user_id, followed_user)
);

CREATE TABLE activity_types (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(255) NOT NULL
);

CREATE TABLE activities (
                            id SERIAL PRIMARY KEY,
                            user_id INT REFERENCES users(id),
                            type_id INT REFERENCES activity_types(id),
                            review_id INT REFERENCES reviews(id),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
                               id SERIAL PRIMARY KEY,
                               user_id INT REFERENCES users(id),
                               activity_id INT REFERENCES activities(id),
                               message TEXT,
                               status VARCHAR(50)
);

CREATE TABLE notification_user (
                                   notification_id INT REFERENCES notifications(id),
                                   user_id INT REFERENCES users(id),
                                   PRIMARY KEY (notification_id, user_id)
);


