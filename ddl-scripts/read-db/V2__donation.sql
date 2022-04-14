CREATE TABLE IF NOT EXISTS donation(
  id SERIAL PRIMARY KEY,
  datetime BIGINT NOT NULL,
  amount DECIMAL(18, 8) NOT NULL
);
CREATE INDEX donation_datetime ON donation(datetime)