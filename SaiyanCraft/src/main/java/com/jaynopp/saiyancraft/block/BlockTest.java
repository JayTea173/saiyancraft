package com.jaynopp.saiyancraft.block;

import java.util.List;

import com.jaynopp.core.item.JNBlockInitializer;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.lib.Names;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockTest extends Block implements JNBlockInitializer{
	public static BlockTest instance;
	public static final int NUMLEVELS = 7;
	public static void Initialize(){
		instance = new BlockTest();
	}
	
	public BlockTest() {
		super(Material.ROCK);
		JNBlockInitializer.super.Initialize((Block)this, SaiyanCraft.resourcePrefix, Names.Tiles.TEST_BLOCK);
		setHardness(500.0f);
		setResistance(30.0f);
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	    this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.ONE));
		// TODO Auto-generated constructor stub	
	}
	
	public enum EnumType implements IStringSerializable {
	    ONE(0, "1"),
	    TWO(1, "2"),
		THREE(2, "3"),
		FOUR(3, "4"),
		FIVE(4, "5"),
		SIX(5, "6"),
		SEVEN(6, "7");

	    private int ID;
	    private String name;
	    
	    private EnumType(int ID, String name) {
	        this.ID = ID;
	        this.name = name;
	    }
	    
	    @Override
	    public String getName() {
	        return name;
	    }

	    public int getID() {
	        return ID;
	    }
	    
	    @Override
	    public String toString() {
	        return getName();
	    }
	    
	    public static EnumType fromMeta (int meta){
	    	switch(meta){
	    	default:
	    		return ONE;
	    	case 1:
	    		return TWO;
	    	case 2:
	    		return THREE;
	    	case 3: 
	    		return FOUR;
	    	case 4:
	    		return FIVE;
	    	case 5:
	    		return SIX;
	    	case 6:
	    		return SEVEN;
	    	}
	    }
	}
	
	public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockTest.EnumType.class);
	
	@Override
	protected BlockState createBlockState() {
	    return new BlockState(this, new IProperty[] { TYPE });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
	    return getDefaultState().withProperty(TYPE, EnumType.fromMeta(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    EnumType type = (EnumType) state.getValue(TYPE);
	    return type.getID();
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}
	
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
	    list.add(new ItemStack(itemIn, 1, 0)); //Meta 0
	    list.add(new ItemStack(itemIn, 1, 1)); //Meta 1
	    list.add(new ItemStack(itemIn, 1, 2)); //Meta 2
	    list.add(new ItemStack(itemIn, 1, 3)); //Meta 3
	    list.add(new ItemStack(itemIn, 1, 4)); //Meta 4
	    list.add(new ItemStack(itemIn, 1, 5)); //Meta 5
	    list.add(new ItemStack(itemIn, 1, 6)); //Meta 6
	}
}
