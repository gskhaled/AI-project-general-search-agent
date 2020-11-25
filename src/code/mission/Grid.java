package mission;

import java.util.Arrays;

public class Grid {
    private short rows;
    private short columns;
    private short xEthan;
    private short yEthan;
    private short xSub;
    private short ySub;
    private short[] xPoss;
    private short[] yPoss;
    private short[] damages;
    private short c;

    public Grid(String grid) {
        String[] s = grid.split(";");
        String[] rc = s[0].split(",");
        rows = Short.parseShort(rc[0]);
        columns = Short.parseShort(rc[1]);

        String[] xyEthan = s[1].split(",");
        xEthan = Short.parseShort(xyEthan[0]);
        yEthan = Short.parseShort(xyEthan[1]);

        String[] xySub = s[2].split(",");
        xSub = Short.parseShort(xySub[0]);
        ySub = Short.parseShort(xySub[1]);

        String[] xyPoss = s[3].split(",");
        xPoss = new short[xyPoss.length / 2];
        yPoss = new short[xyPoss.length / 2];
        int j = 0;
        for (int i = 0; i < xyPoss.length; i++) {
            xPoss[j] = Short.parseShort(xyPoss[i]);
            i++;
            yPoss[j] = Short.parseShort(xyPoss[i]);
            j++;
        }

        String[] damagess = s[4].split(",");
        damages = new short[damagess.length];
        for (int i = 0; i < damagess.length; i++) {
            damages[i] = Short.parseShort(damagess[i]);
        }

        c = Short.parseShort(s[5]);
    }

    @Override
    public String toString() {
        return "Grid{" +
                "rows=" + rows +
                ", columns=" + columns +
                ", xEthan=" + xEthan +
                ", yEthan=" + yEthan +
                ", xSub=" + xSub +
                ", ySub=" + ySub +
                ", xPoss=" + Arrays.toString(xPoss) +
                ", yPoss=" + Arrays.toString(yPoss) +
                ", damages=" + Arrays.toString(damages) +
                ", c=" + c +
                '}';
    }

    public short getRows() {
        return rows;
    }

    public short getColumns() {
        return columns;
    }

    public short getxEthan() {
        return xEthan;
    }

    public short getyEthan() {
        return yEthan;
    }

    public short getxSub() {
        return xSub;
    }

    public short getySub() {
        return ySub;
    }

    public short[] getxPoss() {
        return xPoss;
    }

    public short[] getyPoss() {
        return yPoss;
    }

    public short[] getDamages() {
        return damages;
    }

    public short getC() {
        return c;
    }
}
