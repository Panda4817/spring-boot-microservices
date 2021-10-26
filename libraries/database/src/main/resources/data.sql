DELETE FROM restaurants_to_sites;
DELETE FROM sites;
DELETE FROM restaurants;
INSERT INTO sites (id, name, lat, lng) VALUES (1, 'name', 5.1, 2.1);
INSERT INTO restaurants (id, name, address) VALUES (1, 'name', 'address');
INSERT INTO restaurants_to_sites (restaurant_id, site_id, distance) VALUES (1, 1, 0.1);