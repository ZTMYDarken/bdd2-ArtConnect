CREATE DATABASE IF NOT EXISTS art_connect;
USE art_connect;

DROP TABLE IF EXISTS Artist_Discipline;
DROP TABLE IF EXISTS Exhibition_Artwork;
DROP TABLE IF EXISTS Artwork_Tag;
DROP TABLE IF EXISTS Member_Discipline;
DROP TABLE IF EXISTS Discipline;
DROP TABLE IF EXISTS ArtworkTag;
DROP TABLE IF EXISTS Review;
DROP TABLE IF EXISTS Exhibition;
DROP TABLE IF EXISTS Artwork;
DROP TABLE IF EXISTS Gallery;
DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS Workshop;
DROP TABLE IF EXISTS Artist;
DROP TABLE IF EXISTS CommunityMember;


CREATE TABLE Artist(
   id_artist INT,
   name VARCHAR(50),
   birthYear INT,
   bio VARCHAR(200),
   contactEmail VARCHAR(50),
   phone VARCHAR(50),
   city VARCHAR(50),
   website VARCHAR(50),
   socialMedia VARCHAR(50),
   isActive Boolean,
   PRIMARY KEY(id_artist)
);

CREATE TABLE Artwork(
   id_artwork INT,
   title VARCHAR(50),
   creationYear INT,
   type VARCHAR(50),
   medium VARCHAR(50),
   dimensions VARCHAR(50),
   description VARCHAR(200),
   price DECIMAL(15,2),
   status VARCHAR(50),
   id_artist INT,
   PRIMARY KEY(id_artwork),
   FOREIGN KEY (id_artist) REFERENCES Artist(id_artist)
);

CREATE TABLE ArtworkTag(
   id_tag INT,
   name VARCHAR(50),
   PRIMARY KEY(id_tag)
);

CREATE TABLE CommunityMember(
   id_member INT,
   name VARCHAR(50),
   email VARCHAR(50),
   birthYear INT,
   phone VARCHAR(50),
   city VARCHAR(50),
   membershipType VARCHAR(50),
   PRIMARY KEY(id_member)
);

CREATE TABLE Discipline(
   id_discipline INT,
   name VARCHAR(50),
   PRIMARY KEY(id_discipline)
);

CREATE TABLE Gallery(
   id_gallery INT,
   name VARCHAR(50),
   adress VARCHAR(50),
   ownerName VARCHAR(50),
   openingHours VARCHAR(50),
   contactPhone VARCHAR(50),
   rating DECIMAL(15,2),
   website VARCHAR(50),
   PRIMARY KEY(id_gallery)
);

CREATE TABLE Exhibition(
   id_exhibition INT,
   title VARCHAR(50),
   startDate DATE,
   endDate DATE,
   description VARCHAR(200),
   curatorName VARCHAR(50),
   theme VARCHAR(50),
   id_gallery INT,
   PRIMARY KEY(id_exhibition),
   FOREIGN KEY (id_gallery) REFERENCES Gallery(id_gallery)
);

CREATE TABLE Review(
   id_review INT,
   rating INT,
   comment VARCHAR(50),
   reviewDate DATE,
   id_member INT,
   id_artwork INT,
   PRIMARY KEY(id_review),
   FOREIGN KEY (id_member) REFERENCES CommunityMember(id_member),
   FOREIGN KEY (id_artwork) REFERENCES Artwork(id_artwork)
);

CREATE TABLE Workshop(
   id_workshop INT,
   title VARCHAR(50),
   dateWorkshop DATE,
   durationMinutes INT,
   maxParticipants INT,
   price DECIMAL(15,2),
   location VARCHAR(50),
   description VARCHAR(200),
   level VARCHAR(50),
   id_artist INT,
   PRIMARY KEY(id_workshop),
   FOREIGN KEY (id_artist) REFERENCES Artist(id_artist)
);

CREATE TABLE Booking(
   id_booking INT,
   bookingDate DATE,
   paymentStatus VARCHAR(50),
   id_workshop INT,
   id_member INT,
   PRIMARY KEY(id_booking),
   FOREIGN KEY (id_workshop) REFERENCES Workshop(id_workshop),
   FOREIGN KEY (id_member) REFERENCES CommunityMember(id_member)
);

