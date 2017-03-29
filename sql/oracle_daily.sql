--2016 Feb 4th
--when I touch Oracle again, I want to record the useful SQL I used to learn and--dig into the system.
--all_tables is the start point, it is actually is a view name.
select table_name from all_tables where table_name=upper('all_tables');

select table_name from all_tables where table_name=upper('dba_tables');

select view_name from all_views where view_name=upper('all_tables');

select view_name from all_views where view_name=upper('dba_tables');

select view_name from all_views where view_name=upper('all_views');


select table_name from all_tables where table_name like '%SPACE%';
select view_name from all_views where view_name like '%SPACE%';

describe user_tablespaces;
SQL> select tablespace_name from user_tablespaces;

TABLESPACE_NAME
------------------------------
SYSTEM
SYSAUX
UNDOTBS1
TEMP
USERS


create user a identified by a tablespace users;

grant create session to a;
grant create table to a;



create table t as select * from all_users;
create unique index t_idx1 on t(username);
create index t_idx2 on t(created);
analyze table t compute statistics
	for table
	for all indexed columns
	for all indexes;

