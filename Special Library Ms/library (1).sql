-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 15, 2025 at 02:22 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `bookid` int(11) NOT NULL,
  `bookname` varchar(255) NOT NULL,
  `author` varchar(200) NOT NULL,
  `publisher` varchar(200) NOT NULL,
  `Status` enum('Borrowed','Available') DEFAULT 'Available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`bookid`, `bookname`, `author`, `publisher`, `Status`) VALUES
(1001, 'Chemistry', 'James j', 'Newman ', 'Borrowed'),
(1002, 'Mathematics', 'Liard Leon', 'MK', 'Available'),
(1003, 'Physics ', 'Jack wish', 'Newman', 'Available'),
(1004, 'Calculus', 'Flurien', 'MK ', 'Available'),
(1005, 'Geography', 'Marine billy', 'Germ 001', 'Borrowed'),
(1006, 'Computer Networks', 'Vincent G X', 'VMH Group', 'Available'),
(1007, 'Signals and Systems', 'Alan V. Oppenheim', 'ABC ', 'Available'),
(1008, 'Clean Code', 'Robert C. Martin', 'ABD', 'Available'),
(1009, 'Introduction to Algorithms', 'Thomas H', 'MIT Press', 'Available'),
(1010, 'Artificial Intelligence: A Modern Approach', 'Stuart Russell, Peter Norvig', 'Pearson', 'Available'),
(1011, 'Digital Signal Processing', 'John G. Proakis', 'Pearson', 'Available'),
(1012, 'Digital Signal Processing', 'John G. Proakis', 'Pearson', 'Available'),
(1013, 'Operating System Concepts', 'Abraham Silberschatz', 'Wiley', 'Available'),
(1014, 'Data Science', 'Joel Grus', 'O\'Reilly Media', 'Available'),
(1015, 'Python', 'Eric Matthes', 'No Starch Press', 'Available'),
(1016, 'Database', 'Henry F. Korth', 'McGraw-Hill Education', 'Available'),
(1017, 'Java', 'Herbert Schildt', 'McGraw-Hill Education', 'Available'),
(1018, 'Design', 'Erich Gamma, Richard Helm', 'Addison-Wesley', 'Available'),
(1019, 'Software Engineering', 'Ian Sommerville', 'Pearson', 'Available'),
(1020, 'Modern Operating Systems', 'Andrew S. Tanenbaum', 'Pearson', 'Available'),
(1021, 'The C Programming Language', 'Brian W. Kernighan, Dennis M. Ritchie', 'Prentice Hall', 'Available'),
(1022, 'Discrete Mathematics', 'Kenneth H. Rosen', 'McGraw-Hill Education', 'Available'),
(1024, 'Numerical Methods', 'Richard L. Burden, J. Douglas Faires', 'Cengage Learning', 'Available'),
(1025, 'Database System Concepts', 'Henry F. Korth', 'McGraw-Hill Education', 'Available'),
(1026, 'Geography', 'Marine billy', 'Germ 001', 'Available'),
(1027, 'JavaScript: The Good Parts', 'Douglas Crockford', 'O\'Reilly Media', 'Available'),
(1028, 'Learning Python', 'Mark Lutz', 'Cengage Learning', 'Available'),
(1029, 'Deep Learning', 'Ian Goodfellow', 'MIT Press', 'Available'),
(1030, 'The Art of Computer Programming', 'Donald E. Knuth', 'Addison-Wesley', 'Available'),
(1031, 'Guns, Germs, and Steel', 'Jared Diamond', 'W. W. Norton & Company', 'Available'),
(1032, 'A People\'s History of the United States', 'Howard Zinn', 'Harper Perennial Modern Classics', 'Available'),
(1033, 'A New History of the World', 'Peter Frankopan', 'Vintage', 'Available'),
(1034, 'The Crusades: The Authoritative History', 'Thomas Asbridge', 'Ecco', 'Available'),
(1035, 'SPQR: A History of Ancient Rome', 'Mary Beard', 'Liveright', 'Available'),
(1036, 'The Rise and Fall of the Third Reich', 'William L. Shirer', 'Simon & Schuster', 'Available'),
(1037, 'A Short History of Nearly Everything', 'Bill Bryson', 'Broadway Books', 'Available'),
(1038, 'The Wright Brothers', 'David McCullough', 'Simon & Schuster', 'Available'),
(1039, 'The Diary of a Young Girl', 'Anne Frank', 'Bantam', 'Available'),
(1040, 'Genghis Khan and the Making of the Modern World', 'Jack Weatherford', 'Crown', 'Available'),
(1041, 'SPQR: A History of Ancient Rome', 'Mary Beard', 'Liveright', 'Available'),
(1042, 'Team of Rivals', 'Doris Kearns Goodwin', 'Simon & Schuster', 'Available'),
(1043, '1776', 'David McCullough', 'Pen and Sword Books', 'Available'),
(1044, 'The Peloponnesian War', 'Donald Kagan', 'Penguin Books', 'Available'),
(1045, 'A Distant Mirror', 'Barbara W. Tuchman', 'Random House', 'Available'),
(1046, 'The Fall of the Roman Empire', 'Peter Heather', 'Oxford University Press', 'Available'),
(1047, 'The American Revolution', 'Gordon S. Wood', 'Modern Library', 'Available');

