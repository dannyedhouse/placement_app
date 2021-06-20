SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `placements`
--

-- --------------------------------------------------------

--
-- Table structure for table `placementTable`
--

CREATE TABLE `placementTable` (
  `PlacementID` int(11) NOT NULL,
  `PlacementName` text NOT NULL,
  `Company` text NOT NULL,
  `Deadline` text NOT NULL,
  `Type` text NOT NULL,
  `Salary` text NOT NULL,
  `Location` text NOT NULL,
  `Description` text NOT NULL,
  `Subject` text NOT NULL,
  `Miles` int(11) NOT NULL,
  `Paid` text NOT NULL,
  `URL` text NOT NULL,
  `Sync` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `preferencesTable`
--

CREATE TABLE `preferencesTable` (
  `PrefID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `Paid` tinyint(1) NOT NULL,
  `Type` text NOT NULL,
  `Subject` text NOT NULL,
  `Miles` int(11) NOT NULL,
  `Relocate` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `profileTable`
--

CREATE TABLE `profileTable` (
  `UserID` int(11) NOT NULL,
  `Username` text NOT NULL,
  `Name` text NOT NULL,
  `Email` text NOT NULL,
  `Phone` int(11) NOT NULL,
  `DOB` date NOT NULL,
  `Address` text NOT NULL,
  `About` text NOT NULL,
  `Experience` text NOT NULL,
  `University` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `userFavTable`
--

CREATE TABLE `userFavTable` (
  `FavID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `PlacementID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `placementTable`
--
ALTER TABLE `placementTable`
  ADD PRIMARY KEY (`PlacementID`);

--
-- Indexes for table `preferencesTable`
--
ALTER TABLE `preferencesTable`
  ADD PRIMARY KEY (`PrefID`);

--
-- Indexes for table `profileTable`
--
ALTER TABLE `profileTable`
  ADD PRIMARY KEY (`UserID`);

--
-- Indexes for table `userFavTable`
--
ALTER TABLE `userFavTable`
  ADD PRIMARY KEY (`FavID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `placementTable`
--
ALTER TABLE `placementTable`
  MODIFY `PlacementID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `preferencesTable`
--
ALTER TABLE `preferencesTable`
  MODIFY `PrefID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `profileTable`
--
ALTER TABLE `profileTable`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `userFavTable`
--
ALTER TABLE `userFavTable`
  MODIFY `FavID` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