CREATE TABLE Artist_Discipline(
   id_artist INT,
   id_discipline INT,
   PRIMARY KEY(id_artist, id_discipline),
   FOREIGN KEY (id_artist) REFERENCES Artist(id_artist),
   FOREIGN KEY (id_discipline) REFERENCES Discipline(id_discipline)
);

CREATE TABLE Exhibition_Artwork(
   id_exhibition INT,
   id_artwork INT,
   PRIMARY KEY(id_exhibition, id_artwork),
   FOREIGN KEY (id_exhibition) REFERENCES Exhibition(id_exhibition),
   FOREIGN KEY (id_artwork) REFERENCES Artwork(id_artwork)
);

CREATE TABLE Artwork_Tag(
   id_artwork INT,
   id_tag INT,
   PRIMARY KEY(id_artwork, id_tag),
   FOREIGN KEY (id_tag) REFERENCES ArtworkTag(id_tag),
   FOREIGN KEY (id_artwork) REFERENCES Artwork(id_artwork)
);

CREATE TABLE Member_Discipline(
   id_member INT,
   id_discipline INT,
   PRIMARY KEY(id_member, id_discipline),
   FOREIGN KEY (id_member) REFERENCES CommunityMember(id_member),
   FOREIGN KEY (id_discipline) REFERENCES Discipline(id_discipline)
);

USE art_connect;

-- =========================================
-- ARTIST
-- =========================================
INSERT INTO Artist (id_artist, name, birthYear, bio, contactEmail, phone, city, website, socialMedia, isActive) VALUES
(1, 'Nora Belkacem', 1991, 'Visual artist focused on urban painting', 'nora.belkacem@artconnect.com', '+33-610000001', 'Paris', 'www.norabelkacem.art', '@nora.colors', TRUE),
(2, 'Milo Carpentier', 1987, 'Photographer exploring industrial spaces', 'milo.carpentier@artconnect.com', '+33-610000002', 'Lille', 'www.milocarpentier.photo', '@milo.frames', TRUE),
(3, 'Aya Benali', 1994, 'Sculptor inspired by organic forms', 'aya.benali@artconnect.com', '+33-610000003', 'Marseille', 'www.ayabenali.studio', '@aya.volume', TRUE),
(4, 'Samuel Ortega', 1989, 'Painter of contemporary night scenes', 'samuel.ortega@artconnect.com', '+34-610000004', 'Madrid', 'www.samuelortega.art', '@samuel.nocturne', TRUE),
(5, 'Elena Markovic', 1993, 'Mixed media and textile collage artist', 'elena.markovic@artconnect.com', '+381-610000005', 'Belgrade', 'www.elenamarkovic.rs', '@elena.layers', TRUE);

-- =========================================
-- DISCIPLINE
-- =========================================
INSERT INTO Discipline (id_discipline, name) VALUES
(1, 'Painting'),
(2, 'Photography'),
(3, 'Sculpture'),
(4, 'Mixed Media'),
(5, 'Installation'),
(6, 'Textile Art');

-- =========================================
-- ARTIST_DISCIPLINE
-- =========================================
INSERT INTO Artist_Discipline (id_artist, id_discipline) VALUES
(1, 1), (1, 4),
(2, 2),
(3, 3), (3, 5),
(4, 1),
(5, 4), (5, 6);

-- =========================================
-- ARTWORKTAG
-- =========================================
INSERT INTO ArtworkTag (id_tag, name) VALUES
(1, 'Urban'),
(2, 'Night'),
(3, 'Industrial'),
(4, 'Organic'),
(5, 'Textile'),
(6, 'Color'),
(7, 'Memory'),
(8, 'Minimal'),
(9, 'Contemporary'),
(10, 'Light');