-- --------------------------------------------------------

--
-- Table structure for table `borrowing`
--

CREATE TABLE `borrowing` (
  `bookid` int(11) NOT NULL,
  `studentname` varchar(255) NOT NULL,
  `book` varchar(200) NOT NULL,
  `date_borrowed` date NOT NULL,
  `date_return` date NOT NULL,
  `Status` enum('Borrowed','Returned') DEFAULT 'Borrowed',
  `DoneBy` varchar(100) DEFAULT 'straton',
  `DoneAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `borrow_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowing`
--

INSERT INTO `borrowing` (`bookid`, `studentname`, `book`, `date_borrowed`, `date_return`, `Status`, `DoneBy`, `DoneAt`, `borrow_id`) VALUES
(1002, 'Mutesi', 'Mathematics', '2025-04-01', '2025-04-05', 'Returned', 'Vincent', '2025-05-11 17:16:10', 2),
(1004, 'Mutesi', 'Calculus', '2025-04-01', '2025-04-03', 'Returned', 'straton', '2025-04-18 21:05:03', 4),
(1005, 'MUSAZA Patrick', 'Geography', '2025-04-04', '2025-04-19', 'Returned', 'Vincent', '2025-05-11 16:03:56', 5),
(1007, 'Mutesi', 'Signals and Systems', '2025-04-04', '2025-04-18', 'Returned', 'Vincent', '2025-04-29 18:14:50', 6),
(1008, 'Thierry', 'Clean Code', '2025-04-01', '2025-04-05', 'Returned', 'Patrick Mbabazi', '2025-04-18 21:05:03', 7),
(1008, 'Mutesi', 'Clean Code', '2025-04-23', '2025-04-30', 'Returned', 'Vincent', '2025-05-09 15:16:59', 12),
(1004, 'Mutesi', 'Calculus', '2025-04-01', '2025-04-03', 'Returned', 'Vincent', '2025-05-11 17:28:08', 13),
(1008, 'Mutesi', 'Clean Code', '2025-04-23', '2025-04-30', 'Returned', 'Vincent', '2025-05-11 16:00:52', 17),
(1004, 'Mutesi', 'Calculus', '2025-04-01', '2025-04-03', 'Returned', 'Ishimwe Justine', '2025-05-11 17:01:28', 34),
(1002, 'Mutesi', 'Mathematics', '2025-04-01', '2025-04-05', 'Returned', 'Vincent', '2025-05-11 19:14:16', 37),
(1008, 'Thierry', 'Clean Code', '2025-04-01', '2025-04-05', 'Returned', 'Patrick Mbabazi', '2025-05-11 20:09:16', 40),
(1001, 'MUSAZA Patrick', 'Chemistry', '2025-05-11', '2025-05-12', 'Returned', 'Patrick Mbabazi', '2025-05-12 05:38:57', 41),
(1008, 'Thierry', 'Clean Code', '2025-04-01', '2025-04-05', 'Returned', 'Patrick Mbabazi', '2025-05-11 20:08:31', 42),
(1001, 'Mbabazi Patrick', 'Chemistry', '2025-05-12', '2025-05-13', 'Returned', 'Patrick Mbabazi', '2025-05-12 06:00:27', 43),
(1001, 'Mbabazi Patrick', 'Chemistry', '2025-05-12', '2025-05-13', 'Borrowed', 'Patrick Mbabazi', '2025-05-12 06:04:48', 44),
(1005, 'MUSAZA Patrick', 'Geography', '2025-05-13', '2025-05-14', 'Borrowed', 'Ishimwe Justine', '2025-05-12 07:09:24', 45),
(1008, 'leacturer', 'clean code', '2025-05-12', '2025-05-14', 'Borrowed', 'Patrick Mbabazi', '2025-05-12 11:22:01', 46),
(1008, 'leacturer', 'clean code', '2025-05-12', '2025-05-14', 'Returned', 'Patrick Mbabazi', '2025-05-12 11:23:05', 47);

-- --------------------------------------------------------

--
-- Table structure for table `librarians`
--

CREATE TABLE `librarians` (
  `librarianid` int(11) NOT NULL,
  `names` varchar(255) NOT NULL,
  `email` varchar(200) NOT NULL,
  `address` varchar(200) NOT NULL,
  `Role` varchar(50) NOT NULL DEFAULT 'Librarian'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `librarians`
--

INSERT INTO `librarians` (`librarianid`, `names`, `email`, `address`, `Role`) VALUES
(11001, 'Habayimana Vincent', 'Vincent@gmail.com', 'Ruli', 'Librarian'),
(11002, 'patrick', 'vincentpriest8@gmail.com', 'Gisozi', 'Librarian'),
(11003, 'Martin', 'martin@gmail.com', 'Burera', 'Librarian'),
(11004, 'Vincent', 'vincent@gmail.com', 'Gasabo', 'Admin'),
(11005, 'Patrick Mbabazi', 'mbabazi@gmail.com', 'Musanze', 'Librarian'),
(11006, 'Ishimwe Justine', 'justine@gmail.com', 'Gakenke', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `reported`
--

CREATE TABLE `reported` (
  `id` int(11) NOT NULL,
  `studentname` varchar(100) NOT NULL,
  `bookname` varchar(100) NOT NULL,
  `bookid` varchar(50) NOT NULL,
  `borroweddate` date NOT NULL,
  `returneddate` date NOT NULL,
  `Reportedcase` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reported`
--

INSERT INTO `reported` (`id`, `studentname`, `bookname`, `bookid`, `borroweddate`, `returneddate`, `Reportedcase`) VALUES
(3, 'MUSAZA Patrick', 'Geography', '1005', '2025-05-13', '2025-05-14', 'Destroyed Book'),
(5, 'Mbabazi Patrick', 'Chemistry', '1001', '2025-05-12', '2025-05-13', 'None');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `studentid` int(11) NOT NULL,
  `names` varchar(255) NOT NULL,
  `email` varchar(200) NOT NULL,
  `address` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`studentid`, `names`, `email`, `address`) VALUES
