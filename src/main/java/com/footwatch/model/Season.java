package com.footwatch.model;

public enum Season {
    S2000E2001,
    S2001E2002,
    S2002E2003,
    S2003E2004,
    S2004E2005,
    S2005E2006,
    S2006E2007,
    S2007E2008,
    S2008E2009,
    S2009E2010,
    S2010E2011,
    S2011E2012,
    S2012E2013,
    S2013E2014,
    S2014E2015,
    S2015E2016,
    S2016E2017,
    S2017E2018,
    S2018E2019,
    S2019E2020,
    S2020E2021;

    @Override
    public String toString() {
        return super.toString().replace("S", "").replace("E", "/");
    }

    public static Season getEnum(String value) {
        for (Season season : values())
            if (season.toString().equalsIgnoreCase(value)) return season;
        throw new IllegalArgumentException();
    }
}
