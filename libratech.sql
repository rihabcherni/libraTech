-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 31, 2024 at 05:58 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `libratech`
--

-- --------------------------------------------------------

--
-- Table structure for table `details_emprunt`
--

CREATE TABLE `details_emprunt` (
  `details_emprunts_id` int(11) NOT NULL,
  `lecteur_cin` int(11) NOT NULL,
  `livre_id` int(11) NOT NULL,
  `Date_Emprunt` date DEFAULT NULL,
  `Date_Retour_Prevu` date DEFAULT NULL,
  `date_retour_reel` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `details_emprunt`
--

INSERT INTO `details_emprunt` (`details_emprunts_id`, `lecteur_cin`, `livre_id`, `Date_Emprunt`, `Date_Retour_Prevu`, `date_retour_reel`) VALUES
(1, 12345678, 1, '2024-02-01', '2024-02-03', '2024-02-07'),
(3, 23456789, 2, '2024-02-01', '2024-02-11', '2024-02-25'),
(4, 12345678, 4, '2024-01-07', '2024-01-12', '2024-02-07'),
(5, 31245679, 7, '2024-01-07', '2024-01-15', '2024-02-25'),
(7, 12345678, 1, '2024-02-11', '2024-02-21', '2024-02-25'),
(8, 12345678, 1, '2024-01-22', '2024-01-25', '2024-02-01'),
(9, 12345678, 2, '2024-01-01', '2024-01-10', '2024-01-11'),
(10, 12345678, 2, '2024-01-01', '2024-01-10', '2024-02-11'),
(11, 12345678, 2, '2024-01-01', '2024-01-10', '2024-01-11'),
(12, 12345678, 2, '2024-01-01', '2024-01-10', '2024-01-01'),
(13, 12345678, 2, '2024-01-01', '2024-01-10', NULL),
(14, 12345678, 2, '2024-01-01', '2024-01-10', NULL),
(15, 12345678, 1, '2024-01-24', '2024-02-02', '2024-03-10'),
(16, 12345678, 2, '2024-01-24', '2024-02-01', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `employe`
--

CREATE TABLE `employe` (
  `employe_id` int(11) NOT NULL,
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `mot_de_passe` varchar(30) NOT NULL,
  `poste` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `employe`
--

INSERT INTO `employe` (`employe_id`, `nom`, `prenom`, `email`, `phone`, `mot_de_passe`, `poste`) VALUES
(1, 'admin', 'admin', 'admin@gmail.com', '23720548', 'a123', 'administrateur'),
(2, 'Cherni', 'Rihab', 'rihabcherni25@gmail.com', '25361458', 'r123', 'Assistant'),
(3, 'Cherni', 'Arij', 'arij@gmail.com', '21548796', '4545', 'Librarian'),
(4, 'Cherni', 'Rania', 'rania@gmail.com', '21547965', 'r123', 'Manager'),
(5, 'molka', 'elloumi', 'molka@gmail.com', '21574522', 'm123', 'Librarian');

-- --------------------------------------------------------

--
-- Table structure for table `etagere`
--

CREATE TABLE `etagere` (
  `etagere_id` int(11) NOT NULL,
  `Nom_Etage` varchar(50) DEFAULT NULL,
  `capacite` int(11) NOT NULL,
  `quantite_instantanee` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `etagere`
--

INSERT INTO `etagere` (`etagere_id`, `Nom_Etage`, `capacite`, `quantite_instantanee`) VALUES
(1, 'Etage_1', 9, 9),
(2, 'Etage_2', 5, 4),
(3, 'Etage_3', 10, 3),
(4, 'Etage_4', 20, 0);

-- --------------------------------------------------------

--
-- Table structure for table `lecteur`
--

CREATE TABLE `lecteur` (
  `cin` int(11) NOT NULL,
  `nom` varchar(30) DEFAULT NULL,
  `prenom` varchar(30) DEFAULT NULL,
  `Adresse` varchar(30) DEFAULT NULL,
  `Email` varchar(30) DEFAULT NULL,
  `Date_Inscription` date DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `Date_Naissance` date DEFAULT NULL,
  `genre` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `lecteur`
--

INSERT INTO `lecteur` (`cin`, `nom`, `prenom`, `Adresse`, `Email`, `Date_Inscription`, `phone`, `Date_Naissance`, `genre`) VALUES
(12345678, 'Ben Ali', 'Amel', 'Avenue Habib Bourguiba, Tunis', 'amel.benali0@example.com', '2024-01-01', '21541244', '2000-01-01', 'féminin'),
(12345679, 'Gharbi', 'Mehdi', 'Rue Mohamed V, Sfax', 'mehdi.gharbi@example.com', '2022-02-15', '22147859', '2001-11-07', 'masculin'),
(23456789, 'Trabelsi', 'Sarra', 'Avenue Habib Thameur, Sousse', 'sarra.trabelsi@example.com', '2021-03-10', '25417806', '2020-05-07', 'féminin'),
(31245679, 'Bouzidi', 'Omar', 'Boulevard 7 Novembre, Monastir', 'omar.bouzidi@example.com', '2022-04-20', '25147965', '2002-01-07', 'masculin');

-- --------------------------------------------------------

--
-- Table structure for table `livre`
--

CREATE TABLE `livre` (
  `code` int(20) NOT NULL,
  `etagere_id` int(11) NOT NULL,
  `titre` varchar(30) NOT NULL,
  `auteur` varchar(30) NOT NULL,
  `codeISBN` int(10) NOT NULL,
  `categorie` varchar(30) NOT NULL,
  `Disponibilite` tinyint(1) DEFAULT NULL,
  `Date_ajout` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `livre`
--

INSERT INTO `livre` (`code`, `etagere_id`, `titre`, `auteur`, `codeISBN`, `categorie`, `Disponibilite`, `Date_ajout`) VALUES
(1, 1, 'Stupeur et tremblement', 'Nathalie Nothomb', 452154215, 'Romantique', 1, '2024-01-19'),
(2, 1, 'Les misérables', 'Victor Hugo', 154125421, 'Romantique', 0, '2024-01-09'),
(3, 3, 'Le mystère de la vieille bibli', 'Agatha Christie', 1947483647, 'Policier', 1, '2024-01-19'),
(4, 2, 'Meurtre au manoir', 'Agatha Christie', 1887483647, 'Policier', 1, '2024-01-19'),
(5, 1, 'Crime et Châtiment', 'Fyodor Dostoevsky', 142483647, 'Policier', 1, '2024-01-18'),
(6, 2, 'Amour et Intrigues', 'Jane Austen', 1147483647, 'Romantique', 1, '2024-01-19'),
(7, 3, 'Le secret d\'Emma', 'Jane Austen', 2147483647, 'Romantique', 1, '2024-01-19'),
(8, 3, 'Les liens du cœur', 'Emily Brontë', 2147483647, 'Romantique', 1, '2024-01-19'),
(9, 2, 'Dune', 'Frank Herbert', 1234567890, 'Science Fiction', 1, '2024-01-20'),
(10, 1, 'The Shining', 'Stephen King', 2147483647, 'horreur', 1, '2024-01-20'),
(12, 1, 'Nouveau Livre', 'Auteur Inconnu', 123456789, 'Inconnu', 1, '2024-01-01'),
(14, 1, 'Nouveau Livre', 'Auteur Inconnu', 123456789, 'Inconnu', 1, '2024-01-01'),
(15, 1, 'Nouveau Livre', 'Auteur Inconnu', 123456789, 'Inconnu', 1, '2024-01-01'),
(16, 1, 'Nouveau Livre', 'Auteur Inconnu', 123456789, 'Inconnu', 1, '2024-01-01'),
(17, 2, 'Nouveau Livre', 'Auteur Inconnu', 2145214, 'Inconnu', 1, '2024-01-01'),
(18, 1, 'Nouveau Livre', 'Auteur Inconnu', 123456789, 'Inconnu', 1, '2024-01-01');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `details_emprunt`
--
ALTER TABLE `details_emprunt`
  ADD PRIMARY KEY (`details_emprunts_id`),
  ADD KEY `fk_details_emprunt_lecteur` (`lecteur_cin`),
  ADD KEY `fk_details_emprunt_livre` (`livre_id`);

--
-- Indexes for table `employe`
--
ALTER TABLE `employe`
  ADD PRIMARY KEY (`employe_id`);

--
-- Indexes for table `etagere`
--
ALTER TABLE `etagere`
  ADD PRIMARY KEY (`etagere_id`);

--
-- Indexes for table `lecteur`
--
ALTER TABLE `lecteur`
  ADD PRIMARY KEY (`cin`);

--
-- Indexes for table `livre`
--
ALTER TABLE `livre`
  ADD PRIMARY KEY (`code`),
  ADD KEY `fk_livre_etagere` (`etagere_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `details_emprunt`
--
ALTER TABLE `details_emprunt`
  MODIFY `details_emprunts_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `employe`
--
ALTER TABLE `employe`
  MODIFY `employe_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `etagere`
--
ALTER TABLE `etagere`
  MODIFY `etagere_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `livre`
--
ALTER TABLE `livre`
  MODIFY `code` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `details_emprunt`
--
ALTER TABLE `details_emprunt`
  ADD CONSTRAINT `fk_details_emprunt_lecteur` FOREIGN KEY (`lecteur_cin`) REFERENCES `lecteur` (`cin`),
  ADD CONSTRAINT `fk_details_emprunt_livre` FOREIGN KEY (`livre_id`) REFERENCES `livre` (`code`);

--
-- Constraints for table `livre`
--
ALTER TABLE `livre`
  ADD CONSTRAINT `fk_livre_etagere` FOREIGN KEY (`etagere_id`) REFERENCES `etagere` (`etagere_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
