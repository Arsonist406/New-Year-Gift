
INSERT INTO gift (name, description)
VALUES
		('Birthday Surprise',   'A delightful mix of chocolates and gummies perfect for birthdays!'),
		('Sweet Tooth Pack',    'Assorted candies with a variety of flavors and textures.'),
		('Chocolate Lover''s Box', 'Premium selection of the finest chocolate bars.')
ON CONFLICT DO NOTHING;

INSERT INTO gift_candy_weights (gift_id, candy_id, weight_in_grams)
VALUES
(1,         1,         100),   -- Dairy Milk
(1,         3,         150),   -- Gummy Bears
(1,         6,         80),    -- Starburst
(1,         10,        50),    -- Reese's

(2,         5,         70),    -- Twix
(2,         7,         120),   -- Skittles
(2,         12,        90),    -- Almond Joy
(2,         15,        60),    -- Milky Way
(2,         18,        100),   -- Butterfinger

(3,         1,         200),   -- Dairy Milk
(3,         2,         150),   -- Snickers
(3,         8,         100),   -- M&M's
(3,         14,        120)   -- Hershey's Kisses
ON CONFLICT DO NOTHING;