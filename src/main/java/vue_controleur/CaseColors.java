package vue_controleur;

import java.awt.*;

public enum CaseColors {
    COL2(new Color(252, 176, 69)),
    COL4(new Color(242, 151, 70)),
    COL8(new Color(233, 126, 71)),
    COL16(new Color(223, 101, 72)),
    COL32(new Color(213, 76, 74)),
    COL64(new Color(204, 51, 75)),
    COL128(new Color(189, 52, 96)),
    COL256(new Color(174, 53, 117)),
    COL512(new Color(160, 55, 138)),
    COL1024(new Color(145, 56, 159)),
    COL2048(new Color(131, 58, 180));

    private final Color color;

    CaseColors(Color theColor) {
        color = theColor;
    }

    public Color getColor() {
        return color;
    }
}
