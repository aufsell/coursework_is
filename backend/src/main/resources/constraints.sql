ALTER TABLE roles
    ADD CONSTRAINT uq_roles_name UNIQUE (name);

ALTER TABLE fermentation_types
    ADD CONSTRAINT uq_fermentation_types_name UNIQUE (name);

ALTER TABLE activity_types
    ADD CONSTRAINT uq_activity_types_name UNIQUE (name);

ALTER TABLE reviews
    ADD CONSTRAINT chk_reviews_rating_range
        CHECK (rating >= 0 AND rating <= 5);

ALTER TABLE beers
    ADD CONSTRAINT chk_beers_price_non_negative
        CHECK (price IS NULL OR price >= 0);

ALTER TABLE beers
ADD CONSTRAINT chk_beers_volume_positive
        CHECK (volume IS NULL OR volume > 0);

ALTER TABLE beers
    ADD CONSTRAINT chk_beers_abv_range
        CHECK (abv IS NULL OR (abv >= 0 AND abv <= 100));

ALTER TABLE beers
    ADD CONSTRAINT chk_beers_ibu_non_negative
        CHECK (ibu IS NULL OR ibu >= 0);

ALTER TABLE beers
    ADD CONSTRAINT chk_beers_srm_non_negative
        CHECK (srm IS NULL OR srm >= 0);

ALTER TABLE beers
    ADD CONSTRAINT chk_beers_og_positive
        CHECK (og IS NULL OR og > 0);

ALTER TABLE tasteprofiles
    ADD CONSTRAINT chk_tasteprofiles_price_non_negative
        CHECK (price IS NULL OR price >= 0);

ALTER TABLE tasteprofiles
    ADD CONSTRAINT chk_tasteprofiles_abv_range
        CHECK (abv_pref IS NULL OR (abv_pref >= 0 AND abv_pref <= 100));

ALTER TABLE tasteprofiles
    ADD CONSTRAINT chk_tasteprofiles_ibu_non_negative
        CHECK (ibu_pref IS NULL OR ibu_pref >= 0);

ALTER TABLE tasteprofiles
    ADD CONSTRAINT chk_tasteprofiles_srm_non_negative
        CHECK (srm_pref IS NULL OR srm_pref >= 0);

ALTER TABLE tasteprofiles
    ADD CONSTRAINT chk_tasteprofiles_og_positive
        CHECK (og_pref IS NULL OR og_pref > 0);