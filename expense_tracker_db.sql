CREATE DATABASE IF NOT EXISTS `expense-tracker-api` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `expense-tracker-api`;

DROP TABLE IF EXISTS `et_users`;
create table et_users(
	user_id integer primary key not null AUTO_INCREMENT,
	first_name varchar(20) not null,
	last_name varchar(20) not null,
	email varchar(30) not null,
	password text not null
);

DROP TABLE IF EXISTS `et_categories`;
create table et_categories(
	category_id integer primary key not null AUTO_INCREMENT,
	user_id integer not null,
	title varchar(20) not null,
	description varchar(50) not null
);
alter table et_categories add constraint cat_users_fk
foreign key (user_id) references et_users(user_id);

DROP TABLE IF EXISTS `et_transactions`;
create table et_transactions(
	transaction_id integer primary key not null AUTO_INCREMENT,
	category_id integer not null,
	user_id integer not null,
	amount numeric(10,2) not null,
	note varchar(50) not null,
	transaction_date bigint not null	
);
alter table et_transactions add constraint trans_cat_fk
foreign key (category_id) references et_categories(category_id);
alter table et_transactions add constraint trans_users_fk
foreign key (user_id) references et_users(user_id);
alter table et_transactions AUTO_INCREMENT=1000;