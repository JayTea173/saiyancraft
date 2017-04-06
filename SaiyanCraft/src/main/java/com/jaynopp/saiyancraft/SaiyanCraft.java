package com.jaynopp.saiyancraft;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SaiyanBattlerMessageHandler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SyncSaiyanBattlerMessage;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataMessageHandler;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.item.ItemValueManager;
import com.jaynopp.saiyancraft.player.BlockableDamageMessage;
import com.jaynopp.saiyancraft.player.BlockableDamageMessageHandler;
import com.jaynopp.saiyancraft.player.ClientPlayerFallMessage;
import com.jaynopp.saiyancraft.player.DamageSenderMessage;
import com.jaynopp.saiyancraft.player.DamageSenderMessageHandler;
import com.jaynopp.saiyancraft.player.KnockBackLocalMessage;
import com.jaynopp.saiyancraft.player.KnockBackLocalMessageHandler;
import com.jaynopp.saiyancraft.player.ServerSaiyanPlayerMessageHandler;
import com.jaynopp.saiyancraft.player.moves.UseMoveMessage;
import com.jaynopp.saiyancraft.player.moves.UseMoveMessageHandler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


@Mod(modid = SaiyanCraft.modId, name = SaiyanCraft.name, version = SaiyanCraft.VERSION)
public class SaiyanCraft
{
    public static final String modId = "saiyancraft";
    public static final String name = "SaiyanCraft";
    public static final String VERSION = "1.0.1";
    public static final String resourcePrefix = modId.toLowerCase() + ":";
    
    public static SimpleNetworkWrapper network;
    
    @SidedProxy(serverSide = "com.jaynopp.saiyancraft.CommonProxy", clientSide = "com.jaynopp.saiyancraft.ClientProxy")
    public static CommonProxy proxy;
    
    @Mod.Instance(modId)
    public static SaiyanCraft instance;
    
    public static ItemValueManager itemValueManager;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(modId);
    	network.registerMessage(SaiyanDataMessageHandler.class, SyncSaiyanDataMessage.class, 0, Side.SERVER);
    	network.registerMessage(SaiyanDataMessageHandler.class, SyncSaiyanDataMessage.class, 0, Side.CLIENT);
    	network.registerMessage(ServerSaiyanPlayerMessageHandler.class, ClientPlayerFallMessage.class, 1, Side.SERVER);
    	network.registerMessage(SaiyanBattlerMessageHandler.class, SyncSaiyanBattlerMessage.class, 2, Side.CLIENT);
    	network.registerMessage(UseMoveMessageHandler.class, UseMoveMessage.class, 3, Side.SERVER);
    	network.registerMessage(UseMoveMessageHandler.class, UseMoveMessage.class, 3, Side.CLIENT);
    	network.registerMessage(BlockableDamageMessageHandler.class, BlockableDamageMessage.class, 4, Side.CLIENT);
    	network.registerMessage(DamageSenderMessageHandler.class, DamageSenderMessage.class, 5, Side.SERVER);
    	network.registerMessage(KnockBackLocalMessageHandler.class, KnockBackLocalMessage.class, 6, Side.CLIENT);
    	proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
    	proxy.postInit(event);
    }
}