# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table game (
  id                        integer not null,
  startrounds               integer,
  round                     integer,
  waiting_pairs             integer,
  round_done                boolean,
  constraint pk_game primary key (id))
;

create table matchup (
  id                        integer not null,
  player1_name              varchar(255),
  player2_name              varchar(255),
  game_id                   integer,
  round                     integer,
  p1choice1                 integer,
  p2choice1                 integer,
  p1choice2                 integer,
  p2choice2                 integer,
  constraint pk_matchup primary key (id))
;

create table player (
  name                      varchar(255) not null,
  password                  varchar(255),
  npc                       boolean,
  game_id                   integer,
  matchup_id                integer,
  economic_score            integer,
  social_score              integer,
  constraint pk_player primary key (name))
;

create sequence game_seq;

create sequence matchup_seq;

create sequence player_seq;

alter table matchup add constraint fk_matchup_player1_1 foreign key (player1_name) references player (name) on delete restrict on update restrict;
create index ix_matchup_player1_1 on matchup (player1_name);
alter table matchup add constraint fk_matchup_player2_2 foreign key (player2_name) references player (name) on delete restrict on update restrict;
create index ix_matchup_player2_2 on matchup (player2_name);
alter table matchup add constraint fk_matchup_game_3 foreign key (game_id) references game (id) on delete restrict on update restrict;
create index ix_matchup_game_3 on matchup (game_id);
alter table player add constraint fk_player_game_4 foreign key (game_id) references game (id) on delete restrict on update restrict;
create index ix_player_game_4 on player (game_id);
alter table player add constraint fk_player_matchup_5 foreign key (matchup_id) references matchup (id) on delete restrict on update restrict;
create index ix_player_matchup_5 on player (matchup_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists game;

drop table if exists matchup;

drop table if exists player;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists game_seq;

drop sequence if exists matchup_seq;

drop sequence if exists player_seq;

