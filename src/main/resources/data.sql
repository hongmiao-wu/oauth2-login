CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS permission (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INT NOT NULL,
    permissions_id INT NOT NULL,
    PRIMARY KEY (role_id, permissions_id),
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    FOREIGN KEY (permissions_id) REFERENCES permission(id) ON DELETE CASCADE
);

INSERT IGNORE INTO role (name) 
VALUES 
	('ROLE_ADMIN'),
    ('ROLE_STAFF'),
	('ROLE_USER');

INSERT IGNORE INTO permission (name)
VALUES
	('PRODUCT_CREATE'),
	('PRODUCT_READ'),
	('PRODUCT_READALL'),
	('PRODUCT_UPDATE'),
	('PRODUCT_DELETE');
	
INSERT IGNORE INTO role_permissions (role_id, permissions_id)
VALUES
	(1,1), (1,2), (1,3), (1,4), (1,5),
	(2,1), (2,2), (2,3), (2,4),
	(3,2), (3,3);