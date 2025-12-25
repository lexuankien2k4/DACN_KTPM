-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: Vinfast_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `banks`
--

DROP TABLE IF EXISTS `banks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `logo_url` varchar(512) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banks`
--

LOCK TABLES `banks` WRITE;
/*!40000 ALTER TABLE `banks` DISABLE KEYS */;
INSERT INTO `banks` VALUES (1,'Techcombank','/images/banks/tcb_logo.png',1),(2,'VPBank','/images/banks/vpb_logo.png',1),(3,'Vietcombank','/images/banks/vcb_logo.png',1),(4,'BIDV','/images/banks/bidv_logo.png',1);
/*!40000 ALTER TABLE `banks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_categories`
--

DROP TABLE IF EXISTS `car_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_categories`
--

LOCK TABLES `car_categories` WRITE;
/*!40000 ALTER TABLE `car_categories` DISABLE KEYS */;
INSERT INTO `car_categories` VALUES (3,'Dòng xe dịch vụ'),(2,'Động cơ xăng '),(1,'Động cơ điện');
/*!40000 ALTER TABLE `car_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_images`
--

DROP TABLE IF EXISTS `car_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `variant_id` bigint NOT NULL COMMENT 'Khóa ngoại, trỏ đến phiên bản xe trong car_variants',
  `image_url` varchar(512) NOT NULL COMMENT 'Đường dẫn đến file ảnh',
  PRIMARY KEY (`id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `car_images_ibfk_1` FOREIGN KEY (`variant_id`) REFERENCES `car_variants` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_images`
--

LOCK TABLES `car_images` WRITE;
/*!40000 ALTER TABLE `car_images` DISABLE KEYS */;
INSERT INTO `car_images` VALUES (1,1,'images/limogreen/limo-img-01.webp'),(2,1,'images/limogreen/limo-img-04.webp'),(3,1,'images/limogreen/limo-img-07.webp'),(4,1,'images/limogreen/limo-img-10.webp'),(5,2,'images/miniogreen/minio-img-black.webp'),(6,2,'images/miniogreen/minio-img-red.webp'),(7,2,'images/miniogreen/minio-img-while.webp'),(8,2,'images/miniogreen/minio-img-yellow.webp'),(9,4,'images/neriogreen/nerio-img-07.webp'),(10,4,'images/neriogreen/nerio-img-10.webp'),(11,4,'images/neriogreen/nerio-img-black.webp'),(12,4,'images/neriogreen/nerio-img-yellow.webp'),(13,3,'images/heriogreen/herio-img-black.webp'),(14,3,'images/heriogreen/herio-img-red.webp'),(15,3,'images/heriogreen/herio-img-sillev.webp'),(16,3,'images/heriogreen/herio-img-yellow.webp'),(17,5,'images/ecvan/ecvan-jade.webp'),(18,5,'images/ecvan/ecvan-red.webp'),(19,5,'images/ecvan/ecvan-white.webp'),(20,5,'images/ecvan/ecvan-yellow.webp'),(21,6,'images/EBUS/anh-1-ebus.webp'),(22,7,'images/vf3/VF3_Interior_Hero.webp'),(23,7,'images/vf3/vf3-1.webp'),(24,7,'images/vf3/vf3section-4.webp'),(25,7,'images/vf3/vf3section-5.webp'),(26,8,'images/vf5/bg-thong-so.webp'),(27,9,'images/vf6/exterior-vf6-2.webp'),(28,10,'images/vf6/exterior-vf6-2.webp'),(29,11,'images/vf7/product-CE1V.webp'),(30,11,'images/vf7/product-CE1M.webp'),(31,11,'images/vf7/product-CE1W.webp'),(32,11,'images/vf7/product-CE11.webp'),(33,12,'images/vf7/product-CE18.webp'),(34,13,'images/vf8/product-1V18.webp'),(35,14,'images/vf8/product-171V.webp'),(36,14,'images/vf8/product-2927.webp'),(37,14,'images/vf8/product-2911.webp'),(38,15,'images/vf9/CE1M.webp'),(39,15,'images/vf9/CE1V.webp'),(40,16,'images/vf9/CE18.webp'),(41,16,'images/vf9/CE22.webp');
/*!40000 ALTER TABLE `car_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_models`
--

DROP TABLE IF EXISTS `car_models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_models` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `category_id` int DEFAULT NULL COMMENT 'Khóa ngoại đến bảng car_categories',
  `body_type` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `car_models_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `car_categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_models`
--

LOCK TABLES `car_models` WRITE;
/*!40000 ALTER TABLE `car_models` DISABLE KEYS */;
INSERT INTO `car_models` VALUES (1,'VinFast VF 3',1,'Mini SUV',1),(2,'VinFast VF 5',1,'A-SUV',1),(3,'VinFast VF 6',1,'B-SUV',1),(4,'VinFast VF 7',1,'C-SUV',1),(5,'VinFast VF 8',1,'D-SUV',1),(6,'VinFast VF 9',1,'E-SUV',1),(7,'VinFast VF e34',1,'C-SUV',1),(8,'VinFast Fadil',2,'Hatchback',1),(9,'VinFast Lux A2.0',2,'Sedan',1),(10,'VinFast Lux SA2.0',2,'D-SUV',1),(11,'VinFast President',2,'E-SUV',1),(12,'Minio Green',3,'Mini Van',1),(13,'Herio Green',3,'Compact Van',1),(14,'Nerio Green',3,'Crossover Van',1),(15,'Limo Green',3,'SUV',1),(16,'EC Van',3,'EC Van',1),(17,'EBus',3,'Bus',1),(18,'VinFast VF Test',1,'Test SUV',1);
/*!40000 ALTER TABLE `car_models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_variants`
--

DROP TABLE IF EXISTS `car_variants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_variants` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `model_id` bigint NOT NULL COMMENT 'Khóa ngoại đến car_models(id)',
  `name` varchar(255) NOT NULL,
  `price` bigint NOT NULL COMMENT 'Giá niêm yết (VNĐ)',
  `is_active` tinyint(1) DEFAULT '1' COMMENT 'True nếu phiên bản này hiển thị',
  `length` int DEFAULT NULL COMMENT 'Chiều dài xe (mm)',
  `width` int DEFAULT NULL COMMENT 'Chiều rộng xe (mm)',
  `height` int DEFAULT NULL COMMENT 'Chiều cao xe (mm)',
  `wheelbase` int DEFAULT NULL COMMENT 'Chiều dài cơ sở (mm)',
  `ground_clearance` int DEFAULT NULL COMMENT 'Khoảng sáng gầm xe (mm)',
  `max_power` int DEFAULT NULL COMMENT 'Công suất tối đa (kW)',
  `max_torque` int DEFAULT NULL COMMENT 'Mô-men xoắn cực đại (Nm)',
  `drivetrain` varchar(255) DEFAULT NULL,
  `drive_modes` varchar(255) DEFAULT NULL,
  `battery` decimal(38,2) DEFAULT NULL,
  `range_nedc` int DEFAULT NULL COMMENT 'Quãng đường di chuyển theo chuẩn NEDC (km)',
  `charge_time_detail` varchar(255) DEFAULT NULL,
  `suspension_system` varchar(255) DEFAULT NULL,
  `brake_system` varchar(255) DEFAULT NULL,
  `wheel_size` int DEFAULT NULL COMMENT 'Kích thước la-zăng (inch)',
  `front_light_type` varchar(255) DEFAULT NULL,
  `trunk_access` varchar(255) DEFAULT NULL,
  `air_conditioning_system` varchar(255) DEFAULT NULL,
  `entertainment_screen` decimal(38,2) DEFAULT NULL,
  `speaker_system` varchar(255) DEFAULT NULL,
  `driver_seat_adjustment` varchar(255) DEFAULT NULL,
  `discounted_price` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `model_id` (`model_id`),
  CONSTRAINT `car_variants_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `car_models` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_variants`
--

LOCK TABLES `car_variants` WRITE;
/*!40000 ALTER TABLE `car_variants` DISABLE KEYS */;
INSERT INTO `car_variants` VALUES (1,15,'Limo Green',749000000,1,4740,1872,1723,2840,170,150,280,'FWD/Cầu trước','Eco/Normal',60.13,450,'30 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa thông gió/Đĩa',18,'LED','Chỉnh cơ','Tự động 1 vùng',10.10,'4 loa','Chỉnh cơ 6 hướng',NULL),(2,12,'Minio Green',549000000,1,3090,1496,1659,2065,165,20,65,'FWD/Cầu trước','Eco/Normal',14.70,170,'Khoảng 3 giờ (Sạc thường)','MacPherson/MacPherson','Đĩa/Tang trống',13,'LED','Chỉnh cơ','Máy lạnh, chỉnh cơ',7.00,'2 loa','Chỉnh cơ 4 hướng',NULL),(3,13,'Herio Green',849000000,1,3967,1723,1579,2514,160,100,135,'FWD/Cầu trước','Eco/Sport',37.23,326,'Khoảng 5 giờ (Sạc thường)','MacPherson/Dầm xoắn','Đĩa/Đĩa',16,'Bi-halogen, projector','Chỉnh cơ','Chỉnh cơ',8.00,'2 loa','Chỉnh cơ 6 hướng',NULL),(4,14,'Nerio Green',949000000,1,4300,1768,1615,2840,170,150,280,'FWD/Cầu trước','Eco/Normal',60.13,450,'30 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa thông gió/Đĩa',18,'LED','Chỉnh cơ','Tự động 1 vùng',10.10,'4 loa','Chỉnh cơ 6 hướng',NULL),(5,16,'EC Van',949000000,1,4238,1820,1950,2730,170,100,220,'FWD/Cầu trước','Eco/Normal',59.60,350,'Khoảng 6 giờ (Sạc thường)','MacPherson/Nhíp lá','Đĩa thông gió/Tang trống',16,'Halogen','Cửa lùa','Chỉnh cơ',7.00,'2 loa','Chỉnh nỉ',NULL),(6,17,'EBus',1949000000,1,12000,2500,3200,6800,180,140,450,'RWD/Cầu sau','Normal',228.00,260,'Khoảng 2 giờ (Sạc nhanh)','Hệ thống treo khí nén','Phanh khí nén',22,'LED','Cửa tự động','Điều hòa công suất lớn',10.10,'Hệ thống loa thông báo','Ghế lái đệm khí',NULL),(7,1,'Tiêu chuẩn',315000000,1,3190,1679,1622,2075,191,32,110,'RWD/Cầu sau','Eco/Normal',18.64,210,'36 phút (10%-70%)','MacPherson/Trục cứng','Đĩa/Tang trống',16,'Halogen','Chỉnh cơ','Điều hòa 1 vùng',10.00,'2 loa','Chỉnh cơ 4 hướng',NULL),(8,2,'Plus',458000000,1,3967,1723,1579,2513,182,100,135,'FWD/Cầu trước','Eco/Normal/Sport',37.23,326,'30 phút (10%-70%)','MacPherson/Thanh xoắn','Đĩa thông gió/Đĩa',17,'Halogen','Chỉnh cơ','Tự động 1 vùng',8.00,'4 loa','Chỉnh cơ 6 hướng',NULL),(9,3,'Eco',675000000,1,4238,1820,1594,2730,170,100,135,'FWD/Cầu trước','Eco/Normal/Sport',59.60,399,'24 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa/Đĩa',17,'LED','Chỉnh cơ','Tự động 1 vùng',12.90,'6 loa','Chỉnh cơ 8 hướng',NULL),(10,3,'Plus',765000000,1,4238,1820,1594,2730,170,150,310,'FWD/Cầu trước','Eco/Normal/Sport',59.60,381,'24 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa/Đĩa',19,'LED','Chỉnh điện','Tự động 2 vùng',12.90,'8 loa','Chỉnh điện 8 hướng',NULL),(11,4,'S',850000000,1,4545,1890,1635,2840,190,130,250,'FWD/Cầu trước','Eco/Normal/Sport',59.60,375,'24 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa thông gió/Đĩa',19,'LED','Chỉnh cơ','Tự động 2 vùng',12.90,'8 loa','Chỉnh cơ 8 hướng',NULL),(12,4,'TC (Plus)',999000000,1,4545,1890,1635,2840,190,260,500,'AWD/2 cầu toàn thời gian','Eco/Normal/Sport',75.30,431,'26 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa thông gió/Đĩa',20,'LED','Chỉnh điện','Tự động 2 vùng',12.90,'8 loa','Chỉnh điện 8 hướng',NULL),(13,5,'Eco',1090000000,1,4750,1934,1667,2950,175,260,500,'AWD/2 cầu toàn thời gian','Eco/Normal/Sport',87.70,471,'31 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa thông gió/Đĩa thông gió',19,'LED','Chỉnh điện','Tự động 2 vùng',15.60,'8 loa','Chỉnh điện 8 hướng',NULL),(14,5,'Plus',1270000000,1,4750,1934,1667,2950,175,300,620,'AWD/2 cầu toàn thời gian','Eco/Normal/Sport',87.70,457,'31 phút (10%-70%)','MacPherson/Đa liên kết','Đĩa thông gió/Đĩa thông gió',20,'LED Matrix','Chỉnh điện','Tự động 2 vùng',15.60,'10 loa','Chỉnh điện 12 hướng',NULL),(15,6,'Eco',1491000000,1,5118,2254,1696,3150,197,300,620,'AWD/2 cầu toàn thời gian','Eco/Normal/Sport',92.00,423,'26 phút (10%-70%)','Tay đòn kép/Đa liên kết','Đĩa thông gió/Đĩa thông gió',20,'LED Matrix','Chỉnh điện, đá cốp','Tự động 3 vùng',15.60,'13 loa','Chỉnh điện 8 hướng',NULL),(16,6,'Plus',1676000000,1,5118,2254,1696,3150,197,300,620,'AWD/2 cầu toàn thời gian','Eco/Normal/Sport',92.00,423,'26 phút (10%-70%)','Tay đòn kép/Đa liên kết','Đĩa thông gió/Đĩa thông gió',21,'LED Matrix','Chỉnh điện, đá cốp','Tự động 3 vùng',15.60,'13 loa','Chỉnh điện 12 hướng, massage',NULL),(17,8,'Tiêu chuẩn',425000000,0,3676,1632,1530,2385,150,72,135,'FWD/Cầu trước','Normal',NULL,NULL,NULL,'MacPherson/Thanh xoắn','Đĩa/Tang trống',15,'Halogen','Chỉnh cơ','Chỉnh cơ',7.00,'6 loa','Chỉnh cơ 6 hướng',NULL),(18,8,'Plus',459000000,0,3676,1632,1530,2385,150,72,135,'FWD/Cầu trước','Normal',NULL,NULL,NULL,'MacPherson/Thanh xoắn','Đĩa/Tang trống',15,'Halogen','Chỉnh cơ','Tự động, có cảm biến độ ẩm',7.00,'6 loa','Chỉnh cơ 6 hướng',NULL),(19,9,'Tiêu chuẩn',1115000000,0,4973,1900,1464,2968,150,130,300,'RWD/Cầu sau','Normal',NULL,NULL,NULL,'Tay đòn kép/Đa liên kết','Đĩa thông gió/Đĩa',18,'LED','Chỉnh điện','Tự động 2 vùng',10.40,'8 loa','Chỉnh điện 4 hướng',NULL),(20,9,'Cao cấp (Premium)',1358000000,0,4973,1900,1500,2968,150,170,350,'RWD/Cầu sau','Normal',NULL,NULL,NULL,'Tay đòn kép/Đa liên kết','Đĩa thông gió/Đĩa',19,'LED Matrix','Chỉnh điện, đá cốp','Tự động 2 vùng, lọc không khí ion',10.40,'13 loa có âm-ly','Chỉnh điện 12 hướng',NULL),(21,10,'Tiêu chuẩn',1552000000,0,4940,1960,1773,2933,192,170,350,'RWD/Cầu sau','Normal',NULL,NULL,NULL,'Tay đòn kép/Đa liên kết','Đĩa thông gió/Đĩa thông gió',19,'LED','Chỉnh điện','Tự động 2 vùng',10.40,'8 loa','Chỉnh điện 8 hướng',NULL),(22,10,'Cao cấp (Premium)',1835000000,0,4940,1960,1773,2933,192,170,350,'AWD/2 cầu toàn thời gian','Normal',NULL,NULL,NULL,'Tay đòn kép/Đa liên kết','Đĩa thông gió/Đĩa thông gió',20,'LED Matrix','Chỉnh điện, đá cốp','Tự động 2 vùng, lọc không khí ion',10.40,'13 loa có âm-ly','Chỉnh điện 12 hướng',NULL);
/*!40000 ALTER TABLE `car_variants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultation_requests`
--

DROP TABLE IF EXISTS `consultation_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultation_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `province` varchar(255) DEFAULT NULL,
  `variant_id` bigint NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `scheduled_at` datetime(6) DEFAULT NULL,
  `showroom_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `note` text,
  PRIMARY KEY (`id`),
  KEY `variant_id` (`variant_id`),
  KEY `FK_consultation_requests_showroom` (`showroom_id`),
  CONSTRAINT `consultation_requests_ibfk_1` FOREIGN KEY (`variant_id`) REFERENCES `car_variants` (`id`),
  CONSTRAINT `FK_consultation_requests_showroom` FOREIGN KEY (`showroom_id`) REFERENCES `showrooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultation_requests`
--

LOCK TABLES `consultation_requests` WRITE;
/*!40000 ALTER TABLE `consultation_requests` DISABLE KEYS */;
INSERT INTO `consultation_requests` VALUES (1,'Nguyễn Văn A','0912345678','Hà Nội',1,'Mới',NULL,NULL,NULL,NULL,NULL),(2,'Le Xuan Kien','0963347562','Hà Nội',3,'Đã hẹn lịch','2025-12-06 01:22:00.000000',2,'2025-11-25 09:19:41.537100','Lexuankien11082004@gmail.com','toi muon lai thu xe'),(3,'kiên','0963563695','Hà Nội',1,'Đã hẹn lịch','2025-12-18 22:17:00.000000',1,'2025-12-12 19:14:35.569140','lexuankien@gmail.com',''),(4,'fafaf','0224252527','Hà Nội',3,'Đã hẹn lịch','2025-12-24 01:09:00.000000',2,'2025-12-21 21:06:14.697281','le@gmail.com','vvss');
/*!40000 ALTER TABLE `consultation_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invalidated_token`
--

DROP TABLE IF EXISTS `invalidated_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invalidated_token` (
  `id` varchar(255) NOT NULL,
  `expiry_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invalidated_token`
--

LOCK TABLES `invalidated_token` WRITE;
/*!40000 ALTER TABLE `invalidated_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `invalidated_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan_policies`
--

DROP TABLE IF EXISTS `loan_policies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loan_policies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bank_id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `min_down_payment_rate` decimal(38,2) NOT NULL,
  `max_loan_term_years` int NOT NULL COMMENT 'Kỳ hạn vay tối đa (năm)',
  `preferential_rate_percent` decimal(38,2) DEFAULT NULL,
  `preferential_rate_months` int DEFAULT NULL COMMENT 'Thời gian ưu đãi (tháng)',
  `floating_rate_margin` decimal(38,2) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bank_id` (`bank_id`),
  CONSTRAINT `loan_policies_ibfk_1` FOREIGN KEY (`bank_id`) REFERENCES `banks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan_policies`
--

LOCK TABLES `loan_policies` WRITE;
/*!40000 ALTER TABLE `loan_policies` DISABLE KEYS */;
INSERT INTO `loan_policies` VALUES (1,1,'Ưu đãi Lãi suất Vàng (12T)',0.20,8,8.90,12,3.50,_binary ''),(2,1,'Gói Vay Linh hoạt (36T)',0.30,7,9.50,36,3.00,_binary ''),(3,2,'Lãi suất Cố định 6 tháng',0.25,7,7.50,6,4.00,_binary ''),(4,2,'Vay tối đa 8 năm',0.20,8,9.90,24,3.20,_binary ''),(5,3,'Chính sách VCB Xanh',0.30,10,8.00,12,3.80,_binary ''),(6,3,'Lãi suất Cơ sở',0.20,5,10.50,0,0.00,_binary ''),(7,4,'Hỗ trợ Khách hàng Mới',0.20,7,7.80,6,3.70,_binary ''),(8,4,'Vay trả góp dài hạn',0.25,10,9.20,12,3.30,_binary '');
/*!40000 ALTER TABLE `loan_policies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('ADMIN','Admin role'),('USER','User role');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permissions` (
  `role_name` varchar(255) NOT NULL,
  `permissions_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_name`,`permissions_name`),
  KEY `FKf5aljih4mxtdgalvr7xvngfn1` (`permissions_name`),
  CONSTRAINT `FKcppvu8fk24eqqn6q4hws7ajux` FOREIGN KEY (`role_name`) REFERENCES `role` (`name`),
  CONSTRAINT `FKf5aljih4mxtdgalvr7xvngfn1` FOREIGN KEY (`permissions_name`) REFERENCES `permission` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `showrooms`
--

DROP TABLE IF EXISTS `showrooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `showrooms` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `district` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `province` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `showrooms`
--

LOCK TABLES `showrooms` WRITE;
/*!40000 ALTER TABLE `showrooms` DISABLE KEYS */;
INSERT INTO `showrooms` VALUES (1,'Khu đô thị Vinhomes Ocean Park 1, Đa Tốn','Gia Lâm','VinFast Ocean Park','Hà Nội'),(2,'Tầng B1, TTTM Vincom Mega Mall Times City, 458 Minh Khai','Hai Bà Trưng','VinFast Times City','Hà Nội'),(3,'Tầng B2, TTTM Vincom Mega Mall Royal City, 72A Nguyễn Trãi','Thanh Xuân','VinFast Royal City','Hà Nội'),(4,'Tầng B1, TTTM Vincom Center Landmark 81, 720A Điện Biên Phủ','Bình Thạnh','VinFast Landmark 81','Hồ Chí Minh'),(5,'107 Nguyễn Lương Bằng, Tân Phú','Quận 7','VinFast Phú Mỹ Hưng','Hồ Chí Minh'),(6,'Tầng L1, TTTM Vincom Mega Mall Thảo Điền, 159 Xa Lộ Hà Nội','TP. Thủ Đức','VinFast Thảo Điền','Hồ Chí Minh'),(7,'Tầng L1, TTTM Vincom Plaza Ngô Quyền, 910A Ngô Quyền','Sơn Trà','VinFast Ngô Quyền','Đà Nẵng'),(8,'255 Nguyễn Văn Linh, Vĩnh Trung','Thanh Khê','VinFast Nguyễn Văn Linh','Đà Nẵng'),(9,'Khu đô thị Vinhomes Imperia, Thượng Lý','Hồng Bàng','VinFast Imperia Hải Phòng','Hải Phòng'),(10,'Khu đô thị Vinhomes Star City, Phường Đông Hải','TP. Thanh Hóa','VinFast Star City','Thanh Hóa');
/*!40000 ALTER TABLE `showrooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `dob` date DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('6cd17fa9-9024-4304-ae12-309646143aef','2004-08-11','Le','Kien','$2a$10$hirhIj3RTx6sN0B4CEvkIukevneY1rHsZv3hcUsDjf7SKzitOgOZC','admin'),('891e3838-edea-488b-ab33-aff766656b3f','2004-08-11','admin','admin','$2a$10$v6Iuj1iPRowbyT1dKa90XuiSCuZAvbhl0lSKEM5vD4IHWBcq8fhAC','kien');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` varchar(255) NOT NULL,
  `roles_name` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`roles_name`),
  KEY `FK6pmbiap985ue1c0qjic44pxlc` (`roles_name`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK6pmbiap985ue1c0qjic44pxlc` FOREIGN KEY (`roles_name`) REFERENCES `role` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES ('6cd17fa9-9024-4304-ae12-309646143aef','ADMIN'),('891e3838-edea-488b-ab33-aff766656b3f','USER');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-21 21:28:56
