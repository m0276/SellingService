CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      user_name VARCHAR(255),
                      password VARCHAR(225),
                      email VARCHAR(255) UNIQUE,
                      provider VARCHAR(225),
                      provider_id VARCHAR(225)
);

CREATE TABLE favorite (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      market_cd VARCHAR(255),
                      product_cd VARCHAR(225),
                      time VARCHAR(255),
                      price VARCHAR(225),
                      user_id BIGINT,
                      FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);