select
t1.race_no,
sum(t1.win1) as win1,
sum(t1.win2) as win2,
sum(t1.win3) as win3,
sum(t1.win4) as win4,
max(t1.draw1) as draw1,
max(t1.draw2) as draw2,
max(t1.draw3) as draw3,
max(t1.draw4) as draw4,
max(t1.jockey1) as jockey1,
max(t1.jockey2) as jockey2,
max(t1.jockey3) as jockey3,
max(t1.jockey4) as jockey4,
max(t1.trainer1) as trainer1,
max(t1.trainer2) as trainer2,
max(t1.trainer3) as trainer3,
max(t1.trainer4) as trainer4
from
(
	select
	t.race_no,
	(select case when t.plc='1' then t.ratio else 0 end) as win1,
	(select case when t.plc='2' then t.ratio else 0 end) as win2,
	(select case when t.plc='3' then t.ratio else 0 end) as win3,
	(select case when t.plc='4' then t.ratio else 0 end) as win4,
	(select case when t.plc='1' then t.draw else '' end) as draw1,
	(select case when t.plc='2' then t.draw else '' end) as draw2,
	(select case when t.plc='3' then t.draw else '' end) as draw3,
	(select case when t.plc='4' then t.draw else '' end) as draw4,
	(select case when t.plc='1' then t.jockey else '' end) as jockey1,
	(select case when t.plc='2' then t.jockey else '' end) as jockey2,
	(select case when t.plc='3' then t.jockey else '' end) as jockey3,
	(select case when t.plc='4' then t.jockey else '' end) as jockey4,
	(select case when t.plc='1' then t.trainer else '' end) as trainer1,
	(select case when t.plc='2' then t.trainer else '' end) as trainer2,
	(select case when t.plc='3' then t.trainer else '' end) as trainer3,
	(select case when t.plc='4' then t.trainer else '' end) as trainer4
	from
	(
		select race_no,
		hInfo->>'name' as name,
		hInfo->>'plc' as plc,
		hInfo->>'draw' as draw,
		hInfo->>'trainer' as trainer,
		hInfo->>'jockey' as jockey,
		cast(hInfo->>'ratio' as float) as ratio
		from
		race_hist hist,
		jsonb_array_elements(horse_info) hInfo
		where
		1=1
		and hist.race_date = '2020/03/18'
	) t
) t1
group by t1.race_no
