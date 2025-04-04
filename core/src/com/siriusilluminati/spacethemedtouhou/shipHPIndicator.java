package com.siriusilluminati.spacethemedtouhou;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class shipHPIndicator {

    public static Sprite shipHbSpriteCalcer(TextureAtlas textatlas, int health, int drainValue, int fillValue) {
        // best implementation fr fr
        Sprite result;
        String status;
        String changeValue = "";
        if (drainValue == 0 & fillValue == 0) {
            status = "static";
        } else if (drainValue != 0) {
            status = "drain";
            changeValue = "-" + drainValue;
        } else {
            status = "fill";
            changeValue = "-" + fillValue;
        }

        result = textatlas.createSprite("hb-" + status + "-" + health + changeValue);
        return result;
    }

}