-- =========================================
-- ARTWORK
-- =========================================
INSERT INTO Artwork (id_artwork, title, creationYear, type, medium, dimensions, description, price, status, id_artist) VALUES
(1, 'Walls of Silence', 2022, 'Painting', 'Acrylic on canvas', '120x90 cm', 'Urban composition with muted colors', 1800.00, 'FOR_SALE', 1),
(2, 'Crosswalk Echoes', 2023, 'Painting', 'Acrylic and ink', '100x80 cm', 'Street scene with strong contrasts', 2100.00, 'EXHIBITED', 1),
(3, 'Steel Horizon', 2021, 'Photography', 'Digital print', '70x50 cm', 'Industrial landscape at sunrise', 950.00, 'FOR_SALE', 2),
(4, 'Concrete Breath', 2024, 'Photography', 'Black and white print', '60x40 cm', 'Series about abandoned urban spaces', 1100.00, 'EXHIBITED', 2),
(5, 'Seed Form', 2022, 'Sculpture', 'Resin', '45x30x28 cm', 'Sculpture inspired by plant structures', 2600.00, 'FOR_SALE', 3),
(6, 'Inner Shell', 2023, 'Sculpture', 'Bronze', '55x35x32 cm', 'Organic textured volume', 3200.00, 'EXHIBITED', 3),
(7, 'Midnight Balcony', 2021, 'Painting', 'Oil on canvas', '90x70 cm', 'Night view of an apartment building', 1700.00, 'FOR_SALE', 4),
(8, 'Blue Hour Windows', 2024, 'Painting', 'Oil and pastel', '110x90 cm', 'Interior scene at dusk', 2300.00, 'EXHIBITED', 4),
(9, 'Thread Map', 2023, 'Mixed Media', 'Fabric and paper collage', '80x80 cm', 'Textile-based memory composition', 1500.00, 'FOR_SALE', 5),
(10, 'Folded Memory', 2024, 'Mixed Media', 'Textile collage', '95x75 cm', 'Layered fabric storytelling piece', 1900.00, 'EXHIBITED', 5);

-- =========================================
-- ARTWORK_TAG
-- =========================================
INSERT INTO Artwork_Tag (id_artwork, id_tag) VALUES
(1, 1), (1, 6), (1, 9),
(2, 1), (2, 10),
(3, 3), (3, 8),
(4, 3), (4, 9),
(5, 4), (5, 8),
(6, 4), (6, 9),
(7, 2), (7, 9),
(8, 2), (8, 10),
(9, 5), (9, 7),
(10, 5), (10, 7), (10, 9);

-- =========================================
-- COMMUNITYMEMBER
-- =========================================
INSERT INTO CommunityMember (id_member, name, email, birthYear, phone, city, membershipType) VALUES
(1, 'Lina Moreau', 'lina.moreau@community.com', 1999, '+33-620000001', 'Paris', 'PREMIUM'),
(2, 'Karim Haddad', 'karim.haddad@community.com', 1995, '+33-620000002', 'Lyon', 'FREE'),
(3, 'Sofia Marin', 'sofia.marin@community.com', 2001, '+34-620000003', 'Madrid', 'PREMIUM'),
(4, 'Hugo Ferreira', 'hugo.ferreira@community.com', 1993, '+351-620000004', 'Porto', 'FREE'),
(5, 'Maya Ionescu', 'maya.ionescu@community.com', 1998, '+40-620000005', 'Bucharest', 'PREMIUM');

-- =========================================
-- MEMBER_DISCIPLINE
-- =========================================
INSERT INTO Member_Discipline (id_member, id_discipline) VALUES
(1, 1), (1, 4),
(2, 2), (2, 5),
(3, 1), (3, 3),
(4, 2), (4, 6),
(5, 4), (5, 6);

-- =========================================
-- GALLERY
-- =========================================
INSERT INTO Gallery (id_gallery, name, adress, ownerName, openingHours, contactPhone, rating, website) VALUES
(1, 'North Atelier', '12 Forge Street, Lille', 'Claire Denis', '10:00-18:00', '+33-700000001', 4.60, 'www.northatelier.fr'),
(2, 'Sienna House', '8 Verdi Passage, Paris', 'Julien Serra', '11:00-19:00', '+33-700000002', 4.80, 'www.siennahouse.fr'),
(3, 'Umbral Space', '24 Sun Street, Madrid', 'Lucia Herrera', '10:30-20:00', '+34-700000003', 4.70, 'www.umbralspace.es');

