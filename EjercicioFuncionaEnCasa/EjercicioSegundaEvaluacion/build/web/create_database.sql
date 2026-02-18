SHOW DATABASES;
CREATE DATABASE EjercicioSegundaEvaluacion;
USE EjercicioSegundaEvaluacion;

DROP TABLE Alumno CASCADE;
DROP TABLE Curso CASCADE;
DROP TABLE Empresa CASCADE;
DROP TABLE Practica CASCADE;
DROP TABLE Profesor CASCADE;

CREATE TABLE Alumno(
    id_alumno int AUTO_INCREMENT NOT NULL,
    nombre varchar(255),
    apellidos varchar(255),
    email varchar(255),
    curso_matriculado varchar(255) NULL,
    fecha_nac DATE not null,
    PRIMARY KEY (id_alumno)
);

CREATE TABLE Curso(
    nombre varchar(255) NOT NULL,
    PRIMARY KEY (nombre)
);

CREATE TABLE Empresa(
    id_empresa int AUTO_INCREMENT NOT NULL,
    nombre varchar(255),
    descripcion varchar(255),
    nombre_completo varchar(255),
    email_tutor_laboral varchar(255),
    PRIMARY KEY (id_empresa)
);

CREATE TABLE Practica(
    id_practica int AUTO_INCREMENT NOT NULL,
    empresa_id int,
    alumno_id int,
    fecha_comienzo DATE,
    fecha_finalizacion DATE,
    comentarios varchar(255),
    PRIMARY KEY (id_practica)
);

CREATE TABLE Profesor(
    id_profesor int AUTO_INCREMENT NOT NULL,
    nombre varchar(255),
    apellidos varchar(255),
    email varchar(255),
    password varchar(255),
    directiva bit,
    PRIMARY KEY (id_profesor)
);

ALTER TABLE Alumno ADD CONSTRAINT fk_alumno_curso FOREIGN KEY (curso_matriculado) REFERENCES Curso(nombre) ON DELETE CASCADE;
ALTER TABLE Practica ADD CONSTRAINT fk_practica_alumno FOREIGN KEY (alumno_id) REFERENCES Alumno(id_alumno) ON DELETE CASCADE;
ALTER TABLE Practica ADD CONSTRAINT fk_practica_empresa FOREIGN KEY (empresa_id) REFERENCES Empresa(id_empresa) ON DELETE CASCADE;
ALTER TABLE Alumno ADD CONSTRAINT fk_curso_alumno FOREIGN KEY (curso_matriculado) REFERENCES Curso(nombre) ON DELETE SET NULL ON UPDATE CASCADE;

/*Inserts de prueba*/
INSERT INTO Curso (nombre) VALUES ('1DAW');

INSERT INTO Alumno (nombre, apellidos, email, curso_matriculado, fecha_nac) VALUES
('Ana', 'García', 'ana.garcia@email.com', '1DAW', '2004-03-15'),
('Luis', 'Martínez', 'luis.martinez@email.com', '1DAW', '2004-07-22'),
('María', 'López', 'maria.lopez@email.com', '1DAW', '2004-11-05'),
('Carlos', 'Pérez', 'carlos.perez@email.com', '1DAW', '2004-01-30'),
('Lucía', 'Rodríguez', 'lucia.rodriguez@email.com', '1DAW', '2004-06-18');

INSERT INTO Empresa (nombre, descripcion, nombre_completo, email_tutor_laboral) VALUES
('TechSolutions', 'Empresa de software', 'Pedro Gómez', 'pedro.gomez@techsolutions.com'),
('InnovaDesign', 'Diseño gráfico y web', 'Laura Sánchez', 'laura.sanchez@innovadesign.com'),
('EcoEnergy', 'Energías renovables', 'Javier Torres', 'javier.torres@ecoenergy.com'),
('BuildIt', 'Construcción e ingeniería', 'Ana Molina', 'ana.molina@buildit.com'),
('Foodies', 'Restauración y catering', 'Miguel Ruiz', 'miguel.ruiz@foodies.com');

INSERT INTO Practica (empresa_id, alumno_id, fecha_comienzo, fecha_finalizacion, comentarios) VALUES
(1, 1, '2025-02-01', '2025-06-30', 'Muy buena práctica'),
(2, 2, '2025-03-01', '2025-07-31', 'Aprendió mucho'),
(3, 3, '2025-01-15', '2025-05-15', 'Práctica completa'),
(4, 4, '2025-02-20', '2025-06-20', 'Excelente rendimiento'),
(5, 5, '2025-03-10', '2025-07-10', 'Buen seguimiento');

INSERT INTO Profesor (nombre, apellidos, email, password, directiva) VALUES
('Sofía', 'Martín', 'sofia.martin@email.com', 'pass123', 1),
('Jorge', 'Fernández', 'jorge.fernandez@email.com', 'pass123', 0),
('Elena', 'Ruiz', 'elena.ruiz@email.com', 'pass123', 0),
('Miguel', 'Gómez', 'miguel.gomez@email.com', 'pass123', 1),
('Clara', 'Vega', 'clara.vega@email.com', 'pass123', 0);


/*MOSTRAR TODOS LOS ALUMNOS DE UN CURSO*/
SELECT nombre, apellidos FROM Alumno
WHERE curso_matriculado = "1DAW";