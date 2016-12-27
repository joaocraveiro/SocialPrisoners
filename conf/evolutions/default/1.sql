# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table game (
  id                            integer not null,
  startrounds                   integer,
  round                         integer,
  waiting_pairs                 integer,
  round_done                    boolean,
  constraint pk_game primary key (id)
);
create sequence game_seq;

create table matchup (
  id                            integer not null,
  player1_name                  varchar(255),
  player2_name                  varchar(255),
  game_id                       integer,
  round                         integer,
  p1choice1                     integer,
  p2choice1                     integer,
  p1choice2                     integer,
  p2choice2                     integer,
  constraint uq_matchup_player1_name unique (player1_name),
  constraint uq_matchup_player2_name unique (player2_name),
  constraint pk_matchup primary key (id)
);
create sequence matchup_seq;

create table player (
  name                          varchar(255) not null,
  password                      varchar(255),
  npc                           boolean,
  game_id                       integer,
  matchup_id                    integer,
  economic_score                integer,
  social_score                  integer,
  constraint uq_player_matchup_id unique (matchup_id),
  constraint pk_player primary key (name)
);

alter table matchup add constraint fk_matchup_player1_name foreign key (player1_name) references player (name) on delete restrict on update restrict;

alter table matchup add constraint fk_matchup_player2_name foreign key (player2_name) references player (name) on delete restrict on update restrict;

alter table matchup add constraint fk_matchup_game_id foreign key (game_id) references game (id) on delete restrict on update restrict;
create index ix_matchup_game_id on matchup (game_id);

alter table player add constraint fk_player_game_id foreign key (game_id) references game (id) on delete restrict on update restrict;
create index ix_player_game_id on player (game_id);

alter table player add constraint fk_player_matchup_id foreign key (matchup_id) references matchup (id) on delete restrict on update restrict;


# --- !Downs

alter table matchup drop constraint if exists fk_matchup_player1_name;

alter table matchup drop constraint if exists fk_matchup_player2_name;

alter table matchup drop constraint if exists fk_matchup_game_id;
drop index if exists ix_matchup_game_id;

alter table player drop constraint if exists fk_player_game_id;
drop index if exists ix_player_game_id;

alter table player drop constraint if exists fk_player_matchup_id;

drop table if exists game;
drop sequence if exists game_seq;

drop table if exists matchup;
drop sequence if exists matchup_seq;

drop table if exists player;

