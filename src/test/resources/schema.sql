create table COMMENT (comment_id bigint not null auto_increment, author varchar(30), created_time datetime(6), last_modified_time datetime(6), post_id bigint, recommendation_count integer, report_count integer, text text, parent_id bigint, primary key (comment_id)) engine=InnoDB;
create table MEMBER (member_id bigint not null auto_increment, email varchar(50), name varchar(30), nickname varchar(30), role varchar(10), profile_file_id bigint, primary key (member_id)) engine=InnoDB;
create table POST (post_id bigint not null auto_increment, author varchar(30), category varchar(20), created_time datetime(6), last_modified_time datetime(6), recommendation_count integer, report_count integer, text text, view_count integer, primary key (post_id)) engine=InnoDB;
create table POST_FILE (file_id bigint not null auto_increment, file_type varchar(50), store_file_name varchar(50), upload_file_name varchar(50), post_id bigint, primary key (file_id)) engine=InnoDB;
create table PROFILE_FILE (file_id bigint not null auto_increment, store_file_name varchar(50), primary key (file_id)) engine=InnoDB;
alter table COMMENT add constraint FK_COMMENT_COMMENT foreign key (parent_id) references COMMENT (comment_id);
alter table COMMENT add constraint FK_COMMENT_POST foreign key (comment_id) references POST (post_id);
alter table MEMBER add constraint FK_MEMBER_PROFILE_FILE foreign key (profile_file_id) references PROFILE_FILE (file_id);
alter table POST_FILE add constraint FK_POST_FILE_POST foreign key (post_id) references POST (post_id);