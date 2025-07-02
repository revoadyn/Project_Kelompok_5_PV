-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2025 at 11:51 AM
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
-- Database: `mylaundry`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_antar`
--

CREATE TABLE `tb_antar` (
  `kode_antar` varchar(15) NOT NULL,
  `tipe` varchar(15) NOT NULL,
  `biaya` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_antar`
--

INSERT INTO `tb_antar` (`kode_antar`, `tipe`, `biaya`) VALUES
('A01', 'Tidak Diantar', 0),
('A02', 'Jemput Saja', 5000),
('A03', 'Antar & Jemput', 8000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_karyawan`
--

CREATE TABLE `tb_karyawan` (
  `id_karyawan` varchar(15) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `jk` varchar(20) NOT NULL,
  `no_telp` varchar(15) NOT NULL,
  `agama` varchar(25) NOT NULL,
  `jabatan` varchar(25) NOT NULL,
  `alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_karyawan`
--

INSERT INTO `tb_karyawan` (`id_karyawan`, `nama`, `jk`, `no_telp`, `agama`, `jabatan`, `alamat`) VALUES
('K01', 'Siti Aisyah', 'Perempuan', '08923818293', 'Islam', 'Operator - Cuci', 'Jl. Condet Raya'),
('K02', 'Ayu Anjani', 'Perempuan', '08928471923', 'Islam', 'Operator - Setrika', 'Jl. Pasar Minggu'),
('K03', 'Joko Susanto', 'Laki - laki', '08782931255', 'Islam', 'Kurir', 'Jl. Pejaten Raya');

-- --------------------------------------------------------

--
-- Table structure for table `tb_layanan`
--

CREATE TABLE `tb_layanan` (
  `id_layanan` varchar(15) NOT NULL,
  `layanan` varchar(30) NOT NULL,
  `estimasi_waktu` varchar(20) NOT NULL,
  `harga` int(20) NOT NULL,
  `keterangan` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_layanan`
--

INSERT INTO `tb_layanan` (`id_layanan`, `layanan`, `estimasi_waktu`, `harga`, `keterangan`) VALUES
('L0001', 'Cuci Kering (Reguler)', '3 Hari', 6000, 'Cuci, Kering dan Lipat'),
('L0002', 'Cuci Kering (Express)', '1 Hari', 8000, 'Selesai cepat, tanpa lipat/setrika'),
('L0003', 'Cuci Setrika (Reguler)', '3 Hari', 8000, 'Cuci dan setrika, dilipat rapi'),
('L0004', 'Cuci Setrika (Express)', '1 Hari', 10000, 'Cuci dan setrika kilat'),
('L0005', 'Cuci Bed Cover/Selimut', '5 Hari', 12000, 'Cucian berat, proses khusus'),
('L0006', 'Boneka/Item Spesial', '5 Hari', 15000, 'Cuci lembut untuk boneka, sepatu, dll'),
('L0007', 'Dry Cleaning/Jas Kebaya', '5 Hari', 25000, 'Untuk pakaian formal dan sensitif'),
('L0008', 'Cuci Karpet', '7 Hari', 20000, 'Termasuk vakum, pengeringan total'),
('L0009', 'Cuci Gorden', '7 Hari', 18000, 'Lipat rapi');

-- --------------------------------------------------------

--
-- Table structure for table `tb_login`
--

CREATE TABLE `tb_login` (
  `id_admin` varchar(15) NOT NULL,
  `password` varchar(20) NOT NULL,
  `nama` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_login`
--

INSERT INTO `tb_login` (`id_admin`, `password`, `nama`) VALUES
('ID01', '12345', 'Revo Ady Nugroho'),
('ID02', '12345', 'Marsel Rulianan H.'),
('ID03', '12345', 'Yogi Prasetyo'),
('ID04', '12345', 'Muhammad April Putra N.Z'),
('ID05', '12345', 'Najwan Maulidani'),
('ID06', '12345', 'Farhan Hidayatullah'),
('ID07', '12345', 'Mohamad Iqbal AlFaris'),
('ID08', '12345', 'Tamara Damaris J.S');

-- --------------------------------------------------------

--
-- Table structure for table `tb_nota`
--

CREATE TABLE `tb_nota` (
  `id_nota` varchar(20) NOT NULL,
  `tanggal` date NOT NULL,
  `id_admin` varchar(20) DEFAULT NULL,
  `id_pelanggan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_nota`
--

INSERT INTO `tb_nota` (`id_nota`, `tanggal`, `id_admin`, `id_pelanggan`) VALUES
('IN01', '2025-06-28', 'ID01', 'IP0001'),
('IN02', '2025-06-28', 'ID01', 'IP0001'),
('IN03', '2025-06-28', 'ID01', 'IP0001'),
('IN04', '2025-06-29', 'ID01', 'IP0002'),
('IN05', '2025-06-29', 'ID01', 'IP0001'),
('IN06', '2025-06-29', 'ID01', 'IP0003'),
('IN07', '2025-06-29', 'ID01', 'IP0003'),
('IN08', '2025-06-30', 'ID01', 'IP0003'),
('IN09', '2025-06-30', NULL, 'IP0001'),
('IN10', '2025-06-30', 'ID01', 'IP0001');

-- --------------------------------------------------------

--
-- Table structure for table `tb_nota_detail`
--

CREATE TABLE `tb_nota_detail` (
  `id_nota` varchar(20) NOT NULL,
  `id_layanan` varchar(20) NOT NULL,
  `layanan` varchar(25) NOT NULL,
  `estimasi_waktu` varchar(15) NOT NULL,
  `harga` int(20) NOT NULL,
  `tipe` varchar(20) NOT NULL,
  `biaya` int(20) NOT NULL,
  `berat` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_nota_detail`
--

INSERT INTO `tb_nota_detail` (`id_nota`, `id_layanan`, `layanan`, `estimasi_waktu`, `harga`, `tipe`, `biaya`, `berat`) VALUES
('IN03', 'L0002', 'Cuci Kering (Express)', '1 Hari', 8000, 'Tidak Diantar', 0, 1),
('IN04', 'L0002', 'Cuci Kering (Express)', '1 Hari', 8000, 'Jemput Saja', 5000, 1),
('IN04', 'L0003', 'Cuci Setrika (Reguler)', '3 Hari', 8000, 'Jemput Saja', 5000, 2),
('IN05', 'L0001', 'Cuci Kering (Reguler)', '3 Hari', 6000, 'Jemput Saja', 5000, 2),
('IN05', 'L0005', 'Cuci Bed Cover/Selimut', '5 Hari', 12000, 'Jemput Saja', 5000, 2),
('IN06', 'L0001', 'Cuci Kering (Reguler)', '3 Hari', 6000, 'Antar & Jemput', 8000, 1),
('IN06', 'L0004', 'Cuci Setrika (Express)', '1 Hari', 10000, 'Antar & Jemput', 8000, 1),
('IN06', 'L0005', 'Cuci Bed Cover/Selimut', '5 Hari', 12000, 'Antar & Jemput', 8000, 1),
('IN07', 'L0001', 'Cuci Kering (Reguler)', '3 Hari', 6000, 'Tidak Diantar', 0, 2),
('IN07', 'L0005', 'Cuci Bed Cover/Selimut', '5 Hari', 12000, 'Tidak Diantar', 0, 2),
('IN07', 'L0007', 'Dry Cleaning/Jas Kebaya', '5 Hari', 25000, 'Tidak Diantar', 0, 1),
('IN08', 'L0002', 'Cuci Kering (Express)', '1 Hari', 8000, 'Antar & Jemput', 8000, 2),
('IN08', 'L0004', 'Cuci Setrika (Express)', '1 Hari', 10000, 'Antar & Jemput', 8000, 3),
('IN08', 'L0005', 'Cuci Bed Cover/Selimut', '5 Hari', 12000, 'Antar & Jemput', 8000, 2),
('IN08', 'L0006', 'Boneka/Item Spesial', '5 Hari', 15000, 'Antar & Jemput', 8000, 1),
('IN08', 'L0007', 'Dry Cleaning/Jas Kebaya', '5 Hari', 25000, 'Antar & Jemput', 8000, 1),
('IN08', 'L0008', 'Cuci Karpet', '7 Hari', 20000, 'Antar & Jemput', 8000, 1),
('IN08', 'L0009', 'Cuci Gorden', '7 Hari', 18000, 'Antar & Jemput', 8000, 1),
('IN09', 'L0001', 'Cuci Kering (Reguler)', '3 Hari', 6000, 'Tidak Diantar', 0, 1),
('IN10', 'L0001', 'Cuci Kering (Reguler)', '3 Hari', 6000, 'Antar & Jemput', 8000, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tb_pelanggan`
--

CREATE TABLE `tb_pelanggan` (
  `id_pelanggan` varchar(15) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `jk` varchar(20) NOT NULL,
  `no_telp` varchar(15) NOT NULL,
  `jenis_pelanggan` varchar(15) NOT NULL,
  `alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_pelanggan`
--

INSERT INTO `tb_pelanggan` (`id_pelanggan`, `nama`, `jk`, `no_telp`, `jenis_pelanggan`, `alamat`) VALUES
('IP0001', 'Alif Fajar', 'Laki - laki', '081728392012', 'Biasa', 'Jl. Setapak'),
('IP0002', 'Adelia Angg', 'Perempuan', '0891283829122', 'Biasa', 'Jl. Cipadu'),
('IP0003', 'Reza Wahyu N', 'Laki - laki', '08872839122', 'Member', 'Jl. Setapak');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_antar`
--
ALTER TABLE `tb_antar`
  ADD PRIMARY KEY (`kode_antar`);

--
-- Indexes for table `tb_karyawan`
--
ALTER TABLE `tb_karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `tb_layanan`
--
ALTER TABLE `tb_layanan`
  ADD PRIMARY KEY (`id_layanan`);

--
-- Indexes for table `tb_login`
--
ALTER TABLE `tb_login`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `tb_nota`
--
ALTER TABLE `tb_nota`
  ADD PRIMARY KEY (`id_nota`);

--
-- Indexes for table `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
