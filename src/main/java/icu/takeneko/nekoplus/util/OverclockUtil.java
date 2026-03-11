package icu.takeneko.nekoplus.util;

import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import it.unimi.dsi.fastutil.objects.Reference2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;

import java.util.ArrayList;
import java.util.Comparator;
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
            result.put(m, 1);
        }

        long sumPn = 0L;
        for (NPOverclockablePowerConsumer m : ocList) {
            sumPn += m.getBaseOverclockCost();
        }
        long baseline = (long) currentConsume + sumPn;
        long budget = (long) generate - baseline;

        if (budget <= 0L) {
            return result;
        }


        List<Node> nodes = new ArrayList<>();
        for (NPOverclockablePowerConsumer m : ocList) {
            nodes.add(new Node(m));
        }

        nodes.sort(Comparator.comparingLong(n -> n.extraCap));
        for (Node n : nodes) {
            if (n.extraCap <= 0L) {
                n.currentX = 1;
                continue;
            }
            if (budget >= n.extraCap) {
                n.currentX = n.maxRatio;
                budget -= n.extraCap;
            } else {
                n.currentX = 1;
            }
        }

        if (budget == 0L) {
            for (Node n : nodes) {
                int x = n.currentX;
                if (n.m.isOverclockable()) {
                    n.m.setEfficiency(x);
                }
                result.put(n.m, x);
            }
            return result;
        }


        List<Node> remaining = new ArrayList<>();
        for (Node n : nodes) {
            if (n.currentX < n.maxRatio) remaining.add(n);
        }

        remaining.sort(Comparator.comparingLong(n -> n.pn));

        for (Node n : remaining) {
            if (budget <= 0L) break;
            if (n.pn <= 0L) {
                n.currentX = n.maxRatio;
                continue;
            }
            long stepsCanTake = Math.min(n.maxRatio - n.currentX, budget / n.pn);
            if (stepsCanTake > 0L) {
                n.currentX += (int) stepsCanTake;
                budget -= stepsCanTake * n.pn;
            }

        }

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
        final long extraCap;
        int currentX = 1;

        Node(NPOverclockablePowerConsumer m) {
            this.m = m;
            this.pn = m.getBaseOverclockCost();
            this.maxRatio = Math.max(1, m.maxOverclockRatio());
            this.extraCap = this.pn * (long) (this.maxRatio - 1);
        }

        long remainingSteps() {
            return Math.max(0L, maxRatio - currentX);
        }
    }
}
