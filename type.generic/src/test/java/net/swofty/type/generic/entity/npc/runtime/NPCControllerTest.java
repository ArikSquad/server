package net.swofty.type.generic.entity.npc.runtime;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NPCControllerTest {

    @AfterEach
    void cleanup() {
        new ArrayList<>(HypixelNPC.getRegisteredNPCs()).forEach(HypixelNPC::unregister);
        HypixelNPC.getPerPlayerNPCs().clear();
    }

    @Test
    void autoStartRouteBeginsWhileManualRouteDoesNot() {
        TestNPC autoNpc = new TestNPC(specWithRoute(new NPCRoute(
                "auto",
                List.of(RouteWaypoint.of(new Pos(1, 0, 0))),
                20,
                RouteMode.ONCE,
                ActivationMode.AUTO_START
        )));
        autoNpc.register();
        autoNpc.controller().tick();
        assertEquals(1.0, autoNpc.controller().snapshot().position().x(), 0.001);

        TestNPC manualNpc = new TestNPC(specWithRoute(new NPCRoute(
                "manual",
                List.of(RouteWaypoint.of(new Pos(1, 0, 0))),
                20,
                RouteMode.ONCE,
                ActivationMode.MANUAL
        )));
        manualNpc.register();
        manualNpc.controller().tick();
        assertEquals(0.0, manualNpc.controller().snapshot().position().x(), 0.001);
    }

    @Test
    void routeModesAdvanceAsExpected() {
        TestNPC loopNpc = new TestNPC(specWithRoute(new NPCRoute(
                "loop",
                List.of(RouteWaypoint.of(new Pos(1, 0, 0)), RouteWaypoint.of(new Pos(2, 0, 0))),
                20,
                RouteMode.LOOP,
                ActivationMode.MANUAL
        )));
        loopNpc.controller().navigation("loop").start();
        tick(loopNpc, 3);
        assertEquals(1.0, loopNpc.controller().snapshot().position().x(), 0.001);

        TestNPC pingPongNpc = new TestNPC(specWithRoute(new NPCRoute(
                "ping",
                List.of(RouteWaypoint.of(new Pos(1, 0, 0)), RouteWaypoint.of(new Pos(2, 0, 0))),
                20,
                RouteMode.PING_PONG,
                ActivationMode.MANUAL
        )));
        pingPongNpc.controller().navigation("ping").start();
        tick(pingPongNpc, 3);
        assertEquals(1.0, pingPongNpc.controller().snapshot().position().x(), 0.001);
    }

    @Test
    void stopResumeAndResetControlNavigation() {
        TestNPC npc = new TestNPC(specWithRoute(new NPCRoute(
                "route",
                List.of(RouteWaypoint.of(new Pos(5, 0, 0))),
                10,
                RouteMode.ONCE,
                ActivationMode.MANUAL
        )));

        npc.controller().navigation("route").start();
        npc.controller().tick();
        double progressed = npc.controller().snapshot().position().x();
        assertTrue(progressed > 0);

        npc.controller().navigation("route").stop();
        tick(npc, 3);
        assertEquals(progressed, npc.controller().snapshot().position().x(), 0.001);

        npc.controller().navigation("route").resume();
        npc.controller().tick();
        assertTrue(npc.controller().snapshot().position().x() > progressed);

        npc.controller().navigation("route").reset();
        assertEquals(0.0, npc.controller().snapshot().position().x(), 0.001);
    }

    @Test
    void loadoutOverrideWinsOverBehaviorDefault() {
        NPCLoadout behaviorLoadout = new NPCLoadout(ItemStack.of(Material.STICK), ItemStack.AIR,
                ItemStack.AIR, ItemStack.AIR, ItemStack.AIR, ItemStack.AIR);
        TestNPC npc = new TestNPC(new NPCBehaviorSpec(Map.of(), Map.of(), behaviorLoadout));

        assertEquals(Material.STICK, npc.controller().snapshot().loadout().mainHand().material());
        npc.controller().setLoadout(new NPCLoadout(ItemStack.of(Material.DIAMOND_SWORD), ItemStack.AIR,
                ItemStack.AIR, ItemStack.AIR, ItemStack.AIR, ItemStack.AIR));
        assertEquals(Material.DIAMOND_SWORD, npc.controller().snapshot().loadout().mainHand().material());
    }

    @Test
    void loopingSequencesProducePulsesAndOffsets() {
        NPCSequence sequence = new NPCSequence(
                "emote",
                List.of(
                        new NPCActionStep.JumpStep(4, 0.4),
                        new NPCActionStep.HeadShakeStep(4, 20, 2),
                        new NPCActionStep.SwingStep(NPCAnimationHand.MAIN_HAND, 4, 2)
                ),
                ActivationMode.MANUAL,
                true
        );
        TestNPC npc = new TestNPC(new NPCBehaviorSpec(Map.of(), Map.of(sequence.id(), sequence), NPCLoadout.EMPTY));

        npc.controller().sequences().start("emote");
        npc.controller().tick();
        assertTrue(npc.controller().snapshot().verticalOffset() > 0);

        tick(npc, 5);
        assertNotEquals(0.0f, npc.controller().snapshot().headYawOffset());

        tick(npc, 4);
        assertFalse(npc.controller().snapshot().pulses().isEmpty());
    }

    @Test
    void snapshotPersistsCurrentStateWithoutViewers() {
        NPCSequence sequence = new NPCSequence(
                "swing",
                List.of(new NPCActionStep.SwingStep(NPCAnimationHand.MAIN_HAND, 2, 1)),
                ActivationMode.MANUAL,
                true
        );
        TestNPC npc = new TestNPC(new NPCBehaviorSpec(
                Map.of("route", new NPCRoute(
                        "route",
                        List.of(RouteWaypoint.of(new Pos(2, 0, 0))),
                        20,
                        RouteMode.ONCE,
                        ActivationMode.MANUAL
                )),
                Map.of(sequence.id(), sequence),
                NPCLoadout.EMPTY
        ));

        npc.controller().navigation("route").start();
        npc.controller().sequences().start("swing");
        tick(npc, 2);

        NPCControllerSnapshot snapshot = npc.controller().snapshot();
        assertTrue(snapshot.position().x() > 0);
        assertFalse(snapshot.pulses().isEmpty());
    }

    private static void tick(TestNPC npc, int ticks) {
        for (int i = 0; i < ticks; i++) {
            npc.controller().tick();
        }
    }

    private static NPCBehaviorSpec specWithRoute(NPCRoute route) {
        return new NPCBehaviorSpec(Map.of(route.id(), route), Map.of(), NPCLoadout.EMPTY);
    }

    private static final class TestNPC extends HypixelNPC {
        private final NPCBehaviorSpec behaviorSpec;

        private TestNPC(NPCBehaviorSpec behaviorSpec) {
            super(new HumanConfiguration() {
                @Override
                public String texture(HypixelPlayer player) {
                    return "";
                }

                @Override
                public String signature(HypixelPlayer player) {
                    return "";
                }

                @Override
                public String[] holograms(HypixelPlayer player) {
                    return new String[]{"Test", "NPC"};
                }

                @Override
                public Pos position(HypixelPlayer player) {
                    return Pos.ZERO;
                }

                @Override
                public boolean supportsRuntimeBehavior() {
                    return true;
                }

                @Override
                public Pos runtimeSpawnPosition() {
                    return Pos.ZERO;
                }
            });
            this.behaviorSpec = behaviorSpec;
        }

        @Override
        protected NPCBehaviorSpec behavior() {
            return behaviorSpec;
        }

        @Override
        public void onClick(NPCInteractEvent event) {
        }
    }
}
