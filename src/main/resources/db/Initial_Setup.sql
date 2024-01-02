CREATE TABLE IF NOT EXISTS `company_category` (
  `company_category_id` int NOT NULL AUTO_INCREMENT,
  `company_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`company_category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Company_category';


CREATE TABLE IF NOT EXISTS `education_class_details` (
  `class_id` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='education_class_Details';

INSERT INTO `education_class_details` (`class_id`, `class_name`) VALUES
	(1, 'HSC'),
	(2, 'SSLC');

CREATE TABLE IF NOT EXISTS `education_division` (
  `division_id` int NOT NULL AUTO_INCREMENT,
  `division_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`division_id`) USING BTREE,
  UNIQUE KEY `DepartmentName` (`division_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='education_division';


CREATE TABLE IF NOT EXISTS `higher_education_details` (
  `education_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `university_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `institution_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `degree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Field_of_study` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `graduation_year` int NOT NULL,
  `school_id` int NOT NULL,
  PRIMARY KEY (`education_id`) USING BTREE,
  KEY `FK_higher_education_details_school_details` (`school_id`) USING BTREE,
  KEY `FK_higher_education_details_user_profile` (`user_id`),
  CONSTRAINT `FK_higher_education_details_school_details` FOREIGN KEY (`school_id`) REFERENCES `school_details` (`school_id`),
  CONSTRAINT `FK_higher_education_details_user_profile` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Higher_Education_Details';


CREATE TABLE IF NOT EXISTS `professional_experience` (
  `experience_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `company_category_id` int NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_sate` date DEFAULT NULL,
  `description` text,
  `school_id` int NOT NULL,
  PRIMARY KEY (`experience_id`) USING BTREE,
  KEY `FK_professional_experience_company_category` (`company_category_id`),
  KEY `FK_professional_experience_school_details` (`school_id`) USING BTREE,
  KEY `FK_professional_experience_user_profile` (`user_id`),
  CONSTRAINT `FK_professional_experience_company_category` FOREIGN KEY (`company_category_id`) REFERENCES `company_category` (`company_category_id`),
  CONSTRAINT `FK_professional_experience_school_details` FOREIGN KEY (`school_id`) REFERENCES `school_details` (`school_id`),
  CONSTRAINT `FK_professional_experience_user_profile` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Professional_Experience';


CREATE TABLE IF NOT EXISTS `role_permission` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `permission_id` int NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `role_id_permission_id` (`role_id`,`permission_id`),
  KEY `FK_role_permission_user_permission` (`permission_id`),
  CONSTRAINT `FK_role_permission_user_permission` FOREIGN KEY (`permission_id`) REFERENCES `user_permission` (`permission_id`),
  CONSTRAINT `FK_role_permission_user_role` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `school_details` (
  `school_id` int NOT NULL AUTO_INCREMENT,
  `school_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `school_adress` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `state_Id` int DEFAULT NULL,
  `district_id` int DEFAULT NULL,
  `thaluk_id` int DEFAULT NULL,
  PRIMARY KEY (`school_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='School_details';


CREATE TABLE IF NOT EXISTS `teachers_school_record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `joined_year` int DEFAULT NULL,
  `school_last_year` int DEFAULT NULL,
  `teached_division_Ids` json NOT NULL COMMENT 'list of division ids',
  `teached_class_Ids` json NOT NULL COMMENT 'list of class ids',
  `type` int DEFAULT NULL,
  `school_id` int NOT NULL,
  PRIMARY KEY (`record_id`) USING BTREE,
  KEY `FK_teachers_school_record_teachers_type` (`type`),
  KEY `FK_teachers_school_record_school_details` (`school_id`),
  KEY `FK_teachers_school_record_user_profile` (`user_id`),
  CONSTRAINT `FK_teachers_school_record_school_details` FOREIGN KEY (`school_id`) REFERENCES `school_details` (`school_id`),
  CONSTRAINT `FK_teachers_school_record_teachers_type` FOREIGN KEY (`type`) REFERENCES `teachers_type` (`type_id`),
  CONSTRAINT `FK_teachers_school_record_user_profile` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='teachers_school_record';


CREATE TABLE IF NOT EXISTS `teachers_type` (
  `type_id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='teachers_type';


CREATE TABLE IF NOT EXISTS `user_login` (
  `login_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_login` timestamp NULL DEFAULT NULL,
  `login_attempts` int NOT NULL DEFAULT '0',
  `is_locked` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`login_id`) USING BTREE,
  UNIQUE KEY `User_name` (`user_name`) USING BTREE,
  KEY `FK_user_login_user_profile` (`user_id`),
  CONSTRAINT `FK_user_login_user_profile` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='User_Login';


CREATE TABLE IF NOT EXISTS `user_permission` (
  `permission_id` int NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`permission_id`) USING BTREE,
  UNIQUE KEY `PermissionName` (`permission_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `user_privacy_settings` (
  `user_id` bigint NOT NULL,
  `public_fields` json DEFAULT NULL COMMENT 'list of fields that the user has set as public',
  `private_fields` json DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `FK_user_privacy_settings_user_profile` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='user_Privacy_Settings';


CREATE TABLE IF NOT EXISTS `user_profile` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `first_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `birth_date` date NOT NULL,
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `profile_picture_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `cover_photo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `registration_date` timestamp NULL DEFAULT (now()),
  `last_login` timestamp NULL DEFAULT NULL,
  `user_role_Id` int NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `Email` (`email`) USING BTREE,
  UNIQUE KEY `PhoneNumber` (`phone_number`) USING BTREE,
  KEY `FK_userprofile_userrole` (`user_role_Id`) USING BTREE,
  CONSTRAINT `FK_user_profile_user_role` FOREIGN KEY (`user_role_Id`) REFERENCES `user_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `user_role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `RoleName` (`role_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `user_role_validation` (
  `role_validation_id` int NOT NULL AUTO_INCREMENT,
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `role_id` int NOT NULL,
  `validator_id` int NOT NULL,
  PRIMARY KEY (`role_validation_id`) USING BTREE,
  UNIQUE KEY `Display_Name` (`display_name`) USING BTREE,
  KEY `FK_user_role_validation_user_role` (`role_id`) USING BTREE,
  KEY `FK_user_role_validation_validation_flow_details` (`validator_id`) USING BTREE,
  CONSTRAINT `FK_user_role_validation_user_role` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`role_id`),
  CONSTRAINT `FK_user_role_validation_validation_flow_details` FOREIGN KEY (`validator_id`) REFERENCES `validation_flow_details` (`validation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='user_role_validation';


CREATE TABLE IF NOT EXISTS `user_school_record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `enrollment_year` int NOT NULL,
  `school_last_year` int NOT NULL,
  `last_class_id` int NOT NULL,
  `school_division_Id` int NOT NULL,
  `school_id` int NOT NULL,
  PRIMARY KEY (`record_id`) USING BTREE,
  KEY `FK_user_school_record_school_division` (`school_division_Id`),
  KEY `FK_user_school_record_school_qualification` (`last_class_id`) USING BTREE,
  KEY `FK_school_record_user_profile` (`user_id`) USING BTREE,
  KEY `FK_user_school_record_school_details` (`school_id`) USING BTREE,
  CONSTRAINT `FK_user_school_record_school_details` FOREIGN KEY (`school_id`) REFERENCES `school_details` (`school_id`),
  CONSTRAINT `FK_user_school_record_school_division` FOREIGN KEY (`school_division_Id`) REFERENCES `education_division` (`division_id`),
  CONSTRAINT `FK_user_school_record_school_qualification` FOREIGN KEY (`last_class_id`) REFERENCES `education_class_details` (`class_id`),
  CONSTRAINT `FK_user_school_record_user_profile` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `validation_flow_details` (
  `validation_id` int NOT NULL AUTO_INCREMENT,
  `validator_flow_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`validation_id`) USING BTREE,
  UNIQUE KEY `bean_name` (`bean_name`) USING BTREE,
  UNIQUE KEY `Validator_Name` (`validator_flow_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='validation_flow_details';