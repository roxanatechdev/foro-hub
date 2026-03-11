CREATE TABLE respuestas (
    id BIGSERIAL PRIMARY KEY,
    mensaje TEXT NOT NULL,
    topico INT NOT NULL,
    fecha_de_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_de_ultima_actualizacion TIMESTAMP NOT NULL DEFAULT NOW(),
    autor INT NOT NULL,
    solucion BOOLEAN DEFAULT FALSE,
        CONSTRAINT fk_topico FOREIGN KEY (topico) REFERENCES topicos (id) ON DELETE CASCADE,
        CONSTRAINT fk_autor FOREIGN KEY (autor) REFERENCES usuarios (id) ON DELETE CASCADE
    );
