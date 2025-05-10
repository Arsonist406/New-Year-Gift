
INSERT INTO candy (
		name,                     trademark,                                                                 type,              weight,  calories,  fats,  proteins,  carbohydrates,  sugar
) VALUES
		  ('Dairy Milk',               'Cadbury',      'Chocolate',      45,      240,                       13.5,  3.2,       25.5,           24.0),
		  ('Snickers',                 'Mars',         'Chocolate',      50,      250,                       12.0,  4.0,       33.0,           27.0),
		  ('Gummy Bears',              'Haribo',       'Gummies',         200,     350,                       0.0,   6.0,       77.0,           46.0),
		  ('Jolly Rancher',            'Hershey''s',   'Caramel', 280,     700,                       0.0,   0.0,       70.0,          57.0),
		  ('Twix',                     'Mars',         'Chocolate',      50,      250,                       12.0,  3.0,       34.0,           24.0),
		  ('Starburst',                'Mars',         'Gummies',         54,      240,                       4.0,   0.0,       56.0,           34.0),
		  ('Skittles',                 'Mars',         'Gummies',         61,      250,                       2.5,   0.0,       56.0,           47.0),
		  ('M&M''s',                   'Mars',         'Chocolate',      47,      240,                       10.0,  2.0,       34.0,           30.0),
		  ('KitKat',                   'Nestle',       'Wafer',      42,      210,                       11.0,  3.0,       27.0,           22.0),
		  ('Reese''s Peanut Butter Cups', 'Hershey''s','Toffee',      45,      220,                       13.0,  5.0,       25.0,           24.0),
		  ('Sour Patch Kids',          'Mondelez',     'Gummies',         200,     360,                       0.0,   0.0,       90.0,           60.0),
		  ('Almond Joy',               'Hershey''s',   'Chocolate',      49,      230,                       13.0,  3.0,       27.0,           24.0),
		  ('Tootsie Roll',             'Tootsie',      'Toffee',         40,      140,                       3.0,   1.0,       26.0,           20.0),
		  ('Hershey''s Kisses',        'Hershey''s',   'Chocolate',        41,      220,                       13.0,  3.0,       26.0,           24.0),
		  ('Milky Way',                'Mars',         'Chocolate',      52,      240,                       8.0,   2.0,       37.0,           30.0),
		  ('3 Musketeers',             'Mars',         'Chocolate',      54,      240,                       8.0,   2.0,       40.0,           34.0),
		  ('Airheads',                 'Perfetti Van Melle','Toffee',           45,      200,                       0.0,   0.0,       50.0,           30.0),
		  ('Butterfinger',             'Ferrero',      'Chocolate',      57,      250,                       11.0,  4.0,       37.0,           29.0),
		  ('Lifesavers',               'Mars',         'Lollipop',      15,      60,                        0.0,   0.0,       15.0,           10.0),
		  ('Werther''s Original',      'Storck',       'Toffee',        30,      120,                       3.5,   0.5,       22.0,           15.0)
ON CONFLICT DO NOTHING;