-- =========================================
-- EXHIBITION
-- =========================================
INSERT INTO Exhibition (id_exhibition, title, startDate, endDate, description, curatorName, theme, id_gallery) VALUES
(1, 'Fragments of the City', '2026-05-03', '2026-06-15', 'Exhibition about urban textures and rhythms', 'Camille Rocher', 'Urban Narratives', 2),
(2, 'Matter and Breath', '2026-05-20', '2026-07-10', 'Dialogue between sculpture and photography', 'Ruben Alvarez', 'Material Presence', 3),
(3, 'Soft Archives', '2026-06-01', '2026-07-25', 'Exploration of memory and textile art', 'Nina Petrov', 'Memory and Texture', 1);

-- =========================================
-- EXHIBITION_ARTWORK
-- =========================================
INSERT INTO Exhibition_Artwork (id_exhibition, id_artwork) VALUES
(1, 1), (1, 2), (1, 7), (1, 8),
(2, 3), (2, 4), (2, 5), (2, 6),
(3, 9), (3, 10), (3, 1);

-- =========================================
-- WORKSHOP
-- =========================================
INSERT INTO Workshop (id_workshop, title, dateWorkshop, durationMinutes, maxParticipants, price, location, description, level, id_artist) VALUES
(1, 'Urban Color Lab', '2026-06-05', 150, 16, 95.00, 'Paris Studio 4', 'Exploring color in urban painting', 'Beginner', 1),
(2, 'Industrial Photo Walk', '2026-06-08', 180, 14, 85.00, 'Lille Warehouse District', 'Photography walk in industrial areas', 'Intermediate', 2),
(3, 'Organic Forms in Clay', '2026-06-12', 200, 10, 120.00, 'Marseille Sculpture Room', 'Sculpting organic shapes in clay', 'Advanced', 3),
(4, 'Painting the Night', '2026-06-18', 160, 12, 110.00, 'Madrid Creative Hall', 'Capturing night scenes in painting', 'Intermediate', 4),
(5, 'Textile Collage Stories', '2026-06-22', 170, 15, 100.00, 'Belgrade Art House', 'Storytelling through textile collage', 'Beginner', 5);

-- =========================================
-- BOOKING
-- =========================================
INSERT INTO Booking (id_booking, bookingDate, paymentStatus, id_workshop, id_member) VALUES
(1, '2026-05-20', 'PAID', 1, 1),
(2, '2026-05-20', 'PAID', 1, 3),
(3, '2026-05-21', 'PENDING', 2, 2),
(4, '2026-05-21', 'PAID', 2, 4),
(5, '2026-05-22', 'PAID', 3, 3),
(6, '2026-05-22', 'CANCELLED', 3, 5),
(7, '2026-05-23', 'PAID', 4, 1),
(8, '2026-05-23', 'PAID', 4, 4),
(9, '2026-05-24', 'PENDING', 5, 2),
(10, '2026-05-24', 'PAID', 5, 5);

-- =========================================
-- REVIEW
-- =========================================
INSERT INTO Review (id_review, rating, comment, reviewDate, id_member, id_artwork) VALUES
(1, 5, 'Very strong composition and color work', '2026-05-10', 1, 1),
(2, 4, 'Great use of light and framing', '2026-05-11', 2, 3),
(3, 5, 'Impressive sculptural texture', '2026-05-12', 3, 6),
(4, 4, 'Beautiful night atmosphere', '2026-05-13', 4, 7),
(5, 5, 'Creative and emotional textile work', '2026-05-14', 5, 10);


-- ============================================================
-- TÂCHE 2 – VUES
-- ============================================================

