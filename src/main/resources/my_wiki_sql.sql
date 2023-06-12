-- noinspection SqlDialectInspectionForFile
-- noinspection SqlNoDataSourceInspectionForFile

-- my_wiki 建库脚本
CREATE TABLE `wiki_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(500) NOT NULL,
  `created_datetime` datetime NOT NULL,
  `updated_datetime` datetime NOT NULL,
  `is_default` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `is_blog` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `wiki_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  `upload_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `wiki` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(500) NOT NULL,
  `category_type_id` int NOT NULL,
  `markdown_content` longtext,
  `created_datetime` datetime NOT NULL,
  `updated_datetime` datetime NOT NULL,
  `access_datetime` datetime DEFAULT NULL,
  `page_viewed_number` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `category_type_id_idx` (`category_type_id`),
  CONSTRAINT `category_type_id` FOREIGN KEY (`category_type_id`) REFERENCES `wiki_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `nav_node` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `is_root` tinyint(1) NOT NULL,
  `parent_id` int DEFAULT NULL,
  `target` text,
  `depth` int NOT NULL,
  `category_type_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_id_idx` (`parent_id`),
  KEY `nav_node_wiki_category` (`category_type_id`),
  CONSTRAINT `nav_node_wiki_category` FOREIGN KEY (`category_type_id`) REFERENCES `wiki_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `parent_id` FOREIGN KEY (`parent_id`) REFERENCES `nav_node` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=601 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `application_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_expired` bit(1) NOT NULL,
  `locked` bit(1) NOT NULL,
  `credentials_expired` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(45) DEFAULT NULL,
  `user_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 插入管理员用户信息
INSERT INTO `application_user` (`account_expired`, `locked`, `credentials_expired`, `enabled`, `password`, `role`, `user_name`)
VALUES
	(0, 0, 0, 1, 'xxxxxxxx', 'ADMIN', 'peng');

-- 插入首页blog的wiki分类
INSERT INTO `wiki_category` (`category_name`, `created_datetime`, `updated_datetime`, `is_default`, `is_active`, `is_blog`)
VALUES
	('主页分类', '2023-05-27 14:20:01', '2023-05-28 14:03:45', 0, 1, 1);

-- 插入默认的wiki分类
INSERT INTO `wiki_category` (`category_name`, `created_datetime`, `updated_datetime`, `is_default`, `is_active`, `is_blog`)
VALUES
	('Java教程', '2023-04-04 19:00:00', '2023-05-13 14:47:21', 1, 1, 0);