(1, 'Kabera ', 'vincentpriest8@gmail.com', 'Gisozi kn 7'),
(2, 'Thierry', 'vincenthabayimana@gmail.com', 'Huye'),
(3, 'Mbabazi Patrick', 'stratonmbabazipatrick@gmail.com', 'Gisozi'),
(4, 'Straton Mbabazi', 'smbabazipatrick@gmail.com', 'Musanze'),
(11, 'Gentille', 'thierryvincent616@gmail.com', 'Huye'),
(12, 'vincent', 'vincentpriest8@gmail.com', 'kn 123'),
(22, 'MUSAZA Patrick', 'vincentpriest8@gmail.com', 'Kamonyi'),
(123, 'leacturer', 'ukudox@gmail.com', 'kn 5');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`bookid`);

--
-- Indexes for table `borrowing`
--
ALTER TABLE `borrowing`
  ADD PRIMARY KEY (`borrow_id`);

--
-- Indexes for table `librarians`
--
ALTER TABLE `librarians`
  ADD PRIMARY KEY (`librarianid`);

--
-- Indexes for table `reported`
--
ALTER TABLE `reported`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`studentid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `borrowing`
--
ALTER TABLE `borrowing`
  MODIFY `borrow_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT for table `reported`
--
ALTER TABLE `reported`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