-- -----------------------------------------------------------
-- VUE 1 : v_artist_overview
-- Objectif : Vue de synthèse publique sur les artistes actifs 
-- en masquant les données sensibles pour une question de sécurité.
-- -----------------------------------------------------------
CREATE OR REPLACE VIEW v_artist_overview AS
SELECT
    a.id_artist,
    a.name,
    a.city,
    a.birthYear,
    a.bio,
    a.website,
    a.socialMedia,
    GROUP_CONCAT(DISTINCT d.name ORDER BY d.name SEPARATOR ', ') AS disciplines
FROM Artist a
LEFT JOIN Artist_Discipline ad ON a.id_artist = ad.id_artist
LEFT JOIN Discipline d ON ad.id_discipline = d.id_discipline
WHERE a.isActive = TRUE
GROUP BY a.id_artist, a.name, a.city, a.birthYear, a.bio, a.website, a.socialMedia;

select * from v_artist_overview;

-- -----------------------------------------------------------
-- VUE 2 : v_artwork_catalog
-- Objectif : Catalogue complet des œuvres avec artiste et tags.
-- -----------------------------------------------------------
CREATE OR REPLACE VIEW v_artwork_catalog AS
SELECT
    aw.id_artwork,
    aw.title,
    aw.creationYear,
    aw.type,
    aw.medium,
    aw.dimensions,
    aw.description,
    aw.price,
    aw.status,
    a.name AS artist_name,
    a.city AS artist_city,
    GROUP_CONCAT(DISTINCT t.name ORDER BY t.name SEPARATOR ', ') AS tags
FROM Artwork aw
JOIN Artist a ON aw.id_artist  = a.id_artist
LEFT JOIN Artwork_Tag at2 ON aw.id_artwork = at2.id_artwork
LEFT JOIN ArtworkTag  t   ON at2.id_tag = t.id_tag
GROUP BY aw.id_artwork, aw.title, aw.creationYear, aw.type, aw.medium,
         aw.dimensions, aw.description, aw.price, aw.status,
         a.name, a.city;

select * from v_artwork_catalog;
select * from artwork;

-- -----------------------------------------------------------
-- VUE 3 : v_exhibition_program
-- Objectif : Programme des expositions avec galerie et oeuvres exposés
-- -----------------------------------------------------------
CREATE OR REPLACE VIEW v_exhibition_program AS
SELECT
    e.id_exhibition,
    e.title,
    e.startDate,
    e.endDate,
    e.theme,
    e.curatorName,
    e.description,
    g.name AS gallery_name,
    g.adress AS gallery_address,
    g.contactPhone AS gallery_phone,
    COUNT(ea.id_artwork) AS artwork_count
FROM Exhibition e
JOIN Gallery g ON e.id_gallery = g.id_gallery
LEFT JOIN Exhibition_Artwork ea ON e.id_exhibition = ea.id_exhibition
GROUP BY e.id_exhibition, e.title, e.startDate, e.endDate, e.theme,
         e.curatorName, e.description, g.name, g.adress, g.contactPhone;

select * from v_exhibition_program;
select * from exhibition;

-- -----------------------------------------------------------
-- VUE 4 : v_workshop_availability
-- Objectif : Ateliers avec places restantes calculées en temps réel
-- -----------------------------------------------------------
CREATE OR REPLACE VIEW v_workshop_availability AS
SELECT
    w.id_workshop,
    w.title,
    w.dateWorkshop,
    w.durationMinutes,
    w.maxParticipants,
    w.price,
    w.location,
    w.level,
    a.name AS artist_name,
    COUNT(b.id_booking) AS registered_count,
    (w.maxParticipants - COUNT(b.id_booking)) AS available_spots
FROM Workshop w
JOIN Artist a ON w.id_artist = a.id_artist
LEFT JOIN Booking b
    ON w.id_workshop = b.id_workshop
    AND b.paymentStatus <> 'Annulé'
GROUP BY w.id_workshop, w.title, w.dateWorkshop, w.durationMinutes,
         w.maxParticipants, w.price, w.location, w.level, a.name;

