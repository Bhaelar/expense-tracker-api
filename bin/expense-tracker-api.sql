-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 02, 2022 at 05:55 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `expense-tracker-api`
--

-- --------------------------------------------------------

--
-- Table structure for table `et_categories`
--

CREATE TABLE `et_categories` (
  `category_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(20) NOT NULL,
  `description` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `et_transactions`
--

CREATE TABLE `et_transactions` (
  `transaction_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `note` varchar(50) NOT NULL,
  `transaction_date` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `et_users`
--

CREATE TABLE `et_users` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `et_users`
--

INSERT INTO `et_users` (`user_id`, `first_name`, `last_name`, `email`, `password`) VALUES
(21, 'Basit', 'Okenla', 'basit@gmail.com', '$2a$10$Sp1Gtk2jgrbDxP1XWKZh9.4/rKzu7Z1NWqyK/Oo6G5bLJXvoiLbMm');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `et_categories`
--
ALTER TABLE `et_categories`
  ADD PRIMARY KEY (`category_id`),
  ADD KEY `cat_users_fk` (`user_id`);

--
-- Indexes for table `et_transactions`
--
ALTER TABLE `et_transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `trans_cat_fk` (`category_id`),
  ADD KEY `trans_users_fk` (`user_id`);

--
-- Indexes for table `et_users`
--
ALTER TABLE `et_users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `et_categories`
--
ALTER TABLE `et_categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `et_transactions`
--
ALTER TABLE `et_transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1000;

--
-- AUTO_INCREMENT for table `et_users`
--
ALTER TABLE `et_users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `et_categories`
--
ALTER TABLE `et_categories`
  ADD CONSTRAINT `cat_users_fk` FOREIGN KEY (`user_id`) REFERENCES `et_users` (`user_id`);

--
-- Constraints for table `et_transactions`
--
ALTER TABLE `et_transactions`
  ADD CONSTRAINT `trans_cat_fk` FOREIGN KEY (`category_id`) REFERENCES `et_categories` (`category_id`),
  ADD CONSTRAINT `trans_users_fk` FOREIGN KEY (`user_id`) REFERENCES `et_users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
