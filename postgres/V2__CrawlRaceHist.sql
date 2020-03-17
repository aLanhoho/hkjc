select 
t1.race_no,
t1.horse_rank,
sum(t1.win) as win,
sum(t1.place1) as place1,
sum(t1.place2) as place2,
sum(t1.place3) as place3,
sum(t1.quinella) as quinella,
sum(t1.quinellaPlace1) as quinellaPlace1,
sum(t1.quinellaPlace2) as quinellaPlace2,
sum(t1.quinellaPlace3) as quinellaPlace3,
sum(t1.tierce) as tierce,
sum(t1.trio) as trio,
sum(t1.first4) as first4,
sum(t1.quartet) as quartet,
sum(t1.double1) as double1,
sum(t1.double2) as double2,
sum(t1.doubleTrio) as doubleTrio,
sum(t1.tripleTrio) as tripleTrio,
sum(t1.tripleTrioWeak) as tripleTrioWeak,
sum(t1.sixUp) as sixUp
from 
(
	select 
	t.race_no, 
	t.horse_rank,
	(select case when t.pool = 'WIN' then t.ratio else 0 end) as win, /*獨贏*/
	(select case when t.pool = 'PLACE' and t.order = '0' then t.ratio else 0 end) as place1, /*獨贏*/
	(select case when t.pool = 'PLACE' and t.order = '1' then t.ratio else 0 end) as place2, /*位置*/
	(select case when t.pool = 'PLACE' and t.order = '2' then t.ratio else 0 end) as place3, /*位置*/
	(select case when t.pool = 'QUINELLA' then t.ratio else 0 end) as quinella, /*連贏*/
	(select case when t.pool = 'QUINELLA PLACE' and t.order = '0' then t.ratio else 0 end) as quinellaPlace1, /*位置Q*/
	(select case when t.pool = 'QUINELLA PLACE' and t.order = '1' then t.ratio else 0 end) as quinellaPlace2, /*位置Q*/
	(select case when t.pool = 'QUINELLA PLACE' and t.order = '2' then t.ratio else 0 end) as quinellaPlace3, /*位置Q*/
	(select case when t.pool = 'TIERCE' then t.ratio else 0 end) as tierce, /*三重彩*/
	(select case when t.pool = 'TRIO' then t.ratio else 0 end) as trio, /*單T*/
	(select case when t.pool = 'FIRST 4' then t.ratio else 0 end) as first4, /*四連環*/
	(select case when t.pool = 'QUARTET' then t.ratio else 0 end) as quartet, /*四重彩*/
	(select case when t.pool like '% DOUBLE' and t.order = '0' then t.ratio else 0 end) as double1, /*孖寶*/
	(select case when t.pool like '% DOUBLE' and t.order = '1' then t.ratio else 0 end) as double2, /*孖寶*/
	(select case when t.pool like '% DOUBLE TRIO' then t.ratio else 0 end) as doubleTrio, /*孖T*/
	(select case when t.pool = 'TRIPLE TRIO' then t.ratio else 0 end) as tripleTrio, /*三T*/
	(select case when t.pool = 'TRIPLE TRIO(Consolation)' then t.ratio else 0 end) as tripleTrioWeak, /*三T(安慰獎)*/
	(select case when t.pool = 'SIX UP' then t.ratio else 0 end) as sixUp /*六環彩*/

	
	from 
	(
		select race_no, 
		horse_rank,
		dividend->>'pool' as pool,
		dividend->>'order' as order,
		cast(dividend->>'ratio' as float) as ratio
		from
		race_hist hist,
		jsonb_array_elements(race_dividend) dividend
		where 
		1=1
		and hist.race_date = '2020/03/14'
	) t
) t1
group by t1.race_no, t1.horse_rank
order by t1.race_no