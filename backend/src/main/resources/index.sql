EXPLAIN (ANALYZE, BUFFERS, VERBOSE)
SELECT * FROM find_similar_beers(18412, 24);

CREATE INDEX idx_beers_attributes_1 ON beers (ibu);
CREATE INDEX idx_beers_attributes_2 ON beers (srm);
CREATE INDEX idx_beers_attributes_3 ON beers (abv);
CREATE INDEX idx_beers_attributes_4 ON beers (og);
CREATE INDEX idx_beers_attributes_5 ON beers (price);

DROP INDEX idx_beers_attributes_1;
DROP INDEX idx_beers_attributes_2;
DROP INDEX idx_beers_attributes_3;
DROP INDEX idx_beers_attributes_4;
DROP INDEX idx_beers_attributes_5;

EXPLAIN (ANALYZE, BUFFERS, VERBOSE)
SELECT * FROM beers
WHERE ibu BETWEEN 20 AND 55
  AND srm < 25
  AND price BETWEEN 100 AND 400;


SELECT b.*
FROM favourite_beer fb
         JOIN beers b ON fb.beer_id = b.id
WHERE fb.user_id = 28496;


