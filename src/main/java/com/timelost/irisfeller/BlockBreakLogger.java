package com.timelost.irisfeller;

import com.timelost.irisfeller.util.IrisToolbeltManager;
import com.timelost.irisfeller.util.Jobs;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

import java.util.*;

public class BlockBreakLogger implements Listener {
    @SuppressWarnings("deprecation")
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Block b = e.getBlock();
        String id = IrisToolbeltManager.getMantleIdentity(e.getBlock().getWorld(), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());

        if (id != null && e.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("_axe")) {
//            List<ItemStack> items = new LinkedList<>(); // TODO: LATER
            //is an iris block

            if (id.contains("tree")) {
                Jobs.async(() -> {
                    boolean easteregg = Math.random() < 0.01;

                    boolean[] stw = {false};
                    ArrayList<ItemStack> iStack = new ArrayList<>();
                    Set<Block> blocks = getConnectedBlocks(b, id);

                    for (Block i : blocks) {
                        Jobs.sync(() -> {
                            if (stw[0]) {
                                return;
                            }
                            //TODO:  Perm check here too for claims etc...
                            if (e.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("_axe")) {
                                ItemStack is = e.getPlayer().getInventory().getItemInMainHand();

                                if (!i.getBlockData().getAsString().contains("leaves")) { // not leaves

                                    if (!IrisFellerSettings.USE_DURABILITY.get() || !( is.getItemMeta() != null && is.getItemMeta().isUnbreakable())) {

                                        double max = Math.min(((double) is.getEnchantmentLevel(Enchantment.DURABILITY)) / 6, 0.35);
                                        if (Math.random() < 0.5 - max) {
                                            is.setDurability((short) (is.getDurability() + 1));
                                        }
                                    }

                                    if (is.getDurability() >= is.getType().getMaxDurability()) {
                                        is = new ItemStack(Material.AIR);
                                        stw[0] = true;
                                        e.getPlayer().sendMessage("Item Broken");
                                    }
                                    e.getPlayer().getInventory().setItemInMainHand(is);
                                }

                                i.breakNaturally(is);
                                IrisToolbeltManager.deleteMantleBlock(e.getBlock().getWorld(), i.getX(), i.getY(), i.getZ());

                                if (easteregg) {
                                    e.getPlayer().getWorld().playSound(i.getLocation(), Sound.AMBIENT_CAVE, 1, 0.1f + (float) (Math.random() * 1.35));
                                } else {
                                    e.getPlayer().getWorld().playSound(i.getLocation(), Sound.BLOCK_CHORUS_FLOWER_GROW, 0.5f, 0.1f + (float) (Math.random() * 1.35));
                                }

                            } else {
                                stw[0] = true;
                            }
                        });
                    }
                });
            }
        }  // Not an iris block
    }

    private void dumpInv(Player p, ArrayList<ItemStack> iStack) {
        for (ItemStack i : iStack){
            p.getInventory().addItem(i);
        }

    }


    private static final BlockVector[] faces = {
            // Blame dan
            new BlockVector(0, 0, 0),
            new BlockVector(0, 1, 0),
            new BlockVector(0, -1, 0),//
            new BlockVector(1, 0, 0),
            new BlockVector(1, 1, 0),
            new BlockVector(1, -1, 0),//
            new BlockVector(0, 0, 1),
            new BlockVector(0, 1, 1),
            new BlockVector(0, -1, 1),//
            new BlockVector(1, 0, 1),
            new BlockVector(1, 1, 1),
            new BlockVector(1, -1, 1),//
            new BlockVector(1, 0, 1),
            new BlockVector(1, 1, 1),
            new BlockVector(1, -1, 1),//
            new BlockVector(-1, 0, 0),
            new BlockVector(-1, 1, 0),
            new BlockVector(-1, -1, 0),//
            new BlockVector(0, 0, -1),
            new BlockVector(0, 1, -1),
            new BlockVector(0, -1, -1),//
            new BlockVector(-1, 0, 1),
            new BlockVector(-1, 1, 1),
            new BlockVector(-1, -1, 1),//
            new BlockVector(1, 0, -1),
            new BlockVector(1, 1, -1),
            new BlockVector(1, -1, -1),//
            new BlockVector(1, 0, -1),
            new BlockVector(1, 1, -1),
            new BlockVector(1, -1, -1),//
            new BlockVector(-1, 0, 1),
            new BlockVector(-1, 1, 1),
            new BlockVector(-1, -1, 1),
    };

    private void getConnectedBlockRelation(Block block, Set<Block> collection, List<Block> todo, String id) {
        for (BlockVector face : faces) {
            Block b = block.getWorld().getBlockAt(block.getLocation().add(face));
            String iid = IrisToolbeltManager.getMantleIdentity(block.getWorld(), block.getX(), block.getY(), block.getZ());
            if (iid != null && iid.equals(id) && collection.add(b)) {
                todo.add(b);
            }
        }
    }

    private void playSound(BlockBreakEvent e, Block i) {
        if (IrisFellerSettings.USE_SOUNDS.get()) {
            boolean ea = Math.random() < 0.01;
            if (IrisFellerSettings.USE_EASTEREGG_SOUNDS.get() && ea) {
                e.getPlayer().getWorld().playSound(i.getLocation(), Sound.AMBIENT_CAVE, 1, 0.1f + (float) (Math.random() * 1.35));
            } else {
                e.getPlayer().getWorld().playSound(i.getLocation(), Sound.BLOCK_CHORUS_FLOWER_GROW, 0.5f, 0.1f + (float) (Math.random() * 1.35));
            }
        }
    }


    private Set<Block> getConnectedBlocks(Block block, String id) {
        Set<Block> set = new LinkedHashSet<>();
        LinkedList<Block> list = new LinkedList<>();
        list.add(block);
        while ((block = list.poll()) != null) {
            getConnectedBlockRelation(block, set, list, id);
        }
        return set;
    }

}