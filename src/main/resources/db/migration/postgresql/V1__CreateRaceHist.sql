drop table if exists race_hist cascade;

create table race_hist (
	race_date varchar(10) not null,
	race_no int not null,
	create_dt timestamp not null,
	update_dt timestamp not null,
	horse_rank jsonb not null,
	horse_info jsonb not null,
	race_details jsonb not null,
	race_dividend jsonb not null,
	constraint race_hist_pkey PRIMARY KEY (race_date, race_no)
);

CREATE INDEX race_hist_date_idx on race_hist(race_date);
