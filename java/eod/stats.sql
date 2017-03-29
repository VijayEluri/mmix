insert into stats 
select 'index_by',
	   max(decode(a.name, 'session uga memory max', b.value, null)) uga,
	   max(decode(a.name, 'session pga memory max', b.value, null)) pga
  from v$statname a, v$mystat b
 where a.name like '%memory%max'
   and a.statistic# = b.statistic#