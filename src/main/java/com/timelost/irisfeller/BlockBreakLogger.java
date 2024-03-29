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
import java.util.stream.Collectors;

public class BlockBreakLogger implements Listener {
    @SuppressWarnings("deprecation")
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Block b = e.getBlock();
        String id = IrisToolbeltManager.getMantleIdentity(e.getBlock().getWorld(), e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());

        if (id != null && e.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("_" + IrisFellerSettings.LOGGER_TOOL.get()) && id.contains("tree")) {
            //TODO:  Perm check here too for claims etc...

            List<Material> blockDupeList = new ArrayList<>();

            Jobs.async(() -> {
                boolean[] stw = {false};
                Set<Block> blocks = getConnectedBlocks(b, id);


                for (Block i : blocks) {
                    Jobs.sync(() -> {
                        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                        if (stw[0]) {
                            return;
                        }
                        if (IrisToolbeltManager.getMantleBlock(e.getBlock().getWorld(), i.getX(), i.getY(), i.getZ()) == null) {
                            blocks.remove(i);
                            return;
                        }
                        if (IrisToolbeltManager.getMantleBlock(e.getBlock().getWorld(), i.getX(), i.getY(), i.getZ()).getMaterial() != i.getBlockData().getMaterial()) {
                            IrisFeller.info(IrisToolbeltManager.getMantleBlock(e.getBlock().getWorld(), i.getX(), i.getY(), i.getZ()).getMaterial() + "  :  " + i.getBlockData().getMaterial());
                            blocks.remove(i);
                            return;
                        }
                        if (e.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("_axe")) {
                            if (!i.getBlockData().getAsString().contains("leaves")) { // not damage for leaves
                                if (!IrisFellerSettings.USE_DURABILITY.get() || !(is.getItemMeta() != null && is.getItemMeta().isUnbreakable())) {
                                    double max = Math.min(((double) is.getEnchantmentLevel(Enchantment.DURABILITY)) / 6, 0.35);
                                    if (Math.random() < 0.5 - max) {
                                        is.setDurability((short) (is.getDurability() + 1));
                                    }
                                }
                                if (is.getDurability() >= is.getType().getMaxDurability()) {
                                    is = new ItemStack(Material.AIR);
                                    if (!IrisFellerSettings.DROP_NATURALLY.get()) {
                                        dimPocketItems(blockDupeList, e); // TODO REMOVE
                                    }
                                    stw[0] = true;
                                }
                                e.getPlayer().getInventory().setItemInMainHand(is);
                            }
                            blockDupeList.add(i.getType()); // Add to list
                            blocks.remove(i); // Drop from List
                            breakBlock(i, is, e.getPlayer(), e); // Deletes the block regardless

                            if (blocks.size() == 0 && !IrisFellerSettings.DROP_NATURALLY.get()) {
                                dimPocketItems(blockDupeList, e);
                                stw[0] = true;
                            }
                        } else {
                            if (!IrisFellerSettings.DROP_NATURALLY.get()) {
                                dimPocketItems(blockDupeList, e); // TODO REMOVE
                            }
                            stw[0] = true;
                        }
                    });

                }
            });

        }  // Not an iris block
    }

    private void dimPocketItems(List<Material> blockDupeList, BlockBreakEvent e) {
        Map<Material, Long> couterMap = blockDupeList.stream().collect(Collectors.groupingBy(d -> d, Collectors.counting()));
        Jobs.sync(() -> couterMap.forEach((material, count) -> {
            if (material.toString().contains("LEAVES")) {
                if (e.getPlayer().getInventory().firstEmpty() != -1) {
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.getMaterial(material.toString().replace("_LEAVES", "_SAPLING")), 5));
                } else {
                    e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.getMaterial(material.toString().replace("_LEAVES", "_SAPLING")), 5));
                }

                return;
            }


            //Main Stacks
            if (count >= 64) {
                for (int i = 1; i < count + 1; i++) {
                    if ((i % 64) == 0) {
                        if (e.getPlayer().getInventory().firstEmpty() != -1) {
                            e.getPlayer().getInventory().addItem(new ItemStack(material, 64));
                        } else {
                            e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(material, 64));
                        }
                    }
                }
            }
            //rest of the stack
            if (e.getPlayer().getInventory().firstEmpty() != -1) {
                e.getPlayer().getInventory().addItem(new ItemStack(material, (int) (count % 64)));
            } else {
                e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(material, (int) (count % 64)));
            }

        }));
    }

    private void breakBlock(Block i, ItemStack is, Player p, BlockBreakEvent e) {
        IrisToolbeltManager.deleteMantleBlock(e.getBlock().getWorld(), i.getX(), i.getY(), i.getZ());
        //todo Perms or plugin Hooks for breaks
        if (IrisFellerSettings.DROP_NATURALLY.get()) {
            i.breakNaturally(is);
        } else {
            i.getWorld().getBlockAt(i.getLocation()).setType(Material.AIR);
        }
        playSound(e, i);
    }

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
        return blockCleanup(set);
    }

    private Set<Block> blockCleanup(Set<Block> set) {

        List<Material> blacklist = IrisFellerSettings.BLACKLIST.get();
        Set<Block> noAirSet = new LinkedHashSet<>();
        set.forEach(block -> {
            if (!block.getType().isAir() && !blacklist.contains(block.getBlockData().getMaterial())) {            //todo: BLACKLIST
                noAirSet.add(block);
            }
        });


        return noAirSet;
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

}