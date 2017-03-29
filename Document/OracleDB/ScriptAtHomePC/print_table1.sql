create or replace procedure 
print_table ( p_query in varchar2 )
authid current_user
is
	theCursor 	integer default dbms_sql.open_cursor;
	columnValue 	varchar2(4000);
	status 		integer;
	descTable	dbms_sql.desc_tab;
	columnCount	number;
	cs 		varchar2;
	dateFormat	varchar2(255);

	
begin
	dbms_sql.parse( theCursor, p_query, dbms_sql.native);
	dbms_sql.describe_columns( theCursor, columnCount, descTable );
	for i  in 1 ..columnCount loop
		(theCursor, i, columnValue, 4000);
	endloop;

	status := dbms_sql.execute(theCursor);

	while( dbms_sql.fetch_rows(theCursor) > 0)
	loop
		for i in 1..columnCount loop
			dbms_sql.columnValue(theCursor, i, columnValue );
			dbms_outpout.putline( rpad(descTable(i).col_name, 30 ) 
			|| ':' || substr(columnValue, 1, 200 ) );
		endloop;
		dbms_output.put_line('---------------------');
	endloop;
exception
 	when others then
		restore;
		raise;
end;
/
	