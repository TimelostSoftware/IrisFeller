package com.timelost.irisfeller;

import com.timelost.irisfeller.util.IrisToolbeltManager;
import com.timelost.irisfeller.util.J;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BlockBreakLogger implements Listener {

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Block b = e.getBlock();
        String id = IrisToolbeltManager.getMantleIdentity(e.getBlock().getWorld(), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());


        if (id != null) {
            //is an iris block


            // Actual Break Code:
            if (id.contains("tree")) {
                J.a(() -> {
                    boolean easteregg = false;
                    java.util.Random rand = new java.util.Random();
                    double f = rand.nextDouble();
                    if (f < 0.01) {
                        easteregg = true;
                    }
                    int j = 0;
                    boolean stw[] = {false};
                    Set<Integer> ids = new LinkedHashSet<>();
                    Set<Block> blocks = getConnectedBlocks(b, id);

                    for (Block i : blocks) {

                        boolean finalEasteregg = easteregg;
                        ids.add(J.s(() -> {
                            stw[0] = true;
                            // Perm check here too for claims etc...
                            if (e.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("_axe")) { //Player holding ANY axe.
                                ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                                short durability = is.getDurability();
                                if (is.getItemMeta() != null && is.getItemMeta().isUnbreakable()) {
                                    IrisToolbeltManager.deleteMantleBlock(e.getBlock().getWorld(), i.getX(), i.getY(), i.getZ());
                                } else {
                                    //do damage
                                    if (!i.getBlockData().getAsString().contains("leaves")) { // not leaves
                                        is.setDurability((short) (durability + 1));
                                        e.getPlayer().getInventory().setItemInMainHand(is);
                                    }

                                    i.breakNaturally(is);
                                    IrisToolbeltManager.deleteMantleBlock(e.getBlock().getWorld(), i.getX(), i.getY(), i.getZ());
                                }

                                if (finalEasteregg) {
                                    e.getPlayer().getWorld().playSound(i.getLocation(), Sound.AMBIENT_CAVE, 1, 0.1f + (float) (Math.random() * 1.35));
                                } else {
                                    e.getPlayer().getWorld().playSound(i.getLocation(), Sound.BLOCK_CHORUS_FLOWER_GROW, 0.5f, 0.1f + (float) (Math.random() * 1.35));
                                }

                            } else {
                                for (int idd : ids) { // PROBABLY NOT SAFE?
                                    Bukkit.getScheduler().cancelTask(idd);
                                }
                            }

                        }, (j++) / 10));

                    }


                });
            }

        } else {
            // Not an iris block
        }


    }


    private static final BlockVector[] faces = {
            // Thanks dan
            new BlockVector(0, 0, 0),
            new BlockVector(0, 1, 0),
            new BlockVector(0, -1, 0),

            new BlockVector(1, 0, 0),
            new BlockVector(1, 1, 0),
            new BlockVector(1, -1, 0),

            new BlockVector(0, 0, 1),
            new BlockVector(0, 1, 1),
            new BlockVector(0, -1, 1),

            new BlockVector(1, 0, 1),
            new BlockVector(1, 1, 1),
            new BlockVector(1, -1, 1),

            new BlockVector(1, 0, 1),
            new BlockVector(1, 1, 1),
            new BlockVector(1, -1, 1),

            new BlockVector(-1, 0, 0),
            new BlockVector(-1, 1, 0),
            new BlockVector(-1, -1, 0),

            new BlockVector(0, 0, -1),
            new BlockVector(0, 1, -1),
            new BlockVector(0, -1, -1),

            new BlockVector(-1, 0, 1),
            new BlockVector(-1, 1, 1),
            new BlockVector(-1, -1, 1),

            new BlockVector(1, 0, -1),
            new BlockVector(1, 1, -1),
            new BlockVector(1, -1, -1),

            new BlockVector(1, 0, -1),
            new BlockVector(1, 1, -1),
            new BlockVector(1, -1, -1),

            new BlockVector(-1, 0, 1),
            new BlockVector(-1, 1, 1),
            new BlockVector(-1, -1, 1),


    };

    private void getConnectedBlockRelation(Block block, Set<Block> collection, List<Block> todo, String id) {
        for (BlockVector face : faces) {
            Block b = block.getWorld().getBlockAt(block.getLocation().add(face));
            String iid = IrisToolbeltManager.getMantleIdentity(block.getWorld(), block.getX(), block.getY(), block.getZ());
            if (iid != null) {
                if (iid.equals(id)) {
                    if (collection.add(b)) {
                        todo.add(b);
                    }
                }
            }
        }
    }

    public Set<Block> getConnectedBlocks(Block block, String id) {
        Set<Block> set = new LinkedHashSet<>();
        LinkedList<Block> list = new LinkedList<>();

        list.add(block);

        while ((block = list.poll()) != null) {
            getConnectedBlockRelation(block, set, list, id);
        }
        return set;
    }

}