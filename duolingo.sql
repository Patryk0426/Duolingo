-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sty 28, 2025 at 07:48 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `duolingo`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `answers`
--

CREATE TABLE `answers` (
  `id_answer` int(11) NOT NULL,
  `id_question` int(11) NOT NULL,
  `answer` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `answers`
--

INSERT INTO `answers` (`id_answer`, `id_question`, `answer`) VALUES
(1, 1, 'cat'),
(2, 2, 'dog'),
(3, 3, 'I would like a coffee'),
(4, 4, 'Where is the nearest shop?'),
(5, 5, 'railway station'),
(6, 6, 'petrol station'),
(7, 7, 'order food'),
(8, 8, 'table'),
(9, 9, 'bread'),
(10, 10, 'chair'),
(11, 11, 'school'),
(12, 12, 'Nice to meet you'),
(13, 13, 'window'),
(14, 14, '\r\nbicycle'),
(15, 15, 'friend');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `languages`
--

CREATE TABLE `languages` (
  `id_language` int(11) NOT NULL,
  `language` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `languages`
--

INSERT INTO `languages` (`id_language`, `language`) VALUES
(1, 'English'),
(2, 'Spanish'),
(3, 'Japanese');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `questions`
--

CREATE TABLE `questions` (
  `id_question` int(11) NOT NULL,
  `question` text NOT NULL,
  `id_language` int(11) NOT NULL,
  `id_stage` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `questions`
--

INSERT INTO `questions` (`id_question`, `question`, `id_language`, `id_stage`) VALUES
(1, 'Jak przetłumaczyć \"kot\" na angielski?', 1, 1),
(2, 'Jak przetłumaczyć \"pies\" na angielski?', 1, 1),
(3, 'Jak powiedzieć \"Chciałbym kawę\" po angielsku?', 1, 2),
(4, 'Jak przetłumaczyć \"Gdzie jest najbliższy sklep?\" na angielski?', 1, 2),
(5, 'Jak jest „dworzec kolejowy” po angielsku?', 1, 2),
(6, 'Jak jest „stacja benzynowa” po angielsku?', 1, 2),
(7, 'Jak jest „zamawiać jedzenie” po angielsku?', 1, 2),
(8, 'Jak jest „stół” po angielsku?', 1, 1),
(9, 'Jak jest „chleb” po angielsku?', 1, 1),
(10, 'Jak jest „krzesło” po angielsku?', 1, 1),
(11, 'Jak jest „szkoła” po angielsku?', 1, 1),
(12, 'Jak powiedzieć „miło cię poznać” po angielsku?\r\n', 1, 1),
(13, 'Jak jest „okno” po angielsku?', 1, 1),
(14, 'Jak jest „rower” po angielsku?', 1, 1),
(15, 'Jak jest „przyjaciel” po angielsku?', 1, 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `stages`
--

CREATE TABLE `stages` (
  `id_stage` int(11) NOT NULL,
  `stage_name` varchar(255) NOT NULL,
  `id_language` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stages`
--

INSERT INTO `stages` (`id_stage`, `stage_name`, `id_language`) VALUES
(1, 'English_Beginner', 1),
(2, 'English_Intermediate', 1),
(3, 'Spanish_Beginner', 2),
(4, 'Spanish_Intermediate', 2),
(5, 'Japanese_Beginner', 3),
(6, 'Japanese_Intermediate', 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `id_user` bigint(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `type` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `name`, `login`, `password`, `type`) VALUES
(1, 'admin', 'admin', 'A1s2d3f4', 1),
(2, 'darek', 'smo', 'smo', 2),
(3, 'a', 'a', 'a', 1),
(4, 'f', 'f', 'f', 2),
(5, 'essa', 'teresa', '123', 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user_progress`
--

CREATE TABLE `user_progress` (
  `id_progress` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_question` int(11) NOT NULL,
  `id_stage` int(11) NOT NULL,
  `is_correct` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user_type`
--

CREATE TABLE `user_type` (
  `Id` int(11) NOT NULL,
  `Name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_type`
--

INSERT INTO `user_type` (`Id`, `Name`) VALUES
(1, 'Admin'),
(2, 'User');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `answers`
--
ALTER TABLE `answers`
  ADD PRIMARY KEY (`id_answer`),
  ADD KEY `id_question` (`id_question`);

--
-- Indeksy dla tabeli `languages`
--
ALTER TABLE `languages`
  ADD PRIMARY KEY (`id_language`);

--
-- Indeksy dla tabeli `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`id_question`),
  ADD KEY `id_language` (`id_language`),
  ADD KEY `id_stage` (`id_stage`);

--
-- Indeksy dla tabeli `stages`
--
ALTER TABLE `stages`
  ADD PRIMARY KEY (`id_stage`),
  ADD KEY `id_language` (`id_language`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `login` (`login`);

--
-- Indeksy dla tabeli `user_progress`
--
ALTER TABLE `user_progress`
  ADD PRIMARY KEY (`id_progress`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_question` (`id_question`),
  ADD KEY `id_stage` (`id_stage`);

--
-- Indeksy dla tabeli `user_type`
--
ALTER TABLE `user_type`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `answers`
--
ALTER TABLE `answers`
  MODIFY `id_answer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `languages`
--
ALTER TABLE `languages`
  MODIFY `id_language` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `id_question` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `stages`
--
ALTER TABLE `stages`
  MODIFY `id_stage` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `answers`
--
ALTER TABLE `answers`
  ADD CONSTRAINT `answers_ibfk_1` FOREIGN KEY (`id_question`) REFERENCES `questions` (`id_question`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
