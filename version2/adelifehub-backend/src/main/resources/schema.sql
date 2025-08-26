CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  email VARCHAR(128) UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  nickname VARCHAR(64),
  avatar_url VARCHAR(255),
  phone VARCHAR(32),
  roles_json VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS posts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  images_json JSON NULL,
  author_id BIGINT NOT NULL,
  likes INT NOT NULL DEFAULT 0,
  status ENUM('active','archived','deleted') NOT NULL DEFAULT 'active',
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL, deleted_by BIGINT NULL, delete_reason VARCHAR(100) NULL,
  INDEX idx_posts_status_created (status, created_at DESC),
  FOREIGN KEY (author_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS listings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type ENUM('rental','job','sale') NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  price DOUBLE, price_unit VARCHAR(32),
  location VARCHAR(120), latitude DOUBLE, longitude DOUBLE,
  images_json JSON NULL,
  status ENUM('active','archived','closed','deleted') NOT NULL DEFAULT 'active',
  author_id BIGINT NOT NULL,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL, deleted_by BIGINT NULL, delete_reason VARCHAR(100) NULL,
  INDEX idx_listings_type_status_created (type, status, created_at DESC),
  FOREIGN KEY (author_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS rental_details (
  listing_id BIGINT PRIMARY KEY,
  bedrooms INT, bathrooms INT, parking INT, furnished BOOLEAN, bond INT, available_from DATE, lease_term VARCHAR(64),
  FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS job_details (
  listing_id BIGINT PRIMARY KEY,
  company VARCHAR(120), job_type ENUM('fulltime','parttime','casual','intern'),
  salary_min DOUBLE, salary_max DOUBLE, salary_unit VARCHAR(32), remote BOOLEAN, visa_required BOOLEAN,
  FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS comments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  target_type ENUM('listing','post') NOT NULL, target_id BIGINT NOT NULL,
  author_id BIGINT NOT NULL, content TEXT NOT NULL,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP, edited_at TIMESTAMP NULL, deleted_at TIMESTAMP NULL,
  INDEX idx_comments_target (target_type, target_id, created_at DESC), FOREIGN KEY (author_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS listing_favorites (
  user_id BIGINT NOT NULL, listing_id BIGINT NOT NULL, created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(user_id, listing_id), FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE, FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS post_favorites (
  user_id BIGINT NOT NULL, post_id BIGINT NOT NULL, created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(user_id, post_id), FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE, FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS reports (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  target_type ENUM('listing','post') NOT NULL, target_id BIGINT NOT NULL,
  reporter_id BIGINT NOT NULL, reason_code ENUM('spam','scam','hate','adult','illegal','other') NOT NULL, reason_text VARCHAR(255),
  status ENUM('pending','resolved','rejected') NOT NULL DEFAULT 'pending', created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_reports_status_created (status, created_at DESC), FOREIGN KEY (reporter_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS moderation_notes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  target_type ENUM('listing','post') NOT NULL, target_id BIGINT NOT NULL, actor_id BIGINT NOT NULL, note VARCHAR(255),
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP, INDEX idx_notes_target_created (target_type, target_id, created_at DESC),
  FOREIGN KEY (actor_id) REFERENCES users(id)
);
