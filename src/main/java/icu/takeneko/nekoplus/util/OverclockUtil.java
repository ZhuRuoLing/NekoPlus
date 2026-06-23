package icu.takeneko.nekoplus.util;

import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import it.unimi.dsi.fastutil.objects.Reference2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;

import java.util.ArrayList;
import java.util.List;

public class OverclockUtil {

    // not really works
    public static Reference2IntMap<NPOverclockablePowerConsumer> overclock(List<NPOverclockablePowerConsumer> allMachines, int generate, int currentConsume) {
        Reference2IntMap<NPOverclockablePowerConsumer> result = new Reference2IntLinkedOpenHashMap<>();
        if (allMachines.isEmpty()) {
            return result;
        }

        List<NPOverclockablePowerConsumer> ocList = new ArrayList<>();
        for (NPOverclockablePowerConsumer m : allMachines) {
            if (m == null) continue;
            if (m.isOverclockable()) {
                ocList.add(m);
            }
            result.put(m, 0);
        }

        long baseInputPower = 0L;
        for (NPOverclockablePowerConsumer m : ocList) {
            baseInputPower += m.getBaseInputPower();
        }
        long baseline = (long) currentConsume + baseInputPower;
        long budget = (long) generate - baseline;

        if (budget <= 0L) {
            return result;
        }


        List<Node> nodes = new ArrayList<>();
        for (NPOverclockablePowerConsumer m : ocList) {
            nodes.add(new Node(m));
        }

        boolean changed;
        do {
            changed = false;
            for (Node n : nodes) {
                if (budget <= 0L) break;
                if (n.remainingSteps() <= 0L) continue;
                if (n.pn <= 0L) {
                    n.currentX = n.maxRatio;
                    changed = true;
                    continue;
                }
                if (budget >= n.pn) {
                    n.currentX++;
                    budget -= n.pn;
                    changed = true;
                }
            }
        } while (changed);

        for (Node n : nodes) {
            int x = n.currentX;
            if (n.m.isOverclockable()) {
                n.m.setEfficiency(x);
            }
            result.put(n.m, x);
        }

        return result;
    }

    private static class Node {
        final NPOverclockablePowerConsumer m;
        final long pn;
        final int maxRatio;
        int currentX = 0;

        Node(NPOverclockablePowerConsumer m) {
            this.m = m;
            this.pn = m.getBaseOverclockCost();
            this.maxRatio = Math.max(0, m.maxOverclockRatio());
        }

        long remainingSteps() {
            return Math.max(0L, maxRatio - currentX);
        }
    }
}
