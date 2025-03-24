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
INSERT INTO avdeling (navn) VALUES ('IT'), ('HR'), ('Økonomi');

-- First insert employees without department
INSERT INTO ansatt (brukernavn, fornavn, etternavn, ansettelsesdato, stilling, maanedslonn)
VALUES ('jad', 'Jan', 'Davidsen', '2020-01-01', 'Utvikler', 50000),
       ('mos', 'Mona', 'Svendsen', '2019-04-15', 'HR-konsulent', 45000),
       ('tor', 'Tor', 'Hansen', '2018-08-01', 'Økonomisjef', 60000);

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
VALUES ('ola', 'Ola', 'Nordmann', '2021-03-01', 'Utvikler', 48000, 1),
       ('kar', 'Kari', 'Hansen', '2021-02-15', 'HR-assistent', 42000, 2);

-- Insert projects
INSERT INTO prosjekt (navn, beskrivelse)
VALUES ('Webportal', 'Utvikling av ny webportal for kunder'),
       ('HR-system', 'Implementering av nytt HR-system');

-- Insert project participation
INSERT INTO prosjektdeltakelse (ansatt_id, prosjekt_id, rolle, timer)
VALUES (1, 1, 'Utvikler', 120),
       (4, 1, 'Tester', 80),
       (2, 2, 'Prosjektleder', 150),
       (5, 2, 'Konsulent', 100);