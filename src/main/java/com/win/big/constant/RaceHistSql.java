package com.win.big.constant;

public class RaceHistSql {


    public static final String SQL = "select json_agg(t_final) as result\n" +
        "from\n" +
        "(\n" +
            "select \n" +
            "* \n" +
            "from \n" +
            "(\n" +
                "select \n" +
                "t1.race_no,\n" +
                "t1.horse_rank,\n" +
                "sum(t1.win) as win,\n" +
                "sum(t1.place1) as place1,\n" +
                "sum(t1.place2) as place2,\n" +
                "sum(t1.place3) as place3,\n" +
                "sum(t1.quinella) as quinella,\n" +
                "sum(t1.quinellaPlace1) as quinellaPlace1,\n" +
                "sum(t1.quinellaPlace2) as quinellaPlace2,\n" +
                "sum(t1.quinellaPlace3) as quinellaPlace3,\n" +
                "(select case when sum(t1.threePickOne1) = -1 then 0 else sum(t1.threePickOne1) end) as threePickOne1,\n" +
                "(select case when sum(t1.threePickOne2) = -1 then 0 else sum(t1.threePickOne2) end) as threePickOne2,\n" +
                "(select case when sum(t1.threePickOne3) = -1 then 0 else sum(t1.threePickOne3) end) as threePickOne3,\n" +
                "sum(t1.tierce) as tierce,\n" +
                "sum(t1.trio) as trio,\n" +
                "sum(t1.first4) as first4,\n" +
                "sum(t1.quartet) as quartet,\n" +
                "sum(t1.double1) as double1,\n" +
                "sum(t1.double2) as double2,\n" +
                "sum(t1.doubleTrio) as doubleTrio,\n" +
                "sum(t1.tripleTrio) as tripleTrio,\n" +
                "sum(t1.tripleTrioWeak) as tripleTrioWeak,\n" +
                "sum(t1.sixUp1) as sixUp1,\n" +
                "sum(t1.sixUp2) as sixUp2\n" +
                "from \n" +
                "(\n" +
                    "select \n" +
                    "t.race_no, \n" +
                    "t.horse_rank,\n" +
                    "(select case when t.pool = 'WIN' then t.ratio else 0 end) as win, /*獨贏*/\n" +
                    "(select case when t.pool = 'PLACE' and t.order = '0' then t.ratio else 0 end) as place1, /*獨贏*/\n" +
                    "(select case when t.pool = 'PLACE' and t.order = '1' then t.ratio else 0 end) as place2, /*位置*/\n" +
                    "(select case when t.pool = 'PLACE' and t.order = '2' then t.ratio else 0 end) as place3, /*位置*/\n" +
                    "(select case when t.pool = 'QUINELLA' then t.ratio else 0 end) as quinella, /*連贏*/\n" +
                    "(select case when t.pool = 'QUINELLA PLACE' and t.order = '0' then t.ratio else 0 end) as quinellaPlace1, /*位置Q*/\n" +
                    "(select case when t.pool = 'QUINELLA PLACE' and t.order = '1' then t.ratio else 0 end) as quinellaPlace2, /*位置Q*/\n" +
                    "(select case when t.pool = 'QUINELLA PLACE' and t.order = '2' then t.ratio else 0 end) as quinellaPlace3, /*位置Q*/\n" +
                    "(select case when t.pool = '3 PICK 1' and t.order = '0' then t.ratio else 0 end) as threePickOne1, /*3揀1*/\n" +
                    "(select case when t.pool = '3 PICK 1' and t.order = '1' then t.ratio else 0 end) as threePickOne2, /*3揀1*/\n" +
                    "(select case when t.pool = '3 PICK 1' and t.order = '2' then t.ratio else 0 end) as threePickOne3, /*3揀1*/\n" +
                    "(select case when t.pool = 'TIERCE' then t.ratio else 0 end) as tierce, /*三重彩*/\n" +
                    "(select case when t.pool = 'TRIO' then t.ratio else 0 end) as trio, /*單T*/\n" +
                    "(select case when t.pool = 'FIRST 4' then t.ratio else 0 end) as first4, /*四連環*/\n" +
                    "(select case when t.pool = 'QUARTET' then t.ratio else 0 end) as quartet, /*四重彩*/\n" +
                    "(select case when t.pool like '% DOUBLE' and t.order = '0' then t.ratio else 0 end) as double1, /*孖寶*/\n" +
                    "(select case when t.pool like '% DOUBLE' and t.order = '1' then t.ratio else 0 end) as double2, /*孖寶*/\n" +
                    "(select case when t.pool like '% DOUBLE TRIO' then t.ratio else 0 end) as doubleTrio, /*孖T*/\n" +
                    "(select case when t.pool = 'TRIPLE TRIO' then t.ratio else 0 end) as tripleTrio, /*三T*/\n" +
                    "(select case when t.pool = 'TRIPLE TRIO(Consolation)' then t.ratio else 0 end) as tripleTrioWeak, /*三T(安慰獎)*/\n" +
                    "(select case when t.pool = 'SIX UP' and t.order = '0' then t.ratio else 0 end) as sixUp1, /*六環彩*/\n" +
                    "(select case when t.pool = 'SIX UP' and t.order = '1' then t.ratio else 0 end) as sixUp2 /*六環彩*/\n" +
                    "\n" +
                    "\n" +
                    "from \n" +
                    "(\n" +
                        "select race_no, \n" +
                        "horse_rank,\n" +
                        "dividend->>'pool' as pool,\n" +
                        "dividend->>'order' as order,\n" +
                        "cast(dividend->>'ratio' as float) as ratio\n" +
                        "from\n" +
                        "race_hist hist,\n" +
                        "jsonb_array_elements(race_dividend) dividend\n" +
                        "where \n" +
                        "1=1\n" +
                        "and hist.race_date = :date\n" +
                    ") t\n" +
                ") t1\n" +
                "group by t1.race_no, t1.horse_rank\n" +
                "order by t1.race_no\n" +
            ") race_hist_d\n" +
            "left join \n" +
            "(\n" +
                "select\n" +
                "t1.race_no,\n" +
                "sum(t1.win1) as win1,\n" +
                "sum(t1.win2) as win2,\n" +
                "sum(t1.win3) as win3,\n" +
                "sum(t1.win4) as win4,\n" +
                "max(t1.draw1) as draw1,\n" +
                "max(t1.draw2) as draw2,\n" +
                "max(t1.draw3) as draw3,\n" +
                "max(t1.draw4) as draw4,\n" +
                "max(t1.jockey1) as jockey1,\n" +
                "max(t1.jockey2) as jockey2,\n" +
                "max(t1.jockey3) as jockey3,\n" +
                "max(t1.jockey4) as jockey4,\n" +
                "max(t1.trainer1) as trainer1,\n" +
                "max(t1.trainer2) as trainer2,\n" +
                "max(t1.trainer3) as trainer3,\n" +
                "max(t1.trainer4) as trainer4\n" +
                "from\n" +
                "(\n" +
                    "select \n" +
                    "t.race_no,\n" +
                    "(select case when t.plc='1' then t.ratio else 0 end) as win1,\n" +
                    "(select case when t.plc='2' then t.ratio else 0 end) as win2,\n" +
                    "(select case when t.plc='3' then t.ratio else 0 end) as win3,\n" +
                    "(select case when t.plc='4' then t.ratio else 0 end) as win4,\n" +
                    "(select case when t.plc='1' then t.draw else '' end) as draw1,\n" +
                    "(select case when t.plc='2' then t.draw else '' end) as draw2,\n" +
                    "(select case when t.plc='3' then t.draw else '' end) as draw3,\n" +
                    "(select case when t.plc='4' then t.draw else '' end) as draw4,\n" +
                    "(select case when t.plc='1' then t.jockey else '' end) as jockey1,\n" +
                    "(select case when t.plc='2' then t.jockey else '' end) as jockey2,\n" +
                    "(select case when t.plc='3' then t.jockey else '' end) as jockey3,\n" +
                    "(select case when t.plc='4' then t.jockey else '' end) as jockey4,\n" +
                    "(select case when t.plc='1' then t.trainer else '' end) as trainer1,\n" +
                    "(select case when t.plc='2' then t.trainer else '' end) as trainer2,\n" +
                    "(select case when t.plc='3' then t.trainer else '' end) as trainer3,\n" +
                    "(select case when t.plc='4' then t.trainer else '' end) as trainer4\n" +
                    "from \n" +
                    "(\n" +
                        "select race_no, \n" +
                        "hInfo->>'name' as name,\n" +
                        "hInfo->>'plc' as plc,\n" +
                        "hInfo->>'draw' as draw,\n" +
                        "hInfo->>'trainer' as trainer,\n" +
                        "hInfo->>'jockey' as jockey,\n" +
                        "cast(hInfo->>'ratio' as float) as ratio\n" +
                        "from\n" +
                        "race_hist hist,\n" +
                        "jsonb_array_elements(horse_info) hInfo\n" +
                        "where \n" +
                        "1=1\n" +
                        "and hist.race_date = :date\n" +
                    ") t\n" +
                ") t1\n" +
                "group by t1.race_no\n" +
            ") race_hist_h\n" +
            "on race_hist_d.race_no = race_hist_h.race_no \n" +
            "order by race_hist_d.race_no\n" +
        ") as t_final";
}
