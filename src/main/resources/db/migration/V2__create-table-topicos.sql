CREATE TABLE topicos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL UNIQUE,
    mensaje TEXT NOT NULL UNIQUE,
    fecha_de_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_de_ultima_actualizacion TIMESTAMP NOT NULL DEFAULT NOW(),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    autor INT NOT NULL,
    curso VARCHAR(100) NOT NULL,
    CONSTRAINT fk_autor FOREIGN KEY (autor) REFERENCES usuarios (id) ON DELETE CASCADE
    );
