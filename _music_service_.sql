-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 14, 2020 at 05:03 PM
-- Server version: 5.5.25
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `music_service`
--

-- --------------------------------------------------------

--
-- Table structure for table `album`
--

CREATE TABLE IF NOT EXISTS `album` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `creationDate` varchar(255) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `album`
--

INSERT INTO `album` (`id`, `name`, `creationDate`, `author_id`, `group_id`) VALUES
(1, 'The Last Stand', '2016 08 19', NULL, 1),
(3, 'Kill em All', '1983 07 25', NULL, 2),
(4, 'That''s Life', '1966 01 01', 3, NULL),
(5, 'Never gonna give you up', '1987 07 27', 4, NULL),
(6, 'Wish you were here', '1975 09 12', NULL, 3);

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE IF NOT EXISTS `author` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `birthday` varchar(255) DEFAULT NULL,
  `dateOfDeath` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `author`
--

INSERT INTO `author` (`id`, `name`, `birthday`, `dateOfDeath`, `description`, `group_id`) VALUES
(1, 'James Alan Hetfield', '1963 08 03', NULL, 'американский рок-музыкант; вокалист и ритм-гитарист метал-группы Metallica.', 2),
(2, 'Joakim Broden', '1980 10 05', NULL, ' шведско-чешский музыкант, вокалист, фронтмен, автор текстов песен и музыки шведской хэви-пауэр-метал-группы Sabaton.', 1),
(3, 'Frank Sinatra', '1915 12 12', '1998 05 14', 'американский киноактёр, кинорежиссёр, продюсер, шоумен, певец (крунер). Одиннадцать раз становился лауреатом премии «Грэмми»', NULL),
(4, 'Rick Astley', '1966 02 06', NULL, 'британский исполнитель танцевальной музыки 1980-х годов, баритон.', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `author_group`
--

CREATE TABLE IF NOT EXISTS `author_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author_id` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `category_genre`
--

CREATE TABLE IF NOT EXISTS `category_genre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `genre_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `genre`
--

CREATE TABLE IF NOT EXISTS `genre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `genre`
--

INSERT INTO `genre` (`id`, `name`) VALUES
(1, 'Trash Metall'),
(2, 'Power Metall'),
(3, 'Pop'),
(4, 'Art Rock');

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

CREATE TABLE IF NOT EXISTS `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `creationDate` varchar(255) DEFAULT NULL,
  `dateOfDestroy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`id`, `name`, `creationDate`, `dateOfDestroy`) VALUES
(1, 'Sabaton', '1999 12 01', NULL),
(2, 'Metallica', '1981 01 01', NULL),
(3, 'Pink Floyd', '1965 01 01', '2015 01 01');

-- --------------------------------------------------------

--
-- Table structure for table `music`
--

CREATE TABLE IF NOT EXISTS `music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `genre_id` int(11) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `album_id` int(11) DEFAULT NULL,
  `creationDate` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `music`
--

INSERT INTO `music` (`id`, `name`, `genre_id`, `author_id`, `group_id`, `album_id`, `creationDate`, `content`) VALUES
(1, 'Motorbreath', 1, NULL, 2, 3, '1983 07 25', 'C:\\music files\\Metallica - Motorbreath.mp3'),
(2, 'Rorke''s Drift', 2, NULL, 1, 1, '2016 08 19', 'C:\\music files\\Sabaton - Rorke''s Drift.mp3'),
(3, 'that''s Life', 3, 3, NULL, 4, '1966 01 01', 'C:\\music files\\Frank Sinatra - That''s Life.mp3'),
(4, 'Never gonna give you up', 3, 4, NULL, 5, '1987 07 27', 'C:\\music files\\Astley - Never gonna give you up.mp3'),
(5, 'Have a Cigar', 4, NULL, 3, 6, '1975 11 15', 'C:\\music files\\Pink Floyd - Have a Cigar.mp3');

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE IF NOT EXISTS `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `tag`
--

INSERT INTO `tag` (`id`, `name`, `category_id`) VALUES
(1, 'brutal', 1),
(2, 'historical', 1),
(3, 'screaming', 1),
(4, 'joker', 2),
(5, 'relaxing', 2);

-- --------------------------------------------------------

--
-- Table structure for table `tag_album`
--

CREATE TABLE IF NOT EXISTS `tag_album` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_id` int(11) DEFAULT NULL,
  `album_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `tag_album`
--

INSERT INTO `tag_album` (`id`, `tag_id`, `album_id`) VALUES
(1, 1, 1),
(6, 2, 1),
(7, 1, 3),
(8, 4, 4),
(9, 3, 3),
(10, 5, 5),
(11, 5, 6);

-- --------------------------------------------------------

--
-- Table structure for table `tag_category`
--

CREATE TABLE IF NOT EXISTS `tag_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tag_category`
--

INSERT INTO `tag_category` (`id`, `name`) VALUES
(1, 'Heavy'),
(2, 'calm');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `regDate` varchar(255) DEFAULT NULL,
  `role` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `surname`, `email`, `password`, `regDate`, `role`) VALUES
(1, 'admin', 'admin', 'admin', '$2a$12$D7nqoRy5VP4D0ywN36VJuOcgvFGWWAOMDDlZzCBBZo5ppFkLi/3K.', '2020 03 08', 'ADMIN'),
(2, 'Bogdan', 'Pronin', 'pronin@gmail.com', '$2a$12$ijfK3sEdjx3YprBP9rEmoeOFWrvGxmEzlPiyXN4kgB2aCEMby0nyS', '2020 03 08', 'USER'),
(3, 'Farukh', 'Iminoov', 'faraday@gmail.com', '$2a$12$I3VxAzJir03ICNhpNQ/VIOU627rvxtv2wxYv6RlnkVTmKljfHKIXG', NULL, 'USER');

-- --------------------------------------------------------

--
-- Table structure for table `user_music`
--

CREATE TABLE IF NOT EXISTS `user_music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `music_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `user_music`
--

INSERT INTO `user_music` (`id`, `music_id`, `user_id`) VALUES
(2, 1, 1),
(3, 3, 1),
(4, 4, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
