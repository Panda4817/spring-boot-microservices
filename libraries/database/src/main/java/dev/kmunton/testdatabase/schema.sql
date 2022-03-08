DROP TABLE IF EXISTS sites CASCADE;
CREATE TABLE sites(id serial PRIMARY KEY, name text NOT NULL, lat numeric NOT NULL, lng numeric NOT NULL);

DROP TABLE IF EXISTS restaurants CASCADE;
CREATE TABLE restaurants(id serial PRIMARY KEY, name text NOT NULL, description text, website text, address text NOT NULL);

DROP TABLE IF EXISTS restaurants_to_sites CASCADE;
CREATE TABLE restaurants_to_sites(restaurant_id integer NOT NULL, site_id integer NOT NULL, distance numeric NOT NULL, PRIMARY KEY (restaurant_id, site_id));

DROP TABLE IF EXISTS reviews CASCADE;
CREATE TABLE reviews(id serial PRIMARY KEY, rating integer NOT NULL CHECK (rating > 0 AND rating < 6), comment text, timestamp timestamp with time zone NOT NULL DEFAULT(current_timestamp(0)), restaurant_id integer REFERENCES restaurants (id));