select * from v_workshop_availability;
-- -----------------------------------------------------------
-- VUE 5 : v_member_activity
-- Objectif : Résumé de l'activité de chaque membre avec 
-- le nb ateliers suivis, nb avis, montant total dépensé.
-- -----------------------------------------------------------
CREATE OR REPLACE VIEW v_member_activity AS
SELECT
    cm.id_member,
    cm.name,
    cm.city,
    cm.membershipType,
    COUNT(DISTINCT b.id_booking) AS workshops_booked,
    COUNT(DISTINCT r.id_review) AS reviews_written,
    COALESCE(SUM(CASE WHEN b.paymentStatus = 'Payé' THEN w.price ELSE 0 END), 0) AS total_spent
FROM CommunityMember cm
LEFT JOIN Booking b  ON cm.id_member = b.id_member
LEFT JOIN Workshop w ON b.id_workshop = w.id_workshop
LEFT JOIN Review r   ON cm.id_member = r.id_member
GROUP BY cm.id_member, cm.name, cm.city, cm.membershipType;

select * from v_member_activity;


-- ============================================================
-- TÂCHE 2 – INDEX
-- ============================================================

-- INDEX 1 : Artwork par statut
-- Pour de trouver rapidement ce qui est "Disponible".
CREATE INDEX idx_artwork_status ON Artwork(status);

-- INDEX 2 : Artwork par artiste
-- Pour retrouver toutes les œuvres d'un artiste en un clin d'œil.
CREATE INDEX idx_artwork_artist ON Artwork(id_artist);

-- INDEX 3 : Booking par workshop
-- Pour voir instantanément qui est inscrit à quel atelier.
CREATE INDEX idx_booking_workshop ON Booking(id_workshop);

-- INDEX 4 : Exhibition par dates
-- Pour identifier les expositions en cours selon le calendrier.
CREATE INDEX idx_exhibition_dates ON Exhibition(startDate, endDate);


-- ============================================================
-- TÂCHE 3 – TRIGGERS
-- ============================================================

DELIMITER //

-- -----------------------------------------------------------
-- TRIGGER 1 : trg_booking_check_capacity
-- S'active avant l'insertion d'une réservation.
-- Ce trigger permet d'empêcher de dépasser 
-- la capacité maximale d'un atelier. 
-- -----------------------------------------------------------
CREATE TRIGGER trg_booking_check_capacity
BEFORE INSERT ON Booking
FOR EACH ROW
BEGIN
    DECLARE v_max   INT;
    DECLARE v_count INT;

    SELECT maxParticipants INTO v_max
    FROM Workshop WHERE id_workshop = NEW.id_workshop;

    SELECT COUNT(*) INTO v_count
    FROM Booking
    WHERE id_workshop = NEW.id_workshop
      AND paymentStatus <> 'Annulé';

    IF v_count >= v_max THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Atelier complet : capacité maximale atteinte.';
    END IF;
END //

-- -----------------------------------------------------------
-- TRIGGER 2 : trg_exhibition_date_check
-- Quand : AVANT insertion ET mise à jour d'une exposition.
-- Pourquoi : Garantit que la date de fin est toujours
-- postérieure à la date de début (cohérence des données).
-- -----------------------------------------------------------
CREATE TRIGGER trg_exhibition_date_check_insert
BEFORE INSERT ON Exhibition
FOR EACH ROW
BEGIN
    IF NEW.endDate <= NEW.startDate THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La date de fin doit être postérieure à la date de début.';
    END IF;
END //

CREATE TRIGGER trg_exhibition_date_check_update
BEFORE UPDATE ON Exhibition
FOR EACH ROW
BEGIN
    IF NEW.endDate <= NEW.startDate THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La date de fin doit être postérieure à la date de début.';
    END IF;
END //


-- -----------------------------------------------------------
-- TRIGGER 3 : trg_review_rating_range
-- S'active avant l'insertion d'un avis.
-- Permet de contraidre la note entre 1 et 5 étoiles.
-- -----------------------------------------------------------
CREATE TRIGGER trg_review_rating_range
BEFORE INSERT ON Review
FOR EACH ROW
BEGIN
    IF NEW.rating < 1 OR NEW.rating > 5 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La note doit être comprise entre 1 et 5.';
    END IF;
