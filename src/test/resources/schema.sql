drop table if exists POST_FILE;
drop table if exists MEMBER;
drop table if exists PROFILE_FILE;
drop table if exists COMMENT;
drop table if exists TREE_PATH;
drop table if exists POST;

create table POST (
                      post_id bigint(15) not null auto_increment,
                      author varchar(30),
                      title text,
                      category varchar(20),
                      created_time datetime(6),
                      last_modified_time datetime(6),
                      recommendation_count integer,
                      report_count integer,
                      text text,
                      view_count integer,
                      primary key (post_id)) engine=InnoDB;
create table COMMENT (
    comment_id bigint(10) zerofill not null auto_increment,
    author varchar(30),
    created_time datetime(6),
    last_modified_time datetime(6),
    recommendation_count integer,
    report_count integer,
    text text,
    post_id bigint(15),
    primary key (comment_id),
    constraint FK_COMMENT_POST foreign key (post_id) references POST(post_id)) engine=InnoDB;
create table PROFILE_FILE (
                              file_id bigint(15) not null auto_increment,
                              store_file_name varchar(50),
                              primary key (file_id)) engine=InnoDB;
create table MEMBER (
    member_id bigint(10) not null auto_increment,
    email varchar(50),
    name varchar(30),
    nickname varchar(30),
    role varchar(10),
    profile_file_id bigint(15),
    primary key (member_id),
    constraint FK_MEMBER_PROFILE_FILE foreign key (profile_file_id) references PROFILE_FILE(file_id)) engine=InnoDB;
create table POST_FILE (
    file_id bigint(15) not null auto_increment,
    file_type varchar(50),
    store_file_name varchar(50),
    upload_file_name varchar(50),
    post_id bigint(15),
    primary key (file_id),
    constraint FK_POST_FILE_POST foreign key (post_id) references POST (post_id)) engine=InnoDB;
create table TREE_PATH(
    post_id    bigint(15) not null,
    ancestor   bigint(10) zerofill not null,
    descendant bigint(10) zerofill not null,
    depth      int,
    primary key (post_id, ancestor, descendant),
    constraint FK_TREE_PATH_POST_ID_COMMENT
        foreign key (post_id) references COMMENT (post_id),
    constraint FK_TREE_PATH_ANCESTOR_COMMENT
        foreign key (ancestor) references COMMENT (comment_id),
    constraint FK_TREE_PATH_DESCENDANT_COMMENT
        foreign key (descendant) references COMMENT (comment_id)
) engine=InnoDB;
