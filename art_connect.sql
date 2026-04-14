CREATE DATABASE IF NOT EXISTS art_connect;
USE art_connect;

DROP TABLE IF EXISTS Artist_Discipline;
DROP TABLE IF EXISTS Exhibition_Artwork;
DROP TABLE IF EXISTS Artwork_Tag;
DROP TABLE IF EXISTS Member_Discipline;
DROP TABLE IF EXISTS Discipline;
DROP TABLE IF EXISTS Workshop;
DROP TABLE IF EXISTS Review;
DROP TABLE IF EXISTS Exhibition;
DROP TABLE IF EXISTS Artwork;
DROP TABLE IF EXISTS Artist;
DROP TABLE IF EXISTS ArtworkTag;
DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS CommunityMember;
DROP TABLE IF EXISTS Gallery;


CREATE TABLE Artist(
   id_artist INT,
   name VARCHAR(50),
   birthYear INT,
   bio VARCHAR(50),
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
   description VARCHAR(50),
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
   description VARCHAR(50),
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
   description VARCHAR(50),
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
