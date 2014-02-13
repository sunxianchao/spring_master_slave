--到写库执行以下
drop table if exists user;

create table user
(
   id                    int not null auto_increment,
   name                   varchar(200),
   constraint pk_user primary key (id)
) charset=utf8 ENGINE=InnoDB;


create table address(
   id                  int not null auto_increment,
   userId              int,
   city                varchar(200),
   constraint pk_address primary key(id),
   constraint fk_address_user foreign key(userId) references user(id) on delete cascade
   
)charset=utf8 ENGINE=InnoDB;