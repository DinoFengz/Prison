package tech.mcprison.prison.spigot.autofeatures.events;

import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.events.player.PrisonPlayerInteractEvent;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.SelectionManager;
import tech.mcprison.prison.spigot.block.OnBlockBreakMines;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.util.Location;

public class PrisonDebugBlockInspector
//	extends OnBlockBreakMines
{
	OnBlockBreakMines obbMines;
	
	public PrisonDebugBlockInspector() {
		super();
		
		obbMines = new OnBlockBreakMines();
	}

    public void init() {
        Prison.get().getEventBus().register(this);
    }

    @Subscribe
    public void onPlayerInteract( PrisonPlayerInteractEvent e ) {
        ItemStack ourItem = e.getItemInHand();
        ItemStack toolItem = SelectionManager.SELECTION_TOOL;

        if ( !ourItem.equals(toolItem) || !Output.get().isDebug() ) {
            return;
        }
        //e.setCanceled(true);

        Player player = e.getPlayer();
        
        Location location = e.getClicked();
        SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
        
        UUID playerUUID = e.getPlayer().getUUID();
        Mine mine = obbMines.findMine( playerUUID, sBlock,  null, null ); 
        
        if ( mine == null ) {
        	
        	player.sendMessage(
        			String.format(
	        			"&dDebugBlockInfo: &7Not in a mine. &5%s &7%s",
	        			sBlock.getBlockName(), location.toWorldCoordinates()) );
        	
        }
        else {
        	player.sendMessage( 
        			String.format(
        					"&dDebugBlockInfo: &3Mine &7%s. " +
			    			"&5%s &7%s",
			    			mine.getName(), 
			    			sBlock.getBlockName(), 
			    			location.toWorldCoordinates()) );
        	
			// Get the mine's targetBlock:
			MineTargetPrisonBlock tBlock = mine.getTargetPrisonBlock( sBlock );

			
			String message = String.format( "&3TargetBlock: &7%s  " +
					"&3Mined: %s%b  &3Broke: &7%b", 
					tBlock.getPrisonBlock().getBlockName(),
					(tBlock.isMined() ? "&d" : "&2"),
					tBlock.isMined(), 
					tBlock.isAirBroke()
					);
        	
			player.sendMessage( message );
			
			String message2 = String.format( "    &3Counted: &7%b  &3Edge: &7%b  " +
					"&3Exploded: %s%b &3IgnorAllEvents: &7%b", 
					tBlock.isCounted(),
					tBlock.isEdge(),
					(tBlock.isExploded() ? "&d" : "&2"),
					tBlock.isExploded(),
					tBlock.isIgnoreAllBlockEvents()
					);
			
			player.sendMessage( message2 );
        }
        
        
//        if (e.getAction() == PrisonPlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
//            // Set first position
//            Selection sel = Prison.get().getSelectionManager().getSelection(e.getPlayer());
//            sel.setMin(e.getClicked());
//            Prison.get().getSelectionManager().setSelection(e.getPlayer(), sel);
//            e.getPlayer()
//                .sendMessage("&7First position set to &8" + e.getClicked().toBlockCoordinates());
//
//            checkForEvent(e.getPlayer(), sel);
//        } else if (e.getAction() == PrisonPlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
//            // Set second position
//            Selection sel = Prison.get().getSelectionManager().getSelection(e.getPlayer());
//            sel.setMax(e.getClicked());
//            Prison.get().getSelectionManager().setSelection(e.getPlayer(), sel);
//            e.getPlayer()
//                .sendMessage("&7Second position set to &8" + e.getClicked().toBlockCoordinates());
//
//            checkForEvent(e.getPlayer(), sel);
//        }
    }
}