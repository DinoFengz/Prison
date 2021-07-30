package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;

public class AutoManagerCrazyEnchants
	extends AutoManagerEventsManager
{
	
	public AutoManagerCrazyEnchants() {
		super();
	}
	
	@Override
	public void registerEvents() {
		
		initialize();
		
	}

		
	public class AutoManagerBlastUseEventListener
		extends AutoManagerCrazyEnchants
		implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onCrazyEnchantsBlockExplode(BlastUseEvent e) {
			genericBlockExplodeEventAutoManager( e );
		}
	}
	
    public class OnBlockBreakBlastUseEventListener 
		extends AutoManagerCrazyEnchants
		implements Listener {
    	
        @EventHandler(priority=EventPriority.NORMAL) 
        public void onCrazyEnchantsBlockExplode(BlastUseEvent e) {
        	genericBlockExplodeEvent( e );
        }
    }
    
    public class OnBlockBreakBlastUseEventListenerMonitor
	    extends AutoManagerCrazyEnchants
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.MONITOR) 
    	public void onCrazyEnchantsBlockExplode(BlastUseEvent e) {
    		genericBlockExplodeEventMonitor( e );
    	}
    }
    

	@Override
	public void initialize() {
		boolean isEventEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
		
		if ( !isEventEnabled ) {
			return;
		}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			Output.get().logInfo( "AutoManager: checking if loaded: CrazyEnchants" );
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
					this.getClass().getClassLoader() );
			
			Output.get().logInfo( "AutoManager: Trying to register CrazyEnchants" );
			
			String eP = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
			BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );
			
			if ( eventPriority != BlockBreakPriority.DISABLED ) {
				
				EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
				
				
				OnBlockBreakBlastUseEventListener normalListener = 
											new OnBlockBreakBlastUseEventListener();
				OnBlockBreakBlastUseEventListenerMonitor normalListenerMonitor = 
											new OnBlockBreakBlastUseEventListenerMonitor();
				
				
				SpigotPrison prison = SpigotPrison.getInstance();
				
				PluginManager pm = Bukkit.getServer().getPluginManager();
				
				if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
					
					AutoManagerBlastUseEventListener autoManagerlListener = 
														new AutoManagerBlastUseEventListener();

					pm.registerEvent(BlastUseEvent.class, autoManagerlListener, ePriority,
							new EventExecutor() {
						public void execute(Listener l, Event e) { 
							((AutoManagerBlastUseEventListener)l)
							.onCrazyEnchantsBlockExplode((BlastUseEvent)e);
						}
					},
					prison);
					prison.getRegisteredBlockListeners().add( autoManagerlListener );
				}
				
				pm.registerEvent(BlastUseEvent.class, normalListener, ePriority,
						new EventExecutor() {
						public void execute(Listener l, Event e) { 
							((OnBlockBreakBlastUseEventListener)l)
							.onCrazyEnchantsBlockExplode((BlastUseEvent)e);
					}
				},
				prison);
				prison.getRegisteredBlockListeners().add( normalListener );
				
				pm.registerEvent(BlastUseEvent.class, normalListenerMonitor, EventPriority.MONITOR,
						new EventExecutor() {
						public void execute(Listener l, Event e) { 
							((OnBlockBreakBlastUseEventListenerMonitor)l)
							.onCrazyEnchantsBlockExplode((BlastUseEvent)e);
					}
				},
				prison);
				prison.getRegisteredBlockListeners().add( normalListenerMonitor );
				
			}
			
			// The following is paper code:
//				var executor = EventExecutor
//						.create( AutoManagerBlastUseEventListener.class
//								.getDeclaredMethod( "onCrazyEnchantsBlockExplode", BlastUseEvent.class ),
//								BlastUseEvent.class );
//				
//				Bukkit.getServer().getPluginManager()
//					.register( BlastUseEvent.class, this, EventPriority.LOW, executor, SpigotPrison.getInstance() );
		}
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
			Output.get().logInfo( "AutoManager: CrazyEnchants is not loaded" );
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: CrazyEnchants failed to load. [%s]", e.getMessage() );
		}
	}
   
	
    @Override
    public void unregisterListeners() {
    	
    	super.unregisterListeners();

    }
	
	@Override
	public void dumpEventListeners() {
    	boolean isEventEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
							this.getClass().getClassLoader() );
			

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"BlastUseEvent", 
					new SpigotHandlerList( BlastUseEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				Output.get().logInfo( "" );
				eventDisplay.toLog( LogLevel.INFO );
			}
		}
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: CrazyEnchants failed to load. [%s]", e.getMessage() );
		}
	}
    
}