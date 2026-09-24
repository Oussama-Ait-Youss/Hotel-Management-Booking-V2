-- 2. Insertion des Utilisateurs
-- Passwords are hashed with PBKDF2WithHmacSHA256
-- Iterations: 600000
-- Key length: 256 bits
-- Salt: 16 random bytes, Base64 encoded
--
-- Demo credentials:
-- ADMIN:
--   email    = admin@hotel-almadar.com
--   password = Admin@123
--
-- CLIENTS:
--   password = Client@123

INSERT INTO users (
    first_name,
    last_name,
    email,
    password_hash,
    salt,
    role
) VALUES
      (
          'Oussama',
          'Ait Youss',
          'admin@hotel-almadar.com',
          'tJ97zwT93G3IQUAcNrvg9gub91Me+Di5AfuII5OuHm0=',
          'szS0/jjLP8h3kF9nj/JX0g==',
          'ADMIN'
      ),
      (
          'Ahmed',
          'Benali',
          'ahmed.benali@example.com',
          'f+RxP6N9CZEofquOoNXdrORBO19449skdB7zSGdCsy0=',
          'MU6Y6sd4nsOtHZEibiXL0A==',
          'CLIENT'
      ),
      (
          'Fatima',
          'Zahra',
          'fatima.z@example.com',
          'Q/LewmRRqx0Bai50mEUn9Q1r7tgsJE0J47VTHgSE1YM=',
          'DexJEp31outtt8o3T/lZiw==',
          'CLIENT'
      ),
      (
          'Karim',
          'Tazi',
          'karim.tazi@example.com',
          'ncRt94nJIEYF5f+MYxt1m+PoBSEiGjtWu2mI4x4kexo=',
          'Mh8ulyLB6DJMhrF7K2xvrw==',
          'CLIENT'
      ),
      (
          'John',
          'Doe',
          'john.doe@example.com',
          'rjME5kelA3AmuXhvDtv5n3sV5ORbVlCTPDFCvwxR0T0=',
          'H9jjoe5D4u2h17R5+b4blQ==',
          'CLIENT'
      ),
      (
          'Sarah',
          'Connor',
          'sarah.c@example.com',
          'm3sooTJc/TsDVY0Yod0MFqPlSJQHnt8hOG86to7osL4=',
          '8gHyn3ewtGo4fWy7XcjpvA==',
          'CLIENT'
      ),
      (
          'Youssef',
          'Alaoui',
          'youssef.a@example.com',
          'v2/s4zE/5I3VHcXbh8hYIDfPz5M6wEOrIjlGqFFdkeo=',
          'RiUYOtoXyAiINNnp53dIIA==',
          'CLIENT'
      ),
      (
          'Leila',
          'Mernissi',
          'leila.m@example.com',
          'kSCD07Jl+zRtZlOE7rDVL6LI+QWODppHScEkJd8oML4=',
          'ZEbuXzZdO8CqBiPa+2QNqg==',
          'CLIENT'
      ),
      (
          'Omar',
          'Chraibi',
          'omar.c@example.com',
          '313oQrgDkBAIskGRPsqqwlwhxQtwUQ8UeYJGyY33xww=',
          'XJFgx6+wCvrr8hzSVdkDsA==',
          'CLIENT'
      ),
      (
          'Nadia',
          'Bennis',
          'nadia.b@example.com',
          '0A2YT29oKMV09Yvqkyp4kt3QY/pYTlE0s/buACZQGDE=',
          '38cx+HU3/+ZipmN5ybBe0g==',
          'CLIENT'
      ),
      (
          'Amine',
          'Kabbaj',
          'amine.k@example.com',
          'QGT64rn63R76i3/2Mup/1lwRnDK17N4vBX6MRt21Bdo=',
          'L6zUt/9gRk9wu8+kQGSAfA==',
          'CLIENT'
      ),
      (
          'Rachid',
          'El Fassi',
          'rachid.e@example.com',
          'zD8sKwYHGQv9OociEQcVVctBRSpga0stQE8QC42qW9Y=',
          'VlKKEu7XsZiIyk+2LYY5lQ==',
          'CLIENT'
      ),
      (
          'Maria',
          'Garcia',
          'maria.g@example.com',
          'NSBkOcc+Iw1bisYsnoJfj+LCljc0xBPAxLiCdit5evY=',
          '3P+RopGMYNfYi9nCs3rphQ==',
          'CLIENT'
      ),
      (
          'Kenza',
          'Tahiri',
          'kenza.t@example.com',
          'ikjQKQJ0ilACIiL+BR2ZJcG98lNGl5KjxQMl5NbAns8=',
          'wHvOnlgpaB7uo6/a+Xzy4g==',
          'CLIENT'
      ),
      (
          'Hassan',
          'Bennani',
          'hassan.b@example.com',
          'juYe+qEa7iOeJuaFeVmQIn0SVV2Gp31KT8ym2zqOk1g=',
          'mDktRmQh7ZeJ0pRbQFlb1A==',
          'CLIENT'
      );