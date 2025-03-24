-- First, set the search path to your schema
SET search_path TO h591443_oblig3;

-- Drop tables if they exist
DROP TABLE IF EXISTS prosjektdeltakelse CASCADE;
DROP TABLE IF EXISTS prosjekt CASCADE;
DROP TABLE IF EXISTS ansatt CASCADE;
DROP TABLE IF EXISTS avdeling CASCADE;

-- Create tables
CREATE TABLE avdeling (
    id SERIAL PRIMARY KEY,
    navn VARCHAR(30) NOT NULL
);

CREATE TABLE ansatt (
    id SERIAL PRIMARY KEY,
    brukernavn VARCHAR(4) UNIQUE NOT NULL,
    fornavn VARCHAR(30) NOT NULL,
    etternavn VARCHAR(30) NOT NULL,
    ansettelsesdato DATE NOT NULL,
    stilling VARCHAR(30) NOT NULL,
    maanedslonn DECIMAL(10, 2) NOT NULL,
    avdeling_id INTEGER REFERENCES avdeling(id)
);

-- Add sjef column after both tables exist
ALTER TABLE avdeling ADD COLUMN sjef_id INTEGER REFERENCES ansatt(id);

CREATE TABLE prosjekt (
    id SERIAL PRIMARY KEY,
    navn VARCHAR(30) NOT NULL,
    beskrivelse TEXT
);

CREATE TABLE prosjektdeltakelse (
    id SERIAL PRIMARY KEY,
    ansatt_id INTEGER REFERENCES ansatt(id),
    prosjekt_id INTEGER REFERENCES prosjekt(id),
    rolle VARCHAR(30) NOT NULL,
    timer INTEGER NOT NULL DEFAULT 0,
    UNIQUE(ansatt_id, prosjekt_id)
);

-- Insert sample data
INSERT INTO avdeling (navn) VALUES ('Utvikling'), ('Markedsføring'), ('Finans');

-- First insert employees without department
INSERT INTO ansatt (brukernavn, fornavn, etternavn, ansettelsesdato, stilling, maanedslonn)
VALUES ('erik', 'Erik', 'Pedersen', '2020-03-15', 'Senior Utvikler', 55000),
       ('lisa', 'Lisa', 'Berg', '2019-06-10', 'Markedssjef', 52000),
       ('bent', 'Bent', 'Johansen', '2018-11-20', 'Finansdirektør', 65000);

-- Update departments with managers
UPDATE avdeling SET sjef_id = 1 WHERE id = 1;
UPDATE avdeling SET sjef_id = 2 WHERE id = 2;
UPDATE avdeling SET sjef_id = 3 WHERE id = 3;

-- Update employees with departments
UPDATE ansatt SET avdeling_id = 1 WHERE id = 1;
UPDATE ansatt SET avdeling_id = 2 WHERE id = 2;
UPDATE ansatt SET avdeling_id = 3 WHERE id = 3;

-- Insert more employees
INSERT INTO ansatt (brukernavn, fornavn, etternavn, ansettelsesdato, stilling, maanedslonn, avdeling_id)
VALUES ('mari', 'Mari', 'Olsen', '2021-02-15', 'Systemutvikler', 49000, 1),
       ('paal', 'Pål', 'Kristiansen', '2021-01-10', 'Markedsanalytiker', 44000, 2);

-- Insert projects
INSERT INTO prosjekt (navn, beskrivelse)
VALUES ('Kundeportal', 'Utvikling av ny kundeportal med selvbetjeningsløsninger'),
       ('Kampanje2025', 'Digital markedsføringskampanje for nye produkter');

-- Insert project participation
INSERT INTO prosjektdeltakelse (ansatt_id, prosjekt_id, rolle, timer)
VALUES (1, 1, 'Arkitekt', 135),
       (4, 1, 'Frontend-utvikler', 90),
       (2, 2, 'Kampanjeleder', 160),
       (5, 2, 'Innholdsprodusent', 110);