DROP TABLE IF EXISTS employees;

DROP TABLE IF EXISTS users;

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  mail VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  departement_Id Long NOT NULL
);
 
 CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL
);

INSERT INTO employees (first_name, last_name, mail, password, departement_Id) VALUES
  ('Laurent', 'GINA', 'laurentgina@mail.com', 'laurent',1),
  ('Sophie', 'FONCEK', 'sophiefoncek@mail.com', 'sophie',2),
  ('Agathe', 'FEELING', 'agathefeeling@mail.com', 'agathe',3),
  ('Jolen', 'PIERRE', 'joleng@mail.com', 'jolen',1),
  ('Georges', 'ALIX', 'georgesg@mail.com', 'georges',1) ,
  ('Moyiz', 'ORASE', 'moyizg@mail.com', 'moyiz',1),
  ('Kolon', 'JEAN', 'kolong@mail.com', 'kolon',1),
  ('Moliere', 'KALBWA', 'moliereg@mail.com', 'moliere',2),
  ('Tibwa', 'CARL', 'tibwag@mail.com', 'tibwa',2),
  ('Manise', 'JEAN', 'maniseg@mail.com', 'manise',1),
  ('Vagner', 'JEAN', 'vagnerg@mail.com', 'vagner',1),
  ('Manitha', 'CARL', 'manithag@mail.com', 'manitha',1),
  ('Josue', 'HENRY', 'josueg@mail.com', 'josue',1);
  
INSERT INTO users (username, password, role) VALUES
('user1', '{bcrypt}$2a$10$eBvT7T/8ZrPfS9ZywjH4/.9A01T/TVhPZh6vHcq0cbYFZ7z8X2Hyi', 'ROLE_USER'),
('admin1', '{bcrypt}$2a$10$7HgK5Vh6P4QYhbq3tdv7WuPWTUgNYM1PaxHbG.7ZnDFBipgWkI7my', 'ROLE_ADMIN');
