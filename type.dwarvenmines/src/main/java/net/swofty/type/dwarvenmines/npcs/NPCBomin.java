package net.swofty.type.dwarvenmines.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.behavior.NPCBehavior;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class NPCBomin extends HypixelNPC {

    public NPCBomin() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Bomin", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "TGTjMaF34VrUBh0Xpwo6Adj4u3aog6vUkqArWxzVVtlyNXcQ4Tv1TiJkFj3kPBBMPyHz5gBn21/+gZDENpiE0dVYatpdbWpuCKogGp33CqAMylwLJhYo876ONPQwlooJu5v43+xuHN9QzlrVCXYwjDDqNp96mPItS1CTGC3PMQWQbOMWMJxc1C4eQfdV1N1HW1vlygl3hhjFw4ef0P8d9zx/URztjYnCC92p5wHzThIkf6XpLbss2EUVsOP26QkGNoWN9iRwHKmXDso5FZxBArXA4MBqCAiTXFbfrRoUpAMRx5om83SMk1G6IPpjp+iZAP3bBHyJ0c3o1h35FYANH9Qyn+QvVEOkwH6r8pNeTXhOfT3MSPvLjzjqOJ01oO5+NCCzi5HF+6o9qDD3y9HTfKB6N1XKQlF2fME3EEBAS4BeOvcgFVtDdazPY9v576q+qgSh1IeYwBORK1u1P7g0N+OS8ippwBN3WtjR2AE34SVo2IJlfy8+E2k97XascDBIYq8Fx9CsYmCeZ1PGZXMMhYz42V5491utJoYMSUmcyfZ69xyPP/DC+eM4M0zMGAC84Q7pMm4Tfv1OjCQ0jnYZaERCkQed/8uJNYNol7DM86kGrFfNXUmWJmwUoN9Q7flzOBmd2E55CIa8ywy1+AXgkvyyIQoNKCKRuHvpP79toFY=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTczNzI0MzY2NzA3NCwKICAicHJvZmlsZUlkIiA6ICI2OTE1MGMxMTk3M2E0MGViOGExNjZiNTY5OThmNWEzMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJMeXgyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y2NmY2ZjZhZjU1Mjk5Y2UzYjYxYmIwODg2ZmE1ODk2MjdmOTg3MzQ2YmQwYWFhYzkwNmZjODlhZTkyZjMzYzAiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(26.6, 203, -144.8, 0, 0);
            }

            @Override
            public boolean supportsBehavior() {
                return true;
            }

            @Override
            public Pos behaviorStartPosition() {
                return new Pos(26.6, 203, -144.8, 0, 0);
            }
        });
    }

    @Override
    protected NPCBehavior behavior() {
        return controller -> {
            if (controller.ticksLived() % 10 != 0) {
                return;
            }

            controller.jump(0.35, 8);
            controller.swingMainHand();
            controller.rotate(45);
        };
    }

    @Override
    public void onClick(NPCInteractEvent event) {

    }
}
