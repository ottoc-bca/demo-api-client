INSERT INTO department (name) SELECT 'HR' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'HR');
INSERT INTO department (name) SELECT 'IT' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'IT');
INSERT INTO department (name) SELECT 'Business' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'Business');
