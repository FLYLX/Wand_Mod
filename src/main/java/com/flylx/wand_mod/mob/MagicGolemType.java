package com.flylx.wand_mod.mob;

public class MagicGolemType {
    public static String MagicTypeToString(MagicGolemTypes magicGolemTypes) {
        switch (magicGolemTypes) {
            case FIRE:
                return "fire";
            case FROZE:
                return "froze";
            case POISON:
                return "poison";
            case END:
                return "end";
            case STONE:
                return "stone";
            default:
                return "fire";
        }
    }

    public static MagicGolemTypes StringToMagicType(String magicGolemTypes) {
        switch (magicGolemTypes) {
            case "fire":
                return MagicGolemTypes.FIRE;
            case "froze":
                return MagicGolemTypes.FROZE;
            case "poison":
                return MagicGolemTypes.POISON;
            case "end":
                return MagicGolemTypes.END;
            case "stone":
                return MagicGolemTypes.STONE;
            default:
                return MagicGolemTypes.FIRE;
        }
    }


}