END //

DELIMITER ;



-- ============================================================
-- TÂCHE 4 – PROCEDURES & FUNCTIONS
-- ============================================================

DELIMITER //

-- -----------------------------------------------------------
-- FUNCTION 1 : get_participants_count
-- Permet de voir le nombre de participants à un évènement
-- -----------------------------------------------------------
CREATE FUNCTION get_participants_count(p_id_workshop INT) RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE nb INT;
    SELECT COUNT(*) INTO nb FROM Booking WHERE id_workshop = p_id_workshop;
    RETURN nb;
END //


-- -----------------------------------------------------------
-- PROCEDURE 2 : create_workshop
-- Créer un workshop
-- -----------------------------------------------------------
CREATE PROCEDURE create_workshop(
    IN p_id INT, IN p_title VARCHAR(50), IN p_date DATE,
    IN p_duration INT, IN p_max INT, IN p_price DECIMAL(15,2),
    IN p_loc VARCHAR(50), IN p_desc VARCHAR(50), IN p_level VARCHAR(50), IN p_artist INT
)
BEGIN
    INSERT INTO Workshop VALUES (p_id, p_title, p_date, p_duration, p_max, p_price, p_loc, p_desc, p_level, p_artist);
END //


-- -----------------------------------------------------------
-- PROCEDURE 3 : get_artists_by_city
-- artistes par ville
-- -----------------------------------------------------------
CREATE PROCEDURE get_artists_by_city(IN p_city VARCHAR(50))
BEGIN
    SELECT a.name, GROUP_CONCAT(d.name SEPARATOR ', ') AS disciplines
    FROM Artist a
    JOIN Artist_Discipline ad ON a.id_artist = ad.id_artist
    JOIN Discipline d ON ad.id_discipline = d.id_discipline
    WHERE a.city = p_city AND a.isActive = TRUE
    GROUP BY a.id_artist;
END //
DELIMITER ;




-- ============================================================
-- TÂCHE 5 – TRANSACTIONS
-- ============================================================

-- Scénario : Un membre s'inscrit à un parcours 
-- composé de deux ateliers. L'opération doit être atomique.

DELIMITER //

CREATE PROCEDURE sp_register_for_art_path(
    IN p_member_id INT,
    IN p_workshop1_id INT,
    IN p_workshop2_id INT
)
BEGIN
    -- Déclaration d'un gestionnaire d'erreur pour annuler la transaction en cas de problème
    -- (Par exemple si un trigger lève une exception "Atelier complet")
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- En cas d'erreur, on annule tout
        ROLLBACK;
        SELECT 'ERREUR : Échec de l''inscription au parcours. L''un des ateliers est peut-être complet ou inexistant. Aucune réservation n''a été effectuée.' AS Message;
    END;

    -- Début de la transaction
    START TRANSACTION;

        -- 1. Première réservation (Urban Color Lab)
        INSERT INTO Booking (id_booking, bookingDate, paymentStatus, id_workshop, id_member)
        VALUES ((SELECT COALESCE(MAX(id_booking), 0) + 1 FROM Booking b), CURDATE(), 'PENDING', p_workshop1_id, p_member_id);

        -- 2. Deuxième réservation (Industrial Photo Walk)
        INSERT INTO Booking (id_booking, bookingDate, paymentStatus, id_workshop, id_member)
        VALUES ((SELECT COALESCE(MAX(id_booking), 0) + 1 FROM Booking b), CURDATE(), 'PENDING', p_workshop2_id, p_member_id);

    -- Si tout s'est bien passé, on valide les changements
    COMMIT;
    
    SELECT 'SUCCÈS : Le membre a été inscrit avec succès aux deux ateliers du parcours.' AS Message;

END //

DELIMITER ;

-- ============================================================
-- TEST DE LA TRANSACTION
-- ============================================================

-- Test 1 : Inscription réussie au parcours (Ateliers 1 et 2 pour le membre 2)
CALL sp_register_for_art_path(2, 1, 2);

-- Vérification
SELECT * FROM v_member_activity WHERE id_member = 2;